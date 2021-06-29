layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 表单submit提交
     *  form.on('submit(按钮的lay-filter属性值)', function(data){
     *
     *       return false; //阻止表单跳转。
     *  });
     */

    form.on('submit(login)', function(data){
        // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}

        // 获取到字段信息之后，发送ajax请求进行验证信息
        // ctx：当前项目路径
        $.ajax({
            url:ctx + "/user/login",  // 向该地址发送请求
            type:"post",
            data:{
                // 传到后台的形参名必须和controller函数中的参数名一致
                userName:data.field.username,
                userPwd:data.field.password
            },
            success:function (result) {  // result是回调函数，用来接收后端返回的数据
                // console.log(result);
                // 判断是否登录成功，200表示登录成功
                if (result.code == 200){
                    /**
                     * 设置用户是登录状态(保存用户登录的信息)
                     * 方式1：利用session会话，
                     *     保存登录用户的信息，如果会话存在，则用户是登录状态，否则为非登录状态
                     *     缺点：服务器关闭，会话则会失效
                     * 方式2：利用cookie
                     *     保存用户信息，cookie未失效，则用户是登录状态
                     */
                    // 弹出提示信息
                    layer.msg("登录成功", {icon: 6}, function () {
                        // 判断是否有勾选记住我 这个复选框，如果选中，则设置cookie对象7天内有效
                        if ($("#rememberMe").prop("checked")) {
                            // 如果选中，设置有效时间，将用户信息设置到cookie中
                            $.cookie("userIdStr", result.result.userIdStr,{expires:7});
                            $.cookie("userName", result.result.userName,{expires:7});
                            $.cookie("trueName", result.result.trueName,{expires:7});
                        } else {
                            // 将登录成功的用户信息保存到cookie中
                            $.cookie("userIdStr", result.result.userIdStr);
                            $.cookie("userName", result.result.userName);
                            $.cookie("trueName", result.result.trueName);
                        }
                        // 登陆成功之后，跳转到首页
                        window.location.href = ctx + "/main";
                    })
                }else {
                    // 登录失败,弹出提示信息
                    layer.msg(result.msg, {icon:5});
                }
            }
        })
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

});