package br.com.erudio.controller

import br.com.erudio.date.vo.v1.BooksVO
import br.com.erudio.services.BooksService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books/v1")
class BooksController {
    @Autowired
    private lateinit var booksService: BooksService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"])
    fun findAllBooks() = booksService.findAllBooks()

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun findByIdBook(
        @PathVariable(value = "id") id:Long,
    ) = booksService.findByIdBook(id)

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"]
    )
    fun createBook(@RequestBody book: BooksVO) = booksService.createBook(book)

    @PutMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"]
    )
    fun updateBook(@RequestBody book: BooksVO) = booksService.updateBook(book)

    @DeleteMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun deleteBook(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
        booksService.deleteBook(id)
        return ResponseEntity.noContent().build<Any>()
    }
}