package com.jakob.springbootdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCharAndVarChar {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testCharAndVarchar() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from actor");
        System.out.println(list);
    }
}
