package com.leyou.item.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spu")
public class SpuController {


    @RequestMapping("page")
    public List<Void>  findAllByCondition(
            @RequestParam(name="key",required = false) String key,
            @RequestParam(name="saleable" ,required = false) String saleable,
            @RequestParam(name="page" ,defaultValue = "1") Integer page,
            @RequestParam(name="rows",defaultValue = "5") Integer rows
            ){



        return null;
    }

}
