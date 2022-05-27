package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.request.CreateSubCategoryRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateCategoryRequest
import dinporapar.purbalinggamemikat.model.request.UpdateSubCategoryRequest
import dinporapar.purbalinggamemikat.model.response.CategoryResponse
import dinporapar.purbalinggamemikat.model.response.SubCategoryResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import java.io.InputStream

interface SubCategoryService {

    fun create(createSubCategoryRequest: CreateSubCategoryRequest): SubCategoryResponse

    fun list(requestParams: RequestParams, filter: Map<String,String>): ListResponse<SubCategoryResponse>

    fun getObject(filename: String): InputStream?

    fun get(id: Long): SubCategoryResponse

    fun update(id: Long, updateSubCategoryRequest: UpdateSubCategoryRequest) : SubCategoryResponse
}