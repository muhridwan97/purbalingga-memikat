package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.*
import java.sql.Time
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "prices")
@Where(clause = "is_deleted=false")
data class PriceEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long,

    @Column(name="id_object")
    var objectId: Long,

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "id_object", insertable = false, updatable = false)
    var objectEty: ObjectEntity?,

    var price : String,

    var type : String,

    var status : String,

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
