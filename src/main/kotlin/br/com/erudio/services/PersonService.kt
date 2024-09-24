package br.com.erudio.services

import br.com.erudio.controller.PersonController
import br.com.erudio.date.vo.v1.PersonVO
import br.com.erudio.exceptions.RequiredObjectIsNullException
import br.com.erudio.exceptions.ResourceNotFoundException
import br.com.erudio.mapper.DozerMapper
import br.com.erudio.model.Person
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {
    @Autowired
    private lateinit var repository: PersonRepository


    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Find all people!")
        val vos = DozerMapper.parseListObjects(repository.findAll(), PersonVO::class.java)

        for (person in vos) {
            val withSelfRel = linkTo(PersonController::class.java).slash(person.key).withSelfRel()
            person.add(withSelfRel)
        }
        return vos
    }

    fun findById(id: Long): PersonVO {
        logger.info("Fetching person with id: $id")
        val person =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this ID: $id!") }
        val personVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException("Person object cannot be null")
        logger.info("Creating new person with name: ${person.firstName}")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)


        return personVO
    }

    fun update(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException("Person object cannot be null")
        logger.info("Updating one person with ID ${person.key}!")
        val entity = person.key.let {
            repository.findById(it)
                .orElseThrow { ResourceNotFoundException("No records found for this ID!") }
        }

        entity?.firstName = person.firstName
        entity?.lastName = person.lastName
        entity?.address = person.address
        entity?.gender = person.gender
        val personVO = DozerMapper.parseObject(entity?.let { repository.save(it) }, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting person with id : $id")
        val entity =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this ID: $id") }
        repository.delete(entity)
    }
}