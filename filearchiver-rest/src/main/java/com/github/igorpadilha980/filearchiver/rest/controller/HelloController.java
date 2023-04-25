package com.github.igorpadilha980.filearchiver.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class HelloController {

    @GetMapping
    public String hello() {
        return "hello, how you're doing?";
    }

}
