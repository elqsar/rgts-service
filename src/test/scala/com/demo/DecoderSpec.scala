package com.demo

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.demo.actors.consumer.ConsumerSupervisor.BatchQueue
import com.demo.actors.processor.Decoder
import com.demo.messages.Messages._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class DecoderSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGT-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A decoder" must {
    "decode contact properly" in {
      val processor = TestProbe()
      val decoder = system.actorOf(Decoder.props(processor.ref))

      val metadata = RabbitMetadata(BatchQueue, "Contact", 1L)
      val body = createByteBody()
      val message: RabbitMessage = RabbitMessageContact(metadata, body)

      decoder ! message

      processor.expectMsgType[ProcessContact]
    }
  }

  "A decoder" must {
    "decode outlet properly" in {
      val processor = TestProbe()
      val decoder = system.actorOf(Decoder.props(processor.ref))

      val metadata = RabbitMetadata(BatchQueue, "Contact", 1L)
      val body = createByteBody()
      val message: RabbitMessage = RabbitMessageOutlet(metadata, body)

      decoder ! message

      processor.expectMsgType[ProcessOutlet]
    }
  }

  def createByteBody(): Array[Byte] = {
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
    """.stripMargin.getBytes("UTF-8")
  }

}
