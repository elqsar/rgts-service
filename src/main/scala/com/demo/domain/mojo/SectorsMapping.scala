package com.demo.domain.mojo


object SectorsMapper {
  def apply(cisionSectorId: Long) {new SectorsMapping().sectors getOrElse(cisionSectorId, null)}
}

class SectorsMapping {
  var sectors: Map[Long, Long] = Map[Long, Long](
              39L -> 8L,
              3284L -> 11L,
              544L -> 14L,
              4855L -> 14L,
              81L -> 16L,
              322L -> 18L,
              1033L -> 18L,
              3L -> 44L,
              37L -> 46L,
              54L -> 47L,
              65L -> 53L,
              82L -> 54L,
              5733L -> 57L,
              5293L -> 58L,
              88L -> 60L,
              668L -> 60L,
              93L -> 61L,
              152L -> 61L,
              545L -> 63L,
              5706L -> 65L,
              42L -> 66L,
              5309L -> 68L,
              149L -> 69L,
              156L -> 70L,
              163L -> 72L,
              166L -> 73L,
              5322L -> 74L,
              913L -> 75L,
              65L -> 76L,
              5738L -> 77L,
              5734L -> 78L,
              184L -> 80L,
              4003L -> 83L,
              767L -> 84L,
              212L -> 86L,
              533L -> 87L,
              4118L -> 88L,
              232L -> 89L,
              241L -> 90L,
              4255L -> 90L,
              5338L -> 91L,
              5338L -> 92L,
              5445L -> 93L,
              266L -> 94L,
              257L -> 95L,
              5471L -> 96L,
              28L -> 101L,
              37L -> 107L,
              4666L -> 108L,
              611L -> 109L,
              529L -> 110L,
              5256L -> 111L,
              402L -> 115L,
              5333L -> 115L,
              89L -> 116L,
              429L -> 117L,
              444L -> 118L,
              51L -> 119L,
              446L -> 122L,
              647L -> 126L,
              725L -> 128L,
              747L -> 129L,
              307L -> 132L,
              16L -> 133L,
              181L -> 134L,
              18L -> 135L,
              5467L -> 136L,
              26L -> 137L,
              1045L -> 138L,
              3127L -> 139L,
              402L -> 140L,
              607L -> 141L,
              1042L -> 142L,
              35L -> 144L,
              4L -> 145L,
              3206L -> 146L,
              45L -> 147L,
              5331L -> 148L,
              5496L -> 149L,
              5L -> 150L,
              647L -> 151L,
              3199L -> 153L,
              3897L -> 154L,
              3302L -> 157L,
              3304L -> 158L,
              311L -> 159L,
              64L -> 160L,
              1047L -> 161L,
              73L -> 163L,
              613L -> 165L,
              18L -> 166L,
              651L -> 167L,
              625L -> 168L,
              714L -> 169L,
              108L -> 170L,
              618L -> 171L,
              548L -> 172L,
              5448L -> 173L,
              3899L -> 174L,
              136L -> 175L,
              3899L -> 177L,
              613L -> 182L,
              4778L -> 184L,
              149L -> 185L,
              3688L -> 186L,
              5L -> 188L,
              886L -> 190L,
              167L -> 191L,
              5475L -> 193L,
              1054L -> 194L,
              3088L -> 195L,
              564L -> 196L,
              463L -> 197L,
              276L -> 198L,
              108L -> 200L,
              4686L -> 202L,
              88L -> 205L,
              193L -> 206L,
              194L -> 207L,
              647L -> 210L,
              197L -> 211L,
              368L -> 212L,
              1069L -> 214L,
              3966L -> 215L,
              21L -> 218L,
              338L -> 219L,
              248L -> 222L,
              629L -> 223L,
              446L -> 226L,
              228L -> 227L,
              234L -> 228L,
              6L -> 229L,
              194L -> 230L,
              3067L -> 232L,
              682L -> 233L,
              1072L -> 235L,
              5065L -> 238L,
              585L -> 239L,
              4368L -> 241L,
              624L -> 242L,
              1049L -> 246L,
              28L -> 249L,
              4492L -> 250L,
              321L -> 252L,
              171L -> 256L,
              44L -> 258L,
              327L -> 261L,
              3899L -> 263L,
              332L -> 264L,
              28L -> 265L,
              3666L -> 266L,
              153L -> 267L,
              354L -> 268L,
              611L -> 269L,
              62L -> 270L,
              61L -> 272L,
              699L -> 276L,
              613L -> 277L,
              3386L -> 279L,
              4797L -> 281L,
              5829L -> 282L,
              204L -> 284L,
              388L -> 286L,
              3208L -> 287L,
              81L -> 290L,
              775L -> 292L,
              3958L -> 294L,
              276L -> 297L,
              647L -> 298L,
              622L -> 300L,
              446L -> 301L,
              446L -> 303L,
              5095L -> 305L,
              634L -> 307L,
              4689L -> 308L,
              461L -> 313L,
              81L -> 314L,
              637L -> 315L,
              891L -> 316L,
              45L -> 318L,
              5368L -> 323L,
              5425L -> 328L,
              4736L -> 329L,
              562L -> 330L,
              5292L -> 347L,
              2L -> 350L,
              208L -> 355L,
              3666L -> 356L,
              4827L -> 357L,
              54L -> 358L,
              5499L -> 359L,
              149L -> 360L,
              4598L -> 371L,
              767L -> 372L,
              4753L -> 373L,
              149L -> 374L,
              735L -> 377L,
              157L -> 378L,
              252L -> 379L,
              562L -> 380L,
              4721L -> 381L,
              1003L -> 382L,
              182L -> 384L,
              3907L -> 385L,
              7L -> 387L,
              3247L -> 388L,
              255L -> 389L,
              515L -> 390L,
              327L -> 391L,
              184L -> 393L,
              193L -> 394L,
              173L -> 395L,
              3237L -> 396L,
              5032L -> 397L,
              5256L -> 398L,
              5325L -> 399L,
              492L -> 400L,
              533L -> 401L,
              505L -> 402L,
              84L -> 403L,
              5256L -> 404L,
              3943L -> 405L,
              228L -> 407L,
              394L -> 408L,
              152L -> 409L,
              668L -> 410L,
              35L -> 412L,
              626L -> 413L,
              8L -> 414L,
              34L -> 415L,
              61L -> 416L,
              626L -> 417L,
              5464L -> 418L,
              915L -> 419L,
              5464L -> 420L,
              5293L -> 421L,
              61L -> 664L,
              34L -> 668L,
              4481L -> 669L,
              3096L -> 670L,
              3635L -> 671L,
              3153L -> 673L,
              507L -> 674L,
              34L -> 675L,
              5209L -> 676L,
              4453L -> 678L,
              3719L -> 679L,
              283L -> 680L,
              226L -> 681L,
              361L -> 682L,
              444L -> 723L,
              115L -> 724L,
              33L -> 725L,
              1056L -> 726L,
              371L -> 727L,
              392L -> 728L,
              5698L -> 739L,
              4534L -> 740L,
              456L -> 741L,
              5056L -> 743L,
              735L -> 744L,
              241L -> 745L,
              173L -> 746L,
              41L -> 747L,
              22L -> 749L,
              3982L -> 783L,
              36L -> 784L,
              322L -> 785L,
              115L -> 786L,
              171L -> 787L,
              6L -> 788L,
              358L -> 790L,
              97L -> 791L,
              913L -> 792L,
              371L -> 793L,
              2L -> 794L,
              244L -> 795L,
              213L -> 823L,
              4865L -> 882L,
              28L -> 902L,
              603L -> 922L,
              51L -> 942L,
              5335L -> 962L,
              5619L -> 963L,
              562L -> 964L,
              575L -> 965L,
              217L -> 982L,
              5538L -> 1002L,
              61L -> 1003L,
              28L -> 1004L,
              54L -> 1025L,
              902L -> 1028L,
              322L -> 1049L,
              244L -> 1090L,
              501L -> 1110L,
              3119L -> 1130L,
              727L -> 1131L,
              3899L -> 1133L,
              52L -> 1134L,
              727L -> 1690L,
              3966L -> 1691L,
              4489L -> 1692L,
              4782L -> 1693L,
              5539L -> 1730L,
              5531L -> 1790L,
              179L -> 3411L,
              5457L -> 3412L,
              5252L -> 3413L,
              4486L -> 3414L,
              81L -> 3415L,
              529L -> 3416L,
              898L -> 3417L,
              365L -> 3418L,
              4969L -> 3419L,
              516L -> 3420L,
              1073L -> 3513L,
              41L -> 3633L,
              44L -> 3710L,
              206L -> 3725L,
              738L -> 3726L,
              54L -> 3727L,
              182L -> 3728L,
              301L -> 3729L,
              3982L -> 3730L,
              261L -> 3731L,
              262L -> 3732L,
              301L -> 3733L,
              574L -> 3734L,
              4574L -> 3735L,
              3982L -> 3736L,
              581L -> 3737L,
              738L -> 3738L,
              58L -> 3739L,
              43L -> 3740L,
              528L -> 3741L,
              552L -> 3742L,
              301L -> 3743L,
              262L -> 3744L,
              4847L -> 3745L,
              264L -> 3746L,
              3985L -> 3747L,
              5488L -> 3748L,
              5657L -> 3749L,
              551L -> 3750L,
              568L -> 3751L,
              577L -> 3752L,
              596L -> 3753L,
              575L -> 3754L,
              456L -> 3755L,
              325L -> 3756L,
              371L -> 3757L,
              4925L -> 3758L,
              569L -> 3759L,
              3602L -> 3760L,
              3062L -> 3761L,
              3046L -> 3762L,
              572L -> 3763L,
              3901L -> 3764L,
              3562L -> 3765L,
              579L -> 3766L,
              3901L -> 3767L,
              4007L -> 3768L,
              4445L -> 3769L,
              4095L -> 3770L,
              5566L -> 3771L,
              5235L -> 3772L,
              4775L -> 3773L,
              4789L -> 3774L,
              5388L -> 3775L,
              4102L -> 3776L,
              5084L -> 3777L,
              262L -> 3778L,
              264L -> 3779L,
              47L -> 3807L,
              922L -> 3808L,
              915L -> 3809L,
              47L -> 3810L,
              47L -> 3811L,
              911L -> 3812L,
              3454L -> 3813L,
              105L -> 3814L,
              104L -> 3815L,
              3119L -> 3817L,
              41L -> 3818L,
              5293L -> 3819L,
              5322L -> 3820L,
              156L -> 3821L,
              913L -> 3822L,
              163L -> 3823L,
              173L -> 3824L,
              3863L -> 3825L,
              916L -> 3826L,
              5338L -> 3827L,
              244L -> 3828L,
              288L -> 3829L,
              34L -> 3830L,
              4453L -> 3831L,
              171L -> 3832L,
              322L -> 3833L,
              1033L -> 3834L,
              354L -> 3835L,
              33L -> 3836L,
              244L -> 3837L,
              4865L -> 3838L,
              402L -> 3839L,
              429L -> 3840L,
              446L -> 3841L,
              456L -> 3842L,
              647L -> 3843L,
              5401L -> 3899L,
              915L -> 3900L,
              678L -> 3901L,
              3481L -> 3902L,
              3498L -> 3903L,
              4255L -> 3904L,
              5587L -> 3905L,
              5285L -> 3906L,
              3781L -> 3907L,
              901L -> 3908L,
              5538L -> 3909L,
              42L -> 3910L,
              731L -> 3911L,
              3864L -> 3912L,
              96L -> 3913L,
              3481L -> 3914L,
              4742L -> 3915L,
              38L -> 3916L,
              5287L -> 3918L,
              22L -> 3919L,
              226L -> 3920L
  )
}
