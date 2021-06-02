package org.example.servlet;

import org.example.dao.UserDao;
import org.example.exception.AppException;
import org.example.model.User;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:注册
 * User: starry
 * Date: 2021 -05 -31
 * Time: 16:09
 */

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        User user = new User();
        try {
            //1. 解析请求数据：根据接口文档，需要使用反序列化操作
            User input = Util.deserialize(req.getInputStream(),User.class);
            //2. 业务处理：数据库验证账号密码，如果验证通过，创建session，保存用户信息
            if (input.getName() == null || input.getName().equals("")) {
                throw new AppException("用户名为空");
            }
            if (input.getPassword() == null || input.getPassword().equals("")) {
                throw new AppException("密码为空");
            }
            if (input.getNickName() == null || input.getNickName().equals("")) {
                throw new AppException("昵称为空");
            }
            int result = UserDao.insertUser(input);
            user = input;
            //构造操作成功的正常返回数据：ok-true，业务字段
            user.setOk(true);
        }catch (Exception e) {
            e.printStackTrace();
            //构造操作失败的错误信息：ok-false，reason：错误信息
            user.setOk(false);
            //自定义异常，自己抛，为中文信息，可以给用户看
            if (e instanceof AppException) {    //e捕获到的异常是不是自定义异常
                user.setReason(e.getMessage());
            }else { //非自定义异常，英文信息，给前端看“未知错误”
                user.setReason("未知的错误，请联系管理员");
            }
        }
        //3. 返回响应数据：从响应对象获取输出流，打印输出到响应体body中
        resp.getWriter().println(Util.serialize(user));
    }
}
