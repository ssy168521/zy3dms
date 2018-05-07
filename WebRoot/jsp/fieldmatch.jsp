<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>   
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">

    <script src="./lib/jquery.js"></script> 
    <!-- <script src="./lib/jquery-3.2.0.min.js"></script> -->
   	<script src="./lib/bootstrap/js/bootstrap.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<script src="./lib/bootstrap3-editable/js/bootstrap-editable.js"></script>
	<script src="./lib/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>
    
    <script src="js/pintuer.js"></script>
    <script src="js/user.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
	<link rel="stylesheet" href="./lib/bootstrap3-editable/css/bootstrap-editable.css" />
  </head>
  
  <body>
    <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder">建库方案</strong></div>
    <div id="toolbar">
			<form class="form-inline" style="width:100%;" name="xmlform" enctype="multipart/form-data">
			   <div  style="display:inline-block; width: 741px">
			      <label style="height:25px;">产品名称 : </label>
				   <select id="producttype" style="height:25px;width:50%;">
			   
				 
	            <!--  <option>sc产品</option>
                      <option>分景DOM</option>
				      <option>分幅DOM</option> --> 
						</select>																
			<!-- <input style="height:25px;width:100px;" type="text" id="productlevel" value="输入产品"/> -->
				 <span class="help-block">选择或输入归档产品类型</span>
		

 <!-- 			<form class="form-inline" style="width:33%;">
				<label style="height:25px;">数据库表名 : </label>
			 <select id="tablename" style="height:25px;width:165px;">
						 </select>
				<span class="help-block">选择已有数据表</span>
	       </form>
	-->
			        <input id="FileId" type="file" accept="text/xml" name="xmlfile" onchange="$('#btn').click();">
			        <input type="hidden" id="btn" value="确定" >
			        <span class="help-block">选择xml文件</span>			
		        </div>		         
	       </form>
           <!-- <span class="help-block">说明：选择产品类型和数据表后，在表中选择合适的xml节点，然后输入新的字段名，建立字段节点连接关系</span> -->
    

     </div>
    <table id="userTable"></table>
    <form id="subform" method="post">
        <input type="hidden" id="subfieldid" name="subid"/>
    	<input type="hidden" id="subfieldName" name="subuserid"/>
    	<input type="hidden" id="subfieldTypeName" name="subusername"/>
    </form>
    
    <div>  <button onclick="OnSaveSchema()">保存建库方案</button> </div>
  </div>
  
 <script type="text/javascript">
   var obj;
   var productTable;
  // alert("111");
   $.ajax({
       url: "./servlet/producttype",
       async: false,
       type: "POST",
       dataType: "json",
       success: function (data) {
       	// alert(data);
          for (var i = 0; i < data.length; i++) {
        	 // alert(data[i]);
           	if(data[i].substring(0,2) != "tb"){
               $("#producttype").append("<option>" + data[i] + "</option>");
           	}else{
               $("#tablename").append("<option>" + data[i] + "</option>");
           	}
           }
       }
   });
   
   function OnSaveSchema()
   {
   
   
   }
   
   $('#btn').click(function () {
	  var producttype=$('#producttype').val();	
       //var xmlfile =$('#FileId').val(); 
       var xmlfile = document.xmlform.xmlfile.files[0];
       var fm = new FormData();
       fm.append('xmlfile', xmlfile);
		$.ajax({
			url:"servlet/columnName",
			type:"POST",
			async:false,
			data: {"productName":JSON.stringify(producttype)},
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				var obj= eval("("+data+")");
				$('#userTable').bootstrapTable({
					method: 'get',
					cache: false,
					height: 708,
					clickToSelect:true,     //是否选中  
					//toolbar: '#toolbar',
					striped: true,
					pagination: true,
					pageSize: 12,
					pageNumber: 1,
					pageList: [10, 20, 50, 100, 200, 500],
					search: true,
					showColumns: true,
					showRefresh: true,
					showExport: true,
					exportTypes: ['csv','txt','xml'],
					search: false,
					columns: [{field:"checkStatus",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},
					//{field:"fieldid",title:"字段id",align:"center",valign:"middle",sortable:"true"},
					{field:"fieldName",title:"字段名称",align:"center",valign:"middle",sortable:"true"},
					{field:"fieldTypeName",title:"字段数据类型",align:"center",valign:"middle",sortable:"true"},
					{field:"nodepath",title:"节点名称",align:"center",valign:"middle",sortable:"true",
						editable: {
		                    type: 'select',
		                    title: '节点名称',
		                    source: function () {
		                    	var result = [];
		                    	//alert(result);
		                        $.ajax({
		                            url: 'servlet/UploadXML',
		                            async: false,
		                            type: 'post',
		                            data: fm,
		                            contentType: false, //禁止设置请求类型
		                            processData: false,
		                            success: function (info) {
		                		        var jsonstr=eval("("+info+")");
		                            	for(var i = 0; i < jsonstr.length; i++) {
		                            		var str = JSON.stringify(jsonstr[i].nodeName);
		                            		var str1 = JSON.stringify(jsonstr[i].nodepath);
		                            	    result.push({ value:str1,text:str });
		                            	}
		                            }
		                        });
		                        return result;
		                    }
		                }	
					},
					],
					data:obj,
					onEditableSave: function (field, row, oldValue, $el) {
				    	/*var productType=$('#producttype').val();
			                $.ajax({
			                    type: "post",
			                    url: "./servlet/fieldedit",
			                    data: {"objSelec":JSON.stringify(row),
			                    	"productType":JSON.stringify(productType)
			                    	//,"productTable":JSON.stringify(productTable)
			                    	},//json序列化，不能直接传送json对象
			                    dataType: 'JSON',
			                    success: function (data, status) {
			                        if (status == "success") {
			                            alert('提交数据成功');
			                        }
			                    },
			                    error: function () {
			                        alert('编辑失败');
			                    },
			                    complete: function () {

			                    }

			                });*/
			            }
	
				});	
			}
		});
		$(window).resize(function () {
			$('#userTable').bootstrapTable('resetView');//移除表数据
		});
		
	});	
 
 </script>
  
  </body>
</html>
