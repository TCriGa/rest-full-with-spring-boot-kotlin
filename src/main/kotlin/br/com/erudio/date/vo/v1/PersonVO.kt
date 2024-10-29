package br.com.erudio.date.vo.v1

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.hateoas.RepresentationModel

@JsonPropertyOrder("id", "firstName", "lastName", "address", "gender", "enabled")
data class PersonVO (

    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = "",
    var enabled: Boolean = true
) : RepresentationModel<PersonVO>()