package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.Carousel
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.request.CreateCarouselRequest
import dinporapar.purbalinggamemikat.model.request.ListCarouselRequest
import dinporapar.purbalinggamemikat.repository.CarouselRepository
import dinporapar.purbalinggamemikat.service.CarouselService
import dinporapar.purbalinggamemikat.validation.ValidationUtil
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.*
import java.util.stream.Collectors

@Service
class CarouselServiceImpl (
    val carouselRepository: CarouselRepository,
    val validationUtil: ValidationUtil
)  : CarouselService {

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    override fun create(createCarouselRequest: CreateCarouselRequest): CarouselResponse {
        validationUtil.validate(createCarouselRequest)

        var fileName = createCarouselRequest.file!!.originalFilename!!?:"attachment"
        if (createCarouselRequest.file !== null) {
//            var extension = createCarouselRequest.file!!.contentType!!.substringAfterLast("/")
//
//            if(extension.isEmpty()){
//                extension = ".png"
//            }else{
//                extension = "."+extension
//            }
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/foto/" + fileName)
                        .stream(createCarouselRequest.file!!.inputStream, createCarouselRequest.file!!.size, -1)
                        .contentType(createCarouselRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }

        val carousel = Carousel(
            id = Long.MIN_VALUE,
            photo = fileName,
            link = createCarouselRequest.link!!,
            isActive = createCarouselRequest.isActive?:1,
            description = createCarouselRequest.description!!,
            createdBy = createCarouselRequest.createdBy?:1,
            createdAt = Date(),
            updatedAt = null,
            updatedBy = null,
            deletedAt = null,
            deletedBy = null
        )

        val idExist = carouselRepository.findByIdOrNull(Long.MIN_VALUE)
        if (idExist != null){
            throw IllegalArgumentException("Product with id ${Long.MIN_VALUE} already exist")
        }

        carouselRepository.save(carousel)

        return convertCarouselToCarouselResponse(carousel)
    }

    override fun list(listCarouselRequest: ListCarouselRequest): List<CarouselResponse> {
        val page = carouselRepository.findAll(PageRequest.of(listCarouselRequest.page, listCarouselRequest.size))
        val carousel:List<Carousel> = page.get().collect(Collectors.toList())

        carousel.forEach{
            it.photo = "http://10.6.2.15:8080/api/v1/carousels/attachment/" +it.photo
        }
        return carousel.map { convertCarouselToCarouselResponse(it) }
    }

    override fun getObject(filename: String): InputStream? {
        val stream: InputStream = try {
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`("/foto/"+filename)
                    .build()
            )
        } catch (e: Exception) {
            println("error getObject -> $e")
            return null
        }
        return stream
    }

    override fun get(id: Long): CarouselResponse {
        val carousel = findCarouselByIdOrThrowNotFound(id)
        return convertCarouselToCarouselResponse(carousel)
    }

    private fun convertCarouselToCarouselResponse(carousel: Carousel): CarouselResponse {
        return CarouselResponse(
            id = carousel.id,
            photo = carousel.photo,
            link = carousel.link,
            IsActive = carousel.isActive,
            description = carousel.description,
            createdAt = carousel.createdAt,
            createdBy = carousel.createdBy,
            updatedAt = carousel.updatedAt,
            updatedBy = carousel.updatedBy,
            deletedAt = carousel.deletedAt,
            deletedBy = carousel.deletedBy,
        )
    }

    private fun findCarouselByIdOrThrowNotFound(id: Long): Carousel{
        val carousel = carouselRepository.findByIdOrNull(id)
        if (carousel == null){
            throw NotFoundException()
        }else{
            return carousel
        }
    }

}