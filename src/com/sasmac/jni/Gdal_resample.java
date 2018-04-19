package com.sasmac.jni;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * 影像投影转换并重采样得到输出影像（椭球基准面是WGS84，也就是GPS所用的基准面）
 * 
 * @author Administrator
 * @ClassName:Gdal_resample2
 * @date 2018年4月16日 下午2:26:02
 */
public class Gdal_resample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean resu = Resample("E:\\database\\overview\\J46D001001.png", "E:\\database\\overview\\J46D001001_03.png");
		System.out.println(resu);
	}

	/**
	 * 影像投影转换并重采样得到输出影像，同一个椭球体基准面下的转换就是严密的转换
	 * 
	 * @param input
	 *            输入影像路径
	 * @param output
	 *            输出影像路径
	 */
	public static boolean Resample(String input, String output) {
		// String agentfile=output.substring(0, output.lastIndexOf("."))+".tif";
		// 注册GDAL
		gdal.AllRegister();
		// 只读方式读取数据
		Dataset pSrc = gdal.Open(input, gdalconstConstants.GA_ReadOnly);
		if (pSrc == null)
			return false;
		// 打开的影像像素、波段等信息
		int numBands = pSrc.GetRasterCount(); // 读取影像波段数
		int xSize = pSrc.GetRasterXSize();// 栅格尺寸
		int ySize = pSrc.GetRasterYSize();//
		int depth = pSrc.GetRasterBand(1).GetRasterDataType();// 图像深度

		String src_wkt = pSrc.GetProjectionRef();// 获取源图像crs

		SpatialReference src_Crs = new SpatialReference(src_wkt);// 构造投影坐标系统的空间参考(wkt)

		double[] geoTransform = pSrc.GetGeoTransform();
		// 图像范围
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double w_src = geoTransform[1];// 像素宽度
		double h_src = geoTransform[5];// 像素高度
		double xmax = geoTransform[0] + xSize * w_src;
		double ymin = geoTransform[3] + ySize * h_src;

		// 设置输出图像的坐标
		SpatialReference oLatLong;
		oLatLong = src_Crs.CloneGeogCS(); // 获取该投影坐标系统中的地理坐标系
		// 构造一个从投影坐标系到地理坐标系的转换关系
		CoordinateTransformation ct = new CoordinateTransformation(src_Crs, oLatLong);

		// 此注释为错误计算过程
		// double dst1[]=ct.TransformPoint(xmin,ymax);
		// double dst2[]=ct.TransformPoint(xmax,ymin);
		// double dstxmin = dst1[0];
		// double dstymax = dst1[1];
		// double dstxmax = dst2[0];
		// double dstymin = dst2[1]; //39.67609572370327

		// 计算目标影像的左上和右下坐标,即目标影像的仿射变换参数,投影转换为经纬度
		double a[] = ct.TransformPoint(xmin, ymax);
		double b[] = ct.TransformPoint(xmax, ymax);
		double c[] = ct.TransformPoint(xmax, ymin);
		double d[] = ct.TransformPoint(xmin, ymin);
		double dbX[] = { a[0], b[0], c[0], d[0] };
		double dbY[] = { a[1], b[1], c[1], d[1] };

		Gdal_resample test = new Gdal_resample();
		test.res(dbX);// 按从小到大排列
		double dstxmin = dbX[0];
		double dstxmax = dbX[3];
		test.res(dbY);
		double dstymin = dbY[0];
		double dstymax = a[1];
		double[] adfGeoTransform = new double[6];
		adfGeoTransform[0] = dstxmin;
		adfGeoTransform[3] = dstymin;
		adfGeoTransform[1] = (dbX[3] - dbX[0]) / xSize;
		adfGeoTransform[5] = (dbY[3] - dbY[0]) / ySize;
		// adfGeoTransform[2]=(dstxmax-adfGeoTransform[0]-xSize*adfGeoTransform[1])/ySize;
		// adfGeoTransform[4]=(dbY[3]-adfGeoTransform[3]-ySize*adfGeoTransform[5])/xSize;

		// 创建输出图像
		Driver driver = gdal.GetDriverByName("GTiff");
		driver.Register();
		String[] options = { "INTERLEAVE=PIXEL" };
		Dataset pDst = driver.Create(output, xSize, ySize, numBands, depth, options);

		// 写入仿射变换系数及投影
		String dst_wkt = oLatLong.ExportToWkt();
		pDst.SetGeoTransform(adfGeoTransform);
		pDst.SetProjection(dst_wkt);

		/*
		 * eResampleAlg采样模式 GRA_NearestNeighbour=0
		 * 最近邻法，算法简单并能保持原光谱信息不变；缺点是几何精度差，灰度不连续，边缘会出现锯齿状 GRA_Bilinear=1
		 * 双线性法，计算简单，图像灰度具有连续性且采样精度比较精确；缺点是会丧失细节； GRA_Cubic=2
		 * 三次卷积法，计算量大，图像灰度具有连续性且采样精度高； GRA_CubicSpline=3 三次样条法，灰度连续性和采样精度最佳；
		 * GRA_Lanczos=4 分块兰索斯法，由匈牙利数学家、物理学家兰索斯法创立，实验发现效果和双线性接近；
		 */
		// 重新投影
		gdal.ReprojectImage(pSrc, pDst, src_wkt, dst_wkt, 3);

		pDst.delete();
		pSrc.delete();
		return true;
	}

	/**
	 * 比较大小，得到最大值与最小值
	 * 
	 * @param arr
	 * @return
	 */
	public double[] res(double[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length - i - 1; j++) {

				if (arr[j] > arr[j + 1]) {
					double temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
		return arr;
	}
}