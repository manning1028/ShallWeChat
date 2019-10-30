package com.manning.web;


import com.manning.entity.TbUser;
import com.manning.service.TbUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/22 16:26
 */
@Controller
public class LoginController{
    @Autowired
  private   TbUserService tbUserService;

    @RequestMapping("/")
        public String index(){
        return "login/login";
 }

    /**
     * 登录
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model,HttpServletRequest request){
        String account=request.getParameter("account");
        String password=request.getParameter("password");
        System.out.println(account+"---"+password);

        return "login/login";
    }

@RequestMapping("/index")
    public String index(Model model, HttpServletRequest request){
        return "index";
    }


    /**
     * 注册
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Map<String,Object> register(Model model,HttpServletRequest request){
        Map<String,Object> map=new HashMap<String,Object>();
        String nickname=request.getParameter("nickname");
        String email=request.getParameter("email");
        String telphone=request.getParameter("telphone");
        String password=request.getParameter("password");
    TbUser tbUser=new TbUser();
    tbUser.setNickname(nickname);
    tbUser.setTelphone(telphone);
    tbUser.setEmail(email);
    tbUser.setPassword(password);
    tbUserService.save(tbUser);
    map.put("code","00");
    map.put("msg","注册成功");
    System.out.println("=======");
    return map;
    }


    @RequestMapping("/findUserByTelPhone")
    @ResponseBody
 public Map<String,Object> findUserByTelphone(Model model,HttpServletRequest request){
   Map<String,Object> map=new HashMap<String, Object>();
   map.put("code","00");
   String telphone=request.getParameter("telphone");
   if(StringUtils.isNotBlank(telphone)){
       TbUser tbUser=tbUserService.findUserByTelphone(telphone);
       if(tbUser!=null){
           map.put("code","01");
           map.put("msg","手机号已注册");
       }
   }
   return map;
 }


}
