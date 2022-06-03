package dinporapar.purbalinggamemikat

import dinporapar.purbalinggamemikat.properties.ConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties::class)
class PurbalinggaMemikatApplication

fun main(args: Array<String>) {
	runApplication<PurbalinggaMemikatApplication>(*args)
}
