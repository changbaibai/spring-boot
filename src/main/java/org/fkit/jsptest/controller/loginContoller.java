package org.fkit.jsptest.controller;

import org.fkit.jsptest.util.VerifyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;


@Controller
public class loginContoller {
    @RequestMapping("/valicode")
    public void valicode(HttpServletResponse response, HttpSession session) throws Exception{
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("imageCode",objs[0]);

        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        ModelAndView modelAndView = new ModelAndView();
        //获取之前放入session中的验证码，也就是生成的
        String imageCode = (String) httpServletRequest.getSession().getAttribute("imageCode");
        //获取你前台页面输入的验证码
        String parameter = httpServletRequest.getParameter("vrifyCode");
        System.out.println("Session  vrifyCode "+imageCode+" form vrifyCode "+parameter);

        if (!imageCode.equals(parameter)) {
            modelAndView.addObject("info", "错误的验证码");
            modelAndView.setViewName("index");
        } else {
            modelAndView.addObject("info", "登录成功");
            modelAndView.setViewName("succeed");

        }
        return modelAndView;
    }


}
