package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.request.DeleteObjectRequest
import dinporapar.purbalinggamemikat.model.request.ObjectRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateNewsRequest
import dinporapar.purbalinggamemikat.model.response.NewsResponse
import dinporapar.purbalinggamemikat.model.response.ObjectResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import java.io.InputStream

interface ObjectService {

    fun create(objectRequest: ObjectRequest) : ObjectResponse

    fun list(requestParams: RequestParams, filter: Map<String,String>): ListResponse<ObjectResponse>

    fun getObject(filename: String): InputStream?

    fun get(id: Long): ObjectResponse

    fun update(id: Long, objectRequest: ObjectRequest) : ObjectResponse

    fun delete(id: Long, deleteObjectRequest: DeleteObjectRequest) : String

    fun getDistance(requestParams: RequestParams, filter: Map<String,String>): ListResponse<ObjectResponse>
}