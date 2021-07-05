layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;



    // 用户开发计划表单的确认按钮时间
    // 提交表单数据
    form.on('submit(addOrUpdateCusDevPlan)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        // 添加操作
        var url = ctx + "/cus_dev_plan/add";

        // 更新操作
        // 判断计划项id是否为空，不为空，更新操作
        if ($("[name='id']").val()){
            url =  ctx + "/cus_dev_plan/update";
        }

        // 提交表单数据
        $.post(url, data.field, function (result) {
            // console.log(result);
            // alert("执行成功!");
            // 操作成功
            if (result.code==200) {
                // 提示成功,6代表笑脸图标
                top.layer.msg("操作成功!", {icon: 6});
                // 关闭加载层
                top.layer.close(index);
                // 关闭弹出框iframe，即小窗口
                layer.closeAll("iframe");
                // 刷新弹窗的父页面
                // 重新刷新页面内信息
                parent.location.reload();
            } else {
                // 数据提交失败
                layer.msg(result.msg, {icon: 5})
            }
        });
        // 阻止表单提交后出现跳转
        return false;
    });



    // 添加营销机会数据SaleChance弹窗的close单击按钮事件
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得父窗口内的当前iframe层的索引
        parent.layer.close(index); //在父窗口内再执行关闭
    })




















    // /**
    //  * 表单Submit监听
    //  */
    // form.on('submit(addOrUpdateCusDevPlan)', function (data) {
    //
    //     // 提交数据时的加载层 （https://layer.layui.com/）
    //     var index = top.layer.msg("数据提交中,请稍后...",{
    //         icon:16, // 图标
    //         time:false, // 不关闭
    //         shade:0.8 // 设置遮罩的透明度
    //     });
    //
    //     // 得到所有的表单元素的值
    //     var formData = data.field;
    //
    //     // 请求的地址
    //     var url = ctx + "/cus_dev_plan/add";
    //
    //     $.post(url, formData, function (result) {
    //         // 判断操作是否执行成功 200=成功
    //         if (result.code == 200) {
    //             // 成功
    //             // 提示成功
    //             top.layer.msg("操作成功！",{icon:6});
    //             // 关闭加载层
    //             top.layer.close(index);
    //             // 关闭弹出层
    //             layer.closeAll("iframe");
    //             // 刷新父窗口，重新加载数据
    //             parent.location.reload();
    //         } else {
    //             // 失败
    //             layer.msg(result.msg, {icon:5});
    //         }
    //     });
    //
    //     // 阻止表单提交
    //     return false;
    // });
    //
    //
    // /**
    //  * 关闭弹出层
    //  */
    // $("#closeBtn").click(function () {
    //     // 当你在iframe页面关闭自身时
    //     var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
    //     parent.layer.close(index); // 再执行关闭
    // });


});