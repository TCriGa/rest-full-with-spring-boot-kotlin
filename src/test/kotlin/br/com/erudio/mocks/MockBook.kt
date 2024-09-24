package br.com.erudio.mocks

import br.com.erudio.date.vo.v1.BookVO


import br.com.erudio.model.Books


class MockBook {

    fun mockEntityList(): ArrayList<Books> {
        val books: ArrayList<Books> = ArrayList()
        for (i in 0..13) {
            books.add(mockEntity(i))
        }
        return books
    }

    fun mockVOList(): ArrayList<BookVO> {
        val books: ArrayList<BookVO> = ArrayList()
        for (i in 0..13) {
            books.add(mockVO(i))
        }
        return books
    }

    fun mockEntity(number: Int) = Books(
        id = number.toLong(),
        author = "Some Author$number",
        price = 25.0,
        title = "Some Title$number"
    )

    fun mockVO(number: Int) = BookVO(
        key = number.toLong(),
        author = "Some Author$number",
        price = 25.0,
        title = "Some Title$number"
    )
}