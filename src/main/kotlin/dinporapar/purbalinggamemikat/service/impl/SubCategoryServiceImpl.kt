package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.CategoryEntity
import dinporapar.purbalinggamemikat.entity.SubCategoryEntity
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.request.CreateSubCategoryRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateSubCategoryRequest
import dinporapar.purbalinggamemikat.model.response.SubCategoryResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.repository.CategoryRepository
import dinporapar.purbalinggamemikat.repository.SubCategoryRepository
import dinporapar.purbalinggamemikat.service.IpAddressService
import dinporapar.purbalinggamemikat.service.SubCategoryService
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
import javax.servlet.http.HttpServletRequest

@Service
class SubCategoryServiceImpl(
    val subCategoryRepository: SubCategoryRepository,
    val categoryRepository: CategoryRepository,
    val validationUtil: ValidationUtil,
    val filterRequestUtil: FilterRequestUtil,
    private val specification: FilterSpecification<SubCategoryEntity>
) : SubCategoryService{

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    @Value("\${tes.ip}")
    lateinit var ipTes: String

    override fun create(createSubCategoryRequest: CreateSubCategoryRequest): SubCategoryResponse {
        validationUtil.validate(createSubCategoryRequest)
        var extension = createSubCategoryRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (createSubCategoryRequest.file !== null) {
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/subCategory/" + fileName)
                        .stream(createSubCategoryRequest.file!!.inputStream, createSubCategoryRequest.file!!.size, -1)
                        .contentType(createSubCategoryRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }

//        val categoryData =
        val subCategoryEntity = SubCategoryEntity(
            id = Long.MIN_VALUE,
            categoryId = createSubCategoryRequest.categoryId!!,
            category = null,
            name = createSubCategoryRequest.name!!,
            photo = fileName,
            description = createSubCategoryRequest.description!!,
            slug = createSubCategoryRequest.slug!!,
            createdBy = createSubCategoryRequest.createdBy!!,
            createdAt = Date(),
            updatedAt = null,
            updatedBy = null,
            deletedAt = null,
            deletedBy = null
        )

        subCategoryRepository.save(subCategoryEntity)

        return convertToResponse(subCategoryEntity)
    }

    override fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<SubCategoryResponse> {
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

        val list = subCategoryRepository.findAll(generateFilter(filter), pageable)

        val items: List<SubCategoryEntity> = list.get().collect(Collectors.toList())
//        val ipAddress = ipAddressService.getClientIp(requestHttp)
        items.forEach {
            it.photo = ipTes+"/api/v1/subCategories/attachment/" + it.photo
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
                    .`object`("/subCategory/"+filename)
                    .build()
            )
        } catch (e: Exception) {
            println("error getObject -> $e")
            return null
        }
        return stream
    }

    override fun get(id: Long): SubCategoryResponse {
        val response = findSubCategoryByIdOrThrowNotFound(id)
        return convertToResponse(response)
    }

    override fun update(id: Long, updateSubCategoryRequest: UpdateSubCategoryRequest): SubCategoryResponse {
        val category = findSubCategoryByIdOrThrowNotFound(id)
        validationUtil.validate(category)
        var extension = updateSubCategoryRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (updateSubCategoryRequest.file !== null) {
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/subCategory/" + fileName)
                        .stream(updateSubCategoryRequest.file!!.inputStream, updateSubCategoryRequest.file!!.size, -1)
                        .contentType(updateSubCategoryRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }
        category.apply {
            categoryId = updateSubCategoryRequest.categoryId!!
            name = updateSubCategoryRequest.name!!
            photo = fileName
            description = updateSubCategoryRequest.description!!
            slug = updateSubCategoryRequest.slug!!
            updatedBy = updateSubCategoryRequest.updatedBy!!
            updatedAt = Date()
        }
        subCategoryRepository.save(category)
        return convertToResponse(category)
    }

    private fun convertToResponse(subCategoryEntity: SubCategoryEntity): SubCategoryResponse {
        if (subCategoryEntity.category == null) {
            subCategoryEntity.category = categoryRepository.findByIdOrNull(subCategoryEntity.categoryId)
        }

        return SubCategoryResponse(
            id = subCategoryEntity.id,
            name = subCategoryEntity.name,
            categoryName = subCategoryEntity.category!!.name,
            photo = subCategoryEntity.photo,
            slug = subCategoryEntity.slug,
            description = subCategoryEntity.description,
            createdAt = subCategoryEntity.createdAt,
            createdBy = subCategoryEntity.createdBy,
            updatedAt = subCategoryEntity.updatedAt,
            updatedBy = subCategoryEntity.updatedBy,
        )
    }

    private fun generateFilter(filter: Map<String, String>): Specification<SubCategoryEntity>? {
        val options: MutableList<FilterMapper> = mutableListOf()
        val filters = filterRequestUtil.toFilterCriteria(filter, options)
        return specification.buildPredicate(filters)
    }

    private fun findSubCategoryByIdOrThrowNotFound(id: Long): SubCategoryEntity{
        val category = subCategoryRepository.findByIdOrNull(id)
        if (category == null){
            throw NotFoundException()
        }else{
            return category
        }
    }

}