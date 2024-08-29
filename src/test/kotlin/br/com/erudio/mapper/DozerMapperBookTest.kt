package br.com.erudio.mapper

import br.com.erudio.date.vo.v1.BooksVO
import br.com.erudio.mocks.MockBook
import br.com.erudio.model.Books
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DozerMapperBookTest {

    private var inputObject: MockBook? = null

    @BeforeEach
    fun setUp() {
        inputObject = MockBook()
    }

    @Test
    fun parseEntityToVOTest() {
        val output: BooksVO = DozerMapper.parseObject(inputObject!!.mockBookEntity(), BooksVO::class.java)
        assertEquals(1, output.key)
        assertEquals("Book Title Test1", output.title)
        assertEquals("Author Test1", output.author)
        assertEquals(25.00, output.price)
    }

    @Test
    fun parseEntityListToVOListTest() {
        val outputList: List<BooksVO> =
            DozerMapper.parseListObject(inputObject!!.mockBookEntityList(), BooksVO::class.java)

        val outputZero: BooksVO = outputList[0]

        assertEquals(0, outputZero.key)
        assertEquals("Book Title Test0", outputZero.title)
        assertEquals("Author Test0", outputZero.author)
        assertEquals(25.00, outputZero.price)

        val outputSeven: BooksVO = outputList[7]

        assertEquals(7, outputSeven.key)
        assertEquals("Book Title Test7", outputSeven.title)
        assertEquals("Author Test7", outputSeven.author)
        assertEquals(25.00, outputSeven.price)

        val outputTwelve: BooksVO = outputList[12]

        assertEquals(12, outputTwelve.key)
        assertEquals("Book Title Test12", outputTwelve.title)
        assertEquals("Author Test12", outputTwelve.author)
        assertEquals(25.00, outputTwelve.price)

    }

    @Test
    fun parseVOToEntityTest() {
        val output: Books = DozerMapper.parseObject(inputObject!!.mockVO(), Books::class.java)

        assertEquals(1, output.id)
        assertEquals("Book Title Test1", output.title)
        assertEquals("Author Test1", output.author)
        assertEquals(25.00, output.price)
    }

    @Test
    fun parseVOListToEntityListTest() {
        val outputList: List<Books> = DozerMapper.parseListObject(inputObject!!.mockBookList(), Books::class.java)

        val outputZero: Books = outputList[0]

        assertEquals(0, outputZero.id)
        assertEquals("Book Title Test0", outputZero.title)
        assertEquals("Author Test0", outputZero.author)
        assertEquals(25.00, outputZero.price)

        val outputSeven: Books = outputList[7]

        assertEquals(7, outputSeven.id)
        assertEquals("Book Title Test7", outputSeven.title)
        assertEquals("Author Test7", outputSeven.author)
        assertEquals(25.00, outputSeven.price)

        val outputTwelve: Books = outputList[12]

        assertEquals(12, outputTwelve.id)
        assertEquals("Book Title Test12", outputTwelve.title)
    }

}