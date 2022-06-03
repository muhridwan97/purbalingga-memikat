package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.ObjectEntity
import dinporapar.purbalinggamemikat.error.DeleteDataException
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.request.DeleteObjectRequest
import dinporapar.purbalinggamemikat.model.request.ObjectRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.response.ObjectResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.repository.CategoryRepository
import dinporapar.purbalinggamemikat.repository.ObjectRepository
import dinporapar.purbalinggamemikat.repository.SubCategoryRepository
import dinporapar.purbalinggamemikat.service.ObjectService
import dinporapar.purbalinggamemikat.specification.FilterMapper
import dinporapar.purbalinggamemikat.specification.FilterRequestUtil
import dinporapar.purbalinggamemikat.specification.FilterSpecification
import dinporapar.purbalinggamemikat.validation.ValidationUtil
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
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
class ObjectServiceImpl(
    val objectRepository: ObjectRepository,
    val categoryRepository: CategoryRepository,
    val subCategoryRepository: SubCategoryRepository,
    val validationUtil: ValidationUtil,
    val filterRequestUtil: FilterRequestUtil,
    private val specification: FilterSpecification<ObjectEntity>
) : ObjectService{

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    @Value("\${tes.ip}")
    lateinit var ipTes: String

    override fun create(objectRequest: ObjectRequest): ObjectResponse {
        validationUtil.validate(objectRequest)
        var fileName = ""
        if (objectRequest.file !== null) {
            var extension = objectRequest.file!!.contentType!!.substringAfterLast("/")

            if(extension.isEmpty()){
                extension = ".png"
            }else{
                extension = "."+extension
            }
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/object/" + fileName)
                        .stream(objectRequest.file!!.inputStream, objectRequest.file!!.size, -1)
                        .contentType(objectRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }

        val objectEntity = ObjectEntity(
            id = Long.MIN_VALUE,
            categoryId = objectRequest.categoryId!!,
            category = null,
            subCategoryId = objectRequest.subCategoryId!!,
            subCategory = null,
            name = objectRequest.name!!,
            address = objectRequest.address!!,
            phoneNumber = objectRequest.phoneNumber!!,
            website = objectRequest.website!!,
            facility = objectRequest.facility!!,
            photo = fileName,
            latitude = objectRequest.latitude!!,
            longitude = objectRequest.longitude!!,
            slug = objectRequest.slug!!,
            instagram = objectRequest.instagram!!,
            facebook = objectRequest.facebook!!,
            youtube = objectRequest.youtube!!,
            tiktok = objectRequest.tiktok!!,
            status = objectRequest.status!!,
            like = objectRequest.like!!,
            description = objectRequest.description!!,
            createdBy = objectRequest.createdBy!!,
            createdAt = Date(),
            updatedAt = null,
            updatedBy = null,
            deletedAt = null,
            deletedBy = null
        )

        val result = objectRepository.save(objectEntity)

        return convertToResponse(result)
    }

    override fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<ObjectResponse> {
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

        val list = objectRepository.findAll(generateFilter(filter), pageable)

        val items: List<ObjectEntity> = list.get().collect(Collectors.toList())
        items.forEach {
            it.photo = ipTes+"/api/v1/objects/attachment/" + it.photo
        }

        return ListResponse(
            items = items.map { convertToResponse(it) },
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
                    .`object`("/object/"+filename)
                    .build()
            )
        } catch (e: Exception) {
            println("error getObject -> $e")
            return null
        }
        return stream
    }

    override fun get(id: Long): ObjectResponse {
        val response = findObjectByIdOrThrowNotFound(id)
        if(response.photo != null){
            response.photo = ipTes + "/api/v1/objects/attachment/" + response.photo
        }
        return convertToResponse(response)
    }

    override fun update(id: Long, objectRequest: ObjectRequest): ObjectResponse {
        val data = findObjectByIdOrThrowNotFound(id)
        validationUtil.validate(data)
        var fileName = ""
        if (objectRequest.file !== null) {
            var extension = objectRequest.file!!.contentType!!.substringAfterLast("/")

            if(extension.isEmpty()){
                extension = ".png"
            }else{
                extension = "."+extension
            }
            fileName = Date().time.toString()+extension
            try {
                minioClient.removeObject(
                    RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/object/" + data.photo)
                        .build()
                )

                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/object/" + fileName)
                        .stream(objectRequest.file!!.inputStream, objectRequest.file!!.size, -1)
                        .contentType(objectRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }
        data.apply {
            categoryId = objectRequest.categoryId!!
            subCategoryId = objectRequest.subCategoryId!!
            name = objectRequest.name!!
            address = objectRequest.address!!
            phoneNumber = objectRequest.phoneNumber!!
            website = objectRequest.website!!
            facility = objectRequest.facility!!
            photo = fileName
            latitude = objectRequest.latitude!!
            longitude = objectRequest.longitude!!
            slug = objectRequest.slug!!
            instagram = objectRequest.instagram!!
            facebook = objectRequest.facebook!!
            youtube = objectRequest.youtube!!
            tiktok = objectRequest.tiktok!!
            status = objectRequest.status!!
            like = objectRequest.like!!
            description = objectRequest.description!!
            updatedBy = objectRequest.updatedBy!!
            updatedAt = Date()
        }
        val result = objectRepository.save(data)
        return convertToResponse(result)
    }

    override fun delete(id: Long, deleteObjectRequest: DeleteObjectRequest): String {

        val data = findObjectByIdOrThrowNotFound(id)

        try {
            if(deleteObjectRequest.softDelete == true || deleteObjectRequest.softDelete == null){
                data.apply {
                    isDeleted = true
                    deletedBy = 1
                    deletedAt = Date()
                }
                objectRepository.save(data)
            }else{
                objectRepository.delete(data)
            }
        } catch (e: Exception) {
            println("error -> $e")
            throw DeleteDataException()
        }
        return "Delete Successfully"
    }

    override fun getDistance(requestParams: RequestParams, filter: Map<String, String>): ListResponse<ObjectResponse> {
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

        val list = objectRepository.findAll(generateFilter(filter), pageable)

        val items: List<ObjectEntity> = list.get().collect(Collectors.toList())

        items.forEach {
            it.photo = ipTes+"/api/v1/objects/attachment/" + it.photo
        }

        return ListResponse(
            items = items.map { convertToResponse(it) },
            paging = PagingResponse(
                item_per_page = list.size,
                page = list.number,
                total_item = list.totalElements,
                total_page = list.totalPages
            ),
            sorting = filterRequestUtil.toSortResponse(requestParams.sortBy)
        )
    }

    private fun convertToResponse(objectEntity: ObjectEntity): ObjectResponse {
        if (objectEntity.category == null) {
            objectEntity.category = categoryRepository.findByIdOrNull(objectEntity.categoryId)
        }
        if (objectEntity.subCategory == null) {
            objectEntity.subCategory = subCategoryRepository.findByIdOrNull(objectEntity.subCategoryId)
        }

        return ObjectResponse(
            id = objectEntity.id,
            name = objectEntity.name,
            categoryName = objectEntity.category!!.name,
            subCategoryName = objectEntity.subCategory!!.name,
            address = objectEntity.address,
            phoneNumber = objectEntity.phoneNumber,
            website = objectEntity.website,
            facility = objectEntity.facility,
            photo = objectEntity.photo,
            longitude = objectEntity.longitude,
            latitude = objectEntity.latitude,
            slug = objectEntity.slug,
            instagram = objectEntity.instagram,
            facebook = objectEntity.facebook,
            youtube = objectEntity.youtube,
            tiktok = objectEntity.tiktok,
            status = objectEntity.status,
            like = objectEntity.like,
            IsActive = objectEntity.isActive,
            description = objectEntity.description,
            createdAt = objectEntity.createdAt,
            createdBy = objectEntity.createdBy,
            updatedAt = objectEntity.updatedAt,
            updatedBy = objectEntity.updatedBy,
        )
    }

    private fun generateFilter(filter: Map<String, String>): Specification<ObjectEntity>? {
        val options: MutableList<FilterMapper> = mutableListOf()
        val filters = filterRequestUtil.toFilterCriteria(filter, options)
        return specification.buildPredicate(filters)
    }

    private fun findObjectByIdOrThrowNotFound(id: Long): ObjectEntity{
        val result = objectRepository.findByIdOrNull(id)
        if (result == null){
            throw NotFoundException()
        }else{
            return result
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}