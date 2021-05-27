package org.example.servlet;

import org.example.dao.ChannelDao;
import org.example.model.Channel;
import org.example.model.Response;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:显示频道
 * User: starry
 * Date: 2021 -05 -27
 * Time: 7:55
 */

@WebServlet("/channel")
public class ChannelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("applicaation/json");
        Response response = new Response();
        try {
            //查询所有频道列表返回
            List<Channel> list = ChannelDao.query();
            response.setOk(true);
            response.setData(list);
            //ok:true,data[{},{}]
        }catch (Exception e) {
            e.printStackTrace();
            //目前，前端的实现，在后端报错，要返回空的list
            //改造前端为解析ok，reason
            //参考LoginServlet改造
            response.setReason(e.getMessage());
            //ok:false,reason:""
        }
        resp.getWriter().println(Util.serialize(response));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
