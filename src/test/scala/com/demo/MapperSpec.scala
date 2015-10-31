package com.demo

import com.demo.domain.cision.Contact
import com.demo.domain.mojo.MojoContactMapper
import org.scalatest.{Matchers, FlatSpec}

class MapperSpec extends FlatSpec with Matchers {

  "A rds contact" should "mapped to mojo contact" in {
    val rdsContact = Contact(
      Id = 1,
      OutletId = 1,
      MediaContactId = 1,
      FirstName = "John",
      MiddleName = "Doe",
      LastName = "Doe",
      NameSuffix = "The",
      Gender = "Male",
      Title = "No title",
      ExternalIds = null,
      OutletExternalIds = null,
      Topics = null,
      Profile = "Profile",
      Salutation = "Hello",
      Biography = "The biography",
      Status = 1,
      Communication = Option empty,
      Address = null,
      Languages = null,
      PreferredContactTypes = null,
      OptInType = 1,
      NotaPRcontactFlag = false
    )
    val result = MojoContactMapper(rdsContact)

    result.firstName should be ("John")
    result.lastName should be ("Doe")
    result.rdsId should be (1)
  }

}
