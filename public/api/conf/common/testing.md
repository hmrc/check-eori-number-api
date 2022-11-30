You can use the sandbox environment to [test this API](https://developer.service.hmrc.gov.uk/api-documentation/docs/testing).

In the sandbox environment the last digit of each EORI Number will return each type of CheckResponse:
   * 0 or 1 Vaild with no TraderName or Address
   * 2 to 5 Vaild with TraderName and Address
   * 6 or 7 Vaild with no TraderName or Address
   * 8 or 9 Invalid

[Access a set of mock EORI numbers](https://github.com/hmrc/check-eori-number-api/tree/master/public/api/conf/1.0/test-data) to use when testing in the sandbox environment. 
