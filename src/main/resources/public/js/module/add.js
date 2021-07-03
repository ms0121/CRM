layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听表单提交
     */
    form.on("submit(addModule)", function (data) {
        // 弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        // 发送ajax请求，前端的页面填写的数据信息会被进行序列化(data.field)成为json数据传递到后天，
        // 必须设置前后台接收数据的参数名一致，才能将前台的数据注入到module对象的属性中去
        // res 发送请求执行函数之后返回得到的数据信息
        $.post(ctx+"/module/add", data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                    res.msg, {
                        icon: 5
                    }
                );
            }
        });
        return false;
    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
        parent.layer.close(index); // 再执行关闭
    });

});