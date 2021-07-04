layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单submit的单击事件
         form.on('submit(按钮元素的lay-filter属性值)', function (data) {

         });
     */
    // 提交表单数据
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        // 数据请求的地址,添加的操作(也就是要把前端填写的数据信息发送到那个请求地址)
        var url = ctx + "/sale_chance/add";

        // 数据更新的操作
        // 根据隐藏域中是否存在id这个值来判断是添加还是修改的操作，id为空，添加操作，id不为空，修改操作
        var saleChanceId = $("[name='id']").val();
        if (saleChanceId != null && saleChanceId != ''){
            // 跳转到更新的请求地址
            url = ctx + "/sale_chance/update";
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
            if (result.code==200) {
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

    // 添加营销机会数据SaleChance弹窗的close单击按钮事件
    $("#closeeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得父窗口内的当前iframe层的索引
        parent.layer.close(index); //在父窗口内再执行关闭
    });

    // 页面加载的时候发起ajax请求，查询所有的销售人员数据数据
    $.ajax({
        type:"get",
        data:{},
        url:ctx + "/user/queryAllSales",  // 指定请求地址
        success:function (data) {
            // console.log(data);
            // 获取当前编辑用户的指派人的id属性值
            var assignManId = $("[name='saleChanceId']").val();
            // console.log(assignManId);

            // 判断返回的数据是否为空
            if (data != null){
                // 遍历返回的列表数据
                for (var i = 0; i < data.length; i++) {
                    // 设置下拉选项,根据assignManId设置对应的指派人，设置为选中,否则不选中
                    if (assignManId == data[i].id){
                        var opt = "<option value='"+data[i].id+"' selected>" + data[i].uname + "</option>"
                    }else {
                        var opt = "<option value='"+data[i].id+"'>" + data[i].uname + "</option>"
                    }
                    // 将下拉选项设置到下拉框中，就是在下拉框中多写了几个option选项
                    $("#assignMan").append(opt);
                }
            }
            // 查询数据之后，必须重新渲染下拉框，否则看不到效果
            layui.form.render("select")
        }
    })

});