package com.mikelalvarezgo.socikutxa.product.infrastructure

import com.mikelalvarezgo.socikutxa.product.domain.{ProductCategory, ProductId}
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._

object ProductJsonCodec {
  implicit val productIdEncoder: Encoder[ProductId] = Encoder.encodeUUID.contramap(_.value)
  implicit val productIdDecoder: Decoder[ProductId] = Decoder.decodeUUID.map(ProductId.apply)

  implicit val productCategoryEncoder: Encoder[ProductCategory] =
    Encoder.encodeString.contramap(_.value)
  implicit val productCategoryDecoder: Decoder[ProductCategory] = Decoder.decodeString.emap(
      str => ProductCategory.fromString(str).toEither.left.map(_.head.message)
  )
}
