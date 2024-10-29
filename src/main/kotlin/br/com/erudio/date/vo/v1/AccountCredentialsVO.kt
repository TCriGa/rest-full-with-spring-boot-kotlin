package br.com.erudio.date.vo.v1

import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class AccountCredentialsVO(
    val username: String? = null,
    val password: String? = null,
)
