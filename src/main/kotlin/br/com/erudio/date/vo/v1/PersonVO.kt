package br.com.erudio.date.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel

data class PersonVO(
   @Mapping("id")
   @JsonProperty("id")
    var key: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = "",
) : RepresentationModel<PersonVO>()