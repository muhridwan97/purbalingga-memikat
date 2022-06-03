package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Time
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "newses")
@Where(clause = "is_deleted=false")
data class NewsEntity (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long,

    var title : String,

    var body : String,

    var slug : String,

    var date : Date,

    var time: Time,

    var writer : String,

    var photo : String,

    var description : String?,

    var link : String?,

    @Column(name = "is_active")
    var isActive : Boolean? = false,

    @Column(name = "is_deleted")
    var isDeleted : Boolean? = false,

    @Column(name = "created_at")
    @CreationTimestamp
    var createdAt : Date,

    @Column(name = "created_by")
    var createdBy : Int,

    @Column(name = "updated_at")
    @UpdateTimestamp
    var updatedAt : Date?,

    @Column(name = "updated_by")
    var updatedBy : Int?,

    @Column(name = "deleted_at")
    var deletedAt : Date?,

    @Column(name = "deleted_by")
    var deletedBy : Int?
        )