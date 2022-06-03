package dinporapar.purbalinggamemikat.service.impl

import dinporapar.purbalinggamemikat.entity.NewsEntity
import dinporapar.purbalinggamemikat.error.DeleteDataException
import dinporapar.purbalinggamemikat.error.NotFoundException
import dinporapar.purbalinggamemikat.error.UploadException
import dinporapar.purbalinggamemikat.model.request.CreateNewsRequest
import dinporapar.purbalinggamemikat.model.request.DeleteNewsRequest
import dinporapar.purbalinggamemikat.model.request.RequestParams
import dinporapar.purbalinggamemikat.model.request.UpdateNewsRequest
import dinporapar.purbalinggamemikat.model.response.NewsResponse
import dinporapar.purbalinggamemikat.model.response.pageable.ListResponse
import dinporapar.purbalinggamemikat.model.response.pageable.PagingResponse
import dinporapar.purbalinggamemikat.repository.NewsRepository
import dinporapar.purbalinggamemikat.service.NewsService
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
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

@Service
class NewsServiceImpl(
    val newsRepository: NewsRepository,
    val validationUtil: ValidationUtil,
    val filterRequestUtil: FilterRequestUtil,
    private val specification: FilterSpecification<NewsEntity>
) : NewsService {

    @Autowired
    lateinit var minioClient: MinioClient

    @Value("\${minio.bucket.name}")
    lateinit var bucketName: String

    @Value("\${tes.ip}")
    lateinit var ipTes: String

    override fun create(createNewsRequest: CreateNewsRequest): NewsResponse {
        validationUtil.validate(createNewsRequest)
        var fileName = ""
        if (createNewsRequest.file !== null) {
            var extension = createNewsRequest.file!!.contentType!!.substringAfterLast("/")

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
                        .`object`("/news/" + fileName)
                        .stream(createNewsRequest.file!!.inputStream, createNewsRequest.file!!.size, -1)
                        .contentType(createNewsRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }

        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss")
        val timeNews = Time(formatter.parse(createNewsRequest.time!!).time)

        val newsEntity = NewsEntity(
            id = -1,
            title = createNewsRequest.title!!,
            body = createNewsRequest.body!!,
            slug = createNewsRequest.slug!!,
            date = createNewsRequest.date!!,
            time = timeNews,
            writer = createNewsRequest.writer!!,
            photo = fileName,
            link = createNewsRequest.link!!,
            description = createNewsRequest.description!!,
            isActive = createNewsRequest.isActive!!,
            createdBy = createNewsRequest.createdBy!!,
            createdAt = Date(),
            updatedAt = null,
            updatedBy = null,
            deletedAt = null,
            deletedBy = null
        )


        var result = newsRepository.save(newsEntity)

        return convertToResponse(result)
    }

    override fun list(requestParams: RequestParams, filter: Map<String, String>): ListResponse<NewsResponse> {
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

        val list = newsRepository.findAll(generateFilter(filter), pageable)

        val items: List<NewsEntity> = list.get().collect(Collectors.toList())
        items.forEach {
            if(!it.photo.isEmpty()) {
                it.photo = ipTes + "/api/v1/news/attachment/" + it.photo
            }
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
                    .`object`("/news/"+filename)
                    .build()
            )
        } catch (e: Exception) {
            println("error getObject -> $e")
            return null
        }
        return stream
    }

    override fun get(id: Long): NewsResponse {
        val response = findNewsByIdOrThrowNotFound(id)
        if(response.photo != null){
            response.photo = ipTes + "/api/v1/news/attachment/" + response.photo
        }
        return convertToResponse(response)
    }

    override fun update(id: Long, updateNewsRequest: UpdateNewsRequest): NewsResponse {
        val news = findNewsByIdOrThrowNotFound(id)
        validationUtil.validate(news)

        var fileName = news.photo

        if (updateNewsRequest.file !== null) {
            var extension = updateNewsRequest.file!!.contentType!!.substringAfterLast("/")

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
                        .`object`("/news/" + news.photo)
                        .build()
                )

                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`("/news/" + fileName)
                        .stream(updateNewsRequest.file!!.inputStream, updateNewsRequest.file!!.size, -1)
                        .contentType(updateNewsRequest.file!!.contentType)
                        .build()
                )
            } catch (e: Exception) {
                println("error -> $e")
                throw UploadException()
            }
        }
        val timeNews = news.time
        if(updateNewsRequest.time != null){
            val formatter: DateFormat = SimpleDateFormat("HH:mm:ss")
            val timeNews = Time(formatter.parse(updateNewsRequest.time!!).time)
        }

        news.apply {
            title = updateNewsRequest.title!!
            body = updateNewsRequest.body!!
            slug = updateNewsRequest.slug!!
            date = updateNewsRequest.date!!
            time = timeNews!!
            writer = updateNewsRequest.writer!!
            photo = fileName!!
            link = updateNewsRequest.link!!
            description = updateNewsRequest.description!!
            updatedBy = updateNewsRequest.updatedBy!!
            updatedAt = Date()
        }
        newsRepository.save(news)
        return convertToResponse(news)
    }

    override fun delete(id: Long, deleteNewsRequest: DeleteNewsRequest): String {
        val news = findNewsByIdOrThrowNotFound(id)

        try {
            if(deleteNewsRequest.softDelete == true || deleteNewsRequest.softDelete == null){
                news.apply {
                    isDeleted = true
                    deletedBy = 1
                    deletedAt = Date()
                }
                newsRepository.save(news)
            }else{
                try {
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .`object`("/news/" + news.photo)
                            .build()
                    )
                } catch (e: Exception) {
                    println("error -> $e")
                    throw UploadException()
                }
                newsRepository.delete(news)
            }
        } catch (e: Exception) {
            println("error -> $e")
            throw DeleteDataException()
        }
        return "Delete Successfully"
    }

    private fun convertToResponse(newsEntity: NewsEntity): NewsResponse {
        return NewsResponse(
            id = newsEntity.id,
            title = newsEntity.title,
            body = newsEntity.body,
            slug = newsEntity.slug,
            date = newsEntity.date,
            time = newsEntity.time,
            writer = newsEntity.writer,
            photo = newsEntity.photo,
            link = newsEntity.link,
            description = newsEntity.description,
            IsActive = newsEntity.isActive,
            isDeleted = newsEntity.isDeleted,
            createdAt = newsEntity.createdAt,
            createdBy = newsEntity.createdBy,
            updatedAt = newsEntity.updatedAt,
            updatedBy = newsEntity.updatedBy,
            deletedAt = newsEntity.deletedAt,
            deletedBy = newsEntity.deletedBy,
        )
    }

    private fun generateFilter(filter: Map<String, String>): Specification<NewsEntity>? {
        val options: MutableList<FilterMapper> = mutableListOf()
        val filters = filterRequestUtil.toFilterCriteria(filter, options)
        return specification.buildPredicate(filters)
    }

    private fun findNewsByIdOrThrowNotFound(id: Long): NewsEntity{
        val news = newsRepository.findByIdOrNull(id)
        if (news == null){
            throw NotFoundException()
        }else{
            return news
        }
    }
}