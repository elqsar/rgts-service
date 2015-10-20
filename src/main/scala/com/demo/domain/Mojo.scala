package com.demo.domain

case class MojoContact(name: String, id: Long)

class MojoContactMapper(contact: Contact) {
  def map(): MojoContact = {
    new MojoContact(
      name = contact.FirstName,
      id = contact.Id
    )
  }
}

object MojoContactMapper {
  def apply(contact: Contact) = new MojoContactMapper(contact)
}