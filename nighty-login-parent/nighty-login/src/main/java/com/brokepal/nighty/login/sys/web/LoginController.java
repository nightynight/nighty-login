package com.brokepal.nighty.login.sys.web;

import com.brokepal.nighty.login.core.dto.OperationResult;
import com.brokepal.nighty.login.core.exception.RequestParamException;
import com.brokepal.nighty.login.core.util.RSACryptoUtil;
import com.brokepal.nighty.login.security.constant.SecurityConstant;
import com.brokepal.nighty.login.security.idto.IsLoginResult;
import com.brokepal.nighty.login.security.service.SecurityService;
import com.brokepal.nighty.login.security.storage_object.Session;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by chenchao on 17/3/28.
 */
@Controller
@RequestMapping(value = "static")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    /**
     * 用户注册接口
     * @param req 请求中必须带以下字段，否则抛出RequestParamException异常
     *            sessionId
     *            username
     *            password
     * @param resp
     * @return
     */
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(HttpServletRequest req, HttpServletResponse resp) throws RequestParamException {
        resp.setHeader("Access-Control-Allow-Origin","*");

        String sessionId = req.getParameter("sessionId");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean keepPassword = false;

        if (sessionId == null)
            throw new RequestParamException("sessionId can not be null");
        if (username == null)
            throw new RequestParamException("username can not be null");
        if (password == null)
            throw new RequestParamException("password can not be null");
        if ("true".equals(req.getParameter("keepPassword")))
            keepPassword = true;

        OperationResult result;
        do {
            User user = userService.getUserByUsername(username);
            if (user == null){
                user = userService.getUserByEmail(username);
                if (user == null){
                    result = OperationResult.buildFailureResult("用户名不存在");
                    break;
                }
            }

            //判断账号是否冻结（输错多次）
            if (securityService.isLocked(username)){
                result = OperationResult.buildFailureResult("账号已锁定，" + SecurityConstant.LOCK_TIME + "分钟后再登录");
                break;
            }

            //判断密码是否正确
            String str_privateKey = securityService.getPrivateKey(sessionId);
            RSAPrivateKey rsaPrivateKey = null;
            String srcPassword = null;
            try {
                rsaPrivateKey = (RSAPrivateKey) RSACryptoUtil.getPrivateKey(str_privateKey);    //得到私钥
                srcPassword = RSACryptoUtil.RSADecodeWithPrivateKey(rsaPrivateKey,password);	//解密密码
            } catch (Exception e) {
                e.printStackTrace();
                result = OperationResult.buildFailureResult("加密解密异常，请刷新页面再登录");
                break;
            }

            String salt = user.getSalt();
            //用MD5加密密码，数据库中存的就是用户密码经过MD5加密后的字符串
            String passwordMD5 = securityService.MD5Encrypt(srcPassword,salt);
            if (!user.getPassword().equals(passwordMD5)) { //如果密码错误
                result = OperationResult.buildFailureResult("密码错误");
                securityService.addFailCount(username);
                break;
            }

            //通过所有验证，登录成功
            securityService.clearFailInfo(username);
            String token = SecurityUtil.generateToken(user.getUsername(),srcPassword); //生成token
            securityService.login(username,sessionId,token,keepPassword);
            String roleType = "user";
            for (Role role : user.getRoles()){
                if ("1".equals(role.getType())){ //表示该用户是管理员
                    roleType = "admin";
                    break;
                }
            }
            result = OperationResult.buildSuccessResult(new LoginSuccessResult(token,user.getNickname(),user.getUsername(),roleType,user.getResources()));
        } while (false);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/isLogin")
    @ResponseBody
    public ResponseEntity isLogin(@RequestParam("username") String username,
                                  @RequestParam("sessionId") String sessionId,
                                  @RequestParam("token") String token,
                                  HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");

        OperationResult result;
        do {
            IsLoginResult isLoginResult = securityService.isLogin(sessionId, token);
            if (!isLoginResult.getIsLogin()){
                result = OperationResult.buildFailureResult(isLoginResult.getMessage());
                break;
            }

            User user = userService.getUserByUsername(username);
            //已经登录了
            String roleType = "user";
            for (Role role : user.getRoles()){
                if ("1".equals(role.getType())){
                    roleType = "admin";
                    break;
                }
            }
            result = OperationResult.buildSuccessResult(new LoginSuccessResult(token,user.getNickname(),user.getUsername(),roleType,user.getResources()));
        } while (false);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity logout(@RequestParam("sessionId") String sessionId,
                                 HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");

        securityService.logout(sessionId);
        return new ResponseEntity(OperationResult.buildSuccessResult(), HttpStatus.OK);
    }
}
