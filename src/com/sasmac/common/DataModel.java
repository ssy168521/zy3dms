package com.sasmac.common;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

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
	//private Logger myLogger = LogManager.getLogger("mylog");
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
			    // zy302a_nad_007831_895146_20171027111543_01_sec_0001_1710306797_ORTHO_PAN
			    // zy301a_mux_029533_016146_20170504110226_01_sec_0004_1705056054_ORTHO_MS
			    
			    //  TH_014142_20160318_029277_19_M
			    // ZY3_877120_20160508_024045_21_P
			    if(Filename.matches("zy30[1-2]a_(nad|mux)_[0-9]{6}_[0-9]{6}_[0-9]{14}_01_sec_[0-9]{4}_[0-9]{10}_ORTHO_(PAN|MS)"))
			    {
			    	path += "ScenceDOM" + File.separator;
			    	path += fields[0].substring(0,5) + File.separator + fields[4].substring(0,8);
			    }
			    // GF2_PMS1_E109.3_N31.0_20170620_L1A0002430842-MSS1_ORTHO_MS
			    else if(Filename.matches("GF[1-2]_PMS[1-2]_E[0-9]{3}.[0-9]_N[0-9]{2}.[0-9]_[0-9]{8}_L1A[0-9]{10}-(MSS|PAN)[1-2]_.*"))
			    {
			    	path += "ScenceDOM" + File.separator;
			    	path += fields[0] + File.separator + fields[4];
			    }
			    // GF1_E1186N408_20161031_018946_20_P
			    // GF1_E1191N422_20161031_018946_20_M
			    else if(Filename.matches("GF[1-2]_E[0-9]{4}N[0-9]{3}_[0-9]{8}_[0-9]{6}_[0-9]{2}_(P|M)"))
			    {
			    	path += "ScenceDOM" + File.separator;
			    	path += fields[0] + File.separator + fields[2];
			    }

			    //  TH_014142_20160318_029277_19_M
			    // ZY3_877120_20160508_024045_21_P
			   // ZY301_022144_20161207_027283_18_M
			    else if(Filename.matches("(TH|ZY3|ZY301|ZY302)_[0-9]{6}_[0-9]{8}_[0-9]{6}_[0-9]{2}_(P|M)"))
			    {
			    	path += "ScenceDOM" + File.separator;
			    	path += fields[0] + File.separator + fields[2];
			    }
			    else {
				  return "";
			   }
		} else if (strProductType.compareToIgnoreCase("分幅DOM") == 0) {
			if (Filename.length()!=10)
				return "";
			path += "FrameDOM" + File.separator;
			path += Filename.substring(0, 3);
		}else if (strProductType.compareToIgnoreCase("镶嵌线") == 0) {
			if (Filename.length()!=10)
				return "";
			path += "SeamLine" + File.separator;
			path += Filename;
		} else
			return "";

		return path;

	}
}
