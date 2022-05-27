package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.request.CreateCategoryRequest
import dinporapar.purbalinggamemikat.model.request.DeleteCategoryRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateCategoryRequest
import dinporapar.purbalinggamemikat.model.response.CategoryResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import java.io.InputStream

interface CategoryService {

    fun create(createCategoryRequest: CreateCategoryRequest) : CategoryResponse

    fun list(requestParams: RequestParams, filter: Map<String,String>): ListResponse<CategoryResponse>

    fun getObject(filename: String): InputStream?

    fun get(id: Long): CategoryResponse

    fun update(id: Long, updateCategoryRequest: UpdateCategoryRequest) : CategoryResponse

    fun delete(id: Long, deleteCategoryRequest: DeleteCategoryRequest) : String
}