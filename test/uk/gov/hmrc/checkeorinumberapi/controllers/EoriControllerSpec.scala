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

import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import uk.gov.hmrc.checkeorinumberapi.connectors.CheckEoriNumberConnector
import uk.gov.hmrc.checkeorinumberapi.models.{CheckMultipleEoriNumbersRequest, CheckResponse, EoriNumber}
import uk.gov.hmrc.checkeorinumberapi.utils.BaseSpec
import uk.gov.hmrc.customs.api.common.logging.CdsLogger
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class EoriControllerSpec extends BaseSpec {

  val eoriNumber: EoriNumber = "GB123456789000"
  val invalidEoriNumber: EoriNumber = "GB999999999999"
  val checkResponse = CheckResponse(eoriNumber, true, None)
  val invalidCheckResponse = CheckResponse(invalidEoriNumber, false, None)

  val mockLogger = new CdsLogger(serviceConfig)

  val mockConnector = new CheckEoriNumberConnector {

    override def checkEoriNumbers(
      check: CheckMultipleEoriNumbersRequest
    )(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext
    ): Future[Option[List[CheckResponse]]] = check.eoriNumbers match {
      case `eoriNumber`::Nil  => Future.successful(Some(List(checkResponse)))
      case `invalidEoriNumber`::Nil  => Future.successful(Some(List(invalidCheckResponse)))
      case `eoriNumber`::`invalidEoriNumber`::Nil  => Future.successful(Some(List(checkResponse, invalidCheckResponse)))
    }
  }

  val controller = new EoriController(
    mockConnector,
    stubControllerComponents(),
    mockLogger
  )

  "GET /:eoriNumber" should {
    "return 200" in {
      val result = controller.check(eoriNumber)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result) shouldEqual Json.toJson(checkResponse)
    }
    "return expected valid-eori Json" in {
      val result = controller.check(eoriNumber)(fakeRequest)
      contentAsJson(result) shouldEqual Json.toJson(checkResponse)
    }
    "return expected invalid-eori Json" in {
      val result = controller.check(invalidEoriNumber)(fakeRequest)
      contentAsJson(result) shouldEqual Json.toJson(invalidCheckResponse)
    }
  }

  "POST /check-multiple-eori" should {
    val jsonBody = Json.toJson(
      CheckMultipleEoriNumbersRequest(
        List(eoriNumber, invalidEoriNumber)
      )
    )
    val request = FakeRequest("POST", "/check-multiple-eori", FakeHeaders(), jsonBody)
    "return 200" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request)
      status(result) shouldBe Status.OK
    }
    "return expected Json" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request)
      contentAsJson(result) shouldEqual Json.toJson(
        List(checkResponse, invalidCheckResponse)
      )
    }
  }

}
