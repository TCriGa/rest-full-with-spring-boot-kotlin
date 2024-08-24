package br.com.erudio.mockito.services

import br.com.erudio.exceptions.RequiredObjectIsNullException
import br.com.erudio.repository.PersonRepository
import br.com.erudio.services.PersonService
import br.com.erudio.unittests.mapper.mocks.MockPerson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PersonServiceTest {
    private lateinit var inputObject: MockPerson

    @InjectMocks
    private lateinit var service: PersonService

    @Mock
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUp() {
        inputObject = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findAll() {
        // given
        val persons = listOf(inputObject.mockEntity(1), inputObject.mockEntity(2))
        Mockito.`when`(repository.findAll()).thenReturn(persons)

        // when
        val result = service.findAll()
        val personOne = result.first()
        // then
        assertEquals(persons.size, result.size)
        assertTrue(result.contains(inputObject.mockVO(1)))
        assertTrue(result.contains(inputObject.mockVO(2)))
        assertNotNull(result)
        assertNotNull(result.firstOrNull()?.key)
        assertNotNull(result.firstOrNull()?.links)
        assertTrue(personOne.links.toString().contains("</api/person/v1/1>;rel=\"self\""))

    }

    @Test
    fun findById() {
        // given
        val person = inputObject.mockEntity() // mock entidade
        person.id = 1L
        Mockito.`when`(repository.findById(person.id)).thenReturn(java.util.Optional.of(person))

        // when
        val result = service.findById(person.id)

        // then
        assertEquals(person.id, result.key)
        assertEquals(person.firstName, result.firstName)
        assertEquals(person.lastName, result.lastName)
        assertEquals(person.address, result.address)
        assertEquals(person.gender, result.gender)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
    }

    @Test
    fun create() {
        // given
        val entity = inputObject.mockEntity(1)
        val persisted = entity.copy()
        val mockVO = inputObject.mockVO(1)

        Mockito.`when`(repository.save(entity)).thenReturn(persisted)

        // when
        val result = service.create(mockVO)

        // then
        assertEquals(persisted.id, result.key)
        assertEquals(persisted.firstName, result.firstName)
        assertEquals(persisted.lastName, result.lastName)
        assertEquals(persisted.address, result.address)
        assertEquals(persisted.gender, result.gender)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/person/v1/1>;rel=\"self\""))
    }

    @Test
    fun createWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java) {
            service.create(null)
        }
        assertEquals("Person object cannot be null", exception.message)
    }

    @Test
    fun update() {
        // given
        val entity = inputObject.mockEntity(1)
        val persisted = entity.copy()
        val mockVO = inputObject.mockVO(1)

        Mockito.`when`(repository.findById(entity.id)).thenReturn(java.util.Optional.of(entity))
        Mockito.`when`(repository.save(entity)).thenReturn(persisted)

        // when
        val result = service.update(mockVO)

        // then
        assertEquals(persisted.id, result.key)
        assertEquals(persisted.firstName, result.firstName)
        assertEquals(persisted.lastName, result.lastName)
        assertEquals(persisted.address, result.address)
        assertEquals(persisted.gender, result.gender)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("</api/person/v1/1>;rel=\"self\""))
    }

    @Test
    fun updateWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java) {
            service.update(null)
        }
        assertEquals("Person object cannot be null", exception.message)
    }

    @Test
    fun delete() {
        // given
        val entity = inputObject.mockEntity(1)

        Mockito.`when`(repository.findById(entity.id)).thenReturn(java.util.Optional.of(entity))

        // when
        service.delete(entity.id)

        // then
        Mockito.verify(repository).delete(entity)
    }


}