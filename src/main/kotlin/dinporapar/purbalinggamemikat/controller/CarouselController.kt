package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.response.WebResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
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
import javax.validation.Valid

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

//    @GetMapping(
//        produces = ["application/json"]
//    )
//    fun listCarousels(@RequestParam(value = "page", defaultValue = "0") page: Int,
//                     @RequestParam(value = "size", defaultValue = "3") size: Int): WebResponse<List<CarouselResponse>> {
//        val request = ListCarouselRequest(page,size)
//        val response = carouselService.list2(request)
//        return WebResponse(
//            code = 200,
//            status = "OK",
//            data = response
//        )
//    }
    @GetMapping(
        produces = ["application/json"]
    )
    fun listCarousels(
    @Valid request: RequestParams,
    @RequestParam filter: Map<String, String>
    ): WebResponse<ListResponse<CarouselResponse>?> {
        val responses = carouselService.list(request, filter)
        return WebResponse(
            code = 200,
            status = "OK",
            data = responses
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

    @PatchMapping(
        value = ["/{id}"],
        produces = ["application/json"],
        consumes = ["application/json", MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun updateCarousel(@PathVariable("id") id: Long,
                       updateCarouselRequest: UpdateCarouselRequest): WebResponse<CarouselResponse>{

        val carouselResponse = carouselService.update(id, updateCarouselRequest)
        return WebResponse(
            200,
            "OK",
            carouselResponse
        )
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun deleteCarousel(@PathVariable("id") id: Long,
                deleteCarouselRequest: DeleteCarouselRequest): WebResponse<String>{

        val carouselResponse = carouselService.delete(id, deleteCarouselRequest)
        return WebResponse(
            200,
            "OK",
            carouselResponse
        )
    }
}