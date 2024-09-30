package br.com.erudio.services

import br.com.erudio.controller.BooksController
import br.com.erudio.date.vo.v1.BookVO
import br.com.erudio.exceptions.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Books
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger


@Service
class BooksService {

    @Autowired
    private lateinit var bookRepository: BookRepository

    private lateinit var assembler: PagedResourcesAssembler<BookVO>

    private val logger = Logger.getLogger(BooksService::class.java.name)

    fun findAllBooks(pageable: Pageable): PagedModel<EntityModel<BookVO>> {
        logger.info("Finding all books!")
        val page = bookRepository.findAll(pageable)
        val vos = page.map { b -> DozerMapper.parseObject(b, BookVO::class.java) }
        vos.map { p -> p.add(linkTo(BooksController::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)
    }


    fun findByIdBook(id: Long): BookVO {
        logger.info("Finding one book with ID $id!")
        var book = bookRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!") }
        val bookVO: BookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BooksController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun createBook(book: BookVO): BookVO {
        logger.info("Create new book of author {$book.author}")
        val entity = DozerMapper.parseObject(book, Books::class.java)
        val bookVO = DozerMapper.parseObject(bookRepository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BooksController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun updateBook(book: BookVO): BookVO {
        logger.info("Update book with id: ${book.key}")
        val booksEntity = bookRepository.findById(book.key).orElseThrow { ResourceNotFoundException("Books not found") }
        booksEntity.title = book.title
        booksEntity.author = book.author
        booksEntity.price = book.price
        booksEntity.publicationYear = book.publicationYear
        val updatedEntity = bookRepository.save(booksEntity)
        val updatedBookVO = DozerMapper.parseObject(updatedEntity, BookVO::class.java)
        val withSelfRel = linkTo(BooksController::class.java).slash(updatedBookVO.key).withSelfRel()
        updatedBookVO.add(withSelfRel)
        return updatedBookVO
    }

    fun deleteBook(id: Long) {
        logger.info("Delete book with id: $id")
        val booksEntity = bookRepository.findById(id).orElseThrow { ResourceNotFoundException("Books not found") }
        bookRepository.delete(booksEntity)
    }
}