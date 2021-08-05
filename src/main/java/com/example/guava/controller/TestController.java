package com.example.guava.controller;


import com.example.guava.annotation.LimitAop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/get")
    @LimitAop(name = "get",token = 1)
    public String get(){
        return "my is get";
    }

    @GetMapping("/add")
    @LimitAop(name = "add",token = 10)
    public String add(){
        return "my is add";
    }

    @GetMapping("/my")
    @LimitAop(name = "add",token = 10)
    public String my(){
        return "my is add";
    }

}
