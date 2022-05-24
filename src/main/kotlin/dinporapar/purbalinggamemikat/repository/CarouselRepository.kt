package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.CarouselEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CarouselRepository : JpaRepository<CarouselEntity, Long>,JpaSpecificationExecutor<CarouselEntity> {
}