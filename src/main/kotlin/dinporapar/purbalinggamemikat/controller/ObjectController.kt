package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.ObjectResponse
import dinporapar.purbalinggamemikat.model.response.SubCategoryResponse
import dinporapar.purbalinggamemikat.model.response.WebResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.service.ObjectService
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
@RequestMapping("/api/v1/objects")
class ObjectController (val objectService: ObjectService) {

    @RolesAllowed("admin")
    @PostMapping(
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE , MediaType.APPLICATION_JSON_VALUE]
    )
    fun createObject(@ModelAttribute body: ObjectRequest) : WebResponse<ObjectResponse> {
        val response = objectService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }
    @GetMapping(
        produces = ["application/json"]
    )
    fun listObjects(
        @Valid request: RequestParams,
        @RequestParam filter: Map<String, String>
    ): WebResponse<ListResponse<ObjectResponse>?> {
        val responses = objectService.list(request, filter)
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
            .body(IOUtils.toByteArray(objectService.getObject(filename)))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun getObject(@PathVariable("id") id : Long) : WebResponse<ObjectResponse>{
        val response = objectService.get(id)
        return WebResponse(
            200,
            "oke",
            response
        )
    }

    @PatchMapping(
        value = ["/{id}"],
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateObject(@PathVariable("id") id: Long,
                          @ModelAttribute objectRequest: ObjectRequest
    ): WebResponse<ObjectResponse>{

        val response = objectService.update(id, objectRequest)
        return WebResponse(
            200,
            "OK",
            response
        )
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun deleteObject(@PathVariable("id") id: Long,
                          deleteObjectRequest: DeleteObjectRequest
    ): WebResponse<String>{

        val categoryResponse = objectService.delete(id, deleteObjectRequest)
        return WebResponse(
            200,
            "OK",
            categoryResponse
        )
    }
}