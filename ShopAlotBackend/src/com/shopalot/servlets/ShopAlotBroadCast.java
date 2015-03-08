package com.shopalot.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class GODGaliBroadCast
 */
@WebServlet("/ShopAlotBroadCast")
public class ShopAlotBroadCast extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShopAlotBroadCast() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String regId = request.getParameter("regId");
		try{
		Sender sender = new Sender("AIzaSyAHBK1IK7MjDeES0djM117PRR-JQAR2Ae4");
		String frenzyRegion = FileUtils.readFileToString(new File("/home/data/frenzyregion.txt"));
		Message message = new Message.Builder().addData("regionname", frenzyRegion).build(); 
		List<String> devices = new ArrayList<String>();
		//hardcoded id
		devices.add("APA91bHKFVIV0QJFh_AAYTbeGXAg0cMkzIzi-1qrEdNtb4CI6-Uykbuu4c70VKSUCpkK83Ni_8nMPxbpX0XsDT4PMZHccnZk8pR-LZQc_qanv91t1VK0hdnzZ4Etmz1dJhtGJ5XkGk-CBsgHwda9qI1T6gRtDTCX2A");
		MulticastResult result = sender.send(message, devices , 5);
		response.getWriter().print(result);
		response.getWriter().print(result.getCanonicalIds());
		response.getWriter().print(result.getResults().toArray());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
