package com.demo

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.demo.actors.processor.Processor
import com.demo.messages.Messages._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ProcessorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGT-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A processor" must {
    "map to RDS contact and send forward" in {
      val probe = TestProbe()
      val processor = system.actorOf(Processor.props(probe.ref))

      val metadata = RabbitMetadata(BatchQueue, "Contact", 1L)
      val json = createContactJson()
      val message = ProcessContact(metadata, json)

      processor ! message

      val result = probe.expectMsgType[RdsReadyContact]

      result.contact.Id should be(2045883)
    }
  }

  "A processor" must {
    "map to RDS outlet and send forward" in {
      val probe = TestProbe()
      val processor = system.actorOf(Processor.props(probe.ref))

      val metadata = RabbitMetadata(BatchQueue, "Outlet", 1L)
      val json = createOutletJson()
      val message = ProcessOutlet(metadata, json)

      processor ! message

      val result = probe.expectMsgType[RdsReadyOutlet]

      result.outlet.Id should be(1)
    }
  }

  def createOutletJson(): JValue = {
    implicit val formats = DefaultFormats

    parse(
      """
        |{
        | "Id": 1
        |}
      """.stripMargin)
  }

  def createContactJson(): JValue = {
    implicit val formats = DefaultFormats

    parse(
      """
        |{
        |  "Id": 2045883,
        |  "OutletId": 974441,
        |  "MediaContactId": 2900820,
        |  "FirstName": "Sergio",
        |  "MiddleName": null,
        |  "LastName": "UK1",
        |  "NameSuffix": null,
        |  "Gender": null,
        |  "Title": "UK1 contact",
        |  "ExternalIds": [],
        |  "OutletExternalIds": [],
        |  "Topics": {
        |    "Printing3D": 90
        |  },
        |  "Profile": "",
        |  "Salutation": null,
        |  "Biography": null,
        |  "Status": 1,
        |  "Communcation": {
        |    "DefaultEmail": "tester3@cision.com",
        |    "AlternateEmail": null,
        |    "DefaultFax": null,
        |    "AlternateFax": null,
        |    "DefaultPhone": null,
        |    "AlternatePhone": null,
        |    "CellPhone": null,
        |    "DirectEmail": "tester3@cision.com",
        |    "DirectFax": null,
        |    "DirectHomepage": null,
        |    "DirectPhone": null,
        |    "EdCalURL": null,
        |    "Facebook": null,
        |    "GooglePlus": null,
        |    "Instagram": null,
        |    "LinkedIn": null,
        |    "MainEmail": null,
        |    "MainFax": null,
        |    "MainHomepage": null,
        |    "MainPhone": null,
        |    "OtherSocialProfile": null,
        |    "Pinterest": null,
        |    "TollFreePhone": null,
        |    "Twitter": null,
        |    "YouTube": null
        |  },
        |  "Address": {
        |    "AddressLine1": null,
        |    "AddressLine2": null,
        |    "City": "London",
        |    "County": "London",
        |    "State": 400,
        |    "PostalCode": null,
        |    "Country": 7,
        |    "AddressType": 1,
        |    "IsParentAddress": false
        |  },
        |  "Languages": [],
        |  "PreferredContactTypes": [],
        |  "OptInType": 0,
        |  "NotaPRcontactFlag": false
        |}
      """.stripMargin)
  }
}
