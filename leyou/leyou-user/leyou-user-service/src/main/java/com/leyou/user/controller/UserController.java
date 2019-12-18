package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable(name="data") String data,
                                         @PathVariable(name="type") Integer type){
        Boolean u=userService.checkOne(data,type);
        if(u==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(u);
    }

    /**
     * 生成验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> code(@RequestParam(name="phone") String phone){
        userService.sendVerifyToPhone(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 注册
     * @param phone
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(User user,@RequestParam(name="code") String code){
        Boolean u=userService.register(user,code);

        if (u){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("query")
    public ResponseEntity<User> queryByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password){
        User u=userService.queryByUsernameAndPassword(username,password);

        if (u!=null) return ResponseEntity.ok(u);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



}
