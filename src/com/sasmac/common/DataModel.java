package com.sasmac.common;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sasmac.dbconnpool.ConnPoolUtil;

/**
 * 建立路径
 * 
 * @author user
 * @Version 版本
 * @ModifiedBy
 * @date 2018年4月18日上午11:00:35
 */
public class DataModel {

	// 更新所有角色列表
	public static String GetProductTabName(String strProductType) {

		java.sql.Connection conn = ConnPoolUtil.getConnection();
		if (conn == null)
			return "";

		String sql = "select tablename from  tb_metamanager where ProductType='" + strProductType + "'";
		String TableName = "";
		try {
			PreparedStatement psmt = conn.prepareStatement(sql);//
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				TableName = rs.getString(1);
			}
			psmt.close();
			rs.close();

			ConnPoolUtil.close(conn, null, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TableName;
	}

	/**
	 * 产生快视图的相对路径
	 * 
	 * @param strProductType
	 * @param Filename
	 * @return
	 */
	public static String generateoverviewpath(String strProductType, String Filename) {
		if (Filename.isEmpty() || strProductType.isEmpty())
			return "";
		String path = "";
		if (strProductType.compareToIgnoreCase("SC") == 0) {
			if (Filename.startsWith("GF") == true) {
				String fields[] = Filename.split("_");
				path += "SC" + File.separator + fields[0] + File.separator + fields[4] + File.separator + fields[1];
			} else if (Filename.startsWith("zy30") == true) {
				String fields[] = Filename.split("_");
				path += "SC" + File.separator + fields[0].substring(0, 4) + File.separator + fields[4].substring(0, 7)
						+ File.separator + fields[1];
			} else
				return "";
		} else if (strProductType.compareToIgnoreCase("分景DOM") == 0) {
			String fields[] = Filename.split("_");
			// System.out.println(fields.length);
			if (fields.length >= 6) // GF1_PMS1_E115.8_N32.8_20171219_L1A0002861241-PAN1_ORTHO_PAN
			{
				path += "ScenceDOM" + File.separator;
				path += fields[0] + File.separator + fields[4];
			} else {
				return "";
			}
		} else if (strProductType.compareToIgnoreCase("分幅DOM") == 0) {
			if (Filename.length()!=10)
				return "";
			path += "FrameDOM" + File.separator;
			path += Filename.substring(0, 3);
		} else
			return "";

		return path;

	}
}
