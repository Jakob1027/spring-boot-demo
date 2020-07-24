package com.jakob.springbootdemo.controller;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakob.springbootdemo.entity.Person;
import com.jakob.springbootdemo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Jakob
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private Meter requests;

    @Autowired
    private Histogram responseSize;

    @Autowired
    private Timer timer;

    @Autowired
    private PersonService personService;

    @PostMapping("/save")
    public void save(@RequestBody Person person) {
        personService.save(person);
    }

    @GetMapping("/save/{id}")
    public void saveGet(@PathVariable Integer id) throws IOException {
        Person person = personService.get(id);
        System.out.println("get: " + person.hashCode());
        Person savedPerson = personService.save(person);
        System.out.println("save: " + savedPerson.hashCode());
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(savedPerson);
        Person clonedPerson = objectMapper.readValue(s, Person.class);
        System.out.println("clonedPerson:  " + clonedPerson.hashCode());
    }

    @GetMapping("/get")
    public List<Person> getByParamName(@RequestParam String name) {

        return personService.getByName(name);
    }

    @GetMapping("/id/{id}")
    public Person get(@PathVariable Integer id) {
        Person person = personService.get(id);
        System.out.println(person.hashCode());
        return person;
    }

    @GetMapping("/rename/id/{id}")
    public void rename(@PathVariable Integer id) {
        Person person = personService.get(id);
        person.setName("修改后");
        System.out.println(person.hashCode());
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Person> getAll() {
        requests.mark();
        Timer.Context context = timer.time();
        context.stop();
        return personService.getAll();
    }

    @PostMapping("/post/{name}")
    public List<Person> postByName(@PathVariable String name) {
        return personService.getByName(name);
    }

    @GetMapping("/get/{name}")
    public List<Person> getByName(@PathVariable String name) {
        return personService.getByName(name);
    }

    @PostMapping("/post")
    public Person postByResponseBody(@RequestBody Map<String, Object> map, @RequestParam String name) {
        System.out.println(name);
        return personService.getByIdAndName((Integer) map.get("id"), (String) map.get("name"));
    }

    @PostMapping("/enumTest")
    public void enumTestPost(@RequestBody Person person) {
        System.out.println(person);
    }

    @GetMapping("/enumTest")
    public void enumTestGet(String person) throws IOException {
        Person p = new ObjectMapper().readValue(person, Person.class);
        System.out.println(p);
    }
}
