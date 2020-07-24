package com.jakob.springbootdemo.controller;

import com.jakob.springbootdemo.entity.Animal;
import com.jakob.springbootdemo.entity.Template;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jakob
 */
@RestController
public class Controller {

    @PostMapping("getName")
    public void getName(@RequestBody Animal animal) {
        System.out.println(animal.getName());
    }

    @PostMapping("post")
    public void post(@RequestBody Template template) {
        Object o = template.getO();
        Object other = 3.4;
        System.out.println(o.equals(3.4));
//        Integer i = 1;
//        System.out.println(i.equals(1));
    }

}
