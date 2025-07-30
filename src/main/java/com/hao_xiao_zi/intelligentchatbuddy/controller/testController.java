package com.hao_xiao_zi.intelligentchatbuddy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-07-30
 * Time: 19:19
 */
@RestController
public class testController {

    @GetMapping("/test")
    public String test(){
        return "hello world";
    }
}
