package org.example.dao;

import org.example.exception.AppException;
import org.example.model.Message;
import org.example.model.User;
import org.example.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:和消息有关的操作数据库方法
 * User: starry
 * Date: 2021 -05 -22
 * Time: 19:37
 */
public class MessageDao {

    /**
     * 往数据库插入消息信息
     */
    public static int insert(Message msg) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Util.getConnection();
            String sql = "insert into message values(null,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,msg.getUserId());
            statement.setInt(2,msg.getChannelId());
            statement.setString(3,msg.getContent());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            int result = statement.executeUpdate();
            return result;
        }catch (Exception e) {
            throw new AppException("保存消息出错", e);
        }finally {
            Util.close(connection,statement);
        }
    }

    public static List<Message> queryByLastLogout(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        //定义返回数据
        List<Message> list = new ArrayList<>();
        try {
            //1. 获取数据库连接Connection
            connection = Util.getConnection();
            //2. 通过Connection+sql 创建操作命令对象Statement
                String sql = "select m.*,u.nickName from message m join user u on u.userId = m.userId where m.sendTime > (select lastLogout from user where userId = ?);";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            //3. 执行sql：执行前替换占位符
            resultSet = statement.executeQuery();
            //如果是查询操作，处理结果集
            while (resultSet.next()) {//移动到下一行，有数据返回true
                Message message = new Message();
                //获取结果集字段，设置对象属性
                message.setUserId(userId);
                message.setNickName(resultSet.getString("nickName"));
                message.setContent(resultSet.getString("content"));
                message.setChannelId(resultSet.getInt("channelId"));
                list.add(message);
            }
            return list;
        }catch (Exception e) {
            throw new AppException("查询用户[" + userId +"]的历史消息出错", e);
        }finally {
            //释放资源
            Util.close(connection,statement,resultSet);
        }
    }
}
