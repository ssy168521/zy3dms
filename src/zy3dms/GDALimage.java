package zy3dms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import com.sasmac.dbconnpool.ConnPoolUtil;

/**
 * 获取快视图范围
 * 
 * @author user
 * @Version 版本
 * @ModifiedBy
 * @date 2018年4月18日下午2:32:02
 */
public class GDALimage {

	public GDALimage() {

	}

	public static void main(String[] args) {
		// GetImageExtent("E:\\database\\test\\J46D001001.png");
	}

	/**
	 * gdal获取范围
	 * 
	 * @param strImageUrl
	 * @return
	 */
	// 初始点坐标（geomatrix[0]、geomatrix[3])
	// 旋转角度（geomatrix[2]、geomatrix[4])、像素分辨率（geomatrix[1]、geomatrix[5])
	public static String GetImageExtent(String strImageUrl) {
		String strExtent = "";
		gdal.AllRegister();
		Dataset hDataset = gdal.Open(strImageUrl, gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return "";
		int xSize = hDataset.GetRasterXSize();
		int ySize = hDataset.GetRasterYSize();
		double[] geoTransform = hDataset.GetGeoTransform();

		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		// 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
		// spatialReference要根据图像所具有的投影信息,若得到的是投影坐标，则去掉空间参考。经纬度是地理坐标，故需投影。
		strExtent = "{'xmin':" + xmin + ",'ymin':" + ymin + ",'xmax':" + xmax + ",'ymax':" + ymax
				+ ",'spatialReference':{'wkid':4326}}";

		hDataset.delete();
		// System.out.println(strExtent);
		return strExtent;
	}

	/**
	 * 从数据库读取,根据产品表判断读取的数据库表
	 * 
	 * @param dataid
	 * @return
	 */
	public static String GetExtent(String dataid, String tablename) {
		String strExtent = "";
		String Shape = "";
		Connection conn = null;
		Statement stmt = null;
		try {
			// 连接池
			conn = ConnPoolUtil.getConnection();
			stmt = conn.createStatement();
			// AsText()函数将存储的几何对象格式化成字符串
			String sql = "select AsText(Shape) from testdb." + tablename + " where dataid= \"" + dataid + "\";";
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				Shape = result.getString("AsText(Shape)");
				Shape = Shape.substring(Shape.lastIndexOf("(") + 1, Shape.lastIndexOf(","));
			}
			String[] pointstr = Shape.split(",");
			String[] pta = pointstr[0].split(" ");
			String[] ptb = pointstr[1].split(" ");
			String[] ptc = pointstr[2].split(" ");
			String[] ptd = pointstr[3].split(" ");
			// 读出的x值数组
			double dbX[] = { Double.parseDouble(pta[0]), Double.parseDouble(ptb[0]), Double.parseDouble(ptc[0]),
					Double.parseDouble(ptd[0]) };

			// 读出的Y数组
			double dbY[] = { Double.parseDouble(pta[1]), Double.parseDouble(ptb[1]), Double.parseDouble(ptc[1]),
					Double.parseDouble(ptd[1]) };

			GDALimage test = new GDALimage();
			test.res(dbX);// 按从小到大排列
			double Xmin = dbX[0];
			double Xmax = dbX[3];
			test.res(dbY);
			double Ymin = dbY[0];
			double Ymax = dbY[3];

			// 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
			// 经纬度是地理坐标，故需投影。
			strExtent = "{'xmin':" + Xmin + ",'ymin':" + Ymin + ",'xmax':" + Xmax + ",'ymax':" + Ymax
					+ ",'spatialReference':{'wkid':4326}}";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnPoolUtil.close(conn, null, null);
		}

		return strExtent;
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
