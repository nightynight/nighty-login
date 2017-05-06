/**
 * Created by Administrator on 2017/5/5.
 */

import React from 'react';
import './Login.css';

let PAGE_TYPE = ["login", "register", "reset-password"];
let STORAGE_PRE = "nighty";

class Login extends React.Component {
    constructor(props){
        super(props);
        var {serverBaseUrl, messageMethod} = this.props;
        var storage = window.localStorage;
        var sessionId = CommonUtil.generateOnlyString();
        storage.setItem(STORAGE_PRE + "sessionId", sessionId);
        var publicKey = "";


        $.ajax({
            type: 'GET',
            async: false,
            url: serverBaseUrl + "/static/getPublicKey?sessionId=" + sessionId,
            error: function () {
                if (!!messageMethod)
                    messageMethod("请求失败");
                else
                    alert("请求失败");
            },
            success: function (result) {
                if (result.type == "success") {
                    publicKey = result.message;
                }
            }
        });

        var type = location.hash.substring(1);
        this.state = {
            type: type == "" ? PAGE_TYPE[0] : type,
            publicKey,
        }
    }

    toLoginPage = () => {
        this.setState({
            type: PAGE_TYPE[0]
        });
    };
    toRegisterPage = () => {
        this.setState({
            type: PAGE_TYPE[1]
        });
    };
    toResetPasswordPage = () => {
        this.setState({
            type: PAGE_TYPE[2]
        });
    };
    
    /**************************************** 登录 *********************************************/

    static isLogin(serverBaseUrl, messageMethod){
        var data = {type: "failure"};
        var storage = window.localStorage;
        var username = storage.getItem(STORAGE_PRE + 'username');
        var sessionId = storage.getItem(STORAGE_PRE + 'sessionId');
        var token = storage.getItem(STORAGE_PRE + 'token');
        if (!!username && !!sessionId && !!token) {
            $.ajax({
                type: 'GET',
                async: false,
                url: serverBaseUrl + "/static/isLogin?username=" + username + "&sessionId=" + sessionId + "&token=" + token,
                error: function () {
                    if (!!messageMethod)
                        messageMethod("请求失败");
                    else
                        alert("请求失败");
                },
                success: function (result) {
                    data = result;
                }
            });
        }
        return data;
    }

    static logout(serverBaseUrl, loginPage, messageMethod) {
        var storage = window.localStorage;
        var sessionId = storage.getItem(STORAGE_PRE + 'sessionId');
        storage.removeItem(STORAGE_PRE + 'sessionId');
        storage.removeItem(STORAGE_PRE + 'token');
        storage.removeItem(STORAGE_PRE + 'keepPassword');
        if (!!sessionId) {
            $.ajax({
                type: 'POST',
                async: false,
                url: serverBaseUrl + "/static/logout",
                data: {
                    sessionId: sessionId,
                },
                error: function () {
                    if (!!messageMethod)
                        messageMethod("请求失败");
                    else
                        alert("请求失败");
                },
            });
        }
        window.location.href = loginPage;
    }

    static needLogin(serverBaseUrl, loginPage, messageMethod){
        var isLogin = Login.isLogin(serverBaseUrl, messageMethod).type == "success" ? true : false;
        if (!isLogin) {
            window.location.href = loginPage;
        }
    }

    loginSubmit = (e) => {
        e.preventDefault();
        $("#btn_login").attr("disabled","disabled");
        
        let {serverBaseUrl, userHomePageUrl, adminHomePageUrl, messageMethod} = this.props;
        var publicKey = this.state.publicKey;
        var storage = window.localStorage;
        var sessionId = storage.getItem(STORAGE_PRE + "sessionId");
        
        var username = $("#username").val();
        var password = $("#password").val();
        var encrypt = $.jCryption.crypt;  //使用该对象来实现加密
        encrypt.setPublicKey(publicKey);//设置密钥
        var encryptedPasswd = encrypt.encrypt(password);//加密密码
        $.ajax({
            type: 'POST',
            url: serverBaseUrl + "/static/login",
            data: {
                sessionId: sessionId,
                username: username,
                password: encryptedPasswd,
                keepPassword: $("#keepPassword").is(':checked'),
            },
            error: function () {
                $("#btn_login").attr("disabled",false);
                if (!!messageMethod)
                    messageMethod("请求失败");
                else
                    alert("请求失败");
            },
            success: function (result) {
                if (result.type == "success") {
                    var token = result.data.token;
                    storage.setItem(STORAGE_PRE + 'nickname',result.data.nickname);
                    storage.setItem(STORAGE_PRE + 'username',result.data.username);
                    storage.setItem(STORAGE_PRE + 'sessionId',sessionId);
                    storage.setItem(STORAGE_PRE + 'token',token);
                    if ($("#keepPassword").is(':checked')){
                        storage.setItem(STORAGE_PRE + 'keepPassword',true);
                    } else {
                        storage.removeItem(STORAGE_PRE + 'keepPassword');
                    }
                    var roleType = result.data.roleType;
                    if (adminHomePageUrl) {
                        if (roleType == "admin"){
                            window.location.href = adminHomePageUrl;
                            return;
                        }
                    }
                    window.location.href = userHomePageUrl;
                } else {
                    storage.removeItem(STORAGE_PRE + 'sessionId');
                    storage.removeItem(STORAGE_PRE + 'token');
                    storage.removeItem(STORAGE_PRE + 'keepPassword');
                    $("#btn_login").attr("disabled",false);
                    $("#loginFailInfo").html(result.message);
                }
            }
        });
    };


    /**************************************** 注册 *********************************************/
    
    usernameBlur = () => {
        let {serverBaseUrl} = this.props;
        $.ajax({
            type: 'POST',
            url: serverBaseUrl + "/static/isUsernameExist",
            data: {"username" : $("#username").val()},
            success: function (result) {
                if (result.type == "failure") {
                    //提示通过验证
                    $("#usernamePass").removeClass("gray");
                    $("#usernamePass").addClass("green");
                } else {
                    $("#usernamePass").removeClass("green");
                    $("#usernamePass").addClass("gray");
                }
            }
        });
    };
    
    passwordBlur = () => { //验证密码的有效性
        var checkNum = /.+/; //除换行符之外的任何字符串
        if (checkNum.test($("#password").val())) {
            $("#passwordPass").removeClass("gray");
            $("#passwordPass").addClass("green");
        } else {
            $("#passwordPass").removeClass("green");
            $("#passwordPass").addClass("gray");
        }
    };
    
    passwordConfirmBlur = () => {
        if ($("#passwordConfirm").val() == $("#password").val()) {
            $("#passwordConfirmPass").removeClass("gray");
            $("#passwordConfirmPass").addClass("green");
        } else {
            $("#passwordConfirmPass").removeClass("green");
            $("#passwordConfirmPass").addClass("gray");
        }
    };
    
    registerSubmit = (e) => {
        e.preventDefault();
        
        $("#btn_register").attr("disabled","disabled");

        let {serverBaseUrl, userHomePageUrl, messageMethod} = this.props;
        let publicKey = this.state.publicKey;
        var storage = window.localStorage;
        var sessionId = storage.getItem(STORAGE_PRE + "sessionId");

        var nickname = $("#nickname").val();
        var username = $("#username").val();
        var password = $("#password").val();
        var passwordConfirm = $("#passwordConfirm").val();
        var email = $("#email").val();

        var encrypt = $.jCryption.crypt;
        encrypt.setPublicKey(publicKey);
        var encryptedPasswd = encrypt.encrypt(password);
        var encryptedPasswdConfirm = encrypt.encrypt(passwordConfirm);
        $.ajax({
            type: 'POST',
            url: serverBaseUrl + "/static/register",
            data: {
                sessionId: sessionId,
                nickname: nickname,
                username: username,
                password: encryptedPasswd,
                passwordConfirm: encryptedPasswdConfirm,
                email: email,
            },
            error: function () {
                $("#btn_register").attr("disabled",false);
                if (!!messageMethod)
                    messageMethod("请求失败");
                else
                    alert("请求失败");
            },
            success: function (result) {
                if (result.type == "success") {
                    var token = result.data.token;
                    var nickname = result.data.nickname;
                    storage.setItem(STORAGE_PRE + 'nickname',nickname);
                    storage.setItem(STORAGE_PRE + 'username',username);
                    storage.setItem(STORAGE_PRE + 'sessionId',sessionId);
                    storage.setItem(STORAGE_PRE + 'token',token);
                    window.location.href = userHomePageUrl;
                } else {
                    $("#btn_register").attr("disabled",false);
                    $("#registerFailInfo").html(result.message);
                }
            }
        });
    };


    /**************************************** 重置密码 *********************************************/

    //密码和确认密码验证使用register的验证

    sendValidateCode = () => {
        let {serverBaseUrl, messageMethod} = this.props;
        $("#btn_send_validate_code").attr("disabled", true);
        $.ajax({
            type: 'POST',
            url: serverBaseUrl + "/static/sendValidateEmail",
            data: {"email": $("#email").val()},
            dataType: 'json',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            error: function () {
                $("#btn_send_validate_code").attr("disabled",false);
                if (!!messageMethod)
                    messageMethod("请求失败");
                else
                    alert("请求失败");
            },
            success: function (result) {
                if (result.type == "success") {
                    $("#resetPasswordFailInfo").text("");
                    $(".countdown").show();
                    $('.countdown').countDown({
                        startNumber: 30,
                        callBack: function () {
                            $("#btn_send_validate_code").attr("disabled", false);
                            $(".countdown").hide();
                        }
                    });
                } else {
                    $("#resetPasswordFailInfo").text(result.message);
                    $("#btn_send_validate_code").attr("disabled", false);
                }
            }
        });
    };

    resetPasswordSubmit = (e) => {
        e.preventDefault();
        $("#btn_reset").attr("disabled", "disabled");

        let {serverBaseUrl, messageMethod} = this.props;
        let publicKey = this.state.publicKey;
        var storage = window.localStorage;
        var sessionId = storage.getItem(STORAGE_PRE + "sessionId");

        var password = $("#password").val();
        var passwordConfirm = $("#passwordConfirm").val();
        var email = $("#email").val();
        var validateCode = $("#validateCode").val();

        var encrypt = $.jCryption.crypt;
        encrypt.setPublicKey(publicKey);
        var encryptedPasswd = encrypt.encrypt(password);
        var encryptedPasswdConfirm = encrypt.encrypt(passwordConfirm);

        let that = this;
        $.ajax({
            type: 'POST',
            url: serverBaseUrl + "/static/resetPassword",
            data: {
                sessionId: sessionId,
                password: encryptedPasswd,
                passwordConfirm: encryptedPasswdConfirm,
                email: email,
                validateCode: validateCode,
            },
            error: function () {
                $("#btn_reset").attr("disabled",false);
                if (!!messageMethod)
                    messageMethod("请求失败");
                else
                    alert("请求失败");
            },
            success: function (result) {
                if (result.type == "success") {
                    if (!!messageMethod)
                        messageMethod("修改密码成功");
                    else
                        alert("修改密码成功");

                    setTimeout(function () {
                        that.setState({
                            type: PAGE_TYPE[0]
                        });
                    },500);
                } else {
                    $("#btn_reset").attr("disabled",false);
                    $("#resetPasswordFailInfo").html(result.message);
                }
            }
        });
    };

    changeLocationHash(type){
        var originType = location.hash.substring(1);
        if (originType == ""){
            window.location = location.href + "#" +type;
        }
        else {
            window.location = location.href.split("#")[0] + "#" +type;
        }

    }

    render() {
        var loginPage = (
            <div id="div_main" className="container" >
                <h1 id="title">登录</h1>

                <form id="login_form" className="col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4" role="form" onSubmit={this.loginSubmit}>
                    <div id="loginFailInfo" className="failInfo"></div>

                    <div className="form-group">
                        <input type="text" className="form-control" name="username" id="username" placeholder="用户名" required />
                    </div>

                    <div className="form-group">
                        <input type="password" className="form-control" name="password" id="password" placeholder="密码" required/>
                    </div>

                    <div className="form-group" style={{textAlign: "left"}}>
                        <input type="checkbox" id="keepPassword"/><label htmlFor="keepPassword">记住密码</label>
                        <a href="#register" onClick={this.toRegisterPage} id="a_register">注册</a>
                        <a href="#reset-password" onClick={this.toResetPasswordPage} id="a_resetPassword">忘记密码</a>
                    </div>

                    <div className="form-group noMargin">
                        <button type="submit" id="btn_login" className="btn btn-primary btn-block">登录</button>
                    </div>
                </form>
            </div>
        );
        var registerPage = (
            <div id="div_main" className="container" >
                <h1 id="title">注册</h1>
                <form id="register_form" className="col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4" role="form" onSubmit={this.registerSubmit}>
                    <div id="registerFailInfo" className="failInfo"></div>

                    <div className="form-group">
                        <input type="text" className="form-control inlineBlock width-95" name="nickname" id="nickname" placeholder="昵称"  required />
                    </div>

                    <div className="form-group">
                        <input type="text" className="form-control inlineBlock width-95" name="username" id="username" placeholder="用户名" required onBlur={this.usernameBlur} />
                        &nbsp;<span className="fa fa-check-circle gray" id="usernamePass"></span>
                    </div>

                    <div className="form-group">
                        <input type="password" className="form-control inlineBlock width-95" name="password" id="password" placeholder="密码" required onBlur={this.passwordBlur}/>
                        &nbsp;<span className="fa fa-check-circle gray" id="passwordPass"></span>
                    </div>

                    <div className="form-group">
                        <input type="password" className="form-control inlineBlock width-95" name="passwordConfirm" id="passwordConfirm" placeholder="确认密码" required onBlur={this.passwordConfirmBlur}/>
                        &nbsp;<span className="fa fa-check-circle gray" id="passwordConfirmPass"></span>
                    </div>

                    <div className="form-group">
                        <input type="email" className="form-control inlineBlock width-95" name="email" id="email" placeholder="邮箱"  required />
                    </div>

                    <div className="form-group">
                        <input type="text" className="form-control inlineBlock width-95" name="phone" id="phone" placeholder="手机号码" />
                    </div>

                    <div className="form-group noMargin width-95">
                        <button type="submit" id="btn_register" className="btn btn-primary width-50">注册</button>
                        <a href="#login" onClick={this.toLoginPage} className="right" style={{display:"inline-block", paddingTop: "15px"}} id="a_login">已有账号，去登录</a>
                    </div>
                </form>
            </div>
        );
        var resetPasswordPage = (
            <div id="div_main" className="container">
                <h1 id="title">重置密码</h1>
                <form id="reset_password_form" className="col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4" onSubmit={this.resetPasswordSubmit}>
                    <div id="resetPasswordFailInfo" className="failInfo">
                    </div>
                    <div className="form-group">
                        <input type="email" className="form-control inlineBlock width-55" name="email" id="email" placeholder="邮箱" required/>
                        <button type="button" className="btn btn-success width-35 margin-right-20 right" id="btn_send_validate_code" onClick={this.sendValidateCode}>
                            <span className="countdown"></span>发送验证码
                        </button>
                    </div>

                    <div className="form-group">
                        <input type="password" className="form-control inlineBlock width-95" name="password" id="password" placeholder="密码" required onBlur={this.passwordBlur}/>
                        &nbsp;<span className="fa fa-check-circle gray" id="passwordPass"></span>
                    </div>

                    <div className="form-group">
                        <input type="password" className="form-control inlineBlock width-95" name="passwordConfirm" id="passwordConfirm"
                               placeholder="确认密码" required onBlur={this.passwordConfirmBlur}/>
                        &nbsp;<span className="fa fa-check-circle gray" id="passwordConfirmPass"></span>
                    </div>

                    <div className="form-group">
                        <input type="text" className="form-control inlineBlock width-95" name="validateCode" id="validateCode"
                               placeholder="输入收到的验证码" required/>
                    </div>

                    <div className="form-group noMargin width-95">
                        <button type="submit" id="btn_reset" className="btn btn-primary width-50">确定</button>
                        <a href="#login" onClick={this.toLoginPage} className="right" style={{display:"inline-block", paddingTop: "15px"}} id="a_login">记得密码，去登录</a>
                    </div>
                </form>
            </div>
        );
        var result;
        switch (this.state.type){
            case PAGE_TYPE[0] : this.changeLocationHash(PAGE_TYPE[0]); result = loginPage; break;
            case PAGE_TYPE[1] : this.changeLocationHash(PAGE_TYPE[1]);result = registerPage; break;
            case PAGE_TYPE[2] : this.changeLocationHash(PAGE_TYPE[2]);result = resetPasswordPage; break;
            default : result = loginPage; break;
        }
        return result;
    }
}

//导出组件
export default Login;