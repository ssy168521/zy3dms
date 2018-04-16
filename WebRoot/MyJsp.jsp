<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String rootpath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
com.sasmac.util.AppConfUtil util= com.sasmac.util.AppConfUtil.getInstance();
util.SetAppconFile("appconf.xml");

String TDTUrl=util.getProperty( "TDTURL");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="StyleSheet" type="text/css" href="css/home.css" />
<link rel="stylesheet" type="text/css"
	href="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/dijit/themes/tundra/tundra.css" />
<link rel="stylesheet" type="text/css"
	href="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/esri/css/esri.css" />
	
	<style>
html, body, #mapDiv {
  padding: 0;
  margin: 0;
  height: 100%;
}
</style>
<script type="text/javascript"		src="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/init.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script>
var map;
require([
"esri/map","esri/layers/WMSLayer",
"esri/layers/MapImageLayer",
"esri/layers/MapImage",
"dojo/domReady!"
], function(
Map, WMSLayer,MapImageLayer, MapImage
) {

map = new Map("mapDiv", {
				logo : false
			});

var resourceInfo = {  
	            extent: new esri.geometry.Extent(-180,-90,180,90,{wkid: 4326}),  
	            layerInfos: [],  
	            version : '1.1.1'
			}; 
               
			var agsWmsUrl = "<%=TDTUrl%>";
			var agsWmsLayer = new WMSLayer(agsWmsUrl ,{resourceInfo: resourceInfo}); 
			agsWmsLayer.setImageFormat("png");
			agsWmsLayer.setVisibleLayers([0]);
			map.addLayer(agsWmsLayer);
map.on("load", function() {


		var agsWmsUrl = "http://localhost:6080/arcgis/services/MapWorld/MapServer/WMSServer";
		var agsWmsLayer = new WMSLayer(agsWmsUrl ,{resourceInfo: resourceInfo}); 
		agsWmsLayer.setImageFormat("png");
		agsWmsLayer.setVisibleLayers([0]);
		map.addLayer(agsWmsLayer);
		
		// ���������ͼ��
		var mil = new esri.layers.MapImageLayer();
	    mil.setVisibility(true);
		map.addLayer(mil);
		
		// ���������ͼ��,j46D001001
		var mi = new esri.layers.MapImage({
		 'extent': {'xmin':-1551802.164754936,'ymin':3188742.0592723233,'xmax':-1502801.7490601132,'ymax':3234414.144844479,'spatialReference':{'wkt':'PROJCS["WGS_1984_Albers",GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137.0,298.257223563]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]],PROJECTION["Albers"],PARAMETER["false_easting",0.0],PARAMETER["false_northing",0.0],PARAMETER["central_meridian",110.0],PARAMETER["standard_parallel_1",25.0],PARAMETER["standard_parallel_2",47.0],PARAMETER["latitude_of_origin",12.0],UNIT["Meter",1.0]]'}},
		  'href': 'C:\database\overview\test\J46D001001.png'
		});
		
		var mi1 = new esri.layers.MapImage({
		  'extent': { 'xmin': 89.9066, 'ymin': 39.58973, 'xmax': 90.59089, 'ymax': 40.07674,'spatialReference':{'wkid':4326}},
		  'href': 'C:\database\overview\test\J46D001001-1.png'
		});
		 mil1.setVisibility(true);
		 map.addLayer(mil1);
		mil.addImage(mi);
		mil.addImage(mi1);
		});

});
</script>
  </head>
  
  <body>
    This is my JSP page. <br>
    <div id="mapDiv" style="width:100%,height:100%"></div>
  </body>
</html>
