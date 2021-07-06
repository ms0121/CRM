layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    // 客户流失列表展示
    var  tableIns = table.render({
        elem: '#customerLossList',
        url : ctx+'/customer_loss/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerLossListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'cusNo', title: '客户编号',align:"center"},
            {field: 'cusName', title: '客户名称',align:"center"},
            {field: 'cusManager', title: '客户经理',align:"center"},
            {field: 'lastOrderTime', title: '最后下单时间',align:"center"},
            {field: 'lossReason', title: '流失原因',align:"center"},
            {field: 'confirmLossTime', title: '确认流失时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
        ]]
    });


    /**
     * 当点击搜索按钮的时候进行查询数据信息，给搜索按钮绑定单击事件
     */
    $(".search_btn").click(function () {
        // 将搜索的内容显示在当前数据表中
        tableIns.reload({
            // where填写的是从前端获取对应的文本数据，并把它们传入到后端给对应的参数名的参数赋值
            // （传给的是controller中的参数对象对应的变量）
            where: { //设定异步数据接口的额外参数，任意设置
                cusNo: $("input[name='cusNo']").val(),
                cusName: $("input[name='cusName']").val(),
                state: $("#state").val(),
            }
            ,page: {
                curr: 1 //响应之后，设置重新从第 1 页开始显示数据
            }
        });
    });

    //数据表单的行工具栏监听事件，打开导航工具栏  tool(数据表格的lay-filter=""属性值)
    table.on('tool(customerLosses)', function (data) {
        // data为整个页面的所有数据线信息
        var id = data.data.id;
        if (data.event == 'add') {
            // 打开客户流失添加的数据窗口
            addCustomerLoss(id);
        } else if (data.event == 'info'){
            layer.msg("暂不支持查看详情!", {icon: 6});
        }
    });


    /**
     * 显示详情信息
     * @param id
     */
    function addCustomerLoss(id) {
        // 显示详情数据的标题
        var title = "<h2 align='center'>客户流失管理--暂缓管理</h2>";
        // 发送请求到这个地址，从而打开添加的页面信息
        var url = ctx + "/customer_loss/customer_rep?id=" + id;

        layui.layer.open({  // 页面内打开
            type: 2, // 类型
            title: title,
            area: ['650px', '500px'], // 宽高
            content: url,   // 跳转的url地址
            maxmin:true,  // 弹窗的最大最小化
        });
    }


});
