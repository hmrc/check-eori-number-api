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

package uk.gov.hmrc.checkeorinumberapi.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.{Configuration, Environment}
import uk.gov.hmrc.checkeorinumberapi.config.AppContext
import uk.gov.hmrc.checkeorinumberapi.models.{CheckResponse, EoriNumber}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.ExecutionContext

trait BaseSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")
  val env: Environment                                 = Environment.simple()
  val configuration: Configuration                     = Configuration.load(env)
  val serviceConfig                                    = new ServicesConfig(configuration)
  val appContext                                       = new AppContext(configuration, serviceConfig)
  implicit val executionContext: ExecutionContext      = app.injector.instanceOf[ExecutionContext]
  implicit val headerCarrier: HeaderCarrier            = HeaderCarrier()

  val eoriNumber: EoriNumber              = "GB123456789000"
  val invalidEoriNumber: EoriNumber       = "GB999999999999"
  val checkResponse: CheckResponse        = CheckResponse(eoriNumber, valid = true, None)
  val invalidCheckResponse: CheckResponse = CheckResponse(invalidEoriNumber, valid = false, None)

  val notAEoriNumber: List[EoriNumber]       = List("AA123456789")
  val xiEoriNumbers: List[EoriNumber]        = List("XI123456789123", "XI3219876543210")
  val validAndInvalidEoris: List[EoriNumber] = List(eoriNumber, invalidEoriNumber)
  val eorisExceedingLimit: List[EoriNumber]  = List(
    eoriNumber,
    eoriNumber,
    eoriNumber,
    eoriNumber,
    eoriNumber,
    invalidEoriNumber,
    invalidEoriNumber,
    invalidEoriNumber,
    invalidEoriNumber,
    invalidEoriNumber,
    invalidEoriNumber
  )

}
