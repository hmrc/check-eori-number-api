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

package uk.gov.hmrc.checkeorinumberapi.config

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class AppContext @Inject() (servicesConfig: ServicesConfig) {

  private lazy val chenUrl: String = servicesConfig.getConfString("check-eori-number.url", "")
  lazy val eisUrl: String          = s"${servicesConfig.baseUrl("check-eori-number")}/$chenUrl"

  lazy val apiContext: String                 = servicesConfig.getString("api.context")
  lazy val allowXiEoriNumbers: Boolean        = servicesConfig.getBoolean("allowXiEoriNumbers")
  lazy val eisApiLimit: Int                   = servicesConfig.getInt("eisApiLimit")
  lazy val formattedJsonResponseFlag: Boolean = servicesConfig.getBoolean("formattedJsonResponseFlag");
}
