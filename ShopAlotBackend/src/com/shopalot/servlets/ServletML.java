package com.shopalot.servlets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletML
 */
@WebServlet("/ServletML")
public class ServletML extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletML() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}*/

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public int calculation(int variable)
	{
		variable=(-1)* variable;
		variable=variable;
		return variable;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String inputData=request.getParameter("any"); 
		//getResponse+="&set=SETMODE";
		String setMode = request.getParameter("set");
		System.out.println(setMode);
		
		
		
		StringBuilder sbr=new StringBuilder();
		
		if(setMode==null){
		String[] signals = inputData.split("~~");
		String trainingID= "00014389dsjsdksd";
		
		for (int i = 0; i < signals.length; i++) {
			String[] subSignals = signals[i].split("--");
			sbr.append(subSignals[0]+"\n");
			trainingID=subSignals[1];
		}
		
		String path = "/home/data/data.csv";
//		String path = "C:/data.csv";
		File f=new File(path+trainingID);
		
		String path1  = f.getAbsolutePath();
		
		f.createNewFile();
		RandomAccessFile r=new RandomAccessFile(path+trainingID, "r");
		RandomAccessFile w=new RandomAccessFile(path+trainingID, "rw");	
		Long fileLength=w.length();
		r.read();
		w.seek(fileLength);
		w.write(sbr.toString().getBytes());
		r.close();
		w.close();
		
		response.getWriter().print(path);
		}
		else
		{
			String trainingID = request.getParameter("trainingID");
			
			sbr.append(inputData);
			
			
			File f=new File("/home/data/"+trainingID+".txt");
			
			
			String path  = f.getAbsolutePath();
			
			f.createNewFile();
			RandomAccessFile r=new RandomAccessFile("/home/data/"+trainingID+".txt", "r");
			RandomAccessFile w=new RandomAccessFile("/home/data/"+trainingID+".txt", "rw");	
			Long fileLength=w.length();
			r.read();
			w.seek(fileLength);
			w.write(sbr.toString().getBytes());
			r.close();
			w.close();
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
