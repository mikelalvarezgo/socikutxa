package com.mikelalvarezgo.socikutxa.product.infrastructure

import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase.ProductParsed
import com.mikelalvarezgo.socikutxa.product.domain.error.ProductParsingException
import org.apache.poi.ss.usermodel.{Row, WorkbookFactory}

import java.io.InputStream
import scala.jdk.CollectionConverters._
import scala.util.Try

object XlsxProductParser {

  def parse(inputStream: InputStream): Either[ProductParsingException, List[ProductParsed]] = {
    Try {
      val workbook = WorkbookFactory.create(inputStream)
      val sheet    = workbook.getSheetAt(0)
      val rows     = sheet.iterator().asScala.toList

      if (rows.isEmpty) {
        return Left(ProductParsingException("Empty file"))
      }

      val headerRow = rows.head
      val headers   = extractHeaders(headerRow)

      val nameIndex     = headers.indexOf("name")
      val priceIndex    = headers.indexOf("price")
      val categoryIndex = headers.indexOf("category")

      if (nameIndex == -1 || priceIndex == -1 || categoryIndex == -1) {
        return Left(ProductParsingException("Missing required headers: name, price, category"))
      }

      val dataRows = rows.tail

      val products = dataRows.flatMap { row =>
        parseRow(row, nameIndex, priceIndex, categoryIndex)
      }

      workbook.close()
      Right(products)
    }.toEither.left.map(e => ProductParsingException(e.getMessage)).flatMap(identity)
  }

  private def extractHeaders(row: Row): List[String] = {
    row
      .cellIterator()
      .asScala
      .map(cell => cell.getStringCellValue.trim.toLowerCase)
      .toList
  }

  private def parseRow(
      row: Row,
      nameIndex: Int,
      priceIndex: Int,
      categoryIndex: Int
  ): Option[ProductParsed] = {
    Try {
      val name     = row.getCell(nameIndex).getStringCellValue
      val price    = row.getCell(priceIndex).getNumericCellValue.toFloat
      val category = row.getCell(categoryIndex).getStringCellValue

      ProductParsed(name, price, category)
    }.toOption
  }
}
