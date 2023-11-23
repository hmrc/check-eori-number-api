/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.checkeorinumberapi.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.checkeorinumberapi.config.AppContext
import uk.gov.hmrc.checkeorinumberapi.connectors.CheckEoriNumberConnector
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.checkeorinumberapi.models._

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class EoriController @Inject() (
  connector: CheckEoriNumberConnector,
  cc: ControllerComponents,
  appContext: AppContext
)(implicit ec: ExecutionContext)
    extends BackendController(cc) {

  private val ukEoriRegex: String = "^(GB|XI)[0-9]{12,15}$"
  private val xiEoriRegex: String = "^XI[0-9]{12,15}$"

  def checkMultipleEoris: Action[JsValue] = {
    Action.async(parse.json) { implicit request =>
      withJsonBody[CheckMultipleEoriNumbersRequest](cmr => {
        cmr.eoris match {
          case Nil =>
            Future.successful(
              BadRequest(
                "Invalid payload - one or more EORI numbers are required in your body"
              )
            )
          case en if en.size > appContext.eisApiLimit =>
            Future.successful(
              BadRequest(
                s"Invalid payload - you have exceeded the maximum of ${appContext.eisApiLimit} EORI numbers"
              )
            )
          case en if !en.forall(_.matches(ukEoriRegex)) =>
            Future.successful(
              BadRequest(
                "Invalid payload - one or more EORI numbers are not valid, " +
                  "ensure all of your EORI numbers match ^(GB|XI)[0-9]{12,15}$"
              )
            )
          case en if en.exists(x => x.matches(xiEoriRegex)) && !appContext.allowXiEoriNumbers =>
            Future.successful(
              BadRequest(
                "Invalid payload - one or more EORI numbers begin with XI." +
                  " To check an EORI number that starts with XI, " +
                  "use the EORI checker service on the European Commission website"
              )
            )
          case _ =>
            connector.checkEoriNumbers(cmr).map { checkResponse =>
              Ok(Json.toJson(checkResponse))
            }
        }
      })
    }
  }

}
