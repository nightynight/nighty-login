package com.brokepal.nighty.login.sys.web.common;

import com.brokepal.nighty.login.core.dto.OperationResult;
import com.brokepal.nighty.login.core.util.RSACryptoUtil;
import com.brokepal.nighty.login.security.service.SecurityService;
import com.brokepal.nighty.login.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by chenchao on 17/3/28.
 */
@Controller
@RequestMapping(value = "static")
public class RASController {

    @Autowired
    private UserService service;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(value="/getPublicKey")
    @ResponseBody
    public ResponseEntity getPublicKey(@RequestParam("sessionId") String sessionId, HttpServletResponse response)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin","*");

        RSACryptoUtil.KeyPairOfString keyPairOfString = RSACryptoUtil.makeBothKeyOfString();
        String str_publicKey = keyPairOfString.getPublicKey();
        String str_privateKey = keyPairOfString.getPrivateKey();
        //将私钥加入缓存，处理登录请求时取出;登录前sessionId中存的是私钥，登录后存的是token
        securityService.putPrivateKey(sessionId, str_privateKey);

        OperationResult result = OperationResult.buildSuccessResult(str_publicKey);
        return new ResponseEntity(result, HttpStatus.OK);

    }

}
