layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //服务列表展示
    var tableIns = table.render({
        elem: '#customerServeList',
        url : ctx+'/customer_serve/list?state=fw_001',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [5,10,15,20,25],
        limit : 5,
        toolbar: "#toolbarDemo",
        id : "customerServeListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'customer', title: '客户名', minWidth:50, align:"center"},
            {field: 'dicValue', title: '服务类型', minWidth:100, align:'center'},
            {field: 'overview', title: '概要信息', align:'center'},
            {field: 'createPeople', title: '创建人', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
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
                customer: $("input[name='customer']").val(),
                type: $("#type").val(),
            }
            ,page: {
                curr: 1 //响应之后，设置重新从第 1 页开始显示数据
            }
        });
    });

    /**监听表单数据的头部工具栏事件
     * 语法格式
     table.on('toolbar(表单数据的lay-filter=""属性值), function(obj){

     })
     */
    //触发事件,数据表格的头部工具栏的增加删除按钮事件绑定
    table.on('toolbar(customerServes)', function(obj){
        // obj.event：对应元素上设置的lay-even属性值
        if (obj.event == "add"){
            // 添加操作
            openCustomerServeDialog();
        }
    });


    // 打开添加的客户数据的窗口
    function openCustomerServeDialog() {
        // 添加数据的标题
        var title = "<h2 align='center'>客户服务--添加服务</h2>";
        // 发送请求到这个地址，从而打开添加的页面信息
        var url = ctx + "/customer_serve/toAddCustomerServe";

        layui.layer.open({  // 页面内打开
            type: 2, // 类型
            title: title,
            area: ['650px', '500px'], // 宽高
            content: url,   // 跳转的url地址
            maxmin:true,  // 弹窗的最大最小化
        });
    }

});
