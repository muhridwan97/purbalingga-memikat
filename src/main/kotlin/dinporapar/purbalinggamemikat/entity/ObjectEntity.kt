package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.*
import java.sql.Time
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "objects")
@Where(clause = "is_deleted=false")
data class ObjectEntity (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long,

    @Column(name="id_category")
    var categoryId: Long,

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    var category: CategoryEntity?,

    @Column(name="id_sub_category")
    var subCategoryId: Long,

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "id_sub_category", insertable = false, updatable = false)
    var subCategory: SubCategoryEntity?,

    var name : String,

    var address : String,

    @Column(name="phone_number")
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

    var like : Int?,

    var description : String?,

    @Column(name = "is_active")
    var isActive : Boolean? = true,

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