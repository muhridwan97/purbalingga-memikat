package dinporapar.purbalinggamemikat.controller

import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.CategoryResponse
import dinporapar.purbalinggamemikat.model.response.NewsResponse
import dinporapar.purbalinggamemikat.model.response.WebResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.service.NewsService
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
@RequestMapping("/api/v1/news")
class NewsController (val newsService: NewsService){

    @PostMapping(
        produces = ["application/json"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createNews(@ModelAttribute body: CreateNewsRequest) : WebResponse<NewsResponse> {
        val response = newsService.create(body)

        return WebResponse(
            code = 200,
            status = "OK",
            data = response
        )
    }

    @GetMapping(
        produces = ["application/json"]
    )
    fun listNews(
        @Valid request: RequestParams,
        @RequestParam filter: Map<String, String>
    ): WebResponse<ListResponse<NewsResponse>?> {
        val responses = newsService.list(request, filter)
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
            .body(IOUtils.toByteArray(newsService.getObject(filename)))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun getNews(@PathVariable("id") id : Long) : WebResponse<NewsResponse>{
        val response = newsService.get(id)
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
    fun updateNews(@PathVariable("id") id: Long,
                       updateNewsRequest: UpdateNewsRequest
    ): WebResponse<NewsResponse>{

        val newsResponse = newsService.update(id, updateNewsRequest)
        return WebResponse(
            200,
            "OK",
            newsResponse
        )
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = ["application/json"]
    )
    fun deleteCategory(@PathVariable("id") id: Long,
                       deleteNewsRequest: DeleteNewsRequest
    ): WebResponse<String>{

        val newsResponse = newsService.delete(id, deleteNewsRequest)
        return WebResponse(
            200,
            "OK",
            newsResponse
        )
    }
}