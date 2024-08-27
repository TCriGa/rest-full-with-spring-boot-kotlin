package br.com.erudio.repository

import br.com.erudio.model.Books
import org.springframework.data.jpa.repository.JpaRepository


interface BookRepository : JpaRepository<Books, Long?>