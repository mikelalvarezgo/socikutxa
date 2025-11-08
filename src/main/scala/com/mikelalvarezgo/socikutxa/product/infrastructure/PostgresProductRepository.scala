package com.mikelalvarezgo.socikutxa.product.infrastructure

import cats.data.OptionT
import cats.effect.IO
import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductId}
import com.mikelalvarezgo.socikutxa.product.domain.contract.ProductRepository
import doobie.Transactor
import doobie.implicits.toSqlInterpolator
import doobie.implicits._

class PostgresProductRepository(transactor: Transactor[IO])
    extends ProductRepository[IO]
    with PostgresProductOps {
  override def create(p: Product): IO[Unit] = {
    sql"""
          INSERT INTO product (id, category, name, price)
          VALUES (${p.id.raw}, ${p.productCategory.value}, ${p.productName},${p.price})
        """.update.run.transact(transactor).void
  }

  override def findBy(id: ProductId): OptionT[IO, Product] = {
    OptionT[IO, Product](
        sql"""
        SELECT id, name, category, price FROM product WHERE id = ${id.raw}
      """.query[Product]
          .option
          .transact(transactor)
    )
  }

  override def findAll(): IO[List[Product]] = {
    sql"""
      SELECT id, name, category, price FROM product
    """
      .query[Product]
      .stream
      .compile
      .toList
      .transact(transactor)
  }

  def deleteAll(): IO[Unit] = {
    sql"""
      DELETE FROM product
    """.update.run.transact(transactor).void
  }
}
