$('#pwd_submit').click(function () {
    var pwd = $("#new_pwd").val();
    var repwd = $("#re_pwd").val();
    var oldpwd = $("#old_pwd").val();
    if(pwd.length<7 || pwd.length>32 || repwd.length<7 || repwd.length>32 ){
        alert("密码长度不符合规则");
        return false;
    }
    if(pwd !== repwd){
        alert("两次输入的密码不一致");
        return false;
    }
    if(oldpwd === pwd){
        alert("新密码与旧密码不能相同");
        return false;
    }
    $('#pwd_submit').submit();
});