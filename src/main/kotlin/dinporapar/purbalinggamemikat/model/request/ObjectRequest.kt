package dinporapar.purbalinggamemikat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ObjectRequest (
    @field:NotNull
    var categoryId: Long?,

    @field:NotNull
    var subCategoryId: Long?,

    @field:NotBlank
    var name: String?,

    @field:NotBlank
    var address : String?,

    @field:NotBlank
    var phoneNumber : String,

    var website : String?= "",

    var facility : String?,

    @field:NotBlank
    var latitude : String,

    @field:NotBlank
    var longitude : String,

    @field:NotBlank
    var slug : String?,

    var instagram : String? = "",

    var facebook : String?= "",

    var youtube : String?= "",

    var tiktok : String?= "",

    var status : String? = "PENDING",

    var like: Int?,

    var description: String? = "",

    @field:Min(value = 0)
    var createdBy: Int?= 1,

    @field:Min(value = 0)
    var updatedBy: Int?= 1,

    @SuppressWarnings("java:S1948")
    var file: MultipartFile? = null
        )