package com.shopalot.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetWifiNames
 */
@WebServlet("/GetWifiNames")
public class GetWifiNames extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetWifiNames() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String trainingID= request.getParameter("trainingID");
		
		File f=new File("/home/data/"+trainingID+".txt");
		FileInputStream inputStream = new FileInputStream(f);
		StringBuilder stringBuilder = new StringBuilder();
		int a;
		while((a=inputStream.read())!=-1)
		{
			if(a==10)
				break;
			
				stringBuilder.append((char)a);
		}
		inputStream.close();
		response.getWriter().print(stringBuilder.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
