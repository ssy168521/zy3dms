package com.sasmac.meta;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

		String filePath = imageFile.getParent(); // .replace("\\", "\\\\");

		String RasterFormat = strTifFile.substring(strTifFile.lastIndexOf(".") + 1);
		// split切分字符串，循环求出第三个各字符串即为日期
		String[] ss = filename.split("_");
		String acquisitionTime = ss[2]; // 产品制作时间
		long dataid = System.currentTimeMillis();

		// 当前系统时间就是归档时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(acquisitionTime);

		m_SpatialMeta.setArchiveTime(new Date());
		m_SpatialMeta.setProductid(dataid);
		m_SpatialMeta.setDataid(dataid);
		m_SpatialMeta.setAcquisitionTime(date);
		m_SpatialMeta.setFileName(filename); // 写入文件名
		m_SpatialMeta.setFilePath(filePath); // 写入文件路径
		m_SpatialMeta.setSatellite(ss[0]); // 写入卫星
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
		m_SpatialMeta.setExtentBottom(ymax);

		// ct.TransformPoint()，投影坐标转地理坐标
		double a[] = ct.TransformPoint(xmin, ymax);
		double b[] = ct.TransformPoint(xmax, ymax);
		double c[] = ct.TransformPoint(xmax, ymin);
		double d[] = ct.TransformPoint(xmin, ymin);

		// 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00000"); // 限定小数位数
		String wkt = "GEOMFROMTEXT('polygon((";
		// 平面投影坐标转为经纬度
		wkt += df.format(a[0]) + " " + df.format(a[1]) + ",";
		wkt += df.format(b[0]) + " " + df.format(b[1]) + ",";
		wkt += df.format(c[0]) + " " + df.format(c[1]) + ",";
		wkt += df.format(d[0]) + " " + df.format(d[1]) + ",";
		wkt += df.format(a[0]) + " " + df.format(a[1]) + "))')";
		m_SpatialMeta.setWktString(wkt);

		hDataset.delete();
		return m_SpatialMeta;
	}
}
