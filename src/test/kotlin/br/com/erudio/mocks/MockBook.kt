package br.com.erudio.mocks

import br.com.erudio.date.vo.v1.BooksVO
import br.com.erudio.model.Books

class MockBook {
    fun mockBookEntity(number: Int): Books {
        val book = Books()
        book.id = number.toLong()
        book.author = "Author Test$number"
        book.title = "Book Title Test$number"
        book.price = 25.00
        return book
    }

    fun mockBookVO(number: Int): BooksVO {
        val bookVo = BooksVO()
        bookVo.key = number.toLong()
        bookVo.author = "Author Test$number"
        bookVo.title = "Book Title Test$number"
        bookVo.price = 25.00
        return bookVo
    }

    fun mockBookEntity(): Books = mockBookEntity(1)

    fun mockVO(): BooksVO = mockBookVO(1)

    fun mockBookEntityList(): ArrayList<Books> {
        val books = ArrayList<Books>()
        for (i in 0..13) {
            books.add(mockBookEntity(i))
        }
        return books
    }

    fun mockBookList(): ArrayList<Books> {
        val books = ArrayList<Books>()
        for (i in 0..13) {
            books.add(mockBookEntity(i))
        }
        return books
    }
}
