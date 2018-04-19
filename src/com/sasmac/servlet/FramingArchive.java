package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import com.sasmac.common.ThreadManager;
import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;
import com.web.thread.SfiCopyArchiveThread;
import com.web.thread.SfiScanArchiveThread;
import com.web.thread.MyHmArchiveThread;
import com.web.thread.SviCopyArchiveThread;
import com.web.thread.SviScanArchiveThread;
import com.web.thread.ArchiveThread;
import com.web.util.AppUtil;

public class FramingArchive extends HttpServlet {
	private Logger myLogger = LogManager.getLogger("mylog");

	/**
	 * Constructor of the object.
	 */
	public FramingArchive() {
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String srcPath = request.getParameter("ArchivePath");// 归档源路径
		String taskname = request.getParameter("taskname"); // 任务名称
		String arcmethod = request.getParameter("arcmethod"); // 归档方式
		PrintWriter out = response.getWriter();

		if (srcPath.isEmpty()) {
			out.print("请设置归档路径!!!");
			return;
		}

		File pf = new File(srcPath);

		if (!pf.exists()) {
			out.println(srcPath + ":归档路径不存在！");
			return;
		}
		int Arcmethod = 0;
		String ProductType = "";

		out.println("正在归档!");
		ArchiveThread pThread = new ArchiveThread(ProductType, srcPath, Arcmethod);
		pThread.setTaskName(taskname);
		ThreadManager pthreadpool = new ThreadManager();
		pthreadpool.submmitJob(pThread); // 开始归档

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
