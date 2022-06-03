package dinporapar.purbalinggamemikat.model.response

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Time
import java.util.*
import javax.persistence.Column

data class NewsResponse (
    val id : Long,

    var title : String,

    var body : String,

    var slug : String,

    var date : Date?,

    var time : Time,

    var writer : String,

    var photo : String,

    var description : String?,

    var link : String?,

    var IsActive : Boolean?,

    var isDeleted : Boolean?,

    var createdAt : Date,

    var createdBy : Int,

    var updatedAt : Date?,

    var updatedBy : Int?,

    var deletedAt : Date?,

    var deletedBy : Int?
        )