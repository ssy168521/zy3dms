package com.sasmac.meta;

import java.sql.Connection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.sasmac.common.DataModel;

public class SceneDOMSpatialMeta extends SpatialMeta {

	private Logger myLogger = LogManager.getLogger("mylog");

	@Override
	public boolean insertmeta(Connection conn) throws Exception {

		String tablename = DataModel.GetProductTabName("分景DOM");
		QueryRunner qr = new QueryRunner();
		// 执行数据插入操作
		String sql = "insert into " + tablename;
		String storagefile = getFilePath();
		storagefile = storagefile.replace("\\", "\\\\");

		sql += "(fid,dataid,FileName,FilePath,FileSize,satellite,sensor,acquisitionTime,archiveTime,"
				+ "productid,productLevel,DataType,ExtentTop,ExtentLeft," + "ExtentBottom,ExtentRight,Shape)"
				+ " values(";
		sql += null + ",";
		sql += getDataid() + ",";

		sql += "'" + getFileName() + "',";
		sql += "'" + storagefile + "',";
		sql += getFileSize() + ",";
		sql += "'" + getSatellite() + "',";
		sql += "'" + getSensor() + "',";
		Date a = getAcquisitionTime();
		if (a != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = formatter.format(a);
			sql += "'" + str + "',";

		} else {
			sql += "null,";
		}
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = formatter.format(getArchiveTime());
		sql += "'" + str + "',";

		sql += getProductid() + ",";
		sql += getProductLevel() + ",";

		sql += "'" + getDataType() + "',";
		sql += "'" + getExtentTop() + "',";
		sql += "'" + getExtentLeft() + "',";
		sql += "'" + getExtentBottom() + "',";
		sql += "'" + getExtentRight() + "',";
		sql += getWktString();
		sql += ");";
		try {
			Statement stmt = conn.createStatement();
			boolean b1 = stmt.execute(sql);
			// boolean b2 = stmt.execute(sql2);
			if (!b1) {
				myLogger.info("geotiff元数据导入数据库成功！");
			} else {
				myLogger.info("geotiff元数据导入数据库出错！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return true;
	}
}
