package br.com.erudio.integrationtests.controller.withYml

import br.com.erudio.ConfigsTest
import br.com.erudio.integrationtests.controller.withYml.mapper.YMLMapper
import br.com.erudio.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.TokenVO
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YMLMapper
    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests() {
        tokenVO = TokenVO()
        objectMapper = YMLMapper()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "leandro",
            password = "admin123"
        )

        tokenVO = RestAssured
            .given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(ConfigsTest.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(ConfigsTest.SERVER_PORT)
            .accept(ConfigsTest.CONTENT_TYPE_YML)
            .contentType(ConfigsTest.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)


        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }

    @Test
    @Order(1)
    fun testRefreshToken() {

        tokenVO = RestAssured.given()
            .config(
                RestAssured.config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(ConfigsTest.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/refresh")
            .port(ConfigsTest.SERVER_PORT)
            .accept(ConfigsTest.CONTENT_TYPE_YML)
            .contentType(ConfigsTest.CONTENT_TYPE_YML)
            .pathParam("username", tokenVO.username)
            .header(
                ConfigsTest.HEADER_PARAM_AUTHORIZATION,
                "Bearer ${tokenVO.refreshToken}"
            )
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}