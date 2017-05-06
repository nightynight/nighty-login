package com.brokepal.nighty.login.sys.web;

import com.brokepal.nighty.login.core.dto.OperationResult;
import com.brokepal.nighty.login.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/1/24.
 */
@Controller
public class TestController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "{\"data\":\"hello\"}";
    }

    @RequestMapping(value = "static/test")
    @ResponseBody
    public String stativHello() {
        return "{\"data\":\"test\"}";
    }

    @RequestMapping(value = "static/dto")
    @ResponseBody
    public ResponseEntity dto() {
        return new ResponseEntity(OperationResult.buildFailureResult("error"), HttpStatus.OK);
    }

    @RequestMapping(value = "static/user")
    @ResponseBody
    public ResponseEntity user() {
        return new ResponseEntity(OperationResult.buildSuccessResult(service.getUserByUsername("aa")), HttpStatus.OK);
    }

}
