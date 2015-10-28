package com.demo

import com.demo.domain.mojo.CityMapper
import org.scalatest.{FlatSpec, Matchers}


class CityMapperSpec extends FlatSpec with Matchers {
  "A Cision city ID" should "be mapped to String" in {
    val result = CityMapper(215643)
    result should be ("York")
  }
}
