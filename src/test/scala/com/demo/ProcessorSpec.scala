package com.demo

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.demo.actors.processor.Processor
import com.demo.domain.{Address, Communcation, Topics, Contact}
import com.demo.messages.Messages.{RabbitMessage, RdsReadyContact}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ProcessorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGDS-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A processor" must {
    "parse json, map to object and send forward" in {
      val probe = TestProbe()
      val processor = system.actorOf(Processor.props(probe.ref))

      val body =
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
        """.stripMargin

      val mediaType = "Contact"
      val message = RabbitMessage(mediaType, body.getBytes("UTF-8"))

      processor ! message

      val expectedMessage = Contact(2045883,974441,2900820,"Sergio",null,"UK1",null,null,"UK1 contact",List(),List(),Topics(90.0),"",null,null,1,Communcation("tester3@cision.com",null,null,null,null,null,null,"tester3@cision.com",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null),Address(null,null,"London","London",400,null,7,1,IsParentAddress = false),List(),List(),0,NotaPRcontactFlag = false)
      probe.expectMsg(RdsReadyContact(expectedMessage))
    }
  }

}
