package dinporapar.purbalinggamemikat.model.response

import java.util.*

data class ObjectResponse (
    val id : Long,

    var categoryName: String,

    var subCategoryName: String,

    var name : String,

    var address : String,

    var phoneNumber : String,

    var website : String?,

    var facility : String?,

    var photo : String,

    var latitude : String,

    var longitude : String,

    var slug : String,

    var instagram : String?,

    var facebook : String?,

    var youtube : String?,

    var tiktok : String?,

    var status : String,

    val like: Int?,

    var IsActive : Boolean?,

    val description: String?,

    val createdAt: Date,

    val createdBy: Int,

    val updatedAt: Date?,

    val updatedBy: Int?,
        )