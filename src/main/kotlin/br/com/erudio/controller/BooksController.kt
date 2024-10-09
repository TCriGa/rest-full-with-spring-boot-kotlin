package br.com.erudio.controller

import br.com.erudio.date.vo.v1.BookVO
import br.com.erudio.services.BooksService
import br.com.erudio.utl.MediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books/v1")
class BooksController {
    @Autowired
    private lateinit var booksService: BooksService

    @GetMapping(produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML])
    @Operation(
        summary = "Finds all Books",
        description = "Finds all Books",
        tags = ["Books"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [Content(array = ArraySchema(schema = Schema(implementation = BookVO::class)))]
            ),
            ApiResponse(
                description = "No Content",
                responseCode = "204",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Not Found",
                responseCode = "404",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
            ApiResponse(
                description = "Internal Error",
                responseCode = "500",
                content = [Content(schema = Schema(implementation = Unit::class))]
            ),
        ]
    )
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "12") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ): ResponseEntity<PagedModel<EntityModel<BookVO>>> {
        val sortDirection: Sort.Direction =
            if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"))
        return ResponseEntity.ok(booksService.findAll(pageable))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]
    )
    @Operation(
        summary = "Finds a book by ID", description = "Finds a book by ID", tags = ["Books"], responses = [ApiResponse(
            description = "Success",
            responseCode = "200",
            content = [Content(schema = Schema(implementation = BookVO::class))]
        ), ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Unauthorized",
            responseCode = "401",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = [Content(schema = Schema(implementation = Unit::class))]
        )]
    )
    fun findByIdBook(
        @PathVariable(value = "id") id: Long,
    ) = booksService.findByIdBook(id)

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]
    )
    @Operation(
        summary = "Creates a new book", description = "Creates a new book", tags = ["Books"], responses = [ApiResponse(
            description = "Success",
            responseCode = "200",
            content = [Content(schema = Schema(implementation = BookVO::class))]
        ), ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Unauthorized",
            responseCode = "401",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = [Content(schema = Schema(implementation = Unit::class))]
        )]
    )
    fun createBook(@RequestBody book: BookVO) = booksService.createBook(book)

    @PutMapping(
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]
    )
    @Operation(
        summary = "Updates a book's information",
        description = "Updates a book's information",
        tags = ["Books"],
        responses = [ApiResponse(
            description = "Success",
            responseCode = "200",
            content = [Content(schema = Schema(implementation = BookVO::class))]
        ), ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Unauthorized",
            responseCode = "401",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = [Content(schema = Schema(implementation = Unit::class))]
        )]
    )
    fun updateBook(@RequestBody book: BookVO) = booksService.updateBook(book)

    @DeleteMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]
    )
    @Operation(
        summary = "Removes a book by ID",
        description = "Removes a book by ID",
        tags = ["Books"],
        responses = [ApiResponse(
            description = "Success",
            responseCode = "204",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Unauthorized",
            responseCode = "401",
            content = [Content(schema = Schema(implementation = Unit::class))]
        ), ApiResponse(
            description = "Internal Server Error",
            responseCode = "500",
            content = [Content(schema = Schema(implementation = Unit::class))]
        )]
    )
    fun deleteBook(@PathVariable(value = "id") id: Long): ResponseEntity<*> {
        booksService.deleteBook(id)
        return ResponseEntity.noContent().build<Any>()
    }
}