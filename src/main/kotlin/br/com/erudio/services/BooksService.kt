package br.com.erudio.services

import br.com.erudio.controller.BooksController
import br.com.erudio.date.vo.v1.BooksVO
import br.com.erudio.exceptions.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Books
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger


@Service
class BooksService {

    @Autowired
    private lateinit var bookRepository: BookRepository

    private val logger = Logger.getLogger(BooksService::class.java.name)

    fun findAllBooks(): List<BooksVO> {
        logger.info("Find all books!")
        val booksVOS = DozerMapper.parseListObject(bookRepository.findAll(), BooksVO::class.java)

        for (book in booksVOS) {
            val withSelfRel = linkTo(BooksController::class.java).slash(book.key).withSelfRel()
            book.add(withSelfRel)
        }

        return booksVOS
    }

    fun findByIdBook(id: Long): BooksVO {
        logger.info("Find book with id: $id")
        val book = bookRepository.findById(id).orElseThrow { RuntimeException("No book found for id: $id") }
        val bookVO = DozerMapper.parseObject(book, BooksVO::class.java)

        val withSelfRel = linkTo(BooksController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun createBook(book: BooksVO): BooksVO {
        logger.info("Create new book of author {$book.author}")
        val entity = DozerMapper.parseObject(book, Books::class.java)
        val bookVO = DozerMapper.parseObject(bookRepository.save(entity), BooksVO::class.java)
        val withSelfRel = linkTo(BooksController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun updateBook(book: BooksVO): BooksVO {
        logger.info("Update book with id: ${book.key}")
        val booksEntity = bookRepository.findById(book.key).orElseThrow { ResourceNotFoundException("Books not found") }
        booksEntity.title = book.title
        booksEntity.author = book.author
        booksEntity.price = book.price
        booksEntity.publicationYear = book.publicationYear
        val updatedEntity = bookRepository.save(booksEntity)
        val updatedBookVO = DozerMapper.parseObject(updatedEntity, BooksVO::class.java)
        val withSelfRel = linkTo(BooksController::class.java).slash(updatedBookVO.key).withSelfRel()
        updatedBookVO.add(withSelfRel)
        return updatedBookVO
    }

    fun deleteBook(id: Long){
        logger.info("Delete book with id: $id")
        val booksEntity = bookRepository.findById(id).orElseThrow { ResourceNotFoundException("Books not found") }
        bookRepository.delete(booksEntity)
    }
}