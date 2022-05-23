package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.request.CreateCarouselRequest
import dinporapar.purbalinggamemikat.model.request.ListCarouselRequest
import dinporapar.purbalinggamemikat.model.response.WebResponse
import dinporapar.purbalinggamemikat.service.CarouselService
import org.apache.commons.io.IOUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.AntPathMatcher
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/carousels")
class CarouselController(val carouselService: CarouselService) {

    @PostMapping(
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createCarousel(body: CreateCarouselRequest) : WebResponse<CarouselResponse> {
        val carouselResponse = carouselService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = carouselResponse
        )
    }

    @GetMapping(
        produces = ["application/json"]
    )
    fun listCarousels(@RequestParam(value = "page", defaultValue = "0") page: Int,
                     @RequestParam(value = "size", defaultValue = "3") size: Int): WebResponse<List<CarouselResponse>> {
        val request = ListCarouselRequest(page,size)
        val response = carouselService.list(request)
        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }

    @GetMapping(value = ["/attachment/**"])
    @Throws(IOException::class)
    fun getFile(request: HttpServletRequest): ResponseEntity<Any?>? {
        val pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE) as String
        val filename = AntPathMatcher().extractPathWithinPattern(pattern, request.servletPath)
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        header.add("Cache-Control", "no-cache, no-store, must-revalidate")
        header.add("Pragma", "no-cache")
        header.add("Expires", "0")
        return ResponseEntity.ok()
            .headers(header)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(IOUtils.toByteArray(carouselService.getObject(filename)))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun getCarousel(@PathVariable("id") id : Long) : WebResponse<CarouselResponse>{
        val carouselResponse = carouselService.get(id)
        return WebResponse(
            200,
            "oke",
            carouselResponse
        )
    }
}