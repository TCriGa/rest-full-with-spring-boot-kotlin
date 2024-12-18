package br.com.erudio.integrationtests.controller.withxml

import br.com.erudio.ConfigsTest
import br.com.erudio.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.erudio.integrationtests.vo.AccountCredentialsVO
import br.com.erudio.integrationtests.vo.PersonVO
import br.com.erudio.integrationtests.vo.TokenVO
import br.com.erudio.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerXmlTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "leandro",
            password = "admin123"
        )

        val token = RestAssured.given()
            .basePath("/auth/signin")
            .port(ConfigsTest.SERVER_PORT)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(ConfigsTest.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    fun testCreate() {
        mockPerson()

        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertTrue(item.id > 0)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Richard", item.firstName)
        assertEquals("Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(true, item.enabled)
    }

    @Test
    @Order(2)
    fun testUpdate() {
        person.lastName = "Matthew Stallman"

        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .body(person)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(true, item.enabled)
    }

    @Test
    @Order(3)
    fun testDisablePersonById() {
        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .pathParam("id", person.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)
    }

    @Test
    @Order(4)
    fun testFindById() {
        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .pathParam("id", person.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)
    }

    @Test
    @Order(5)
    fun testDelete() {
        given()
            .spec(specification)
            .pathParam("id", person.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    fun testFindAll() {
        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .queryParams(
                "page", 3,
                "size", 12,
                "direction", "asc"
            )
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.personVOes

        val item1 = people?.get(0)

        assertNotNull(item1!!.id)
        assertNotNull(item1.firstName)
        assertNotNull(item1.lastName)
        assertNotNull(item1.address)
        assertNotNull(item1.gender)
        assertEquals("Allin", item1.firstName)
        assertEquals("Otridge", item1.lastName)
        assertEquals("09846 Independence Center", item1.address)
        assertEquals("Male", item1.gender)
        assertEquals(false, item1.enabled)

        val item2 = people[6]

        assertNotNull(item2.id)
        assertNotNull(item2.firstName)
        assertNotNull(item2.lastName)
        assertNotNull(item2.address)
        assertNotNull(item2.gender)
        assertEquals("Alvera", item2.firstName)
        assertEquals("MacMillan", item2.lastName)
        assertEquals("59929 Loeprich Place", item2.address)
        assertEquals("Female", item2.gender)
        assertEquals(false, item2.enabled)
    }

    @Test
    @Order(7)
    fun testFindPersonByName() {
        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .pathParam("firstName", "Ayr")
            .queryParams(
                "page", 0,
                "size", 12,
                "direction", "asc"
            )
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.personVOes

        val item1 = people?.get(0)

        assertNotNull(item1!!.id)
        assertNotNull(item1.firstName)
        assertNotNull(item1.lastName)
        assertNotNull(item1.address)
        assertNotNull(item1.gender)
        assertEquals("Ayrton", item1.firstName)
        assertEquals("Senna", item1.lastName)
        assertEquals("São Paulo", item1.address)
        assertEquals("Male", item1.gender)
        assertEquals(true, item1.enabled)
    }

    @Test
    @Order(8)
    fun testFindAllWithoutToken() {

        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(ConfigsTest.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        given()
            .spec(specificationWithoutToken)
            .contentType(ConfigsTest.CONTENT_TYPE_XML)
            .`when`()
            .get()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()

    }

    @Test
    @Order(7)
    fun testHATEOAS() {
        val content = given()
            .spec(specification)
            .contentType(ConfigsTest.CONTENT_TYPE_JSON)
            .queryParams(
                "page", 3,
                "size", 12,
                "direction", "asc"
            )
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val jsonNode = objectMapper.readTree(content)
        val links = jsonNode["_embedded"]["personVOes"].map { it["_links"]["self"]["href"].asText() }

        assertTrue(links.contains("http://localhost:8888/api/person/v1/199"))
        assertTrue(links.contains("http://localhost:8888/api/person/v1/797"))
        assertTrue(links.contains("http://localhost:8888/api/person/v1/686"))
        assertTrue(links.contains("http://localhost:8888/api/person/v1/687"))

        val firstLink = jsonNode["_links"]["first"]["href"].asText()
        val prevLink = jsonNode["_links"]["prev"]["href"].asText()
        val selfLink = jsonNode["_links"]["self"]["href"].asText()
        val nextLink = jsonNode["_links"]["next"]["href"].asText()
        val lastLink = jsonNode["_links"]["last"]["href"].asText()

        assertTrue(firstLink == "http://localhost:8888/api/person/v1?direction=asc&page=0&size=12&sort=firstName,asc")
        assertTrue(prevLink == "http://localhost:8888/api/person/v1?direction=asc&page=2&size=12&sort=firstName,asc")
        assertTrue(selfLink == "http://localhost:8888/api/person/v1?direction=asc&page=3&size=12&sort=firstName,asc")
        assertTrue(nextLink == "http://localhost:8888/api/person/v1?direction=asc&page=4&size=12&sort=firstName,asc")
        assertTrue(lastLink == "http://localhost:8888/api/person/v1?direction=asc&page=83&size=12&sort=firstName,asc")

        val size = jsonNode["page"]["size"].asInt()
        val elementSize = jsonNode["page"]["totalElements"].asInt()
        val pageSize = jsonNode["page"]["totalPages"].asInt()
        val number = jsonNode["page"]["number"].asInt()

        assertTrue(size == 12)
        assertTrue(elementSize == 1007)
        assertTrue(pageSize == 84)
        assertTrue(number == 3)
    }

    private fun mockPerson() {
        person.firstName = "Richard"
        person.lastName = "Stallman"
        person.address = "New York City, New York, US"
        person.gender = "Male"
    }
}