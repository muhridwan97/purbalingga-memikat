package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.NewsResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import java.io.InputStream

interface NewsService {

    fun create(createNewsRequest: CreateNewsRequest) : NewsResponse

    fun list(requestParams: RequestParams, filter: Map<String,String>): ListResponse<NewsResponse>

    fun getObject(filename: String): InputStream?

    fun get(id: Long): NewsResponse

    fun update(id: Long, updateNewsRequest: UpdateNewsRequest) : NewsResponse

    fun delete(id: Long, deleteNewsRequest: DeleteNewsRequest) : String
}