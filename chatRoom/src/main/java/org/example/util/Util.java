package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.example.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description: 通用工具类
 * User: starry
 * Date: 2021 -05 -21
 * Time: 20:42
 */
public class Util {

    // ObjectMapper为该对象做json的序列化
    private static final ObjectMapper mapper = new ObjectMapper();

    // 获取数据库的连接用
    private static final MysqlDataSource dataSource = new MysqlDataSource();

    //设置初始化的属性值
    static {
        // 设置json序列化/反序列化的日期格式
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(dataFormat);  //日期格式化
        // 设置连接数据库的基本属性
        dataSource.setURL("jdbc:mysql://82.156.229.239:3306/java_chatroom");
        dataSource.setUser("root");
        dataSource.setPassword("11111111");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("UTF-8");   //解决插入中文数据乱码问题
    }

    /**
     * json序列化：java对象转化为json字符串
     */
    public static String serialize(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            //把编译时异常转换为运行时异常，这样外面调用此方法不需要管编译时异常
            throw new AppException("json序列化失败"+o, e);
        }
    }

    /**
     * 反序列化json：把json字符串转化为java对象
     */
    public static <T> T deserialize(String s, Class<T> c) {
        try {
            return mapper.readValue(s, c);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            // 如果出现这个异常，一般都是json字符串中的键，在calss中没有找到对应的属性
            throw new AppException("json反序列化失败", e);
        }
    }

    /**
     * 反序列化json重载方法：把json输入流转化为java对象
     */
    public static <T> T deserialize(InputStream is, Class<T> c) {
        try {
            return mapper.readValue(is, c);
        } catch (IOException e) {
//            e.printStackTrace();
            // 如果出现这个异常，一般都是json字符串中的键，在calss中没有找到对应的属性
            throw new AppException("json反序列化失败", e);
        }
    }

    /**
     * 获取数据库连接
     */
    //导入java.sql.Connection包（很可能自己导错）
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new AppException("获取数据库连接失败", e);
        }
    }

    /**
     * 释放jdbc资源
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new AppException("释放数据库资源出错", e);
        }
    }

    /**
     * 释放jdbc连接重载方法
     */
    public static void close(Connection connection, Statement statement) {
        close(connection,statement,null);
    }

    /**
     * 测试工具类方法
     */
    public static void main(String[] args) {
        //测试json序列化
        Map<String, Object> map = new HashMap<>();
        map.put("ok", true);
        map.put("lala", new Date());
        System.out.println(serialize(map));

        //测试数据库连接
        System.out.println(getConnection());
    }

}
