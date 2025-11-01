package com.mikelalvarezgo.socikutxa.product.application

import cats.Applicative
import cats.implicits._
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase.{
  ImportProductsCommand,
  ProductParsed
}
import com.mikelalvarezgo.socikutxa.product.domain.contract.ProductRepository
import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductCategory, ProductId}
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.{Command, UseCase}

class ImportProductsUseCase[P[_] : Applicative](productRepository: ProductRepository[P])
    extends UseCase[P, ImportProductsCommand] {
  override def execute(r: ImportProductsCommand): Validation[P[Unit]] =
    r.products.traverse(validate).map(saveAllProducts)

  private def saveAllProducts(products: List[Product]): P[Unit] =
    products.traverse_(productRepository.create)
  private def validate(p: ProductParsed): Validation[Product]   = {
    (
        ProductId.create.valid,
        p.name.valid,
        p.price.valid,
        ProductCategory.fromString(p.category)
    ).mapN(
        (id, name, price, category) =>
          Product(
              id = id,
              productName = name,
              productCategory = category,
              price = price
          )
    )
  }

}
object ImportProductsUseCase {
  case class ImportProductsCommand(products: List[ProductParsed]) extends Command

  case class ProductParsed(name: String, price: Float, category: String)
}
