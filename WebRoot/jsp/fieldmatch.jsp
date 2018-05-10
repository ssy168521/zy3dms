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
			<form class="form-inline" style="width:100%;" name="xmlform"  enctype="multipart/form-data">
			   <div  style="display:inline-block; width:100%">
			       <label style="height:25px;">产品名称 : </label>
				   <select id="producttype" style="height:25px;width: 15%;" onchange='SelectProduct(this)'></select>																
			        &nbsp;&nbsp;&nbsp;
			       <input id="FileId" type="file" accept="text/xml"  name="xmlfile" style="height:25px;width: 30%;">
			 	   <span class="help-block">选择xml文件</span>
			 	   <input type="button" value="上传" onclick="Uploadxml()"	/>
			 	   <!--  <button value="上传" onclick="Uploadxml()">上传</button>-->
		        </div>		         
	       </form>
    </div>
    <table id="userTable"></table>   
    <div>  <button onclick="OnSaveSchema()">保存建库方案</button> </div>
  </div>
  
 <script type="text/javascript">
   var result = [];//[{value: 1, text: "text1"}, {value: 2, text: "text2"}];
   var field=null;
   $("#producttype").append("<option>请选择产品类型</option>");
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
   function Uploadxml()
   {
      var xmlfile = document.xmlform.xmlfile.files[0];
      var fm = new FormData();
      fm.append('xmlfile', xmlfile);
      var filepath =$("#FileId").val();
      if(filepath==null )
      {
         alert("请选择上传的xml文件");
         return;
      }
     
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
		              result.push({ value:str,text:str1 });
		          }
		         showtable(field,result);;
		      }
		});
		
		//var col=$('#userTable').columns[3];
		//col.editable({
       //      source:result
        //    });
       // $('#userTable').editable
        //$('#userTable').attr('data-original-title','');
           // $('#userTable').editable({
            // source:result
          //  });
      
   }
   function showtable(field,result)
   {
        $('#userTable').bootstrapTable('destroy');
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
					{field:"fieldName",title:"字段名称",align:"center",valign:"middle",sortable:"true"},
					{field:"fieldTypeName",title:"字段数据类型",align:"center",valign:"middle",sortable:"true"},
					{field:"nodepath",title:"节点名称",align:"center",valign:"middle",sortable:"true",
						editable: {
		                    type: 'select',
		                    title: '节点名称',
		                    source: result,
		                }	
					},
					],
					data:field,
					onEditableSave: function (field, row, oldValue, $el) {
					  // $('#userTable').bootstrapTable('updateRow', {index: row.rowId, row: row});
			        }
	
				});	
   }
   function SelectProduct(obj)
   {
   	   var producttype=$('#producttype').val();	
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
			   field=obj;
               showtable(field,result);
			}
		});
		$(window).resize(function () {
			$('#userTable').bootstrapTable('resetView');//移除表数据
		});
   };
   function OnSaveSchema()
   {
			var objSelec=$('#userTable').bootstrapTable('getSelections');
			var strtmp=JSON.stringify(objSelec);
			$.ajax({
						url:"./servlet/SaveSchema",
						type:"POST",
						data:{"objSelec":strtmp,"productType":JSON.stringify(productType)},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
							if(data=="true"){
								alert("保存成功！");
							}
						}
				});
			
   }
   
  // $('#btnSelectXml').click(function () {
  /*function postclick(){
	   var producttype=$('#producttype').val();	
       var xmlfile = document.xmlform.xmlfile.files[0];
       var fm = new FormData();
       fm.append('xmlfile', xmlfile);
       var result = [];
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
       // $('#userTable').editable
        //$('#userTable').attr('data-original-title','');
        $('#userTable').editable({
             source:result
            });
       // $('#userTable').editable.source=result;
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
					$('#userTable').bootstrapTable('updateRow', {index: row.rowId, row: row});
				    	var productType=$('#producttype').val();
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

			                });
			            }
	
				});	
			}
		});
		$(window).resize(function () {
			$('#userTable').bootstrapTable('resetView');//移除表数据
		});
		
	};	*/
 
 </script>
  
  </body>
</html>
