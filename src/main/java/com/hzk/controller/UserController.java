package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzk.entity.User;
import com.hzk.service.UserService;
import com.hzk.util.Result;
import com.hzk.util.SMSUtils;
import com.hzk.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMeg(@RequestBody User user, HttpSession session){
        //1. 获取手机号
        String phone = user.getPhone();
        //2. 判断手机号不为空
        if (!StringUtils.isEmpty(phone)){
            //3 .判断手机号不为空，生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("接收到的验证码 {}",code);
            //4 .调用阿里云的短信API接口，完成发送短信
            //第一个参数就是我们申请的那个签名，第二个参数就是短信模板code ，第三个参数就是手机号
            //第四个参数就是动态的验证码code
//            SMSUtils.sendMessage("");
        //5. 把生成的验证码放到session存储,一个key  、一个value
            session.setAttribute(phone,code);
            return Result.success("手机短信验证码发送成功");
        }
        return Result.error("手机短信验证码发送失败");
    }

    /**
     * 登录的功能（小程序）
     * 因为只传过来两个参数，所以可以使用Map来接收
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map , HttpSession session){
        log.info("Map中东西有 {}" ,map);
        //从map中获取手机号
        String phone = map.get("phone").toString();
        //获取短信验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        Object code1 = session.getAttribute(phone);
        //比对页面提交的验证码和session中的验证码是否一致
        if (code.equals(code1) && code1 != null){
            //如果登录成功的话，把用户的cookie保存到session中

            //判断当前手机号在用户表中是否存在，如果不存在，则保存到用户表中
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(lambdaQueryWrapper);
            //判断是否存在
            if (user == null){
                 user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
           return Result.success(user);

        }
//
        return Result.error("验证码不正确，请重新输入");
    }

    /**
     *移动端的退出登录
     */
    @PostMapping("loginout")
    public Result<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return Result.success("退出登录成功");

    }
}
