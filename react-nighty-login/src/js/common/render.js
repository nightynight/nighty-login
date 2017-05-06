/**
 * Created by chenchao on 17/4/16.
 */

import React from 'react';
import ReactDom from 'react-dom';

window.nighty = {}; //框架命名空间

import Login from '../../component/login/Login';
nighty.Login = {};
nighty.Login.render = function ({domId, serverBaseUrl, userHomePageUrl, adminHomePageUrl, messageMethod}) {
	let params = {
		serverBaseUrl,
		userHomePageUrl,
		adminHomePageUrl,
		messageMethod
	};
	ReactDom.render(
		<Login {...params} />,
		document.getElementById(domId)
	);
};
nighty.Login.isLogin = function (serverBaseUrl, messageMethod) {
    Login.isLogin(serverBaseUrl, messageMethod);
};
nighty.Login.logout = function (serverBaseUrl, loginPage, messageMethod) {
    Login.logout(serverBaseUrl, loginPage, messageMethod);
};
nighty.Login.needLogin = function (serverBaseUrl, loginPage, messageMethod) {
    Login.needLogin(serverBaseUrl, loginPage, messageMethod);
};
$(function(){
	$(".nighty_logout").each(function(){
	    var $bt=$(this);
	    var serverBaseUrl = $bt.attr("data-server-base-url");
	    var loginPage = $bt.attr("data-login-page");
		var messageMethod = $bt.attr("data-message-method");
	    $bt.click(function(){
	        Login.logout(serverBaseUrl, loginPage, messageMethod);
	    })
	});
});

