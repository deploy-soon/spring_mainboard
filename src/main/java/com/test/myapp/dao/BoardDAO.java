package com.test.myapp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.myapp.dto.BoardDTO;

public class BoardDAO {
	
	protected Log log = LogFactory.getLog(BoardDAO.class);
	
	// 1. mybatis-config.xml 파일의 전체 경로를 문자열 상수로 저장
		public static final String MYBATIS_RESOURCE = 
				"com/test/myapp/dao/mybatis-config.xml";
		// 2. 현재 클래스는 싱글톤 패턴을 사용하기 때문에 private 접근 제한자를
		// 사용해서 참조 변수를 선언 : 현재 클래스의 시작 주소를 보관할 참조 변수
		private static BoardDAO refGuestBookDAO;

		// 3. 공장 패턴: 공장 객체를 만들어 주는 빌더 객체의 주소를 보관할
		// 참조 변수를 선언
		private static SqlSessionFactoryBuilder 
		      refSqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
		
		// 4. SqlSessionFactoryBuilder 클래스가 만들어줄 공장 객체의 주소를
		// 보관할 변수를 새로 선언
		private static SqlSessionFactory refSqlSessionFactory;
		
		// 5. 기본 생성자 함수를 private 접근 제하자로 설정하기
		private BoardDAO() { }
		
		// 6. getInstance( ) 함수를 만들어서 new 연산자를 사용해서
		// 현재 클래스에 대한 새로운 객체를 만들어서
		// refGuestBookDAO 참조 변수에 객체의 시작 주소를 저장하고 반환하는
		// 함수를 새로 정의하기
		// -> 다른 클래스에서 사용할 수 있도록 public 접근 제한자를 사용
		public static BoardDAO getInstance() {
			// 1) 무조건 객체를 만들지 않습니다!!
			// -> 먼저 refGuestBookDAO 참조 변수가 갖고 있는 주소를 검사
			//   -> null인 경우에만 새로운 객체를 만들기
			if(refGuestBookDAO == null) {
				// new 연산자는 객체를 만들지 않은 경우에만 실행
				refGuestBookDAO = new BoardDAO();
			}		
			// return 명령어는 무조건 실행
			return refGuestBookDAO;
		}
		
		/*
		 * static 초기화 블럭: static 관련 멤버 변수들을 사용
		 *   -> static { }
		 *   -> 생성자 함수 보다 먼저 실행
		 * 
		 * 초기화 블럭: { } : 모든 멤버 변수들을 사용
		 * 
		 * 생성자 함수: 모든 멤버 변수들을 사용
		 */
		// static 초기화 블럭을 만들기
		/*
		 * 1. 입력하실 명령어 목록
		 *    1) Resources 클래스를 사용해서 mybatis-config.xml
		 *       파일을 읽기 -> 읽은 파일의 주소를 InputStream
		 *       추상 클래스에 저장(참조 변수를 새로 선언해서 사용)
		 *       
		 *    2) SqlSessionFactoryBuilder 클래스가 갖고 있는
		 *       build( ) 함수를 호출해서 새로운 공장 객체를 만들기
		 *       -> 객체의 주소는 refSqlSessionFactory 참조 변수에 저장
		 *       
		 *    3) InputStream 클래스를 다 사용한 후에는 close( )
		 *       함수를 사용해서 파일을 닫기(mybatis-config.xml)
		 */
		static {
			// 1. InputStream 추상 클래스를 사용해서
			// -> mybatis-config.xml 파일의 시작 주소를 보관할 수 있는
			// -> 참조 변수를 선언
			InputStream refInputStream = null;
			
			// 2. getResourceAsStream( ) 함수를 사용하거나
			//   close() 함수를 사용할 때 IOException 예외 상황이
			//   발생할 수 있으므로 try~catch 블럭을 작성하기
			try {
		System.out.println("1. getResourceAsStream() 함수 호출");
		refInputStream = Resources.getResourceAsStream(MYBATIS_RESOURCE);
		System.out.println("2. getResourceAsStream() 함수 실행 성공");
		System.out.println("3. 공장 객체를 만들기: build( ) 함수를 호출");
		refSqlSessionFactory = 
				refSqlSessionFactoryBuilder.build(refInputStream);
		System.out.println("4. 공장 객체 만들기 성공");
			} catch(IOException refIOException) {
		System.out.println("3. 파일 읽기 실패 또는 다른 예외상황 발생!!");
		System.out.println("자세한 내용은 "+refIOException.getMessage());
		refIOException.printStackTrace();
			} finally {
				// 위에서 사용한 입력 스트림 객체를 시스템에 반환하기
				// -> close( ) 함수를 호출해서 mybatis-config.xml 파일을 닫기
				try {
					if(refInputStream != null) {
						refInputStream.close();
					}
				} catch(IOException refIOE2) {
			System.out.println("파일 닫기 실패!!");
			System.out.println("예외상황 내용은 "+refIOE2.getMessage());
			refIOE2.printStackTrace();
				}
			} 
		} // end of static { }
	
	@Autowired
    private SqlSessionTemplate sqlSession;

	protected void printQueryId(String queryId) {
        if(log.isDebugEnabled()){
            log.debug("\t QueryId  \t:  " + queryId);
        }
    }
	
	public Object insert(String queryId, Object params){
        printQueryId(queryId);
        return sqlSession.insert(queryId, params);
    }

	public Object update(String queryId, Object params){
        printQueryId(queryId);
        return sqlSession.update(queryId, params);
    }
     
    public Object delete(String queryId, Object params){
        printQueryId(queryId);
        return sqlSession.delete(queryId, params);
    }
     
    public Object selectOne(String queryId){
        printQueryId(queryId);
        return sqlSession.selectOne(queryId);
    }
     
    public Object selectOne(String queryId, Object params){
        printQueryId(queryId);
        return sqlSession.selectOne(queryId, params);
    }
     
    @SuppressWarnings("rawtypes")
    public List selectList(String queryId){
        printQueryId(queryId);
        return sqlSession.selectList(queryId);
    }
     
    @SuppressWarnings("rawtypes")
    public List selectList(String queryId, Object params){
        printQueryId(queryId);
        return sqlSession.selectList(queryId,params);
    }

	public int regContent(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modifyContent(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getContentCnt(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int insert(BoardDTO refBean) {
		int result = 0;
		// insert sql 명령어가 있는 태그에 접근하기 위한 변수를 선언
		// -> 태그의 주소를 가져오기
		SqlSession refSqlSession = refSqlSessionFactory.openSession();
		
		// SqlSession 인터페이스가 갖고 있는 insert( ) 함수를 호출
		// -> guest_book_mapper.xml 파일에서 만든 네임 스페이스 이름과
		// -> 실행할 sql 명령어가 들어있는 insert 태그의 아이디를 사용
		//    -> 네임스페이스이름 + 점(.) + 아이디
		result = 
			refSqlSession.insert("dev.insertBoard", refBean);
		// insert sql 명령어를 실행하고
		// 오라클 데이터베이스로부터 받은 실제 테이블에 저장된 레코드 갯수를
		// 검사하기 -> 아직은 메모리에 있는 내용
		if(result == 1) {
			// SqlSession 인터페이스가 갖고 있는 commit( ) 함수를 실행
			// 테이블에 새로운 레코드를 한 개만 삽입
			refSqlSession.commit();
		}
		
		// 힙 메모리에 만들어져 있는 sql 세션 객체를 시스템에 반환하기
		// -> close( ) 함수를 호출
		refSqlSession.close();
		
		return result;
	}
	
	public int getMaxNum() {
		int result = 0;
		SqlSession refSqlSession = refSqlSessionFactory.openSession();
		result = (int)refSqlSession.selectOne("dev.getMaxNum");
		refSqlSession.close();
		return result;
	}
	
	public int updateViewcnt(int bno) {
		int result = 0;
		SqlSession refSqlSession = refSqlSessionFactory.openSession();
		result = refSqlSession.update("dev.updateViewcnt", bno);
		if(result == 1) {
			refSqlSession.commit();
		}
		refSqlSession.close();
		return result;
	}
	
	public BoardDTO selectTuple(int bno) {
		// 결과를 보관할 참조 변수를 선언
		BoardDTO refResult = null;
		// data_mapper.xml 파일에 접근할 때 사용할 참조 변수 선언
		SqlSession refSqlSession = null;
		// SqlSessionFactory 클래스로부터 세션 객체의 주소를 가져오기
		refSqlSession = refSqlSessionFactory.openSession();
		// SqlSession 갖고 있는 selectList( ) 함수를 호출하기
		// -> 반환하는 클래스가 Object -> ArrayList 클래스로 형 변환
		refResult = (BoardDTO)refSqlSession.selectOne("dev.selectOne", bno);
		// 시스템에 세션 객체를 반납하기 -> close( ) 함수를 호출
		refSqlSession.close();
		return refResult;
	}
	
	public ArrayList<BoardDTO> selectAll() {
		// 결과를 보관할 참조 변수를 선언
		ArrayList<BoardDTO>  refResult = null;
		// data_mapper.xml 파일에 접근할 때 사용할 참조 변수 선언
		SqlSession refSqlSession = null;
		// SqlSessionFactory 클래스로부터 세션 객체의 주소를 가져오기
		refSqlSession = refSqlSessionFactory.openSession();
		// SqlSession 갖고 있는 selectList( ) 함수를 호출하기
		// -> 반환하는 클래스가 Object -> ArrayList 클래스로 형 변환
		refResult = (ArrayList)refSqlSession.selectList("dev.selectAll");
		// 시스템에 세션 객체를 반납하기 -> close( ) 함수를 호출
		refSqlSession.close();
		return refResult;
	}

}
