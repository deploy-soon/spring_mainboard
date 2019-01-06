<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="com.test.myapp.dao.BoardDAO" %>
<%@ page import="com.test.myapp.dto.BoardDTO" %>
<%@ page import="java.util.ArrayList" %>
    
<%!
	BoardDAO refBoardDAO = null;
	BoardDTO refBoardDTO = null;
	String bno = null;
	int ibno = 0;
	int iresult = 0;
	ArrayList<BoardDTO> refArrayList = null;
%>
<%
	bno = request.getParameter("bno");
	ibno = Integer.parseInt(bno);
	refBoardDAO = BoardDAO.getInstance();
	iresult = refBoardDAO.updateViewcnt(ibno);
	
	refBoardDTO = refBoardDAO.selectTuple(ibno);
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script type="text/javascript">
	function go_main_board_func(){
		document.location.href = "mainBoard";
	}
	
</script>
</head>
<body>
<h1>mainBoard~</h1>
	<table>
	 <tr>
    <th>NUM</th>
    <th>WRITER</th> 
    <th>TITLE</th>
    <th>CONTENTS</th>
    <th>REGIST DATE</th>
    <th>VIEW</th>
  </tr>
	
	<%
				int bno = refBoardDTO.getBno();
				out.print("<tr><td>"+bno);
				out.print("</td><td>"+refBoardDTO.getWriter());
				out.print("</td><td>"+refBoardDTO.getTitle());
				out.print("</td><td>"+refBoardDTO.getContent());
				out.print("</td><td>"+refBoardDTO.getRegdate());
				out.print("</td><td>"+refBoardDTO.getViewcnt());
			
	%>
	</table>
	
	<form action="" method="post">
		<table>
			<tr>
				<td>
					<input type="button" value="mainboard" onclick="go_main_board_func();">
		</table>
	</form>

</body>
</html>