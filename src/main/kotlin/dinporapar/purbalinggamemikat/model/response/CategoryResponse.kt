package dinporapar.purbalinggamemikat.model.response

import java.util.*

data class CategoryResponse(

    val id: Long,

    val name: String,

    val photo: String,

    val slug: String,

    val order: Int,

    val isModule: Boolean?,

    val moduleName: String?,

    val link: String?,

    val IsActive: Boolean?,

    val isDeleted: Boolean?,

    val description: String?,

    val createdAt: Date,

    val createdBy: Int,

    val updatedAt: Date?,

    val updatedBy: Int?,

    val deletedAt: Date?,

    val deletedBy: Int?,
)