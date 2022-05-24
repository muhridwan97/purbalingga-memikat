package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

data class CreateCategoryRequest (
    @field:NotNull
    var name: String,

    @field:NotNull
    var slug : String,

    var link: String?,

    var description: String?,

    var isActive: Boolean? = true,

    var isModule : Boolean? = false,

    var moduleName : String? = "",

    @field:NotNull
    var createdBy: Int,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
)