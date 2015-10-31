package com.demo.domain.mojo

import com.demo.domain.cision.{Communication, Contact}

case class MojoContact( rdsId: Long,
                        id: Long,
                        rdsOutletId: Long,
                        outlets: List[MojoOutlet],
                        firstName: String,
                        lastName: String,
                        title: String,
                        jobTitle: String,
                        sectors: List[Sector],
                        shortNote: String,
                        salutation: String,
                        biography: String,
                        emailWork: String,
                        mobileNumber: String,
                        emailPersonal: String,
                        linkedInUrl: String,
                        twitterId: String,
                        language: String,
                        addresses: List[MojoAddress]
)

case class MojoAddress(primary: Boolean,
                      branchName: String,
                      line1: String,
                      line2: String,
                      line3: String,
                      city: String,
                      //state:      not needed now
                      postcode: String,
                      country: MojoCountry,
                      phone: String,
                      fax: String )

case class MojoCountry (id: Long)

case class Sector (id: Long)

case class MojoOutlet (id: Long)

class MojoContactMapper(contact: Contact) {


  def map(): MojoContact = {
    new MojoContact(
      rdsId = contact.Id,
      id = 0,
      rdsOutletId = contact.OutletId,
      outlets = List(),
      firstName = contact.FirstName,
      lastName = contact.LastName,
      title = contact.NameSuffix,
      jobTitle = contact.Title,
      sectors = List(),
      shortNote = contact.Profile,
      salutation = contact.Salutation,
      biography = contact.Biography,
      emailWork = contact.Communication.getOrElse(new Communication()).DefaultEmail,
      mobileNumber = contact.Communication.getOrElse(new Communication()).CellPhone,
      emailPersonal = contact.Communication.getOrElse(new Communication()).DirectEmail,
      linkedInUrl = contact.Communication.getOrElse(new Communication()).LinkedIn,
      twitterId = contact.Communication.getOrElse(new Communication()).Twitter,
      language = "",
      addresses = List()
    )
  }
}

object MojoContactMapper {
  def apply(contact: Contact) = new MojoContactMapper(contact).map()
}

