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

package uk.gov.hmrc.checkeorinumberapi.models

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import play.api.libs.json._

case class CheckResponse (
  eori: EoriNumber,
  valid: Boolean,
  companyDetails: Option[CompanyDetails],
  processingDate: ProcessingDate = ZonedDateTime.now.withZoneSameInstant(ZoneId.of("Europe/London"))
)

object CheckResponse {
  implicit val processingDateWriter: Writes[ProcessingDate] = new Writes[ProcessingDate] {
    override def writes(o: ProcessingDate): JsValue =
      JsString(o.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")))
  }
  implicit val checkResponseFormat: Format[CheckResponse] = {
    Json.format[CheckResponse]
  }
}