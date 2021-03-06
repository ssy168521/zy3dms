﻿var logInfo = $.extend({}, logInfo);/* 定义全局对象，类似于命名空间或包的作用 */
$(function() {


	/**
	 * 初始化datagrid
	 */
	logInfo.grid = $('#logInfo_table').datagrid({
		fit : true,
		border : true,
		fitColumns : true,
		striped : true,
		nowrap : false,
		idField : 'dataid',//主键
		autoRowHeight : false,
		pagination : true,
		pageNumber : 1,
		pageSize : 20,
		url : myProjectPath + '/log/dataGridInfo?t='+new Date().getTime(),
		rowStyler : function(index, row) {
			return 'height:35px';
		},
		columns : [ [ {
			field : 'id',
			title : 'id',
			width : 100,
			align : 'center'
		}, {
			field : 'satellite',
			title : '卫星',
			width : 100,
			align : 'center'
		}, {
			field : 'sensor',
			title : '传感器',
			width : 100,
			align : 'center'
		}, {
			field : 'sucess_count',
			title : '归档成功数量',
			width : 100,
			align : 'center'
		}, {
			field : 'failed_count',
			title : '归档失败数量',
			width : 100,
			align : 'center'
		}] ],
		onLoadSuccess : function(data) {
			// datagrid渲染完毕后渲染自定义的linkbutton
			if (data.rows.length == 0) {
				var body = $(this).data().datagrid.dc.body2;
				uj.noData(body);
			} else {

			}
			// 清除选择行
			logInfo.grid.datagrid('clearChecked');
		},
		onLoadError : function() {
			var body = $(this).data().datagrid.dc.body2;
			uj.errorData(body);
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			// datagrid中取消右键点击行
			e.preventDefault();
		}
	});
});
