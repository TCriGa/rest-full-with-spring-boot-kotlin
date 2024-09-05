package br.com.erudio.integrationtests.swagger

import br.com.erudio.ConfigsTest
import br.com.erudio.integrationtests.testcontainer.AbstractIntegrationTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class SwaggerIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun shouldDisplaySwaggerUiPage() {
        val content = given()
            .basePath("/swagger-ui/index.html")
            .port(ConfigsTest.SERVER_PORT)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        assertTrue(content.contains("Swagger UI"))
    }

}