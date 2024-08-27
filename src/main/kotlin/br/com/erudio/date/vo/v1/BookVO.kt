package br.com.erudio.date.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.*

data class BooksVO(
    @Mapping("id")
    @JsonProperty("id")
    var key: Long = 0,

    var title: String = "",
    var author: String = "",
    var price: Double = 0.0,
    var publicationYear: Date? = null,
    var person: PersonVO = PersonVO() // Assuming PersonVO is defined elsewhere in the project.
): RepresentationModel<BooksVO>()
