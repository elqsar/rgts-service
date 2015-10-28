package com.demo.domain.mojo


object MediaTypeMapper {
  def apply(cisionMediaTypeId: Long) {new MediaTypeMapping().mediaType.getOrElse(cisionMediaTypeId, null)}
}

class MediaTypeMapping {
  var mediaType = Map[Long, Long](
    16L -> 3694L,
    15L -> 3695L,
    45L -> 3696L,
    14L -> 3697L,
    8L -> 3698L,
    49L -> 3699L,
    49L -> 3700L,
    27L -> 3701L,
    56L -> 3702L,
    54L -> 3703L,
    79L -> 3706L,
    79L -> 3707L,
    75L -> 3708L
  )
}
