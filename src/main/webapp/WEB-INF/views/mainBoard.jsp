<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
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
	<form action="" method="post">
		<table>
			<tr>
				<td>
					<input type="button" value="regist list" onclick="go_reg_board_func();">
		</table>
	</form>
</body>
</html>