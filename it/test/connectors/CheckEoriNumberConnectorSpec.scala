/*
 * Copyright 2024 HM Revenue & Customs
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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlMatching}
import org.scalatest.OptionValues
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import play.api.Application
import play.api.http.Status.{NOT_FOUND, OK}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.checkeorinumberapi.connectors.CheckEoriNumberConnectorImpl
import uk.gov.hmrc.checkeorinumberapi.models.{CheckMultipleEoriNumbersRequest, CheckResponse, EoriNumber}
import uk.gov.hmrc.http.{HeaderCarrier, UpstreamErrorResponse}
import org.scalatest.matchers.must.Matchers._
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import utils.WiremockServer

import scala.concurrent.ExecutionContext.Implicits.global
import java.time.{ZoneId, ZonedDateTime}

class CheckEoriNumberConnectorSpec
    extends AnyFreeSpec
    with WiremockServer
    with ScalaFutures
    with OptionValues
    with IntegrationPatience {

  "CheckEoriNumberConnector" - {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    lazy val app: Application = new GuiceApplicationBuilder()
      .configure(
        conf = "microservice.services.check-eori-number.port" -> mockServer.port()
      ).build()

    lazy val connector = app.injector.instanceOf[CheckEoriNumberConnectorImpl]

    "return eori check response as 'true' for valid input" in {
      val eoriNumber: EoriNumber = "GB123456787665"
      val processingDate         = ZonedDateTime.now.withZoneSameInstant(ZoneId.of("Europe/London"))
      val expectedCheckResponse = CheckResponse(
        eoriNumber,
        valid = true,
        None,
        processingDate
      )

      val jsonResponse = Json.toJson(List(expectedCheckResponse))
      stubPost(jsonResponse, OK)

      val response = connector.checkEoriNumbers(CheckMultipleEoriNumbersRequest(List(eoriNumber)))
      whenReady(response) { res =>
        res.value.head.copy(processingDate = processingDate) mustEqual expectedCheckResponse
      }
    }

    "return UpstreamErrorResponse and status NOT_FOUND" in {

      val eoriNumber: EoriNumber = "InvalidEORINumber"
      stubPost(Json.obj(), NOT_FOUND)

      val response = intercept[UpstreamErrorResponse] {
        await(connector.checkEoriNumbers(CheckMultipleEoriNumbersRequest(List(eoriNumber))))
      }

      response.statusCode mustBe NOT_FOUND

    }

  }

  private def stubPost(body: JsValue, status: Int) = {
    mockServer.stubFor(
      post(urlMatching(s"/check-eori-number/check-multiple-eori"))
        .willReturn(
          aResponse()
            .withStatus(status)
            withBody (body.toString())
        )
    )
  }

}
