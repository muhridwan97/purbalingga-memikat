package dinporapar.purbalinggamemikat.service

import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.request.CreateCarouselRequest
import dinporapar.purbalinggamemikat.model.request.ListCarouselRequest
import java.io.InputStream

interface CarouselService {

    fun create(CreateCarouselRequest: CreateCarouselRequest) : CarouselResponse

    fun list(listCarouselRequest: ListCarouselRequest): List<CarouselResponse>

    fun getObject(filename: String): InputStream?

    fun get(id : Long) : CarouselResponse
}