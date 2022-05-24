package dinporapar.purbalinggamemikat.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "carousels")
@Where(clause = "is_deleted=false")
data class CarouselEntity (

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long,

    @Column(name = "photo")
    var photo : String,

    @Column(name = "link")
    var link : String?,

    @Column(name = "description")
    var description : String?,

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