package com.jakob.springbootdemo;

import lombok.Data;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

@Data
public class TestLargeTable {

    private static Logger logger = LoggerFactory.getLogger(TestLargeTable.class);

    @Test
    public void largeSelect() throws SQLException, InterruptedException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
        PreparedStatement ps = connection.prepareStatement("select * from actor");
        ps.setFetchSize(Integer.MIN_VALUE);
        ResultSet resultSet = ps.executeQuery();
        for (int i = 0; i < 1000; i++) {
            resultSet.next();
            String first_name = resultSet.getString("first_name");
        }
        resultSet.close();
//        new Thread(() -> {
//            while (true) {
//                try {
//                    if (!resultSet.next()) break;
//                    String firstName = resultSet.getString("first_name");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    logger.error("ResultSet 遍历异常", e);
//                }
//            }
//        }).start();
//        Thread.sleep(1000);
//        resultSet.close();
        ps.close();
        connection.close();
    }
}
