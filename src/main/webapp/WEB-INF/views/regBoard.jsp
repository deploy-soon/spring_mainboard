<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="com.test.myapp.dao.BoardDAO" %>
<%!
	BoardDAO refBoardDAO = null;
	int maxNum = 0;
%>
<%
	refBoardDAO = BoardDAO.getInstance();
	maxNum = refBoardDAO.getMaxNum();
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Regist Board</title>
</head>
<body>
	<h1>regist new board</h1>
	<form action="regBoardProc" method="post">

		<table>
		<tr>
				<td> number:</td>
				<td><input type="number" id="bno" name="bno" value="<%=maxNum%>" readonly
					required></td>
			</tr>
			<tr>
				<td>title</td>
				<td><input type="text" id="title" name="title" maxlength="200"
					required></td>
			</tr>
			<tr>
				<td>content</td>
				<td><textarea rows="20" cols="80" id="content" name="content"
						required maxlength="4000">input content</textarea></td>
			</tr>
			<tr>
				<td>writer</td>
				<td><input type="text" id="writer" name="writer" maxlength="50"
					required></td>
			</tr>
			<tr>
				<td>regdate</td>
				<td><input type="date" id="regdate" name="regdate" required>
				</td>
			</tr>
			<tr>
				<td>viewcnt</td>
				<td><input type="number" id="viewcnt" name="viewcnt" required
					readonly value="1"></td>
			</tr>
			<tr>
				<td> submit :</td>
				<td><input type="submit" value="submit"></td>
			</tr>
			
		</table>
		
	</form>
</body>
</html>