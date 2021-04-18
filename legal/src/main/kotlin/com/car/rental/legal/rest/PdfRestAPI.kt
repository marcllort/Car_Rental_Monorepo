package com.car.rental.legal.rest

import com.car.rental.legal.generator.InvoiceGenerator
import com.car.rental.legal.generator.RouteGenerator
import com.car.rental.legal.model.LegalRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

@RestController
@RequestMapping("/legal")
class PdfRestAPI {

    @GetMapping("/pdf")
    fun customerReport(
        @Autowired invoiceGenerator: InvoiceGenerator,
        @Autowired routeGenerator: RouteGenerator,
        @RequestBody request: LegalRequest
    ): ResponseEntity<InputStreamResource> {

        var bis: InputStream? = null

        when (request.flow) {
            "invoice" -> {
                print("Invoice")
                bis = invoiceGenerator.generateInvoicePDF(request.service!!)
            }
            "route-paper" -> {
                print("route-paper")
                bis = routeGenerator.generateRoutePDF(request.service!!)
            }
            else -> { // Note the block
                print("LEGAL - Error, non-existing flow")
            }
        }


        val headers = HttpHeaders()
        headers.add("Content-Disposition", "attachment; filename=customers.pdf")

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(InputStreamResource(bis!!))
    }

}