package com.mikelalvarezgo.socikutxa.product.application

import cats.implicits._
import com.mikelalvarezgo.socikutxa.product.application.GetProductsUseCase.GetProductsQuery
import com.mikelalvarezgo.socikutxa.product.domain.contract.ProductRepository
import com.mikelalvarezgo.socikutxa.product.domain.Product
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.{Query, UseCase}

class GetProductsUseCase[P[_]](productRepository: ProductRepository[P])
    extends UseCase[P, GetProductsQuery] {
  override def execute(r: GetProductsQuery): Validation[P[List[Product]]] =
    productRepository.findAll().valid

}
object GetProductsUseCase {
  case class GetProductsQuery() extends Query{
    override type Response = List[Product]
  }
}
