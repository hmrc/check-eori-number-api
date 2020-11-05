package uk.gov.hmrc.checkeorinumberapi.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.checkeorinumberapi.config.AppConfig

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
