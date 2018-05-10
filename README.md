# nighty-login
前后端分离的登录框架，分为两个项目，nighty-login-parent为后端框架，webpack-nighty-login为前端框架

## 关于数据库
使用mysql数据库，执行schema.sql文件，初始化数据库

## 关于后端
##### 使用方法：
新建一个web项目，导入nighty-login-1.0-SNAPSHOT.jar，在resources下新建四个文件，修改web.xml，可直接复制nighty-login-template中的文件
##### 注：配置邮箱时，得确保使用的邮箱开启了对应的服务。比如使用163邮箱，使用smtp服务，则需要确保你的163邮箱账号必须开启了smtp服务，可以登录邮箱查看是否开启。

## 关于前端
##### 使用方法：
在页面中引入nighty-login.js，提供一个div，调用
##### nighty.Login.render({domId, serverBaseUrl, userHomePageUrl, adminHomePageUrl, messageMethod}),
##### adminHomePageUrl, messageMethod为可选参数
将这个div渲染成一个登陆组件（包含登陆、注册、重置密码）。

另外，还提供了几个静态方法：
##### nighty.Login.isLogin(serverBaseUrl, messageMethod)，messageMethod为可选参数
##### nighty.Login.logout(serverBaseUrl, loginPage, messageMethod)，messageMethod为可选参数
##### nighty.Login.needLogin(serverBaseUrl, loginPage, messageMethod)，messageMethod为可选参数
##### nighty.Login.getSessionId()
##### nighty.Login.getToken()
如果一个页面需要登录后才能查看，则只需要在<body>之前调用
##### nighty.Login.needLogin(serverBaseUrl, loginPage, messageMethod)

注：
##### 1.serverBaseUrl为后台API的基本URL，如http://localhost:8080/nighty-app
##### 2.userHomePageUrl为用户登录后需要跳转的主页的url
##### 3.adminHomePageUrl为管理员登录后需要跳转的主页的url，如果不传，则也跳转到userHomePageUrl
##### 4.messageMethod为弹出提示信息的方法，如果不传，则为alert

退出登录还可以是：给一个按钮添加一个名为"nighty_logout"的class，并提供一下几个参数
##### data-server-base-url 必填
##### data-login-page 必填

