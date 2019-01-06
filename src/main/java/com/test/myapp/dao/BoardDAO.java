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
	
	// 1. mybatis-config.xml ������ ��ü ��θ� ���ڿ� ����� ����
		public static final String MYBATIS_RESOURCE = 
				"com/test/myapp/dao/mybatis-config.xml";
		// 2. ���� Ŭ������ �̱��� ������ ����ϱ� ������ private ���� �����ڸ�
		// ����ؼ� ���� ������ ���� : ���� Ŭ������ ���� �ּҸ� ������ ���� ����
		private static BoardDAO refGuestBookDAO;

		// 3. ���� ����: ���� ��ü�� ����� �ִ� ���� ��ü�� �ּҸ� ������
		// ���� ������ ����
		private static SqlSessionFactoryBuilder 
		      refSqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
		
		// 4. SqlSessionFactoryBuilder Ŭ������ ������� ���� ��ü�� �ּҸ�
		// ������ ������ ���� ����
		private static SqlSessionFactory refSqlSessionFactory;
		
		// 5. �⺻ ������ �Լ��� private ���� �����ڷ� �����ϱ�
		private BoardDAO() { }
		
		// 6. getInstance( ) �Լ��� ���� new �����ڸ� ����ؼ�
		// ���� Ŭ������ ���� ���ο� ��ü�� ����
		// refGuestBookDAO ���� ������ ��ü�� ���� �ּҸ� �����ϰ� ��ȯ�ϴ�
		// �Լ��� ���� �����ϱ�
		// -> �ٸ� Ŭ�������� ����� �� �ֵ��� public ���� �����ڸ� ���
		public static BoardDAO getInstance() {
			// 1) ������ ��ü�� ������ �ʽ��ϴ�!!
			// -> ���� refGuestBookDAO ���� ������ ���� �ִ� �ּҸ� �˻�
			//   -> null�� ��쿡�� ���ο� ��ü�� �����
			if(refGuestBookDAO == null) {
				// new �����ڴ� ��ü�� ������ ���� ��쿡�� ����
				refGuestBookDAO = new BoardDAO();
			}		
			// return ��ɾ�� ������ ����
			return refGuestBookDAO;
		}
		
		/*
		 * static �ʱ�ȭ ��: static ���� ��� �������� ���
		 *   -> static { }
		 *   -> ������ �Լ� ���� ���� ����
		 * 
		 * �ʱ�ȭ ��: { } : ��� ��� �������� ���
		 * 
		 * ������ �Լ�: ��� ��� �������� ���
		 */
		// static �ʱ�ȭ ���� �����
		/*
		 * 1. �Է��Ͻ� ��ɾ� ���
		 *    1) Resources Ŭ������ ����ؼ� mybatis-config.xml
		 *       ������ �б� -> ���� ������ �ּҸ� InputStream
		 *       �߻� Ŭ������ ����(���� ������ ���� �����ؼ� ���)
		 *       
		 *    2) SqlSessionFactoryBuilder Ŭ������ ���� �ִ�
		 *       build( ) �Լ��� ȣ���ؼ� ���ο� ���� ��ü�� �����
		 *       -> ��ü�� �ּҴ� refSqlSessionFactory ���� ������ ����
		 *       
		 *    3) InputStream Ŭ������ �� ����� �Ŀ��� close( )
		 *       �Լ��� ����ؼ� ������ �ݱ�(mybatis-config.xml)
		 */
		static {
			// 1. InputStream �߻� Ŭ������ ����ؼ�
			// -> mybatis-config.xml ������ ���� �ּҸ� ������ �� �ִ�
			// -> ���� ������ ����
			InputStream refInputStream = null;
			
			// 2. getResourceAsStream( ) �Լ��� ����ϰų�
			//   close() �Լ��� ����� �� IOException ���� ��Ȳ��
			//   �߻��� �� �����Ƿ� try~catch ���� �ۼ��ϱ�
			try {
		System.out.println("1. getResourceAsStream() �Լ� ȣ��");
		refInputStream = Resources.getResourceAsStream(MYBATIS_RESOURCE);
		System.out.println("2. getResourceAsStream() �Լ� ���� ����");
		System.out.println("3. ���� ��ü�� �����: build( ) �Լ��� ȣ��");
		refSqlSessionFactory = 
				refSqlSessionFactoryBuilder.build(refInputStream);
		System.out.println("4. ���� ��ü ����� ����");
			} catch(IOException refIOException) {
		System.out.println("3. ���� �б� ���� �Ǵ� �ٸ� ���ܻ�Ȳ �߻�!!");
		System.out.println("�ڼ��� ������ "+refIOException.getMessage());
		refIOException.printStackTrace();
			} finally {
				// ������ ����� �Է� ��Ʈ�� ��ü�� �ý��ۿ� ��ȯ�ϱ�
				// -> close( ) �Լ��� ȣ���ؼ� mybatis-config.xml ������ �ݱ�
				try {
					if(refInputStream != null) {
						refInputStream.close();
					}
				} catch(IOException refIOE2) {
			System.out.println("���� �ݱ� ����!!");
			System.out.println("���ܻ�Ȳ ������ "+refIOE2.getMessage());
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
		// insert sql ��ɾ �ִ� �±׿� �����ϱ� ���� ������ ����
		// -> �±��� �ּҸ� ��������
		SqlSession refSqlSession = refSqlSessionFactory.openSession();
		
		// SqlSession �������̽��� ���� �ִ� insert( ) �Լ��� ȣ��
		// -> guest_book_mapper.xml ���Ͽ��� ���� ���� �����̽� �̸���
		// -> ������ sql ��ɾ ����ִ� insert �±��� ���̵� ���
		//    -> ���ӽ����̽��̸� + ��(.) + ���̵�
		result = 
			refSqlSession.insert("dev.insertBoard", refBean);
		// insert sql ��ɾ �����ϰ�
		// ����Ŭ �����ͺ��̽��κ��� ���� ���� ���̺� ����� ���ڵ� ������
		// �˻��ϱ� -> ������ �޸𸮿� �ִ� ����
		if(result == 1) {
			// SqlSession �������̽��� ���� �ִ� commit( ) �Լ��� ����
			// ���̺� ���ο� ���ڵ带 �� ���� ����
			refSqlSession.commit();
		}
		
		// �� �޸𸮿� ������� �ִ� sql ���� ��ü�� �ý��ۿ� ��ȯ�ϱ�
		// -> close( ) �Լ��� ȣ��
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
		// ����� ������ ���� ������ ����
		BoardDTO refResult = null;
		// data_mapper.xml ���Ͽ� ������ �� ����� ���� ���� ����
		SqlSession refSqlSession = null;
		// SqlSessionFactory Ŭ�����κ��� ���� ��ü�� �ּҸ� ��������
		refSqlSession = refSqlSessionFactory.openSession();
		// SqlSession ���� �ִ� selectList( ) �Լ��� ȣ���ϱ�
		// -> ��ȯ�ϴ� Ŭ������ Object -> ArrayList Ŭ������ �� ��ȯ
		refResult = (BoardDTO)refSqlSession.selectOne("dev.selectOne", bno);
		// �ý��ۿ� ���� ��ü�� �ݳ��ϱ� -> close( ) �Լ��� ȣ��
		refSqlSession.close();
		return refResult;
	}
	
	public ArrayList<BoardDTO> selectAll() {
		// ����� ������ ���� ������ ����
		ArrayList<BoardDTO>  refResult = null;
		// data_mapper.xml ���Ͽ� ������ �� ����� ���� ���� ����
		SqlSession refSqlSession = null;
		// SqlSessionFactory Ŭ�����κ��� ���� ��ü�� �ּҸ� ��������
		refSqlSession = refSqlSessionFactory.openSession();
		// SqlSession ���� �ִ� selectList( ) �Լ��� ȣ���ϱ�
		// -> ��ȯ�ϴ� Ŭ������ Object -> ArrayList Ŭ������ �� ��ȯ
		refResult = (ArrayList)refSqlSession.selectList("dev.selectAll");
		// �ý��ۿ� ���� ��ü�� �ݳ��ϱ� -> close( ) �Լ��� ȣ��
		refSqlSession.close();
		return refResult;
	}

}
