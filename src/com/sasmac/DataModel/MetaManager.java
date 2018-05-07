package com.sasmac.DataModel;
import com.sasmac.dbconnpool.ConnPoolUtil;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;

import com.web.util.DataUtils;
public class MetaManager {
	private Logger myLogger = LogManager.getLogger("mylog");
	public  String  getTableNameByProductName(String ProductName)
	{
		Connection conn=ConnPoolUtil.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String tabname="";
		if(ProductName.startsWith("\"") && ProductName.endsWith("\""))
		{
			ProductName=ProductName.substring(1,ProductName.length()-1);
		}
		String sql = "SELECT tablename  FROM tb_metamanager WHERE ProductType =\""+ProductName+"\"";
		try
		{
			stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if (result.next()) {
				 tabname=result.getString(1);
				ConnPoolUtil.close(conn, null, null);
				stmt.close();
			    return  tabname;
			} 	
		}
		catch (Exception e) {
			e.printStackTrace();
			return tabname;
		}
		return tabname;
	}
	public  boolean InsertMetaManangerInfo(String tablename, String Satellite,String sensor,String ProductLevel,String ProductType)
	{
		Connection conn=ConnPoolUtil.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name =\"" + tablename + "\" or ProductType=\""+ProductType+"\"";
		try
		{
			stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if (result.next()) {
				myLogger.info("dataset is exists,can't was created！ "+tablename+"or"+ProductType+" is duplicated");
				ConnPoolUtil.close(conn, null, null);
				stmt.close();
			    return false;
			} 	
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		sql = "insert into  tb_metamanager" ;
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
		  ConnPoolUtil.close(conn, null, null);
		  if (ret > 0)
				return true;
			return false;
		} 
		catch (Exception e) {
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
