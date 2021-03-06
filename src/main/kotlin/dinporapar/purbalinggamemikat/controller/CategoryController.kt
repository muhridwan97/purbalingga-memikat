package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.request.*
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
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController (val categoryService: CategoryService) {

    @PostMapping(
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createCategory(@ModelAttribute body: CreateCategoryRequest) : WebResponse<CategoryResponse> {
        val response = categoryService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }

    @RolesAllowed("admin")
    @GetMapping(
        produces = ["application/json"]
    )
    fun listCategories(
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

    @GetMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun getCategory(@PathVariable("id") id : Long) : WebResponse<CategoryResponse>{
        val response = categoryService.get(id)
        return WebResponse(
            200,
            "oke",
            response
        )
    }

    @PatchMapping(
        value = ["/{id}"],
        produces = ["application/json"],
        consumes = ["application/json", MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun updateCategory(@PathVariable("id") id: Long,
                       updateCategoryRequest: UpdateCategoryRequest
    ): WebResponse<CategoryResponse>{

        val categoryResponse = categoryService.update(id, updateCategoryRequest)
        return WebResponse(
            200,
            "OK",
            categoryResponse
        )
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun deleteCategory(@PathVariable("id") id: Long,
                       deleteCategoryRequest: DeleteCategoryRequest
    ): WebResponse<String>{

        val categoryResponse = categoryService.delete(id, deleteCategoryRequest)
        return WebResponse(
            200,
            "OK",
            categoryResponse
        )
    }
}