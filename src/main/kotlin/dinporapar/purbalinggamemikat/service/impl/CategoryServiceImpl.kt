package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.CategoryEntity
import dinporapar.purbalinggamemikat.error.DeleteDataException
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.request.CreateCategoryRequest
import dinporapar.purbalinggamemikat.model.request.DeleteCategoryRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateCategoryRequest
import dinporapar.purbalinggamemikat.model.response.CategoryResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.repository.CategoryRepository
import dinporapar.purbalinggamemikat.service.CategoryService
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
class CategoryServiceImpl (
    val categoryRepository: CategoryRepository,
    val validationUtil: ValidationUtil,
    val filterRequestUtil: FilterRequestUtil,
    private val specification: FilterSpecification<CategoryEntity>

)  : CategoryService {

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    @Value("\${tes.ip}")
    lateinit var ipTes: String

    override fun create(createCategoryRequest: CreateCategoryRequest): CategoryResponse {
        validationUtil.validate(createCategoryRequest)
        var extension = createCategoryRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (createCategoryRequest.file !== null) {
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/category/" + fileName)
                        .stream(createCategoryRequest.file!!.inputStream, createCategoryRequest.file!!.size, -1)
                        .contentType(createCategoryRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }

        var lastOrder = categoryRepository.getLastOrder()
        if (lastOrder==null){
            lastOrder = 0
        }else{
            lastOrder++
        }

        val carouselEntity = CategoryEntity(
            id = -1,
            name = createCategoryRequest.name!!,
            slug = createCategoryRequest.slug!!,
            photo = fileName,
            order = lastOrder,
            link = createCategoryRequest.link!!,
            isActive = createCategoryRequest.isActive!!,
            isModule = createCategoryRequest.isModule!!,
            moduleName = createCategoryRequest.moduleName!!,
            description = createCategoryRequest.description!!,
            createdBy = createCategoryRequest.createdBy!!,
            createdAt = Date(),
            updatedAt = null,
            updatedBy = null,
            deletedAt = null,
            deletedBy = null
        )


        categoryRepository.save(carouselEntity)

        return convertResponse(carouselEntity)
    }

    override fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<CategoryResponse> {
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

        val list = categoryRepository.findAll(generateFilter(filter), pageable)

        val items: List<CategoryEntity> = list.get().collect(Collectors.toList())
        items.forEach {
            it.photo = ipTes+"/api/v1/categories/attachment/" + it.photo
        }

        return ListResponse(
            items = items.map { convertResponse(it) },
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
                    .`object`("/category/"+filename)
                    .build()
            )
        } catch (e: Exception) {
            println("error getObject -> $e")
            return null
        }
        return stream
    }

    override fun get(id: Long): CategoryResponse {
        val response = findCategoryByIdOrThrowNotFound(id)
        return convertResponse(response)
    }

    override fun update(id: Long, updateCategoryRequest: UpdateCategoryRequest): CategoryResponse {
        val category = findCategoryByIdOrThrowNotFound(id)
        validationUtil.validate(category)
        var extension = updateCategoryRequest.file!!.contentType!!.substringAfterLast("/")

        if(extension.isEmpty()){
            extension = ".png"
        }else{
            extension = "."+extension
        }
        var fileName = ""
        if (updateCategoryRequest.file !== null) {
            fileName = Date().time.toString()+extension
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/category/" + fileName)
                        .stream(updateCategoryRequest.file!!.inputStream, updateCategoryRequest.file!!.size, -1)
                        .contentType(updateCategoryRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }
        category.apply {
            name = updateCategoryRequest.name!!
            slug = updateCategoryRequest.slug!!
            order = updateCategoryRequest.order!!
            isModule = updateCategoryRequest.isModule!!
            moduleName = updateCategoryRequest.moduleName!!
            photo = fileName!!
            link = updateCategoryRequest.link!!
            description = updateCategoryRequest.description!!
            isActive = updateCategoryRequest.isActive!!
            updatedBy = updateCategoryRequest.updatedBy!!
            updatedAt = Date()
        }
        categoryRepository.save(category)
        return convertResponse(category)
    }

    override fun delete(id: Long, deleteCategoryRequest: DeleteCategoryRequest): String {
        val category = findCategoryByIdOrThrowNotFound(id)

        try {
            if(deleteCategoryRequest.softDelete == true || deleteCategoryRequest.softDelete == null){
                category.apply {
                    isDeleted = true
                    deletedBy = 1
                    deletedAt = Date()
                }
                categoryRepository.save(category)
            }else{
                categoryRepository.delete(category)
            }
        } catch (e: Exception) {
            println("error -> $e")
            throw DeleteDataException()
        }
        return "Delete Successfully"
    }

    private fun convertResponse(categoryEntity: CategoryEntity): CategoryResponse {
        return CategoryResponse(
            id = categoryEntity.id,
            name = categoryEntity.name,
            photo = categoryEntity.photo,
            slug = categoryEntity.slug,
            link = categoryEntity.link,
            order = categoryEntity.order,
            isModule = categoryEntity.isModule,
            moduleName = categoryEntity.moduleName,
            IsActive = categoryEntity.isActive,
            isDeleted = categoryEntity.isDeleted,
            description = categoryEntity.description,
            createdAt = categoryEntity.createdAt,
            createdBy = categoryEntity.createdBy,
            updatedAt = categoryEntity.updatedAt,
            updatedBy = categoryEntity.updatedBy,
            deletedAt = categoryEntity.deletedAt,
            deletedBy = categoryEntity.deletedBy,
        )
    }

    private fun generateFilter(filter: Map<String, String>): Specification<CategoryEntity>? {
        val options: MutableList<FilterMapper> = mutableListOf()
        val filters = filterRequestUtil.toFilterCriteria(filter, options)
        return specification.buildPredicate(filters)
    }

    private fun findCategoryByIdOrThrowNotFound(id: Long): CategoryEntity{
        val category = categoryRepository.findByIdOrNull(id)
        if (category == null){
            throw NotFoundException()
        }else{
            return category
        }
    }

}