/*
 * Copyright 2022 HM Revenue & Customs
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
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class EoriControllerSpec extends BaseSpec {

  val mockConnector: CheckEoriNumberConnector = new CheckEoriNumberConnector {

    override def checkEoriNumbers(
      check: CheckMultipleEoriNumbersRequest
    )(implicit
      hc: HeaderCarrier,
      ec: ExecutionContext
    ): Future[Option[List[CheckResponse]]] = check.eoris match {
      case `eoriNumber` :: Nil                        => Future.successful(Some(List(checkResponse)))
      case `invalidEoriNumber` :: Nil                 => Future.successful(Some(List(invalidCheckResponse)))
      case `eoriNumber` :: `invalidEoriNumber` :: Nil =>
        Future.successful(Some(List(checkResponse, invalidCheckResponse)))
      case _                                          => throw new RuntimeException("Check eori number request is invalid")
    }
  }

  val controller = new EoriController(
    mockConnector,
    stubControllerComponents(),
    appContext
  )

  "POST /check-multiple-eori" should {

    def request(eoriNumbers: List[EoriNumber] = List.empty) = FakeRequest(
      "POST",
      "/check-multiple-eori",
      FakeHeaders(),
      Json.toJson(
        CheckMultipleEoriNumbersRequest(eoriNumbers)
      )
    )

    "return 200" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request(validAndInvalidEoris))
      status(result) shouldBe Status.OK
    }

    "return 400 for an empty list" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request())
      status(result) shouldBe Status.BAD_REQUEST
      contentAsString(result) should include(
        "Invalid payload - one or more EORI numbers are required in your body"
      )
    }

    "return 400 for exceeding eori limit" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request(eorisExceedingLimit))
      status(result) shouldBe Status.BAD_REQUEST
      contentAsString(result) should include(
        s"Invalid payload - you have exceeded the maximum of ${appContext.eisApiLimit} EORI numbers"
      )
    }

    "return 400 for payload which doesn't match the eori regex" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request(List("AA123456789")))
      status(result) shouldBe Status.BAD_REQUEST
      contentAsString(result) should include(
        "Invalid payload - one or more EORI numbers are not valid, " +
          "ensure all of your EORI numbers match ^GB[0-9]{12,15}$"
      )
    }

    "return 400 for payload which features an XI number" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request(xiEoriNumbers))
      status(result) shouldBe Status.BAD_REQUEST
      contentAsString(result) should include(
        "Invalid payload - one or more EORI numbers begin with XI." +
          " To check an EORI number that starts with XI, " +
          "use the EORI checker service on the European Commission website"
      )
    }

    "return expected Json" in {
      val result: Future[play.api.mvc.Result] = controller.checkMultipleEoris().apply(request(validAndInvalidEoris))
      contentAsJson(result) shouldEqual Json.toJson(
        List(checkResponse, invalidCheckResponse)
      )
    }
  }

}
