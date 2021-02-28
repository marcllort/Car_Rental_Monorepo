package com.car.rental.legal.handler

import com.car.rental.legal.model.LegalRequest
import com.google.gson.Gson
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException

@Component
class Consumer {

    fun consume(requestBody: String): String {
        try {
            var gson = Gson()
            var request = gson.fromJson(requestBody, LegalRequest::class.java)
            println("Converted: '$request'")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "test"
    }

    private fun generateHTMLFromPDF(filename: String) {
        try {
            val pDDocument = PDDocument.load(File("legal/src/main/kotlin/com/car/rental/legal/template/unfilled_form.pdf")) // pdfform.pdf is input file
            val pDAcroForm = pDDocument.documentCatalog.acroForm
            var field = pDAcroForm.getField("name")
            field.setValue("Kalyan")
            field = pDAcroForm.getField("name2")
            field.setValue("Gutta")

            pDDocument.save("legal/src/main/kotlin/com/car/rental/legal/template/filled_form.pdf")
            pDDocument.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}