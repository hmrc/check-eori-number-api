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
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.checkeorinumberapi.connectors.CheckEoriNumberConnector
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.customs.api.common.logging.CdsLogger
import uk.gov.hmrc.checkeorinumberapi.models._
import uk.gov.hmrc.customs.api.common.controllers.ErrorResponse.ErrorInternalServerError

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class SingleEoriController @Inject()(
  checkEoriNumberConnector: CheckEoriNumberConnector,
  cc: ControllerComponents,
  logger: CdsLogger
)(implicit ec: ExecutionContext) extends BackendController(cc) {

  def check(eoriNumber: EoriNumber): Action[AnyContent] = Action.async { implicit request =>
    checkEoriNumberConnector.check(eoriNumber).map {
      case Some(response) => response.headOption match {
        case r@Some(CheckResponse(_, true, _, _)) => Ok(Json.toJson(r))
        case r@Some(CheckResponse(_, false, _, _)) => NotFound(Json.toJson(r))
      }
    }.recover {
      case e =>
        logger.error(e.getMessage, e.fillInStackTrace())
        ErrorInternalServerError.JsonResult
    }
  }
}
