package com.sasmac.meta;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.NameImpl;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import com.web.util.PropertiesUtil;
/**
 *geotools读取shapefile文件到数据库中，单纯的java读取shp文件会出现错误，对于Oracle有相应的插件读取shp文件。
 *主要步骤：1.将shp读取到要素库featureSource
 *2.连接数据库（不限于MySQL，Oracle也行）
 *3.itertor迭代器遍历写入数据库。
 *注意：sds字符设置为GBK，UTF-8可能无法识别中文。若shp中含有中文字段，则会插入失败。
 */
public class SeamLineMetaParser {
	private Logger myLogger = LogManager.getLogger("mylog");
	public boolean createFeatures(String Shpfile) {
		boolean res=false;
		long startTime = System.currentTimeMillis(); //程序开始记录时间
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		File f=new File(Shpfile)
		//URL url=f.toURI().toURL();
		try {
			Shpfile = java.net.URLDecoder.decode(Shpfile,"utf-8");
			ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory
					.createDataStore();  //toURI()转换非法字符
			sds.setCharset(Charset.forName("GBK"));
			SimpleFeatureSource featureSource = sds.getFeatureSource();

			SimpleFeatureType schema = featureSource.getSchema();

			//JDBCDataStore pgDatastore;
			MySQLDataStoreFactory factory1 = new MySQLDataStoreFactory();
			String conffilepath ="";
			
				 conffilepath = SeamLineMetaParser.class.getClassLoader().getResource("").toURI().getPath();
	
			conffilepath += "com/sasmac/conf/dbConnConf.properties";
			PropertiesUtil util = new PropertiesUtil(conffilepath);
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("dbtype", util.getProperty("dbtype"));
			params2.put("host", util.getProperty("host"));
			params2.put("port", util.getProperty("port"));
			params2.put("database", util.getProperty("database"));
			params2.put("user", util.getProperty("user"));
			params2.put("passwd", util.getProperty("password"));
			JDBCDataStore ds;
			try {
				ds = (JDBCDataStore) factory1.createDataStore(params2);
				ContentEntry entry = ds.getEntry(new NameImpl(null, schema
						.getTypeName()));
				if (entry != null) {
					ds.removeSchema(schema.getTypeName().toLowerCase());
				}
				
				ds.createSchema(schema);//
				myLogger.info(" create table success!");
				//
				FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
						.getFeatureWriter(schema.getTypeName().toLowerCase(),
								Transaction.AUTO_COMMIT);

				SimpleFeatureIterator itertor = featureSource.getFeatures()
						.features();

				while (itertor.hasNext()) {
					 // copy the contents of each feature and transform the geometry
					SimpleFeature feature = itertor.next();				
					writer.hasNext();
					SimpleFeature feature1 = writer.next();		
					feature1.setAttributes(feature.getAttributes());
					writer.write();
				}
				itertor.close();

				writer.close();
				ds.dispose();
                res=true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime   = System.currentTimeMillis(); //程序结束记录时间
		long TotalTime = endTime - startTime;       //总消耗时间
		myLogger.info("导入数据库用时："+TotalTime);
		return res;
	}

	public static void main(String[] args) throws Exception {
      //shapefile文件名必须要是小写英文
		//createFeatures("E:\\database\\15lineresult\\national_xqx_fn.shp");
	}

}

