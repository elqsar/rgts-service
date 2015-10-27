package com.demo.domain

case class Contact(
                    Id: Long,
                    OutletId: Long,
                    MediaContactId: Long,
                    FirstName: String,
                    MiddleName: String,
                    LastName: String,
                    NameSuffix: String,
                    Gender: String,
                    Title: String,
                    ExternalIds: List[ExternalId],
                    OutletExternalIds: List[OutletExternalId],
                    Topics: Topics,
                    Profile: String,
                    Salutation: String,
                    Biography: String,
                    Status: Long,
                    Communcation: Communcation,
                    Address: Address,
                    Languages: List[Language],
                    PreferredContactTypes: List[PreferredContactType],
                    OptInType: Long,
                    NotaPRcontactFlag: Boolean
                    )

case class Topics(Printing3D: Double)

case class Language(name: String)

case class ExternalId(id: Long)

case class OutletExternalId(id: Long)

case class PreferredContactType(name: String)

case class Address(
                    AddressLine1: String,
                    AddressLine2: String,
                    City: String,
                    County: String,
                    State: Long,
                    PostalCode: String,
                    Country: Long,
                    AddressType: Long,
                    IsParentAddress: Boolean
                    )

case class Communcation(
                         DefaultEmail: String,
                         AlternateEmail: String,
                         DefaultFax: String,
                         AlternateFax: String,
                         DefaultPhone: String,
                         AlternatePhone: String,
                         CellPhone: String,
                         DirectEmail: String,
                         DirectFax: String,
                         DirectHomepage: String,
                         DirectPhone: String,
                         EdCalURL: String,
                         Facebook: String,
                         GooglePlus: String,
                         Instagram: String,
                         LinkedIn: String,
                         MainEmail: String,
                         MainFax: String,
                         MainHomepage: String,
                         MainPhone: String,
                         OtherSocialProfile: String,
                         Pinterest: String,
                         TollFreePhone: String,
                         Twitter: String,
                         YouTube: String
                         )

case class Outlet(Id: Int)
