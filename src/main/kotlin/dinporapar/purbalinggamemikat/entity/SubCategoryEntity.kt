package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name= "ref_sub_categories")
data class SubCategoryEntity (

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id : Long,

        @Column(name="id_category")
        var categoryId: Long,

        @ManyToOne
        @NotFound(action = NotFoundAction.IGNORE)
        @JoinColumn(name = "id_category", insertable = false, updatable = false)
        var category: CategoryEntity?,

        var name: String,

        var photo: String,

        var description: String,

        var slug: String,

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