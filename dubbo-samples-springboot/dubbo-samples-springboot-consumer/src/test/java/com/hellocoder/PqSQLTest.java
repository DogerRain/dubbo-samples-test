package com.hellocoder;

import org.junit.Test;

import java.sql.*;

/**
 * @Author huangyongwen
 * @Date 2023/11/13 9:39
 * @Description
 **/
public class PqSQLTest {

    // JDBC数据库连接URL
    private static final String JDBC_URL = "jdbc:postgresql://ep-plain-wave-75184973-pooler.ap-southeast-1.postgres.vercel-storage.com:5432/verceldb";
    private static final String USER = "default";
    private static final String PASSWORD = "ir4vw0FWXRyt";

    @Test
    public void testConnJDBC() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 注册JDBC驱动
            Class<?> name = Class.forName("org.postgresql.Driver");

            // 2. 获取数据库连接
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            // 3. 使用PreparedStatement执行SQL语句
            String sql = "SELECT * FROM public.newtable WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);

            // 4. 设置占位符参数
            preparedStatement.setInt(1, 1);

            // 5. 执行查询操作
            resultSet = preparedStatement.executeQuery();

            // 6. 处理查询结果
            while (resultSet.next()) {
                // 获取结果集中的数据
                String id = resultSet.getString("id");
                String columnName = resultSet.getString("app_name");
                // 在这里处理数据，例如打印或进行其他操作
                System.out.println("ID: " + id + ", Column Name: " + columnName);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭资源，确保在使用完毕后释放资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
