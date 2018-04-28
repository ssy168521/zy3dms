package com.sasmac.meta;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class SceneDOMMetaParser {
	private Logger myLogger = LogManager.getLogger("mylog");

	public SceneDOMSpatialMeta ParseMeta(String strTifFile) throws Exception {
		
		SceneDOMSpatialMeta m_SpatialMeta = new SceneDOMSpatialMeta();
		File imageFile = new File(strTifFile);
		String filename = imageFile.getName();
		String MainFileName= filename.substring(0,filename.lastIndexOf("."));
		String filePath = imageFile.getParent(); // .replace("\\", "\\\\");
	
		 
		String RasterFormat = strTifFile.substring(strTifFile.lastIndexOf(".") + 1);
		// split切分字符串，循环求出第五个字符串即为日期
		String[] ss = filename.split("_");
		//String acquisitionTime = ss[4].substring(0,8); // 产品制作时间
		String acquisitionTime ="";
		long dataid = System.currentTimeMillis();
		String satellite="";

		String fields[] = MainFileName.split("_");
		    // zy302a_nad_007831_895146_20171027111543_01_sec_0001_1710306797_ORTHO_PAN
		    // zy301a_mux_029533_016146_20170504110226_01_sec_0004_1705056054_ORTHO_MS
		    if(MainFileName.matches("zy30[1-2]a_(nad|mux)_[0-9]{6}_[0-9]{6}_[0-9]{14}_01_sec_[0-9]{4}_[0-9]{10}_ORTHO_(PAN|MS)"))
		    {
		    	acquisitionTime= fields[4].substring(0,8);
		    	satellite= fields[0].substring(0,5);
		    	m_SpatialMeta.setSensor(fields[1].toUpperCase());
		    }
		    // GF2_PMS1_E109.3_N31.0_20170620_L1A0002430842-MSS1_ORTHO_MS
		   //  GF2_PMS1_E114.0_N27.3_20170510_L1A0002352007-PAN1_PAN
		   //  GF2_PMS2_E115.2_N28.6_20170430_L1A0002335216-MSS2_MS
		    else if(MainFileName.matches("GF[1-2]_PMS[1-2]_E[0-9]{3}.[0-9]_N[0-9]{2}.[0-9]_[0-9]{8}_L1A[0-9]{10}-(MSS|PAN)[1-2]_.*"))
		    {
		    	satellite= fields[0];
		    	acquisitionTime= fields[4];
		    	if(MainFileName.endsWith("MS"))
		    	{
		    		m_SpatialMeta.setSensor("MS");
		    	}
		    	else if(MainFileName.endsWith("PAN"))
		    	{
		    		m_SpatialMeta.setSensor("PAN");
		    	}
		    	
		    }
		    // GF1_E1186N408_20161031_018946_20_P
		    // GF1_E1191N422_20161031_018946_20_M
		    else if(MainFileName.matches("GF[1-2]_E[0-9]{4}N[0-9]{3}_[0-9]{8}_[0-9]{6}_[0-9]{2}_(P|M)"))
		    {
		    	satellite= fields[0];
		    	acquisitionTime= fields[2];
		    	if(MainFileName.endsWith("M"))
		    	{
		    		m_SpatialMeta.setSensor("MUX");
		    	}
		    	else if(MainFileName.endsWith("P"))
		    	{
		    		m_SpatialMeta.setSensor("PAN");
		    	}
		    }
		    //  TH_014142_20160318_029277_19_M
		    // ZY3_877120_20160508_024045_21_P
		   // ZY301_022144_20161207_027283_18_M
		    else if(MainFileName.matches("(TH|ZY3|ZY301|ZY302)_[0-9]{6}_[0-9]{8}_[0-9]{6}_[0-9]{2}_(P|M)"))
		    {
		    	satellite= fields[0];
		    	acquisitionTime=fields[2];
		    	if(MainFileName.endsWith("M"))
		    	{
		    		m_SpatialMeta.setSensor("MUX");
		    	}
		    	else if(MainFileName.endsWith("P"))
		    	{
		    		m_SpatialMeta.setSensor("PAN");
		    	}
		    }
		    else {
		    	myLogger.info("filename is not match regex");
		    	return null;

		   }
		
		// 当前系统时间就是归档时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(acquisitionTime);

		m_SpatialMeta.setArchiveTime(new Date());
		m_SpatialMeta.setProductid(dataid);
		m_SpatialMeta.setDataid(dataid);
		m_SpatialMeta.setAcquisitionTime(date);
		m_SpatialMeta.setFileName(MainFileName); // 写入文件名
		m_SpatialMeta.setFilePath(filePath); // 写入文件路径
		m_SpatialMeta.setSatellite(satellite); // 写入卫星
		m_SpatialMeta.setDataType(RasterFormat); // 写入影像类型
		if (imageFile.exists() && imageFile.isFile()) {
			int fileSize = (int) (imageFile.length() / 1024 / 1024);//
			m_SpatialMeta.setFileSize(fileSize);// 写入文件大小
		} else {
			myLogger.info("file doesn't exist or is not a file");
		}

		gdal.AllRegister();
		// 读取图像
		Dataset hDataset = gdal.Open(strTifFile, gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return m_SpatialMeta;

		String crsString = hDataset.GetProjectionRef();// 获取crs
		SpatialReference Crs = new SpatialReference(crsString);// 构造投影坐标系统的空间参考(wkt)，

		SpatialReference oLatLong;
		oLatLong = Crs.CloneGeogCS(); // 获取该投影坐标系统中的地理坐标系

		// 构造一个从投影坐标系统到地理坐标系统的转换关系
		CoordinateTransformation ct = new CoordinateTransformation(Crs, oLatLong);

		int xSize = hDataset.GetRasterXSize();// 图像宽度
		int ySize = hDataset.GetRasterYSize();// 图像高度

		double[] geoTransform = hDataset.GetGeoTransform();

		// 图像范围
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		m_SpatialMeta.setExtentRight(xmax);
		m_SpatialMeta.setExtentLeft(xmin);
		m_SpatialMeta.setExtentTop(ymax);
		m_SpatialMeta.setExtentBottom(ymin);

		 //ct.TransformPoint();投影转换为经纬度
  		double a[]= ct.TransformPoint(xmin, ymax) ;
  		double bb[]= ct.TransformPoint(xmax, ymax) ;
  		double c[]= ct.TransformPoint(xmax, ymin) ;
  		double d[]= ct.TransformPoint(xmin, ymin) ;     
		
        // 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
      //  DecimalFormat df = new DecimalFormat( "0.00000"); //限定小数位数
        String wkt = "GEOMFROMTEXT('polygon((";
		// 平面投影坐标转为经纬度
		wkt += a[0]+" "+a[1] + ",";
		wkt += bb[0]+" "+bb[1] + ",";
		wkt += c[0]+" "+c[1] + ",";
		wkt += d[0]+" "+d[1] + ",";      		
		wkt += a[0]+" "+a[1] + "))')";
		m_SpatialMeta.setWktString(wkt);

		hDataset.delete();
		return m_SpatialMeta;
	}
}
