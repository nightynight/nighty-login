package com.brokepal.nighty.login.sys.web;

import com.brokepal.nighty.login.core.dto.OperationResult;
import com.brokepal.nighty.login.core.exception.RequestParamException;
import com.brokepal.nighty.login.core.util.RSACryptoUtil;
import com.brokepal.nighty.login.security.service.SecurityService;
import com.brokepal.nighty.login.security.util.SecurityUtil;
import com.brokepal.nighty.login.sys.model.User;
import com.brokepal.nighty.login.sys.service.UserService;
import com.brokepal.nighty.login.sys.storage_object.SendEmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/9.
 */
@Controller
@RequestMapping(value="/static")
public class ResetPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(value="/resetPassword")
    @ResponseBody
    public ResponseEntity Login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin","*");

        String sessionId = req.getParameter("sessionId");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");
        String validateCode = req.getParameter("validateCode");

        if (sessionId == null)
            throw new RequestParamException("sessionId can not be null");
        if (email == null)
            throw new RequestParamException("email can not be null");
        if (password == null)
            throw new RequestParamException("password can not be null");
        if (passwordConfirm == null)
            throw new RequestParamException("passwordConfirm can not be null");
        if (validateCode == null)
            throw new RequestParamException("validateCode can not be null");


        OperationResult result;
        do {
            //验证邮箱
            if (email ==null || "".equals(email)){
                result = OperationResult.buildFailureResult("邮箱不能为空");
                break;
            }

            User user = userService.getUserByEmail(email);
            if (user == null) {
                result = OperationResult.buildFailureResult("邮箱无效");
                break;
            }

            //判断是否发送了验证码
            SendEmailInfo info = userService.getSendEmailInfo(email);
            if (info == null){
                result = OperationResult.buildFailureResult("请发送验证码");
                break;
            }

            //判断验证码是否过期
            Date currentTime = new Date();//获取当前时间
            if(currentTime.getTime() - info.getSendEmailTime().getTime() > 15 * 60 * 1000) {
                result = OperationResult.buildFailureResult("验证码失效，请重新发送");
                break;
            }

            //判断验证码是否正确
            String correctValidateCode = info.getValidateCode();
            if ("".equals(validateCode) || !validateCode.equals(correctValidateCode)){
                result = OperationResult.buildFailureResult("验证码错误");
                break;
            }

            //判断两次密码是否相同
            String str_privateKey = securityService.getPrivateKey(sessionId);
            RSAPrivateKey rsaPrivateKey;
            String srcPassword = null;
            String srcPasswordConfirm = null;
            try {
                rsaPrivateKey = (RSAPrivateKey) RSACryptoUtil.getPrivateKey(str_privateKey);
                srcPassword = RSACryptoUtil.RSADecodeWithPrivateKey(rsaPrivateKey,password);	//解密密码
                srcPasswordConfirm = RSACryptoUtil.RSADecodeWithPrivateKey(rsaPrivateKey,passwordConfirm);	//解密密码
            } catch (Exception e) {
                e.printStackTrace();
                result = OperationResult.buildFailureResult("加密解密异常，请刷新页面再登录");
                break;
            }

            if (!srcPassword.equals(srcPasswordConfirm)) { //两次输入密码是否相同
                result = OperationResult.buildFailureResult("两次密码不一样");
                break;
            }

            //6.验证密码的有效性
            if (srcPassword.length() < 0) {  //TODO 验证密码是否符合规范
                result = OperationResult.buildFailureResult("密码不符合规范");
                break;
            }

            //通过所有验证，重置密码
            String salt = user.getSalt();
            String passwordMD5 = securityService.MD5Encrypt(srcPassword,salt);
            if (userService.updatePasswordByEmail(email,passwordMD5) == 0){
                result = OperationResult.buildFailureResult("重置密码失败");
                break;
            }
            String token = SecurityUtil.generateToken(user.getUsername(),srcPassword); //生成token
            result = OperationResult.buildSuccessResult(token);
        } while (false);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 发送验证码邮件
     * @param req 请求中必须带“email”，且不能为空
     * @param resp
     * @return
     */
    @RequestMapping(value="/sendValidateEmail")
    @ResponseBody
    public ResponseEntity sendValidateEmail(HttpServletRequest req, HttpServletResponse resp) {
        final String email = req.getParameter("email");
        OperationResult result;
        do {
            if (email == null || "".equals(email)){
                result = OperationResult.buildFailureResult("邮箱不能为空");
                break;
            }

            if (userService.getUserByEmail(email) == null) {
                result = OperationResult.buildFailureResult("邮箱不可用");
                break;
            }

            String code = userService.sendValidateCodeEmial(email);
            if (code == null){
                result = OperationResult.buildFailureResult("发送邮件失败");
                break;
            }

            //发送成功
            Date sendEmailTime = new Date();
            userService.setSendEmailInfo(email,new SendEmailInfo(sendEmailTime, code));
            result = OperationResult.buildSuccessResult();
        } while (false);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
