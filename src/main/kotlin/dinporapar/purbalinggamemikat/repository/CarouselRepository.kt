package dinporapar.purbalinggamemikat.repository

import dinporapar.purbalinggamemikat.entity.Carousel
import org.springframework.data.jpa.repository.JpaRepository

interface CarouselRepository : JpaRepository<Carousel, Long> {
}