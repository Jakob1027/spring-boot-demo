package com.jakob.springbootdemo.entity;

/**
 * @author Jakob
 */
public class Monkey extends Animal {

    private Integer age;

    @Override
    public String getName() {
        return "Monkey";
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
