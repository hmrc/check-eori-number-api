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
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.checkeorinumberapi.config.AppConfig
import uk.gov.hmrc.checkeorinumberapi.models._

import scala.concurrent.Future

@Singleton()
class SingleEoriController @Inject()(
  appConfig: AppConfig,
  cc: ControllerComponents
) extends BackendController(cc) {

  def check(eoriNumber: EoriNumber): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok("Hello world"))
  }

}
