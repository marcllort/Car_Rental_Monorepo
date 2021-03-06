package com.car.rental.legal.generator

import com.car.rental.legal.model.Service
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.*

class RoutePaperGenerator {

    val FILE_PATH = "legal/src/main/kotlin/com/car/rental/legal/template/unfilled_form.pdf"

    fun generateRoutePDF(service: Service): InputStream? {
        try {
            val pDDocument =
                PDDocument.load(File(FILE_PATH))
            val pDAcroForm = pDDocument.documentCatalog.acroForm
            var field = pDAcroForm.getField("name")
            field.setValue("Kalyan")
            field = pDAcroForm.getField("name2")
            field.setValue("Gutta")

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