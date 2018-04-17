<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">    
    <title>数据归档</title>   
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">

    <script src="./lib/jquery.js"></script>
   	<script src="./lib/bootstrap/js/bootstrap.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<!-- <script src="./lib/bootstrap-table/bootstrap-table-export.js"></script> -->

    <script src="js/pintuer.js"></script>
    <script src="js/user.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
    <style type="text/css">

   /* body {background-color:#7D9EC0;} */
</style>
  </head> 
<body> 
<div class="panel-head"><strong class="icon-reorder">归档</strong></div>
	  <div class="tabbable" id="tabs-724231">
           <div style="text-align:center; padding-top:15px">
		     <ul class="nav nav-tabs">
				<li class="active" style="width:33%">
					 <a href="#sc-query-region" data-toggle="tab">传感器校正产品</a>
				</li>
				<li style="width:33%">
					 <a href="#sc-query-region" data-toggle="tab">标准分幅DOM产品</a>
				</li>
				<li style="width:33%">
					 <a href="#sc-query-region" data-toggle="tab">分景DOM产品</a>
				</li>
			</ul>
		  </div>
	<div class="tab-content">
	  <div class="tab-pane active" id="sc-query-region">	
		  <form id="queryform" >
		  <input type="hidden" id="ProductType" name="ProductType" >
		    <table >
		    	<tr height="45">
					<td width="400" style="text-align:right; padding-right:0px;">
						<label>任务名称：</label>
					</td>
					<td width="500" style="text-align:left; padding-left:0px;">
						<input id="taskname" class="i-text" name="name-taskname" type="text"  maxlength="1000" value="" style="width: 272px; "/>
					</td>
				</tr>
				<tr height="45">
					<td width="150" style="text-align:right; padding-right:0px;">
						<label>归档源路径：</label>
					</td>
					<td width="500" style="text-align:left; padding-left:0px;">
						<input id="path" class="i-text" name="name-archivepath" type="text"  maxlength="1000" value="" style="width: 272px; "/>
					</td>
				</tr>
				
				<tr class="advancequerycls"; height="10%">
				   <td width="80%" style="text-align:right; padding-left:20px;">										
					 <label title="扫描式归档">
						<input id="scanning" name="sc-Archive" value="SXZ" type="radio">
						扫描归档
					</label>
				   </td>
				   <td width="80%" style="text-align:left; padding-left:20px;">										
					 <label title="迁移式归档">
						<input id="copy" name="sc-Archive" value="SS" type="radio">
						迁移归档
					 </label>
				   </td>
				</tr>
					
				<tr height="45">
					<td width="100" style="text-align:right; padding-right:0px;">
						
					</td>
					<td width="500" style="text-align:left; padding-left:10px;">
						<input id="btn_archive" class="btn btn-primary" type="button" maxlength="1000" value="开始归档" style="height: 33px; width: 91px; ">
					</td>
				</tr>					
			</table>
			</form>		
	  </div>
	</div>
</div>
<script type="text/javascript">

$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {            
       var  activeTab = $(e.target).text();
       $("#ProductType").val(activeTab);
     });
$(function(){
	var pDate=new Date();
	var strProductType=$("#ProductType").val();
	//SC归档
	var taskname= strProductType+pDate.getFullYear()+"-"+(pDate.getMonth()+1)+"-"+pDate.getDate()+" "+pDate.getHours()+":"+pDate.getMinutes()+":"+pDate.getSeconds();
	$("#taskname").val(taskname);
	
});

  //SC产品归档
$("#btn_archive").click(function(){
	var strtaskname=$("#taskname").val();
	var strarchivepath=$("#path").val(); 
	var strProductType=$("#ProductType").val();
	if(strarchivepath =="")
	{
	    alert("请设置归档路径");
	    return;
	}
	
	strarchivepath=strarchivepath.replace(/\\/g,"\\\\");
	var tmp="{\"ArchivePath\":\""+strarchivepath+"\",\"taskname\":\""+strtaskname+"\"}";
	
	//document.getElementsByName("name-archivepath")
	$.ajax(
		{url:"<%=basePath%>"+"/servlet/HandArchive",
		type:"POST",
		data:eval("("+tmp+")"),
		async:false,
		error:function(request){
			alert(" network error!");
		},
		success:function(data){
			document.write(data);
			document.close();
		}
	});
});
    //标准分幅产品归档
$("#btn_archive2").click(function(){
	var arcmethod = "";//单选框
	var archive=document.getElementsByName("sfi-Archive");
	for (i = 0; i < archive.length; i++) {
		if(archive[i].checked){
			arcmethod = archive[i].value
			}  
	}
	//alert(arcmethod);
	var strtaskname=$("#taskname2").val();
	var strarchivepath=$("#path2").val(); 
	if(strarchivepath =="")
	{
	    alert("请设置归档路径");
	    return;
	}
	//文件路径转为绝对路径
	strarchivepath=strarchivepath.replace(/\\/g,"\\\\");
	var tmp="{\"ArchivePath\":\""+strarchivepath+"\",\"taskname\":\""+strtaskname+"\",\"arcmethod\":\""+arcmethod+"\"}";  
	
	//document.getElementsByName("name-archivepath")
	$.ajax(
		{url:"<%=basePath%>"+"/servlet/FramingArchive",
		type:"POST",
		data:eval("("+tmp+")"),
		async:false,
		error:function(request){
			alert(" network error!");
		},
		success:function(data){
			document.write(data);
			document.close();
		}
	});
});
    //分景产品归档
$("#btn_archive3").click(function(){
	var arcmethod = "";  //单选框
	var archive=document.getElementsByName("sfi-Archive");
	for (i = 0; i < archive.length; i++) {
		if(archive[i].checked){
			arcmethod = archive[i].value
		}  
	}
	var strtaskname=$("#taskname3").val();
	var strarchivepath=$("#path3").val(); 
	if(strarchivepath =="")
	{
	    alert("请设置归档路径");
	    return;
	}
	strarchivepath=strarchivepath.replace(/\\/g,"\\\\");
	var tmp="{\"ArchivePath\":\""+strarchivepath+"\",\"taskname\":\""+strtaskname+"\"}";  

	$.ajax(
		{url:"<%=basePath%>"+"/servlet/FramingArchive",
		type:"POST",
		data:eval("("+tmp+")"),
		async:false,
		error:function(request){
			alert(" network error!");
		},
		success:function(data){
			document.write(data);
			document.close();
		}
	});
});
</script>
</body>
</html>
