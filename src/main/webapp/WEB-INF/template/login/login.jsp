<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/template/taglib.jsp"%>
<html>
<head>
    <title>登录</title>
   <link rel="stylesheet" href="${ctx}/static/css/login.css">
</head>
<body>
<div class="htmleaf-container">

    <div id="wrapper" class="login-page">
        <div id="login_form" class="form">
            <div class="register-form">
                <input type="text" placeholder="手机号" id="r_user_phone" name="telphone"/>
                <input type="text" placeholder="昵称" id="r_nickname" name="nickname"/>
                <input type="password" placeholder="密码" id="r_password"  name="password"/>
                <input type="text" placeholder="电子邮件" id="r_emial" name="email"/>

                <button type="button" id="create" >创建账户</button>
                <p class="message">已经有了一个账户? <a href="#">立刻登录</a></p>
            </div>
            <div class="login-form" >
                <input type="text" placeholder="用户名" id="user_name" name="account"/>
                <input type="password" placeholder="密码" id="password" name="password"/>
                <button type="button" id="login" onclick="check_login()">登　录</button>
                <p class="message">还没有账户? <a href="#">立刻创建</a></p>
            </div>
        </div>
    </div>
</div>

<script src="${ctx}/static/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="https://cdn.bootcss.com/blueimp-md5/2.10.0/js/md5.js"></script>
<script type="text/javascript">
    function check_login()
    {
        var account=$("#user_name").val();
        var pass=$("#password").val();
        var password = md5(pass);

        $.ajax({
            url:"${ctx}/login",
            type:"get",
            data:{
                account:account,
                password:password
            },
            contentType: "application/json;charset-UTF-8",
            dataType:"json",
            success:function(data){
                console.log(data);
                 if(data!=null && data.code=="00"){
                     alert(data.msg);
                 }
            },
            error:function(data){

            }
        });
        // if(name=="sc.chinaz.com" && pass=="sc.chinaz.com")
        // {
        //     alert("登录成功！");
        //     $("#user_name").val("");
        //     $("#password").val("");
        //
        // }
        // else
        // {
        //     $("#login_form").removeClass('shake_effect');
        //     setTimeout(function()
        //     {
        //         $("#login_form").addClass('shake_effect')
        //     },1);
        // }
    }
    function check_register(){
        var nickname = $("#r_nickname").val();
        var telphone = $("#r_user_phone").val();
        var pass = $("#r_password").val();
        var email = $("#r_emial").val();
        var password=md5(pass);



        // if(name!="" && pass=="" && email != "")
        // {
        //     alert("注册成功！");
        //     $("#user_name").val("");
        //     $("#password").val("");
        // }
        // else
        // {
        //     $("#login_form").removeClass('shake_effect');
        //     setTimeout(function()
        //     {
        //         $("#login_form").addClass('shake_effect')
        //     },1);
        // }
    }
    $(function(){

        //注册
        $("#create").click(function(){
            var nickname = $("#r_nickname").val();
            var telphone = $("#r_user_phone").val();
            var pass = $("#r_password").val();
            var email = $("#r_emial").val();
            var password=md5(pass);
            var flag=true;
            $(".register-form input").each(function(){
                if($(this).val().trim().length==0){
                    $("#login_form").removeClass('shake_effect');
                    setTimeout(function()
                    {
                        $("#login_form").addClass('shake_effect')
                    },1);
                    alert("请填写完整信息");
                    flag=false;
                    return false;
                }
            });
    if(flag){
            $.ajax({
                url:"${ctx}/register",
                type:"post",
                dataType:"json",
                data:{
                    nickname:nickname,
                    telphone:telphone,
                    email:email,
                    password:password
                },
                success:function(data){
                    if(data!=null && data.code=="00"){
                        alert(data.msg);
                      $("#r_nickname").val("");
                      $("#r_user_phone").val("");
                       $("#r_password").val("");
                      $("#r_emial").val("");
                        setTimeout(function () {
                            $(".login-form").css("display","block");
                             $(".register-form").css("display","none");
                        }, 800 )

                    }
                },
                error:function(data){

                }
            });
    }
        })

        $(".login-form .message a").click(function(){
            $(".register-form").css("display","block");
            $(".login-form").css("display","none");
        });

        $(".register-form .message a").click(function(){
            $(".login-form").css("display","block");
            $(".register-form").css("display","none");
        });

        //失去焦点
        $("#r_user_phone").blur(function () {
            var telphone = $("#r_user_phone").val();
            $.ajax({
                url:"${ctx}/findUserByTelPhone",
                type:"post",
                dataType:"json",
                data:{
                    telphone:telphone,

                },
                success:function(data){
                    if(data!=null && data.code=="01"){
                        alert(data.msg);
                         $("#r_user_phone").val("");
                        $("#login_form").removeClass('shake_effect');
                        setTimeout(function()
                        {
                            $("#login_form").addClass('shake_effect')
                        },1);
                    }
                },
                error:function(data){
                    alert("操作异常，请联系管理员");
                    $("#login_form").removeClass('shake_effect');
                    setTimeout(function()
                    {
                        $("#login_form").addClass('shake_effect')
                    },1);
                }
            });
        });
    })
</script>


</body>
</html>