package com.mikelalvarezgo.socikutxa.modules.product.infrastructure

import com.mikelalvarezgo.socikutxa.infrastructure.BehaviourTestCase
import com.mikelalvarezgo.socikutxa.product.infrastructure.XlsxProductParser
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

class XlsxProductParserTest extends BehaviourTestCase {

  "XlsxProductParser" should {
    "parse a valid xlsx file with 3 products" in {
      Given("a valid xlsx file with 3 products")
      val inputStream = createTestXlsxFile()

      When("parsing the file")
      val result = XlsxProductParser.parse(inputStream)

      Then("the result should contain 3 products with correct data")
      result.isRight shouldBe true
      val products = result.getOrElse(List.empty)
      products should have size 3

      products.head.name shouldBe "Coca Cola"
      products.head.price shouldBe 1.5f
      products.head.category shouldBe "drink"

      products(1).name shouldBe "Chips"
      products(1).price shouldBe 2.0f
      products(1).category shouldBe "snack"

      products(2).name shouldBe "Sandwich"
      products(2).price shouldBe 3.5f
      products(2).category shouldBe "food"
    }

    "return error when file is empty" in {
      Given("an empty xlsx file")
      val inputStream = createEmptyXlsxFile()

      When("parsing the file")
      val result = XlsxProductParser.parse(inputStream)

      Then("the result should be an error about empty file")
      result.isLeft shouldBe true
      result.left.map(_.message should include("Empty file"))
    }

    "return error when headers are missing" in {
      Given("an xlsx file with invalid headers")
      val inputStream = createXlsxFileWithInvalidHeaders()

      When("parsing the file")
      val result = XlsxProductParser.parse(inputStream)

      Then("the result should be an error about missing headers")
      result.isLeft shouldBe true
      result.left.map(_.message should include("Missing required headers"))
    }
  }

  private def createTestXlsxFile(): ByteArrayInputStream = {
    val workbook = new XSSFWorkbook()
    val sheet    = workbook.createSheet("Products")

    // Create header row
    val headerRow = sheet.createRow(0)
    headerRow.createCell(0).setCellValue("name")
    headerRow.createCell(1).setCellValue("price")
    headerRow.createCell(2).setCellValue("category")

    // Create data rows
    val row1 = sheet.createRow(1)
    row1.createCell(0).setCellValue("Coca Cola")
    row1.createCell(1).setCellValue(1.5)
    row1.createCell(2).setCellValue("drink")

    val row2 = sheet.createRow(2)
    row2.createCell(0).setCellValue("Chips")
    row2.createCell(1).setCellValue(2.0)
    row2.createCell(2).setCellValue("snack")

    val row3 = sheet.createRow(3)
    row3.createCell(0).setCellValue("Sandwich")
    row3.createCell(1).setCellValue(3.5)
    row3.createCell(2).setCellValue("food")

    val outputStream = new ByteArrayOutputStream()
    workbook.write(outputStream)
    workbook.close()

    new ByteArrayInputStream(outputStream.toByteArray)
  }

  private def createEmptyXlsxFile(): ByteArrayInputStream = {
    val workbook     = new XSSFWorkbook()
    workbook.createSheet("Products")
    val outputStream = new ByteArrayOutputStream()
    workbook.write(outputStream)
    workbook.close()

    new ByteArrayInputStream(outputStream.toByteArray)
  }

  private def createXlsxFileWithInvalidHeaders(): ByteArrayInputStream = {
    val workbook  = new XSSFWorkbook()
    val sheet     = workbook.createSheet("Products")
    val headerRow = sheet.createRow(0)
    headerRow.createCell(0).setCellValue("invalid")
    headerRow.createCell(1).setCellValue("headers")

    val outputStream = new ByteArrayOutputStream()
    workbook.write(outputStream)
    workbook.close()

    new ByteArrayInputStream(outputStream.toByteArray)
  }
}
