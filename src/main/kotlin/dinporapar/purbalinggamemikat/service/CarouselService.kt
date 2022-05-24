package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import java.io.InputStream

interface CarouselService {

    fun create(CreateCarouselRequest: CreateCarouselRequest) : CarouselResponse

    fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<CarouselResponse>

    fun getObject(filename: String): InputStream?

    fun get(id : Long) : CarouselResponse

    fun update(id: Long, updateCarouselRequest: UpdateCarouselRequest) : CarouselResponse

    fun delete(id: Long, deleteCarouselRequest: DeleteCarouselRequest) : String
    fun list2(listCarouselRequest: ListCarouselRequest): List<CarouselResponse>
}