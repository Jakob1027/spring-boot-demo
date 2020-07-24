package com.jakob.springbootdemo.repository;

import com.jakob.springbootdemo.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Jakob
 */
public interface PersonRepository extends MongoRepository<Person, Integer> {
    List<Person> findByName(String name);

    Person findByIdAndName(Integer id, String name);
}
