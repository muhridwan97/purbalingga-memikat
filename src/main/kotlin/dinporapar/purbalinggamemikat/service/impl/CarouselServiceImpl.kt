package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.CarouselEntity
import dinporapar.purbalinggamemikat.error.DeleteDataException
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.request.*
import dinporapar.purbalinggamemikat.model.response.CarouselResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.repository.CarouselRepository
import dinporapar.purbalinggamemikat.service.CarouselService
import dinporapar.purbalinggamemikat.specification.FilterMapper
import dinporapar.purbalinggamemikat.specification.FilterRequestUtil
import dinporapar.purbalinggamemikat.specification.FilterSpecification
import dinporapar.purbalinggamemikat.validation.ValidationUtil
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.*
import java.util.stream.Collectors


@Service
class CarouselServiceImpl (
    val carouselRepository: CarouselRepository,
    val validationUtil: ValidationUtil,
    val filterRequestUtil: FilterRequestUtil,
    private val specification: FilterSpecification<CarouselEntity>

)  : CarouselService {

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    @Value("\${tes.ip}")
    lateinit var ipTes: String

    override fun create(createCarouselRequest: CreateCarouselRequest): CarouselResponse {
        validationUtil.validate(createCarouselRequest)
        var extension = createCarouselRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (createCarouselRequest.file !== null) {
            fileName = Date().time.toString()+extension
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

        val carouselEntity = CarouselEntity(
            id = Long.MIN_VALUE,
            photo = fileName,
            link = createCarouselRequest.link!!,
            isActive = createCarouselRequest.isActive!!,
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

        carouselRepository.save(carouselEntity)

        return convertCarouselToCarouselResponse(carouselEntity)
    }

    override fun list2(listCarouselRequest: ListCarouselRequest): List<CarouselResponse> {
        val page = carouselRepository.findAll(PageRequest.of(listCarouselRequest.page, listCarouselRequest.size))
        val carouselEntity: List<CarouselEntity> = page.get().collect(Collectors.toList())

        carouselEntity.forEach {
            it.photo = ipTes+"/api/v1/carousels/attachment/" + it.photo
        }
        return carouselEntity.map { convertCarouselToCarouselResponse(it) }
    }

    override fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<CarouselResponse> {
        val size = if (requestParams.size!! == -1) {
            Integer.MAX_VALUE
        } else {
            requestParams.size
        }

        val pageable = PageRequest.of(
            requestParams.page!!,
            size,
            filterRequestUtil.toSortBy(requestParams.sortBy!!)
        )

        val list = carouselRepository.findAll(generateFilter(filter), pageable)

        val items: List<CarouselEntity> = list.get().collect(Collectors.toList())
        items.forEach {
            it.photo = ipTes+"/api/v1/carousels/attachment/" + it.photo
        }
        return ListResponse(
            items = items.map { convertCarouselToCarouselResponse(it) },
            paging = PagingResponse(
                item_per_page = list.size,
                page = list.number,
                total_item = list.totalElements,
                total_page = list.totalPages
            ),
            sorting = filterRequestUtil.toSortResponse(requestParams.sortBy)
        )
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

    override fun update(id: Long, updateCarouselRequest: UpdateCarouselRequest): CarouselResponse {
        val carousel = findCarouselByIdOrThrowNotFound(id)
        validationUtil.validate(carousel)
        var extension = updateCarouselRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (updateCarouselRequest.file !== null) {
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/foto/" + fileName)
                        .stream(updateCarouselRequest.file!!.inputStream, updateCarouselRequest.file!!.size, -1)
                        .contentType(updateCarouselRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }
        carousel.apply {
            photo = fileName!!
            link = updateCarouselRequest.link!!
            description = updateCarouselRequest.description!!
            isActive = updateCarouselRequest.isActive!!
            updatedAt = Date()
        }
        carouselRepository.save(carousel)
        return convertCarouselToCarouselResponse(carousel)
    }

    override fun delete(id: Long, deleteCarouselRequest: DeleteCarouselRequest): String {
        val carousel = findCarouselByIdOrThrowNotFound(id)

        try {
            if(deleteCarouselRequest.softDelete == true || deleteCarouselRequest.softDelete == null){
                carousel.apply {
                    isDeleted = true
                    deletedBy = 1
                    deletedAt = Date()
                }
                carouselRepository.save(carousel)
            }else{
                carouselRepository.delete(carousel)
            }
        } catch (e: Exception) {
            println("error -> $e")
            throw DeleteDataException()
        }
        return "Delete Successfully"
    }

    private fun convertCarouselToCarouselResponse(carouselEntity: CarouselEntity): CarouselResponse {
        return CarouselResponse(
            id = carouselEntity.id,
            photo = carouselEntity.photo,
            link = carouselEntity.link,
            IsActive = carouselEntity.isActive,
            isDeleted = carouselEntity.isDeleted,
            description = carouselEntity.description,
            createdAt = carouselEntity.createdAt,
            createdBy = carouselEntity.createdBy,
            updatedAt = carouselEntity.updatedAt,
            updatedBy = carouselEntity.updatedBy,
            deletedAt = carouselEntity.deletedAt,
            deletedBy = carouselEntity.deletedBy,
        )
    }

    private fun findCarouselByIdOrThrowNotFound(id: Long): CarouselEntity{
        val carousel = carouselRepository.findByIdOrNull(id)
        if (carousel == null){
            throw NotFoundException()
        }else{
            return carousel
        }
    }
    private fun generateFilter(filter: Map<String, String>): Specification<CarouselEntity>? {
        val options: MutableList<FilterMapper> = mutableListOf()
        val filters = filterRequestUtil.toFilterCriteria(filter, options)
        return specification.buildPredicate(filters)
    }

}