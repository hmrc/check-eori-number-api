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

package uk.gov.hmrc.checkeorinumberapi.connectors

import com.google.inject.ImplementedBy
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.checkeorinumberapi.models.{CheckMultipleEoriNumbersRequest, CheckResponse, EoriNumber}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CheckEoriNumberConnectorImpl])
trait CheckEoriNumberConnector {

  def checkEoriNumbers(
    check: CheckMultipleEoriNumbersRequest
  )(
    implicit hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[Option[List[CheckResponse]]]

}

@Singleton
class CheckEoriNumberConnectorImpl @Inject()(
  http: HttpClient,
  environment: Environment,
  configuration: Configuration,
  servicesConfig: ServicesConfig
) extends CheckEoriNumberConnector {

  lazy val chenUrl: String = servicesConfig.getConfString("check-eori-number.url", "")
  lazy val eisUrl: String = s"${servicesConfig.baseUrl("check-eori-number")}/$chenUrl"
  
  // TODO decide how big the lookup list can be!
  def checkEoriNumbers(
    check: CheckMultipleEoriNumbersRequest
  )(
    implicit hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[Option[List[CheckResponse]]] =
    http.POST[CheckMultipleEoriNumbersRequest, List[CheckResponse]](
      url = s"$eisUrl/check-multiple-eori", body = check
    ).map(Some(_))

}