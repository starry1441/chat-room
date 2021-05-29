package org.example.servlet;

import org.example.model.Response;
import org.example.model.User;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:退出登录
 * User: starry
 * Date: 2021 -05 -28
 * Time: 22:03
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User)session.getAttribute("user");
            if (user != null) {
                //用户已登录，删除session中保存的用户信息（注销）
                session.removeAttribute("user");
                //注销成功，返回ok：true
                Response response = new Response();
                response.setOk(true);
                resp.getWriter().println(Util.serialize(response));
                return;
            }
        }
        //用户未登录
        Response response = new Response();
        response.setReason("用户未登录，不允许访问");
        resp.getWriter().println(Util.serialize(response));
    }

}
