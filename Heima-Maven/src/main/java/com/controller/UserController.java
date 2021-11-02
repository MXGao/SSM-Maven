package com.controller;

import com.domain.Info;
import com.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.UserService;
import com.service.impl.UserServiceImpl;
import com.utils.CommonsUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    private Info info;

    @ResponseBody
    @RequestMapping("/checkUsername")
    public Info checkUsername(String username) throws JsonProcessingException {
        //System.out.println(username);
        boolean flag=true;
        String errorMsg=null;
        info = new Info();
        //ModelAndView modelAndView = new ModelAndView();

        User user = userService.checkUsername(username);
        System.out.println(user);
        if (user!=null){
            // 说明用户名已存在
            flag=false;
            errorMsg="用户名已存在，请更换";
        }/*else {
            // 用户名不存在，可以使用
        }*/
        info.setFlag(flag);
        info.setErrorMsg(errorMsg);
        System.out.println(info);


        return info;
    }

    /**
     * 注册
     * @param request
     * @return
     */
    @RequestMapping("/register")
    public String register(HttpServletRequest request){
        // 获取请求参数：
        Map<String,String[]> map = request.getParameterMap();
        User user = new User();
        try {
            // 作为类型转换器(string--->date)
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object o) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = format.parse(o.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 随机生成uid
        String uid = CommonsUtils.getUUID();
        user.setUid(uid);

        boolean isRegister = userService.register(user);
        System.out.println(user);
        if (isRegister){
            // 表示注册成功
            return "redirect:/registerSuccess.jsp";
        }else {
            // 注册失败
            return "redirect:/registerFail.jsp";
        }

    }

    /**
     * 激活
     * @param request
     * @return
     */
    @RequestMapping("/activeMail")
    public String activeMail(HttpServletRequest request){
        // 获取激活码
        String code = request.getParameter("code");
        System.out.println(code);
        if (code !=null){
            // 表示激活码有效，调用service中的方法，激活（看是否有对应的激活码）
            boolean flag = userService.active(code);
            System.out.println(flag);
            if (flag){
                // 激活成功，跳转到登陆页面
                return "redirect:/login.jsp";
            }
        }
        // 激活失败
        return "redirect:/registerFail.jsp";

    }
    @RequestMapping(value = "/login")
    public ModelAndView login(String username, String password, HttpSession session,ModelAndView modelAndView){
        User user = userService.findByUsernameAndPassword(username,password);
        System.out.println(user);
        if (user == null){
            // 表示用户名或密码错误
            modelAndView.addObject("errorMsg","用户名或密码错误!");
            //modelAndView.addObject("user",user);
            modelAndView.setViewName("login");
        }else {
            // 登录成功，跳转到index.jsp，将user存到session中
            session.setAttribute("user",user);
            modelAndView.setViewName("redirect:/index.jsp");
        }
        return modelAndView;
    }

    /**
     * 退出
     * @return
     */
    @RequestMapping("/exit")
    public String exit(HttpSession session){
        // session中user被删除时，就表示退出了
        session.invalidate();// 销毁session

        return "redirect:/index.jsp";
    }


    @RequestMapping(value = "/hello"/*,method = RequestMethod.POST*/)
    public ModelAndView hello(){
        info = new Info();
        info.setFlag(true);
        info.setErrorMsg("哈哈哈哈哈");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("info",info);
        modelAndView.setViewName("hello");
        return modelAndView;
    }

}
