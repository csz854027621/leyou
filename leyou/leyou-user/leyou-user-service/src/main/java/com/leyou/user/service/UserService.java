package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String VERIFYNAME="verify:user:phone";


    public Boolean checkOne(String data, Integer type) {

        User user=new User();
        if (type==1){
            user.setUsername(data);
        }else if(type==2){
            user.setPhone(data);
        }
        int i = userMapper.selectCount(user);

        return i==0?true:false;
    }

    public void sendVerifyToPhone(String phone) {
        //随机生成6位验证码
        String s = NumberUtils.generateCode(6);
        //存储到redis中，方便之后验证
        redisTemplate.opsForValue().set(VERIFYNAME,s,5, TimeUnit.MINUTES);
        Map<String,String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("verify",s);
        //通过rabbitmq发送短信验证码。路由器名字设置在application.yml中
        amqpTemplate.convertAndSend("user.send.verify",map);

    }

    public Boolean register(User user, String code) {
        //获取redis里的 验证码
        String s = redisTemplate.opsForValue().get(VERIFYNAME);
        if(!StringUtils.isNotBlank(s)||!StringUtils.equals(s,code)) return false;
        //获取盐salt
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //加密
        String passwordMd5 = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(passwordMd5);
        user.setCreated(new Date());
        //插入
        int i = userMapper.insertSelective(user);

        if(i!=0) return true;
        else return false;

    }

    public User queryByUsernameAndPassword(String username, String password) {

        User user=new User();
        user.setUsername(username);
        //1.通过username查询User
        user = userMapper.selectOne(user);
        if(user==null) return null;
        //2.获取salt 用md5加密password
        String s = CodecUtils.md5Hex(password, user.getSalt());
        //3.比较原数据和password
        if(StringUtils.equals(s,user.getPassword())) return user;
       //4.不等的话，返回false
        return null;
    }
}
