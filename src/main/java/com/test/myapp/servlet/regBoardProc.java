package com.test.myapp.servlet;

import com.test.myapp.dao.*;
import com.test.myapp.dto.*;

import java.sql.Date;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.sql.DriverManager;
import java.sql.Connection;
import java.io.*;

public class regBoardProc extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public static final String USER_NAME = "spring_user01";
	public static final String PASSWORD = "1234";
	public static final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.oracleDriver";
	
	private static Connection refOracleConnection = null;
	private BoardDAO refBoardDAO = null;
	private BoardDTO refBoardDTO = null;
	
	static {
		try {
			Class.forName(ORACLE_DRIVER);
			System.out.println("load oracle driver");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static boolean connectOracle() {
		boolean result = false;
		try {
			refOracleConnection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
			result = true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	public static boolean closeOracle() {
		boolean result = false;
		try {
			if(refOracleConnection != null) {
				refOracleConnection.close();
				result = true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		request.setCharacterEncoding("EUC-KR");
		String strBno = request.getParameter("bno");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String writer = request.getParameter("writer");
		String regdate = request.getParameter("regdate");
		String viewcnt = request.getParameter("viewcnt");
		if(strBno == null || title == null || content == null || writer == null || regdate == null || viewcnt == null) {
			response.sendRedirect("regBoard");
		}else {
			System.out.println("start");
			refBoardDAO = BoardDAO.getInstance();
			refBoardDTO = new BoardDTO();
			int iBno = Integer.parseInt(strBno);
			Date dRegdate = Date.valueOf(regdate);
			int iviewcnt = Integer.parseInt(viewcnt);
			refBoardDTO.setBno(iBno);
			refBoardDTO.setContent(content);
			refBoardDTO.setRegdate(dRegdate);
			refBoardDTO.setTitle(title);
			refBoardDTO.setViewcnt(iviewcnt);
			refBoardDTO.setWriter(writer);
			int iResult = refBoardDAO.insert(refBoardDTO);
			if(iResult == 1) {
				System.out.println("save new post");
			}else {
				System.out.println("fail");
			}
		}
	}
}
