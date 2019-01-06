<%@ page language="java" contentType="text/html; 
    charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%!
	String strCurPageNo = "";
	int iCurPageNo = 0;
	int iStartRecordNo = 0;
	int iEndRecordNo = 0;
	
	// 화면에 보여줄 글의 갯수 정하기 : 3개
	public static final int RECORD_COUNT = 3;

	int iTotalRecordCount = 0;
	String selectCount = "select count(*) from tbl_spring_board";
	Statement statement = null;
	ResultSet resultSet = null;
	Connection connection = null;

	// 전체 페이지 수 보관 변수
	int iTotalPageCount = 0;

	PreparedStatement ps01 = null;
	ResultSet rs01 = null;
	ArrayList<String> arrayList = new ArrayList<String>();

	/*
	 * 블럭 수를 구하는 함수
	 * 
	 * 블록 수는 [이전], [다음] 링크 텍스트를 만들 때 사용하는 값
	 * 
	 * 매개 변수 : 현재 페이지 번호, 블록 당 페이지 수
	 * 
	 * 반환형 : 블록 수(정수)
	 */
	public static int getBlock(int curPageNo, int pagePerBlock) {

		// 블록 수를 보관할 지역 변수
		int block = 0;

		// 현재 페이지 값이 1 보다 작은 경우에는 오류 처리
		if (curPageNo < 1) {
			System.out.println("현재 페이지 오류: 값은 " + curPageNo);
			return 0;
		}

		// 블록 당 페이지 값이 1 보다 작은 경우에는 오류 처리
		if (pagePerBlock < 1) {
			System.out.println("블록 당 페이지 수 에러 : 값은 " + pagePerBlock);
			return 0;
		}

		// 현재 페이지가 블록 당 페이지 수의 배수인 경우 : 현재 페이지 / 블록 당 페이지 수
		// 현재 페이지가 블록 당 페이지 수의 배수가 아닌 경우 : 현재 페이지 / 블록 당 페이지 수 + 1
		if (curPageNo % pagePerBlock == 0) {
			block = curPageNo / pagePerBlock;
		} else {
			block = curPageNo / pagePerBlock + 1;
		}

		// 블록 수를 반환
		return block;
	}

	// 블럭 내에서 첫 번째 페이지 번호
	public static int getFirstpage(int block, int pagePerBlock) {

		// 블록 수가 1 보다 작은 경우에는 오류 처리
		if (block < 1) {
			System.out.println("블록 수 에러 : 값은 " + block);
			return 0;
		}

		// 블록 당 페이지 수가 1보다 작은 경우에는 오류 처리
		if (pagePerBlock < 1) {
			System.out.println("블록 당 페이지 수 에러 : 값은 " + pagePerBlock);
			return 0;
		}

		// (블록 수 - 1) * 블록 당 페이지 수 + 1
		int firstPage = (block - 1) * pagePerBlock + 1;

		// 첫 번째 페이지 번호 반환
		return firstPage;
	}
	
	/*
	 * 전체 블럭 수를 구해주는 함수
	 */
	public static int getTotalBlock(int pageCount, int pagePerBlock) {
		
		// 전체 블록 수를 보관할 변수
		int totalBlock = 0;
		
		// 페이지 수가 0 이하이면 함수 종료
		if(pageCount <= 0) {
			return 0;
		}
		
		// 블록 당 페이지 수가 0 이하이면 함수 종료
		if(pagePerBlock <= 0) {
			return 0;
		}
		
		
		// 페이지 수가 블록 당 페이지 수의 배수인 경우 : 페이지 수 / 블록 당 페이지 수
		// 페이지 수가 블록 당 페이지 수의 배수가 아닌 경우 : 페이지 수 / 블록 당 페이지 수 + 1
		if (pageCount % pagePerBlock == 0) {
			totalBlock = pageCount / pagePerBlock;
		} else {
			totalBlock = pageCount / pagePerBlock + 1;
		}
		
		// 전체 블록 수 반환
		return totalBlock;
	}
	
	/*
	 * 블럭 내에서 마지막 번째 페이지 번호를 구해주는 함수
	 */
	public static int getLastPage(int block, int pagePerBlock) {
				
		// 블록 수가 1 보다 작은 경우에는 오류 처리
		if(block < 1) {
			System.out.println("블록 수 에러 : 값은 " + block);
			return 0;
		}
	
		// 블록 당 페이지 수가 1보다 작은 경우에는 오류 처리
		if(pagePerBlock < 1) {
			System.out.println("블록 당 페이지 수 에러 : 값은 " + pagePerBlock);
			return 0;
		}
		
		// 마지막 번째 페이지 : 블록 수 * 블록 당 페이지 수
		int lastPage =  block * pagePerBlock;
		
		// 마지막 번째 페이지를 반환
		return lastPage;
	}
%>
<%
	strCurPageNo = request.getParameter("curPageNo");
	if (strCurPageNo == null) {
		iCurPageNo = 1;
	} else {
		iCurPageNo = Integer.parseInt(strCurPageNo);
	}

	out.print("현재 페이지 번호는 " + iCurPageNo + "<br>");

	iStartRecordNo = (iCurPageNo - 1) * RECORD_COUNT + 1;
	iEndRecordNo = iStartRecordNo + RECORD_COUNT - 1;

	out.print("시작 레코드 번호는 " + iStartRecordNo + "<br>");
	out.print("마지막 레코드 번호는 " + iEndRecordNo + "<br>");

	try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "spring_user01", "1234");
		out.print("conn<br>");
		statement = connection.createStatement();
		out.print("statement<br>");
		resultSet = statement.executeQuery(selectCount);
		out.print("resultSet<br>");
		if (resultSet.next() == true) {
			out.print("next()<br>");
			iTotalRecordCount = resultSet.getInt(1);
			out.print("총 레코드 갯수는 " + iTotalRecordCount + "<br>");
		} else {
			out.print("next() == false<br>");
		}
		/*				
			전체 페이지 수를 구해주는 함수 : 전체 레코드 갯수 / 글 수				
		*/
		if (iTotalRecordCount % RECORD_COUNT == 0) {
			iTotalPageCount = iTotalRecordCount / RECORD_COUNT;
		} else {
			iTotalPageCount = iTotalRecordCount / RECORD_COUNT + 1;
		}

		out.print("전체 페이지 수는 " + iTotalPageCount + "<br>");

		StringBuffer sqlStringBuffer = new StringBuffer();

		sqlStringBuffer.append("select * from ");
		sqlStringBuffer.append("(select rownum rnum, bno, title, ");
		sqlStringBuffer.append("        content, regdate, viewcnt ");
		sqlStringBuffer.append("from");
		sqlStringBuffer.append(" (select * from tbl_spring_board order by bno desc)) ");
		sqlStringBuffer.append("where rnum>=? and rnum<=?");

		ps01 = connection.prepareStatement(sqlStringBuffer.toString());
		out.print("ps01 = connection.prepareStatement(sqlStringBuffer.toString());<br>");
		ps01.setInt(1, iStartRecordNo);
		ps01.setInt(2, iEndRecordNo);
		rs01 = ps01.executeQuery();
		out.print("rs01 = ps01.executeQuery();<br>");
		
		arrayList.clear();
		
		while (rs01.next() == true) {
			out.print("rs01.next() == true<br>");

			int rnum = rs01.getInt("rnum");
			int no = rs01.getInt("bno");
			String title = rs01.getString("title");
			String contents = rs01.getString("content");
			java.sql.Date date = rs01.getDate("regdate");
			int count = rs01.getInt("viewcnt");

			String sresult = rnum+","+no + "," + title + "," + contents + "," + date + "," + count;
			arrayList.add(sresult);
		}

	} catch (ClassNotFoundException e) {
		System.out.println("오라클 드라이버 클래스 찾기 실패");
	} catch (SQLException e) {
		System.out.println("getConnection() 함수 실행 에러");
	} catch (Exception e) {
		System.out.println("그밖의 에러 발생");
	} finally {
		try {
			if (rs01 != null)
				rs01.close();
			if (ps01 != null)
				ps01.close();
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (SQLException sqle1) {
			out.print("sql 명령어 실행 오류!!<br>");
			out.print("내용은 " + sqle1.getMessage() + "<br>");
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Test Paging</title>
</head>
<body>
	<h1>Test Paging</h1>
	<table>
		<tr>
			<td>레코드 번호(행 번호)</td>
			<td>글 번호</td>
			<td>글 제목</td>
			<td>글 내용</td>
			<td>글 작성일</td>
			<td>글 조회수</td>
		</tr>
		<%
			int sizeArrayList = arrayList.size();
			if (sizeArrayList > 0) {
				for (int i = 0; i < sizeArrayList; i++) {
					String value = arrayList.get(i);
					String[] values = value.split(",");
		%>

		<tr>
			<td><%=values[0]%></td>
			<td><%=values[1]%></td>
			<td><%=values[2]%></td>
			<td><%=values[3]%></td>			
			<td><%=values[4]%></td>
			<td><%=values[5]%></td>
		</tr>

		<%
				}
			}
		%>
	</table>
	<%
	/*
	 * 7. [이전], [다음] 블록 링크 텍스트 만들기
	 */
	
	// 페이지 그룹 번호(블록)를 저장할 변수 선언과 초기화
	int block = 1;

	// 블록 당 페이지 수를 저장할 변수와 초기화
	int pagePerBlock = 4;

	// 블록(페이지 그룹 번호) 구하기
	/*
	 * [1][2][3][다음]
	 */
	/*
	 * 현재 페이지 번호 % 블록 당 페이지 수 :
	 *   나누어 떨어지는 경우에는 현재 페이지 번호 / 
	 *   블록 당 페이지 수
	 *   나누어 떨어지지 않은 경우에는 현재 페이지 번호 / 
	 *   블록 당 페이지 수 + 1
	 *   
	 * 예) 현재 페이지 1, 블록 당 페이지 수 2
	 *       1 / 2 : 몫은 0, 나머지 1 -> 1 / 2 + 1
	 *       3 / 2 : 몫은 1, 나머지 1 -> 3 / 2 + 1
	 *       2 / 2 : 몫은 1, 나머지 0 -> 2 / 2
	 */
	// 1) 먼저 getBlock() 함수를 호출해서 블록 수를 구함
	block = getBlock(iCurPageNo, pagePerBlock);

	System.out.println("block: "+block);
	
	// block에 속한 첫 번째 페이지 계산
	/*
	 * 공식 : (블럭 수 - 1) * 블록 당 페이지 수 + 1
	 * 
	 * 예) 블럭 수 1, 블록 당 페이지 수 2인 경우
	 *     (1 - 1) * 2 + 1 -> 1
	 *     
	 *    블럭 수 2, 블럭 당 페이지 수 2인 경우
	 *      (2 - 1) * 2 + 1 -> 3 
	 */
	// 2) getFirstpage() 함수를 호출해서 첫 번째 페이지 수 구하기
	int firstPage = getFirstpage(block, pagePerBlock);
	
	System.out.println("firstPage: "+firstPage);
	
	// 총 블록 수를 저장할 변수 선언과 초기화 
	int totalBlock = 0;

	/*
	 * 총 블록 수는 페이지 개수 / 블록 당 페이지 개수
	 * 
	 * 예) 페이지 개수 5, 블록 당 페이지 개수 2
	 *    5 % 2 : 2(몫) 1(나머지)
	 *      나머지가 있으면 몫에 나머지를 더함 : 5 / 2 + 1
	 *      나머지가 없으면 몫만을 구함 : 4 / 2
	 */
	// 3) getTotalBlock() 함수를 호출해서 전체 블록 수를 구함
	totalBlock = getTotalBlock(iTotalPageCount, pagePerBlock);

	System.out.println("pageCount: "+iTotalPageCount);
	System.out.println("totalBlock : "+totalBlock);
	
	// block에 속한 마지막 페이지 계산
	/*
	 * 공식) 블럭 수 * 블록 당 페이지 수
	 * 
	 * 예) 블럭 수 1, 블럭 당 페이지 수 2 : 
	 *     마지막 페이지 번호는  블럭 수 * 블럭 당 페이지 수
	 *     1 * 2
	 *    블럭 수 2, 블럭 당 페이지 수 2
	 *     2 * 2
	 *    블럭 수 3, 블럭 당 페이지 수 2
	 *     3 * 2
	 */
	// 만약 블럭 수가 총 블럭 수 보다 크면 마지막 페이지 번호는 페이지 수로 변경
	/*
	 * 예) 블럭 수 4 전체 블럭 수 4
	 *      마지막 페이지 수는 페이지 수
	 */

	// 4) getLastPage() 함수를 호출해서 마지막 페이지 번호 구하기
	int lastPage = getLastPage(block, pagePerBlock);
	
	/*
	 * 블록 수가 전체 블록 수 보다 크면 마지막 페이지는 페이지 수로 줄임
	 */
	if (block >= totalBlock) {
		  lastPage = iTotalPageCount;
	}
	
	System.out.println("lastPage : "+lastPage);
	
	/*
	 * 최종 값들을 콘솔 화면에 출력
	 */
	System.out.println("페이지 수는 "+iTotalPageCount);
	System.out.println("블록 수는 "+block);		
	System.out.println("총 블록 수는 "+totalBlock);
	System.out.println("첫 번째 페이지 수는 "+firstPage);
	System.out.println("마지막 번째 페이지 수는 "+lastPage);	
	%>
	<%		
		int prevPage = 0;		
		if(block > 1) {			
	  		prevPage = firstPage - 1;
	  	%>
			<a href="test_paging2.jsp?curPageNo=<%=prevPage%>">[이전]</a>
	<%
		}		
	%>
	<%			
		if(firstPage > 0) {
			for(int i = firstPage; i <= lastPage; i++) {
				%>
				<a href="test_paging2.jsp?curPageNo=<%=i%>">[<%=i%>]</a>
	<%}
		}
	%>
	<%
				
		if(block < totalBlock) {		
	    		int nextPage = lastPage + 1;	    
	    	%>
				<a href="test_paging2.jsp?curPageNo=<%=nextPage%>">[다음]</a>
	<%
		}
	%>
</body>
</html>