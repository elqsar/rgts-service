package com.demo.domain.mojo


object FrequencyMapper {
  def apply(cisionFrequencyId: Long) {new FrequencyMapping().frequencyMap getOrElse(cisionFrequencyId, null)}
}

class FrequencyMapping {
  // cision frequency ID | gorkana Frequency ID
  var frequencyMap: Map[Long, Long] =
    Map(
      230L -> 3L, // 10 issues per year
      233L -> 12L, // 2 issues per year
      605L -> 7L, // 2 times a week
      18697L -> 13L, // 3 issues per year
      606L -> 38L, // 3 times a week
      231L -> 30L, // 5 issues per year
      607L -> 131L, // Ad hoc
      231L -> 30L, // Bi Monthly
      226L -> 1L, // Daily
      228L -> 2L, // Fortnightly
      230L -> 3L, // Monthly
      607L -> 41L, // NA
      232L -> 5L, // Quarterly
      226L -> 9L, // Website
      227L -> 4L, // Weekly
      234L -> 6L // Yearly
    )
}
