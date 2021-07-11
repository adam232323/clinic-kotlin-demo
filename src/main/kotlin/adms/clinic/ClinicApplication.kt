package adms.clinic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ClinicApplication

fun main(args: Array<String>) {
	runApplication<ClinicApplication>(*args)
}
