package org.example.dao;

import org.example.exception.AppException;
import org.example.model.User;
import org.example.util.Util;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * Description:用户相关数据库操作
 * User: starry
 * Date: 2021 -05 -22
 * Time: 19:37
 */
public class UserDao {

    /**
     * 根据姓名查询用户所有信息
     */
    public static User queryByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        //定义返回数据
        User user = null;
        try {
            //1. 获取数据库连接Connection
            connection = Util.getConnection();
            //2. 通过Connection+sql 创建操作命令对象Statement
            String sql = "select userId,name,password,nickName,iconPath,signature,lastLogout from user where name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            //3. 执行sql：执行前替换占位符
            resultSet = statement.executeQuery();
            //如果是查询操作，处理结果集
            while (resultSet.next()) {//移动到下一行，有数据返回true
                user = new User();
                //设置结果集字段到用户对象的属性中
                user.setUserId(resultSet.getInt("userid"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setNickName(resultSet.getString("nickName"));
                user.setIconPath(resultSet.getString("iconPath"));
                user.setSignature(resultSet.getString("signature"));
                java.sql.Timestamp lastLogout = resultSet.getTimestamp("lastLogout");
                user.setLastLogout(new Date(lastLogout.getTime()));
//                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//                user.setLastLogout(new Timestamp(System.currentTimeMillis()));
            }
            return user;
        }catch (Exception e) {
            throw new AppException("查询用户账号出错", e);
        }finally {
            //释放资源
            Util.close(connection,statement,resultSet);
        }
    }

    /**
     * 修改用户最后登录时间
     */
    public static int updateLastLogout(Integer userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Util.getConnection();
            String sql = "update user set lastLogout=? where userId=?";
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            statement.setInt(2,userId);
            int result = statement.executeUpdate();
            return result;
        }catch (Exception e) {
            throw new AppException("修改用户上次登录时间出错", e);
        }finally {
            Util.close(connection,statement);
        }
    }
}
