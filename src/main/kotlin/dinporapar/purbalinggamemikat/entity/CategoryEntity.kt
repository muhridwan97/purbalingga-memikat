package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ref_categories")
@Where(clause = "is_deleted=false")
data class CategoryEntity (

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long,

    @Column(name = "name")
    var name : String,

    @Column(name = "photo")
    var photo : String,

    @Column(name = "slug")
    var slug : String,

    @Column(name = "link")
    var link : String?,

    @Column(name = "description")
    var description : String?,

    @Column(name = "the_order")
    var order : Int,

    @Column(name = "is_active")
    var isActive : Boolean? = false,

    @Column(name = "is_module")
    var isModule : Boolean? = false,

    @Column(name = "module_name")
    var moduleName : String?,

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