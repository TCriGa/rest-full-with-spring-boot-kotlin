package br.com.erudio.integrationtests.vo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.*

@JsonPropertyOrder("id", "author", "publicationYear", "price", "title")
data class BooksVO(
    @Mapping("id")
    @JsonProperty("id")
    var key: Long = 0,
    var title: String = "",
    var author: String = "",
    var price: Double = 0.0,
    var publicationYear: Date? = null
): RepresentationModel<BooksVO>()
