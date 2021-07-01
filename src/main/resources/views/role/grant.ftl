<html>
<head>
	<link rel="stylesheet" href="${ctx}/js/zTree_v3-3.5.32/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${ctx}/lib/jquery-3.4.1/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/zTree_v3-3.5.32/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="${ctx}/js/zTree_v3-3.5.32/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript">
		var ctx="${ctx}";
	</script>
</head>
<body>
<#--当前隐藏域中的roleId值是来自于后端，所以需要后端将数据设置在请求域中，而前端的数据则是设置在隐藏域中-->
<input type="hidden" name="roleId" value="${roleId!}"/>

<div id="test1" class="ztree"></div>

<script type="text/javascript" src="${ctx}/js/role/grant.js"></script>
</body>
</html>