package br.com.erudio.mockito.services

import br.com.erudio.mocks.MockBook
import br.com.erudio.repository.BookRepository
import br.com.erudio.services.BooksService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class BooksServiceTest {
    private lateinit var inputObject: MockBook


    @Mock
    private lateinit var mockRepository: BookRepository

    @InjectMocks
    private lateinit var service: BooksService


    @BeforeEach
    fun setUp() {
        inputObject = MockBook()
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun findByIdBook() {
        val mockBookEntity = inputObject.mockEntity(1)
        Mockito.`when`(mockRepository.findById(mockBookEntity.id)).thenReturn(Optional.of(mockBookEntity))

        val result = service.findByIdBook(mockBookEntity.id)
        mockBookEntity.id = 1

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.title)
        assertNotNull(result.author)
        assertNotNull(result.price)
        assertEquals(mockBookEntity.id, result.key)
        assertEquals(mockBookEntity.title, result.title)
        assertEquals(mockBookEntity.author, result.author)
        assertEquals(mockBookEntity.price, result.price)
        assertTrue(result.links.toString().contains("/api/books/v1/1"))
    }

    @Test
    fun createBook() {
        val mockBookEntity = inputObject.mockEntity(1)
        val bookVO = inputObject.mockVO(1)

        Mockito.`when`(mockRepository.save(mockBookEntity)).thenReturn(mockBookEntity.copy())

        val result = service.createBook(bookVO)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertEquals(mockBookEntity.copy().id, result.key)
        assertEquals(mockBookEntity.copy().title, result.title)
        assertEquals(mockBookEntity.copy().author, result.author)
        assertEquals(mockBookEntity.copy().price, result.price)
        assertTrue(result.links.toString().contains("</api/books/v1/1>;rel=\"self\""))
    }

    @Test
    fun updateBook() {
        val mockBookEntity = inputObject.mockEntity(1)
        val bookVO = inputObject.mockVO(1)

        Mockito.`when`(mockRepository.findById(bookVO.key)).thenReturn(Optional.of(mockBookEntity))
        Mockito.`when`(mockRepository.save(mockBookEntity)).thenReturn(mockBookEntity.copy())

        val result = service.updateBook(bookVO)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertEquals(mockBookEntity.copy().id, result.key)
        assertEquals(mockBookEntity.copy().title, result.title)
        assertEquals(mockBookEntity.copy().author, result.author)
        assertEquals(mockBookEntity.copy().price, result.price)
        assertTrue(result.links.toString().contains("</api/books/v1/1>;rel=\"self\""))
    }

    @Test
    fun deleteBook() {
        val mockBookEntity = inputObject.mockEntity(1)

        Mockito.`when`(mockRepository.findById(mockBookEntity.id)).thenReturn(Optional.of(mockBookEntity))
        service.deleteBook(mockBookEntity.id)

        Mockito.verify(mockRepository).delete(mockBookEntity)
    }
}