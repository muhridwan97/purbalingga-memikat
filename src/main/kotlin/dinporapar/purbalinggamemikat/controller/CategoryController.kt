package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.request.CreateCategoryRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.response.CategoryResponse
import dinporapar.purbalinggamemikat.model.response.WebResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.service.CategoryService
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
@RequestMapping("/api/v1/categories")
class CategoryController (val categoryService: CategoryService) {

    @PostMapping(
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createCategory(body: CreateCategoryRequest) : WebResponse<CategoryResponse> {
        val response = categoryService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }

    @GetMapping(
        produces = ["application/json"]
    )
    fun listCarousels(
        @Valid request: RequestParams,
        @RequestParam filter: Map<String, String>
    ): WebResponse<ListResponse<CategoryResponse>?> {
        val responses = categoryService.list(request, filter)
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
            .body(IOUtils.toByteArray(categoryService.getObject(filename)))
    }
}