package com.sasmac.DataModel;
import com.sasmac.dbconnpool.ConnPoolUtil;
import java.sql.Connection;
import com.web.util.DataUtils;
public class MetaManager {
	public static boolean InsertMetaManangerInfo(String tablename, String Satellite,String sensor,String ProductLevel,String ProductType)
	{
		Connection conn=ConnPoolUtil.getConnection();
		String sql = "insert into  tb_metamanager" ;
		sql += "(tablename, satellite,sensor,productLevel,ProductType)"
				+ " values(";
		sql += "'" + tablename + "',";
		sql += "'" + Satellite + "',";
		sql += "'" + sensor + "',";
		sql += "'" + ProductLevel + "',";
		sql += "'" + ProductType + "'";
		sql += ")";
		try {
		  DataUtils datautils = new DataUtils(conn);
		  int ret = datautils.update(sql);
		  if (ret > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean DelDataProduct(String ProductType)
	{
		Connection conn=ConnPoolUtil.getConnection();
		String sql = " delete from tb_metamanager where (ProductType=" ;
		sql += "'" + ProductType + "'";
		sql += ")";
		try {
		  DataUtils datautils = new DataUtils(conn);
		  int ret = datautils.update(sql);
		  if (ret > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
