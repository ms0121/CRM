layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 表单的submit监听
     *      form.on('submit(按钮元素的lay-filter属性值)', function (data) {
                field:
                    again_password: "121434"
                    new_password: "124343"
                    old_password: "1234"

            });
     */
    form.on("submit(saveBtn)", function (data) {
        // data为表单中所有的信息
        console.log(data);
        // 发送ajax请求进行修改密码的操作，data中的键值对的key必须和controller中的形参名一致
        $.ajax({
            url: ctx + "/user/updatePwd",
            type: "post",
            data: {
                oldPwd: data.field.old_password,
                newPwd: data.field.new_password,
                repeatPwd: data.field.again_password
            },
            // 执行请求成功之后的响应信息
            success: function (result) {
                // 打印详细的响应信息
                // console.log(result);
                if (result.code == 200) {
                    // 密码修改成功之后，进行退出
                    layer.msg("用户密码修改成功, 系统将在3秒钟之后退出...", function () {
                        // 清空cookie,指定key和对应的路径的
                        $.removeCookie("userIdStr", {domain:"localhost", path: "/crm"});
                        $.removeCookie("userName", {domain:"localhost", path: "/crm"});
                        $.removeCookie("trueName", {domain:"localhost", path: "/crm"});

                        // 跳转到登录页面
                        window.parent.location.href = ctx + "/index";
                    });
                } else {
                    // 修改密码失败
                    layer.msg(result.msg, {icon: 5});
                }
            }
        })
    })
});