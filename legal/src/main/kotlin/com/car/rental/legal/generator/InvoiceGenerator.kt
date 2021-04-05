package com.car.rental.legal.generator

import com.car.rental.legal.model.Service
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.stereotype.Component
import java.io.*

@Component
class InvoiceGenerator {

    val FILE_PATH = "legal/src/main/kotlin/com/car/rental/legal/template/unfilled_route_paper_form.pdf"

    fun generateInvoicePDF(service: Service): InputStream? {
        try {
            val pDDocument =
                PDDocument.load(File(FILE_PATH))
            val pDAcroForm = pDDocument.documentCatalog.acroForm
            var field = pDAcroForm.getField("client")
            field.setValue(service.clientId.toString())
            field = pDAcroForm.getField("date")
            field.setValue(service.serviceDatetime)
            field = pDAcroForm.getField("time")
            field.setValue(service.serviceDatetime)
            field = pDAcroForm.getField("destination")
            field.setValue(service.destination)
            field = pDAcroForm.getField("passenger")
            field.setValue(service.passengers.toString())
            field = pDAcroForm.getField("info")
            field.setValue(service.description)

            val byteArrayOutputStream = ByteArrayOutputStream()

            pDDocument.save(byteArrayOutputStream)

            pDDocument.close()
            val inputStream: InputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())

            return inputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}