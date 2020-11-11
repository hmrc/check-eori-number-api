/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.checkeorinumberapi.connectors.CheckEoriNumberConnector
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.customs.api.common.logging.CdsLogger
import uk.gov.hmrc.checkeorinumberapi.models._
import uk.gov.hmrc.customs.api.common.controllers.ErrorResponse.ErrorInternalServerError

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class EoriController @Inject()(
  connector: CheckEoriNumberConnector,
  cc: ControllerComponents,
  logger: CdsLogger
)(implicit ec: ExecutionContext) extends BackendController(cc) {

  def check(eoriNumber: EoriNumber): Action[AnyContent] = Action.async { implicit request =>
    connector.checkEoriNumbers(CheckMultipleEoriNumbersRequest(List(eoriNumber))).map {
      case Some(head::_) => {
        if (head.valid) Ok(Json.toJson(head))
        else NotFound(Json.toJson(head))
      }
    }.recover {
      case e =>
        logger.error(e.getMessage, e.fillInStackTrace())
        ErrorInternalServerError.JsonResult
    }
  }

  def checkMultipleEoris: Action[JsValue] = {
    Action.async(parse.json) { implicit request =>
      withJsonBody[CheckMultipleEoriNumbersRequest](cmr => {
        if (cmr.eoriNumbers.size <= 10)
          connector.checkEoriNumbers(cmr).map { checkResponse =>
            Ok(Json.toJson(checkResponse))
          }
        else
          // TODO resolve max, create exception instance etc.
          Future.successful(BadRequest(Json.toJson("more than 10 eori numbers")))
      })
    }
  }

}
