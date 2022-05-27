package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile

data class UpdateCategoryRequest (
    val name: String?,

    val slug: String?,

    val order: Int?,

    val isModule: Boolean?= false,

    val moduleName: String?= "",

    val link: String?= "",

    val description: String?= "",

    val isActive: Boolean?= true,

    val updatedBy: Int? = 1,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
)