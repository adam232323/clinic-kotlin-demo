package adms.clinic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@ActiveProfiles(profiles = ["test"])
@Transactional
@Rollback
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
abstract class MvcIntegrationTest {

  @Autowired
  protected lateinit var mvc: MockMvc

  @Autowired
  protected lateinit var entityManager: EntityManager
}