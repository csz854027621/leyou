package com.leyou.user.api;


import com.leyou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    /**
     * 根据用户名密码查询对象
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public User queryByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password);

}
