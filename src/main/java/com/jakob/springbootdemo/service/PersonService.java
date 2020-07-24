package com.jakob.springbootdemo.service;

import com.jakob.springbootdemo.entity.Person;
import com.jakob.springbootdemo.repository.PersonRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jakob
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @CachePut(value = {"person"}, key = "#person.id")
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Cacheable(value = {"person"}, key = "#id")
    public Person get(Integer id) {
        System.out.println("缓存未命中： " + id);
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public List<Person> getByName(String name) {
        return personRepository.findByName(name);
    }

    public Person getByIdAndName(Integer id, String name) {
        return personRepository.findByIdAndName(id, name);
    }
}
