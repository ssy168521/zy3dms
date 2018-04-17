package com.web.thread;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.common.DataModel;
import com.sasmac.jni.Gdal_resample;
import com.sasmac.jni.ImageProduce;
import com.sasmac.meta.Meta2Database;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
import com.web.util.FileUtil;
import com.web.util.PropertiesUtil;
/**
 * 分景产品扫描归档
 * @author Administrator
 * @ClassName:SviScanArchiveThread
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月29日 下午3:14:14
 */
public class ArchiveThread extends BaseThread implements Runnable {
	private Logger myLogger = LogManager.getLogger("mylog");
	private int iCurridx = 0;// 当前归档的充号
	private String archivePath;
	String ProductionType ;
	int ArchiveMode; 

	private WebService service = null;

	private Connection conn = null;
	/**文件数量 **/
	private int fileCount = 0;
	int bIsSMBfile;;

	/**
	 * 归档线程
	 * 
	 * @param conf
	 */
	public ArchiveThread(String strArchivedir) {
        //调用父类BaseThread的构造方法
		super("archive", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据归档",
				new Date(), null, 0, "");

		archivePath = strArchivedir;
		service = new WebServiceImpl();
		try {
			bIsSMBfile = Constants.AssertFileIsSMBFileDir(strArchivedir);//判断属于哪种归档形式
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* ProductionType 产品类型
	 strArchivedir 归档源路径
	 ArchiveMode 归档方式 0为扫描归档=，1为迁移归档*/
	public ArchiveThread(String strProductionType,String strArchivedir,int iArchiveMode) {
	       //调用父类BaseThread的构造方法
		super("archive", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据归档",
					new Date(), null, 0, "");
		 if(ProductionType.compareToIgnoreCase("分幅DOM")!=0 && ProductionType.compareToIgnoreCase("分景DOM")!=0 )
         {
      	    return;
         }
		archivePath = strArchivedir;
		ProductionType=strProductionType ;
		ArchiveMode = iArchiveMode; 

		service = new WebServiceImpl();
		try {
			bIsSMBfile = Constants.AssertFileIsSMBFileDir(strArchivedir);//判断属于哪种归档形式
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArchiveThread() {
	}

	@Override
	public void run() {   //多线程
		setTaskStartTime(new Date());

		myLogger.info("start archive:" + archivePath);
		try {
			conn = DbUtils.getConnection(true);
			if (bIsSMBfile == 0) {
				java.io.File rootPath = new java.io.File(archivePath);
				if (!rootPath.exists() || rootPath.isFile()) {
					myLogger.info("归档路径不存在！");
					return;
				}
				calcFileCount(archivePath);

				myLogger.info("All archive files count: "
						+ Integer.toString(fileCount));

				ergodicDir(rootPath);

			} else {

				myLogger.info("请检查是否是本地归档！");

			}

			if (!bStopThread) {
				myLogger.info("finish archive " + archivePath);
				setTaskStatus(THREADSTATUS.THREAD_STATUS_FINISHED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(archivePath + " " + Integer.toString(fileCount)
						+ " files archive");
				setTaskProgress(100);
				PrintTaskInfo(conn);   //将归档信息写入数据库
				FinishThread();        //结束归档线程
			} else {
				myLogger.info(archivePath + " :archive task is stoped");
				setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(archivePath + ": archive task is stoped");
				PrintTaskInfo(conn);
				StopThread();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}

	}

	/**
	 * 本机归档 
	 * @param myPath
	 * @throws Exception
	 */
	private void ergodicDir(java.io.File myPath) throws Exception{

		setTaskStatus(THREADSTATUS.THREAD_STATUS_RUNNING.ordinal());
		java.io.File[] aF = myPath.listFiles();
		String filename = "";
		for (java.io.File fF : aF) 
		{
			if (bStopThread) {
				setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
				setTaskEndTime(new Date());
				myLogger.info(" archive  task is Stoped");
				int iProgress = (int) ((double) iCurridx
						/ (double) fileCount * 100);
				setTaskProgress(iProgress);
				break;// 如果中断线程
			}
			if (fF.isDirectory()) {
				ergodicDir(fF.getAbsoluteFile());
				continue;
			}
			iCurridx++;
			
             //无后缀名的文件名
			filename = fF.getName().substring(0, fF.getName().lastIndexOf("."));
			myLogger.info("start archive " + Integer.toString(iCurridx)
					+ " file");
			String tiffpath = myPath.getPath() + File.separator
					+ fF.getName();
			String satellite = "";
			if(ProductionType.compareToIgnoreCase("分景DOM")==0)
            {
				int idx = filename.indexOf("_");
				String prefixname=filename.substring(0,idx);
				if (prefixname.compareToIgnoreCase("GF1")==0){
					satellite = "GF1";
				}else if(prefixname.compareToIgnoreCase("GF2")==0){
					satellite = "GF2";
				}else if(prefixname.compareToIgnoreCase("TH")==0){
					satellite = "TH";
				}else if(prefixname.compareToIgnoreCase("ZY301")==0){
					satellite = "ZY301";
				}else if(prefixname.compareToIgnoreCase("ZY302")==0){
					satellite = "ZY302";
				}
				else if(prefixname.compareToIgnoreCase("ZY3")==0){
					satellite = "ZY3";
				}else{
					myLogger.info("file format is not support: " + filename);
					continue;
				}
				String tablename = DataModel.GetProductTabName(ProductionType);
				 tiffpath = fF.getName();
				if (tiffpath.substring(tiffpath.lastIndexOf(".")).equalsIgnoreCase(".tif")) {
					Meta2Database mdb = new Meta2Database();
					mdb.tif2Db(satellite,filename,tablename,tiffpath);
				} else {					
					myLogger.info("文件格式不对,继续！");
				}
				

           }
           else if(ProductionType.compareToIgnoreCase("分幅DOM")==0)
           {
        	    // 正则表达式比较 J46D001001
				boolean bmatch = filename.matches("J[0-9]{2}D[0-9]{3}[0-9]{3}"); // D代表1：10万分幅比例尺
				if (bmatch == false) {
					myLogger.info("file format is not support: " + filename);
					continue;
				};
				
				String tablename = DataModel.GetProductTabName(ProductionType);
				 
				if (tiffpath.substring(tiffpath.lastIndexOf(".")).equalsIgnoreCase(".tif")) {
					Meta2Database mdb = new Meta2Database();
					mdb.tifToDb(filename,tablename,tiffpath);
				} else {					
					myLogger.info("文件格式不对,继续！");
				}
           }

			//表名
			
			     //所有标准分幅产品信息都存于此表
  /*         // String productName = "分景产品";  //产品名字
			if (service.isFileArchive(conn, tablename, filename)) {
				myLogger.info("file had exist database:" + filename);
				continue;
			}
			if (!isFinishCopy(fF.getAbsoluteFile()))
				continue;

				//geotiff文件全路径
				String tiffpath = myPath.getPath() + File.separator
						+ fF.getName();
				*/
								// tif重采样
				if (tiffpath.substring(tiffpath.lastIndexOf("."))
						.equalsIgnoreCase(".tif")) {
					// tif图像原路径
					String tifpath = myPath.getPath() + fF.separator
							+ fF.getName();
					//String[] ss=filename.split("_");
					//String StoragePath = ss[0]+"\\"+ss[1]+"\\"+ss[4];
					String path = Constants.class.getClassLoader()
							.getResource("/").toURI().getPath();
					PropertiesUtil propertiesUtil = new PropertiesUtil(path
							+ Constants.STR_CONF_PATH);
					// 快视图路径
					String OverviewStoragePath = propertiesUtil
							.getProperty("overviewfilepath");

					myLogger.info(" start ImageRectify overiew-png: "
							+ filename + ".png");
					ImageProduce imgprodu = new ImageProduce();
					boolean res = false;
					
					String RelativePath=DataModel.generateoverviewpath(ProductionType, filename);
					
					String destpath = OverviewStoragePath + RelativePath;
					// String destpath1 = OverviewStoragePath + StoragePath;
					File dest = new File(destpath);
					if (!dest.exists()) {
						dest.mkdirs();//创建此抽象路径名指定的目录，包括所有必需但不存在的父目录
					}
					// 图像重采样
					/*res = imgprodu.ImageRectify(tifpath, destpath
							+ File.separator + filename + ".png", 256, 256);*/

					boolean res1=false;
					res1 = imgprodu.ImageRectify(tifpath, destpath
							+ File.separator + filename + ".png", 256, 256);
					if(!res1){
						myLogger.info("第一次重采样失败！快视图建立错误！");
					}else{
						myLogger.info("第一次重采样成功！开始判断是否重投影！");
						boolean res2=false;
						//判断是否能进行重投影
						res2 = Gdal_resample.Resample( destpath+ File.separator + filename + ".png",
								destpath+ File.separator+"temp.png");
						if(!res2){
							myLogger.info("无法进行重投影！快视图生成结束！");

						}else{
							myLogger.info("需要重投影！开始投影！");
							//先删除第一次重采样图片
							File tempResmple = new File(destpath+ File.separator + filename + ".png");
							tempResmple.delete();
							//重采样后进行投影变换，再次重采样，将投影变换的输出文件作为重采样的输入文件
							res = imgprodu.ImageRectify(destpath+ File.separator+"temp.png",
									destpath+ File.separator + filename + ".png",256,256);
							
							if (!res) {
								myLogger.info(filename + " :png overview build error !");
							} else {
								File tempfile = new File(destpath+ File.separator+"temp.png");
								tempfile.delete();   //删除临时的重投影文件
								myLogger.info("finish ImageRectify overiew-png: "
										+ filename + ".png");
							}
						}
					}
                                 //************
				}
				if(ArchiveMode==1) //迁移归档
                { 
					int ret = FileUtil.mappedBuffer("","");
					if(ret == 0)
					{
						myLogger.info(filename+"文件实体迁移失败！");
					}
                }
			    myLogger.info("finish archive "
					+ Integer.toString(iCurridx + 1) + " file");

			   int iProgress = (int) ((double) iCurridx / (double) fileCount * 100);
			   setTaskProgress(iProgress);
		}
	}

	/**
	 * 文件是否拷贝中 
	 * @param fileName
	 * @return
	 */
	private boolean isFinishCopy(java.io.File fileName) {
		/** 绛夊埌鏂囦欢鎷疯礉瀹屾垚鍐嶈繘琛屽睘鎬ц幏鍙栧強鍒ゆ柇绛夋搷浣� */
		boolean bFlag = false;
		java.io.RandomAccessFile raf = null;
		try {
			raf = new java.io.RandomAccessFile(fileName, "rw");
			raf.seek(fileName.length());
			bFlag = true;
		} catch (Exception e) {
			System.out.println("");
			myLogger.error(e);
		} finally {
			try {
				raf.close();
			} catch (Exception e) {
				e.printStackTrace();
				myLogger.error(e);
			}
		}

		return bFlag;
	}

	/**
	 * 计算文件数量（本地归档）
	 * @param strPath
	 * @throws Exception
	 */
	private void calcFileCount(String strPath) throws Exception {
		if (bIsSMBfile == 0) {
			java.io.File myPath = new File(strPath);
			java.io.File[] aF = myPath.listFiles();
			for (java.io.File fF : aF) {
				if (fF.isDirectory()) {    //若是目录返回true
					calcFileCount(fF.getPath());
				} else {
					fileCount++;
				}
			}
		} else {
			myLogger.info("归档文件不存在！");
		}
	}

}
