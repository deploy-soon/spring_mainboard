<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="com.test.myapp.dao.BoardDAO" %>
<%@ page import="com.test.myapp.dto.BoardDTO" %>
<%@ page import="java.util.ArrayList" %>
<%!
	BoardDAO refBoardDAO = null;
	BoardDTO refBoardDTO = null;
	ArrayList<BoardDTO> refArrayList = null;
%>
<%
	refBoardDAO = BoardDAO.getInstance();
	refArrayList = refBoardDAO.selectAll();
%>
<!DOCTYPE html>
<html>
<head>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
<meta charset="EUC-KR">
<title>Dasboard</title>
<script type="text/javascript">
	function go_reg_board_func(){
		document.location.href = "regBoard";
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
		int size = refArrayList.size();
		if(size == 0){
			out.print("THERE IS NO BOARD");
		}else{
			for(int i=0;i<size;i++){
				refBoardDTO = refArrayList.get(i);
				int bno = refBoardDTO.getBno();
				out.print("<tr><td><a href='eachboard?bno="+bno+"'>"+bno);
				out.print("</td><td>"+refBoardDTO.getWriter());
				out.print("</td><td>"+refBoardDTO.getTitle());
				out.print("</td><td>"+refBoardDTO.getContent());
				out.print("</td><td>"+refBoardDTO.getRegdate());
				out.print("</td><td>"+refBoardDTO.getViewcnt());
			}
		}
	%>
	</table>
	
	<form action="" method="post">
		<table>
			<tr>
				<td>
					<input type="button" value="regist" onclick="go_reg_board_func();">
		</table>
	</form>
</body>
</html>