package com.roadtomastery.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadtomastery.project.entity.request.LoginRequestBody;
import com.roadtomastery.project.entity.response.LoginResponseBody;
import com.roadtomastery.project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginRequestBody requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstname = loginService.verifyLogin(requestBody.getUserId(), requestBody.getPassword());
        if (!firstname.isEmpty()) {
            //如果不空，代表用戶帳號是存在啦，password也是對的。
            HttpSession session = request.getSession();
            session.setAttribute("user_id", requestBody.getUserId());
            session.setMaxInactiveInterval(600);

            LoginResponseBody loginResponseBody = new LoginResponseBody(requestBody.getUserId(), firstname);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(loginResponseBody));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
