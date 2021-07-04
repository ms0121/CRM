layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


       /**
        * 所获得的 tableIns 即为当前容器的实例
        * 数据表格
        */
       var tableIns = table.render({
              id:"roleId"
              // 容器table元素的id属性值
              ,elem: '#roleList' // 表示要绑定的是哪个数据表格
              ,height: 'full-125' // 容器的高度
              ,cellMinWidth: 95  // 单元格的最小宽度
              ,url: ctx + '/role/list' //请求的地址（后台的数据接口）即当前表格的数据来源于该请求
              ,limit:5  // 默认每页显示的数据条数
              ,limits:[5,10,20,30,40,50]  // 每页页数的可选择项
              ,page: true //开启分页
              ,toolbar:"#toolbarDemo"  // 开启头部工具栏选项的按钮
              ,cols: [[ //表头
                     /**
                      * field：要求field属性值与后端返回的数据中对应的属性字段名一致
                      * title:设置列的标题
                      * sort:是否允许排序(默认是false)
                      * fixed：固定列，相当于固定单元格的操作
                      */
                     {type: 'checkbox', fixed: 'center'}    // 复选框
                     ,{field: 'id', title: '编号', sort: true, fixed: 'left'}
                     ,{field: 'roleName', title: '角色名称', align:'center'}
                     ,{field: 'roleRemark', title: '角色标签', align:'center'}
                     ,{field: 'createDate', title: '创建时间', align:'center'}
                     ,{field: 'updateDate', title: '更新时间', align:'center'}
                     // 开启行工具栏
                     ,{title: '操作', templet: '#roleListBar', fixed: 'right', align: 'center', minWidth:150}
              ]]
       });


       /**
        * 当点击搜索按钮的时候进行查询数据信息，给搜索按钮绑定单击事件
        */
       $(".search_btn").click(function () {
              // 将搜索的内容显示在当前数据表中
              tableIns.reload({
                     // where填写的是从前端获取对应的文本数据，并把它们传入到后端给对应的参数名的参数赋值
                     where: { //设定异步数据接口的额外参数，任意设置
                            roleName: $("input[name='roleName']").val()
                     }
                     ,page: {
                            curr: 1 //响应之后，设置重新从第 1 页开始显示数据
                     }
              });
       });


       /**
        * 监听表单数据的头部工具栏事件: toolbar()
        * 触发事件,数据表格的头部工具栏的增加删除按钮事件绑定
        * 语法格式
        table.on('toolbar(数据表格的lay-filter=""属性值), function(obj){

        })
        */
       table.on('toolbar(roles)', function (obj) {
              if (obj.event == "add"){
                     // console.log("添加");
                     openAddOrUpdateRoleDialog(null);
              }else if (obj.event == "grant"){
                     // 获取被选中的用户信息列表， table.checkStatus("elem"); elem当前表单的id号
                     // console.log(obj.config.id); // obj.config.id == roleId
                     // table.checkStatus(obj.config.id); // 获取被选中的数据信息
                     var checkStatus = table.checkStatus(obj.config.id);
                     console.log(checkStatus);
                     // 打开授权的对话框
                     openAddGrantDialog(checkStatus.data);
              }
       });

       /**
        *  数据表单的行工具栏监听事件，打开导航工具栏：tool(数据表格的lay-filter=""属性值)
        */
       table.on('tool(roles)', function(data) {
              // console.log();
              if (data.event == "edit"){
                     // 当前编辑用户的id号
                     openAddOrUpdateRoleDialog(data.data.id);
              }else if (data.event == "del"){
                     // 删除用户信息
                     deleteRoleDiag(data.data.id);
              }
       });

       /**
        * 添加按钮
        */
       function openAddOrUpdateRoleDialog(id) {
              // 添加数据的标题
              var title = "<h2 align='center'>角色管理--添加角色</h2>";
              var url = ctx + "/role/toAddorUpdateRolePage";   // 点击头部工具栏按钮，请求发送到这个地址

              if (id != null){
                     title = "<h2 align='center'>角色管理--更新角色</h2>";
                     url +="?id=" + id;
              }

              // 页面内打开
              layui.layer.open({
                     type: 2, // 类型
                     title: title,   // 标题
                     area: ['450px', '520px'], // 宽高
                     content: url,   // 请求跳转的url地址
                     maxmin:true,  // 弹窗的最大最小化
              });
       }

       /**
        * 删除指定的用户信息
        */
       function deleteRoleDiag(id) {
              layer.confirm('您确定要删除该记录吗?', {icon:3, title:'角色管理'}, function (index) {
                     // 关闭弹出框
                     layer.close(index);
                     // 执行操作
                     $.ajax({
                            type:"post",
                            data:{
                                   id:id
                            },
                            url:ctx + "/role/delete",
                            success: function (data) {
                                   showInfo(data)
                            }
                     });

                     // 跳转到首页
                     tableIns.reload({
                            page: {
                                   curr: 1 //响应之后，设置重新从第 1 页开始显示数据
                            }
                     });
              });
       }

       /**
        * 发送请求之后的提示信息
        * @param result
        */
       function showInfo(result) {
              if (result.code == 200){
                     layer.msg("删除成功", {icon: 6});
                     // 刷新表格
                     tableIns.reload();
              }else {
                     // 提示失败
                     layer.msg(result.msg, {icon:5});
              }
       }

       /**
        * 打开授权页面
        * @param id
        * data：代表的是整个选中的用户信息
        */
       function openAddGrantDialog(data) {
              if (data.length == 0){
                     layer.msg("请选择要授权的角色!", {icon: 5});
                     return;
              }
              if (data.length > 1){
                     layer.msg("暂不支持批量角色授权!", {icon:5});
                     return;
              }
              // 向当前的地址发送请求,从而进行跳转页面
              var url = ctx + "/module/toAddGrantPage?roleId="+data[0].id;
              var title = "<h3>角色管理 - 角色授权</h3>"

              // 打开模态框
              layui.layer.open({
                     title: title,
                     content: url,
                     type: 2,
                     area: ["400px", "500px"],
                     maxmin: true
              });
       }
});
