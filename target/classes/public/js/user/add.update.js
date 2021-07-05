layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    // 引入 formSelects 模块实现下拉框数据填充操作
    var formSelects = layui.formSelects;


    // 添加user弹窗的close单击按钮事件
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得父窗口内的当前iframe层的索引
        parent.layer.close(index); //在父窗口内再执行关闭
    });


    /**
     * 用户角色添加操作，加载角色下拉框
     * 配置远程搜索, 请求头, 请求参数, 请求类型等
     *
     * formSelects.config(ID, Options, isJson);
     *
     * @param ID        前端页面的xm-select的值,
     * @param Options   配置项
     * @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    // 获取当前用户的id
    var userId = $("[name='id']").val();
    formSelects.config('selectId', {
        type: 'post',                //请求方式: post, get, put, delete...
        searchUrl: ctx + '/role/queryAllRoles?userId=' + userId, //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
        keyName: 'roleName',            //自定义返回数据中name的key, 默认 name, 下拉框中的文本内容，要与返回的数据中对应key一致
        keyVal: 'id',            //自定义返回数据中value的key, 默认 value
    }, true);


    // 头工具栏，提交表单数据
    form.on('submit(addOrUpdateUser)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        // 数据请求的地址,添加的操作(也就是要把前端填写的数据信息发送到那个请求地址)
        var url = ctx + "/user/add";

        // 数据更新的操作
        // 根据隐藏域中是否存在id这个值来判断是添加还是修改的操作，id为空，添加操作，id不为空，修改操作
        if ($("[name='id']").val()) {
            // 跳转到更新的请求地址
            url = ctx + "/user/update";
        }

        /** 发送ajax请求（post方式）
         * url：请求地址
         * data.field：前端页面提交表单的所有数据值，所以要求前后端对应的变量名必须一致，否则无法将其封装在对象中使用
         * result: 返回的结果(来自于调用具体的后端方法的返回值)，当前添加/更新操作返回的是 ResultInfo 类型的结果
         */
        $.post(url, data.field, function (result) {
            // console.log(result);
            // alert("执行成功!");
            // 操作成功
            if (result.code == 200) {
                // 提示成功,6代表笑脸图标
                layer.msg("操作成功!", {icon: 6});
                // 关闭加载层
                layer.close(index);
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

});