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

package uk.gov.hmrc.checkeorinumberapi.connectors

import com.google.inject.ImplementedBy

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.checkeorinumberapi.config.AppContext
import uk.gov.hmrc.checkeorinumberapi.models.{CheckMultipleEoriNumbersRequest, CheckResponse}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, StringContextOps}
import uk.gov.hmrc.http.HttpReads.Implicits.*

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CheckEoriNumberConnectorImpl])
trait CheckEoriNumberConnector {

  def checkEoriNumbers(
    check: CheckMultipleEoriNumbersRequest
  )(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[Option[List[CheckResponse]]]

}

@Singleton
class CheckEoriNumberConnectorImpl @Inject() (
  http: HttpClient,
  appContext: AppContext
) extends CheckEoriNumberConnector {

  def checkEoriNumbers(
    check: CheckMultipleEoriNumbersRequest
  )(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[Option[List[CheckResponse]]] =
    http.POST[CheckMultipleEoriNumbersRequest, List[CheckResponse]](
      url = url"${appContext.eisUrl}/check-multiple-eori",
      body = check
    ).map(Some(_))

}
