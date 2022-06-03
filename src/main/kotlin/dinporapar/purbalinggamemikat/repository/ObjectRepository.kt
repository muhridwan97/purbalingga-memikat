package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.ObjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ObjectRepository : JpaRepository<ObjectEntity,Long>, JpaSpecificationExecutor<ObjectEntity> {
}