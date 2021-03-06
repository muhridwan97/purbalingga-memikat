package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateCategoryRequest (
    @field:NotBlank
    var name: String?,

    @field:NotBlank
    var slug : String?,

    var link: String?= "",

    var description: String? = "",

    var isActive: Boolean? = true,

    var isModule : Boolean? = false,

    var moduleName : String? = "",

    @field:NotNull
    @field:Min(value = 0)
    var createdBy: Int,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
)