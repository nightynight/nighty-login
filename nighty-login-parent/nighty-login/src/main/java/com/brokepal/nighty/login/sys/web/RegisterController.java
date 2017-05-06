package com.brokepal.nighty.login.sys.web;

import com.brokepal.nighty.login.core.dto.OperationResult;
import com.brokepal.nighty.login.core.exception.RequestParamException;
import com.brokepal.nighty.login.core.util.CommonUtil;
import com.brokepal.nighty.login.core.util.RSACryptoUtil;
import com.brokepal.nighty.login.security.service.SecurityService;
import com.brokepal.nighty.login.security.util.SecurityUtil;
import com.brokepal.nighty.login.sys.dto.LoginSuccessResult;
import com.brokepal.nighty.login.sys.model.Role;
import com.brokepal.nighty.login.sys.model.User;
import com.brokepal.nighty.login.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
@Controller
@RequestMapping(value="static")
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    /**
     * 用户注册接口
     * @param req 请求中必须带以下字段，否则抛出RequestParamException异常
     *            sessionId
     *            nickname
     *            username
     *            password
     *            passwordConfirm
     *            email
     * @param resp
     * @return
     */
    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity Login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin","*");

        String sessionId = req.getParameter("sessionId");
        String nickname = req.getParameter("nickname");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        if (sessionId == null)
            throw new RequestParamException("sessionId can not be null");
        if (nickname == null)
            throw new RequestParamException("nickname can not be null");
        if (username == null)
            throw new RequestParamException("username can not be null");
        if (password == null)
            throw new RequestParamException("password can not be null");
        if (passwordConfirm == null)
            throw new RequestParamException("passwordConfirm can not be null");
        if (email == null)
            throw new RequestParamException("email can not be null");

        OperationResult result;
        do {
            //验证nickname
            if ("".equals(nickname)){
                result = OperationResult.buildFailureResult("昵称不能为空");
                break;
            }

            //验证username、email
            if ("".equals(username) || email == null || "".equals(email)){
                result = OperationResult.buildFailureResult("用户名和邮箱不能为空");
                break;
            }

            //验证用户名是否可用
            if (userService.getUserByUsername(username) != null) {
                result = OperationResult.buildFailureResult("用户名不可用");
                break;
            }

            //验证邮箱是否可用
            if (userService.getUserByEmail(email) != null) {
                result = OperationResult.buildFailureResult("邮箱不可用");
                break;
            }

            //判断两次密码是否相同，相同的字符串用相同的密钥加密后也是不同的，所以先解密再比较
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
                result = OperationResult.buildFailureResult("两次输入密码不相同");
                break;
            }

            //验证密码的有效性
            if (srcPassword.length() < 0) {  //TODO 验证密码是否符合规范
                result = OperationResult.buildFailureResult("密码不符合规范");
                break;
            }

            //通过所有验证，创建用户
            String salt = CommonUtil.createRandomString(10);    //随机生成一个盐
            String passwordMD5 = securityService.MD5Encrypt(srcPassword,salt);
            User user = new User.Builder()
                    .nickname(nickname).username(username).password(passwordMD5)
                    .salt(salt).email(email).phone(phone).build();
            userService.setDefaultRolesAndResToUser(user);
            if (userService.createUser(user) == 0){ //如果创建失败
                result = OperationResult.buildFailureResult("创建用户失败");
                break;
            }
            //创建成功
            String token = SecurityUtil.generateToken(username,srcPassword); //生成token
            securityService.login(username,sessionId,token,false);
            String roleType = "user";
            result = OperationResult.buildSuccessResult(new LoginSuccessResult(token,nickname,username,roleType,user.getResources()));
        } while (false);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 验证用户名存不存在
     * @param req 请求中必须带“username”，否则抛出RequestParamException异常
     * @param resp
     * @return
     */
    @RequestMapping(value="/isUsernameExist")
    @ResponseBody
    public ResponseEntity getVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws RequestParamException {
        resp.setHeader("Access-Control-Allow-Origin","*");

        String username = req.getParameter("username");
        if (username == null)
            throw new RequestParamException("username can not be null");
        if (userService.getUserByUsername(username) != null)
            return new ResponseEntity(OperationResult.buildSuccessResult("true"), HttpStatus.OK);
        else
            return new ResponseEntity(OperationResult.buildFailureResult("false"), HttpStatus.OK);
    }

}
