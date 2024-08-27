package br.com.erudio.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "books")
data class Books(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 180)
    var author: String = "",

    @Column(name = "publication_year")
    var publicationYear: Date? = null,

    @Column(nullable = false)
    var price: Double = 0.0,

    @Column(nullable = false, length = 250)
    var title: String = ""
)