package br.com.erudio.integrationtests.vo.wrappers

import br.com.erudio.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {
    @JsonProperty("personVOes")
    var personVOes: List<PersonVO>? = null
}