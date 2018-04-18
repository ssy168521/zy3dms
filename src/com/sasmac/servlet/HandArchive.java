package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.common.ThreadManager;
import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;
import com.web.thread.MyHmArchiveThread;
import com.web.thread.ArchiveThread;
import com.web.util.AppUtil;

/**
 * ��Servlet�ж��ļ��鵵��ʽ�����ع鵵����smb�������鵵
 * @author Administrator
 * @ClassName:HandArchive
 * @date 2018年3月12日 上午10:32:14
 */
public class HandArchive extends HttpServlet {
	private Logger myLogger = LogManager.getLogger("mylog");

	/**
	 * Constructor of the object.
	 */
	public HandArchive() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * doPost��ǰ̨����
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String srcPath = request.getParameter("ArchivePath");//�鵵Դ·��
		String taskname = request.getParameter("taskname");  //��������
		String ProductType = request.getParameter("ProductType"); 
		String ArchiveMode = request.getParameter("ArchiveMode");
		int iArchiveMode=0;
		if(ArchiveMode.compareToIgnoreCase("迁移归档")==0)
		{
			iArchiveMode=1;
		}
		else if(ArchiveMode.compareToIgnoreCase("扫描归档")==0)
		{
			iArchiveMode=0;
		}
		else 
		{
			myLogger.info("archive mode is not set！");
			return ;
		}
		PrintWriter out = response.getWriter();

		if(ProductType.compareToIgnoreCase("分幅DOM")==0)
		{
			out.println("Archive is begining");
			ArchiveThread pThread = new ArchiveThread(ProductType,srcPath,iArchiveMode);
			pThread.setTaskName(taskname);
			ThreadManager pthreadpool = new ThreadManager();
			pthreadpool.submmitJob(pThread);   
			return;
		}
		else if(ProductType.compareToIgnoreCase("分景DOM")==0)
		{
			out.println("Archive is begining");
			ArchiveThread pThread = new ArchiveThread(ProductType,srcPath,iArchiveMode);
			pThread.setTaskName(taskname);
			ThreadManager pthreadpool = new ThreadManager();
			pthreadpool.submmitJob(pThread);   
			return;		
		}
			
		
		if (srcPath.isEmpty()) {
			out.print("src path is empty!!");
		} else {

			int flag = -1;
			try {
				
				flag = Constants.AssertFileIsSMBFileDir(srcPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (flag == 0) {                 
				File pf = new File(srcPath);

				if (!pf.exists()) {
					out.println(srcPath + ": is not exists");
				} else {
					out.println("Archive is begining");
					MyHmArchiveThread pThread = new MyHmArchiveThread(srcPath);
					pThread.setTaskName(taskname);
					ThreadManager pthreadpool = new ThreadManager();
					pthreadpool.submmitJob(pThread);   
				}
			} else if (flag == 2) {
				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(srcPath);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return;
				}
				SmbFile remoteFile = new SmbFile(srcPath, auth);

				if (!remoteFile.exists()) {
					out.print(srcPath + ":is not exists");
				} else {
					out.println("Archive is begining!");
					MyHmArchiveThread pThread = new MyHmArchiveThread(srcPath);
					pThread.setTaskName(taskname);
					ThreadManager pthreadpool = new ThreadManager();
					pthreadpool.submmitJob(pThread);
				}
			} else if (flag == 1) {
				srcPath = AppUtil.localFilePathToSMBFilePath(srcPath);
				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(srcPath);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return;
				}
				SmbFile remoteFile = new SmbFile(srcPath, auth);
				if (!remoteFile.exists()) {
					out.print(srcPath + ":is not exists");
				} else {
					out.println("Archive is begining!");
					MyHmArchiveThread pThread = new MyHmArchiveThread(srcPath);
					pThread.setTaskName(taskname);
					ThreadManager pthreadpool = new ThreadManager();
					pthreadpool.submmitJob(pThread);
				}

			} else if (flag == -1) {
				out.print(srcPath + ":is not exists");
			}

		}

		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
