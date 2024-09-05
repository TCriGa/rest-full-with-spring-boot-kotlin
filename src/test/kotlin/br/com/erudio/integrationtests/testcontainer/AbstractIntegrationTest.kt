package br.com.erudio.integrationtests.testcontainer

import br.com.erudio.ConfigsTest
import br.com.erudio.repository.PersonRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
abstract class AbstractIntegrationTest {
    companion object {
        var mysql = MySQLContainer("mysql:9.0.1")

        @LocalServerPort
        val serverPort = ConfigsTest.SERVER_PORT

        @JvmStatic
        @BeforeAll
        fun setup() {
            mysql.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            mysql.stop()
        }

        @Autowired
        var personRepository: PersonRepository? = null

        @BeforeEach
        fun setUp() {
            RestAssured.baseURI = "http://localhost:$serverPort"
            personRepository?.deleteAll()
        }

        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)
        }

    }
}
