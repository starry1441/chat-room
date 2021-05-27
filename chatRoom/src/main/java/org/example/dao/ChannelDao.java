package org.example.dao;

import org.example.exception.AppException;
import org.example.model.Channel;
import org.example.model.User;
import org.example.util.Util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: starry
 * Date: 2021 -05 -22
 * Time: 19:37
 */
public class ChannelDao {
    /**
     * 查询频道列表
     */
    public static List<Channel> query() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        //定义返回数据
        List<Channel> list = new ArrayList<>();
        try {
            //1. 获取数据库连接Connection
            connection = Util.getConnection();
            //2. 通过Connection+sql 创建操作命令对象Statement
            String sql = "select channelId,channelName from channel";
            statement = connection.prepareStatement(sql);
            //3. 执行sql：执行前替换占位符
            resultSet = statement.executeQuery();
            //如果是查询操作，处理结果集
            while (resultSet.next()) {//移动到下一行，有数据返回true
                Channel channel = new Channel();
                //设置属性
                channel.setChannelId(resultSet.getInt("channelId"));
                channel.setChannelName(resultSet.getString("channelName"));
                list.add(channel);
            }
            return list;
        }catch (Exception e) {
            throw new AppException("查询频道列表出错", e);
        }finally {
            //释放资源
            Util.close(connection,statement,resultSet);
        }
    }
}
