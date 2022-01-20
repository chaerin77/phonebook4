package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;

@Repository
public class PhoneDao {
	
	@Autowired//연결
	private SqlSession sqlSession; 
	
	//전체 리스트 가져오기 mybatis 스타일
	public List<PersonVo> getPersonList(){
		System.out.println("PhoneDao.getPersonList()");
		
		//phonebook.xml에 써놓은 쿼리문중 select를 써야함 phonebook.xml의 namespace,id따서 이름적어야함 --*
		List<PersonVo> personList = sqlSession.selectList("phonebook.selectList"); //괄호안에 * 넣기
		System.out.println(personList);
		
		return personList;
	}
	
	
	//전화번호 추가 저장. insert
	public int personInsert(PersonVo personVo) {
		System.out.println("PhoneDao.personInsert()");
		int count = sqlSession.insert("phonebook.insert",personVo);//쿼리문의 별명 넣고 저 데이터를 얘한테 줘야함 데이터가 두개만되어도 무조건 묶어서 넘겨야함
		//return sqlSession.insert("phonebook.insert",personVo); 이렇게 쓸수도있음
		System.out.println(count + "건");
		
		return count;
	}
	
	
	//정보 삭제
	public int personDelete(int personId) {
		System.out.println("PhoneDao.personDelete");

		int count = sqlSession.delete("phonebook.delete", personId);//phonebook.xml에 있는 delete를쓸거다. 거기있는 쿼리문. 
		//넘겨줄 값이 있으면 이렇게(personId) 넘겨줘야함 지금은 숫자하나라서 그냥 넘긴거지만 만약 데이터두개이상이면 vo로묶어서 보내야함
		
		return count; 
	}
	
	
	//정보 가져오기
	public PersonVo selectPerson(int no) {
		System.out.println("PhoneDao.selectperson");
		PersonVo personVo = sqlSession.selectOne("phonebook.selectPerson", no);
		
		return personVo;
	}
	
	
	//정보 수정
	public int personUpdate(PersonVo personVo) {
		System.out.println("PhoneDao.personUpdate");
		int count = sqlSession.update("phonebook.update", personVo);
		
		return count;
	}
	
	/*
	
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;  */
	
	/*
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";*/

	/*
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			//Class.forName(driver);

			// 2. Connection 얻어오기
			//conn = DriverManager.getConnection(url, id, pw);//이 선(Conn)은 냄겨놔야함 우변에는 datasource에서 만든걸 갖고와야함
			// System.out.println("접속성공");
			conn = dataSource.getConnection();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 사람 추가
	public int personInsert(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO person ";
			query += " VALUES (seq_person_id.nextval, ?, ?, ?) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println("[" + count + "건 추가되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	
	//사람 리스트(검색안할때)
	public List<PersonVo> getPersonList() {
		return getPersonList("");
	}

	// 사람 리스트(검색할때)
	public List<PersonVo> getPersonList(String keword) {
		List<PersonVo> personList = new ArrayList<PersonVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";

			if (keword != "" || keword == null) {
				query += " where name like ? ";
				query += " or hp like  ? ";
				query += " or company like ? ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, '%' + keword + '%'); // ?(물음표) 중 1번째, 순서중요
				pstmt.setString(2, '%' + keword + '%'); // ?(물음표) 중 2번째, 순서중요
				pstmt.setString(3, '%' + keword + '%'); // ?(물음표) 중 3번째, 순서중요
			} else {
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo personVo = new PersonVo(personId, name, hp, company);
				personList.add(personVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return personList;

	}


	// 사람 수정
	public int personUpdate(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update person ";
			query += " set name = ? , ";
			query += "     hp = ? , ";
			query += "     company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, personVo.getPersonId()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	// 사람 삭제
	public int personDelete(int personId) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from person ";
			query += " where person_id = ? ";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, personId);// ?(물음표) 중 1번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
	//getPerson 만들기 - 사람 1명 정보만 가져올때
	public PersonVo getPerson(int personId){
		//PersonVo pvo = new PersonVo(name,hp,company);
		
		 PersonVo personVo = null; //PersonVo의 주소만 담을 수 있는 공간 try에서 성공하지 못해서 주소값이 안들어가는 경우가 있을수 있어서 그럴때를 대비해 null을넣어줌
		      
		 getConnection();

		 try {

		    // 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
		    String query = "";
		    query += " select  person_id, ";
		    query += "         name, ";
		    query += "         hp, ";
		    query += "         company ";
		    query += " from person ";
		    query += " where person_id = ? ";

		    pstmt = conn.prepareStatement(query); // 쿼리로 만들기

		    pstmt.setInt(1, personId); // ?(물음표) 중 1번째, 순서중요
		         
		         
		    rs = pstmt.executeQuery();

		    // 4.결과처리
		       rs.next();
		       int id = rs.getInt("person_id");
		       String name = rs.getString("name");
		       String hp = rs.getString("hp");
		       String company = rs.getString("company");

		       personVo = new PersonVo(id, name, hp, company);
		         

		 } catch (SQLException e) {
		    System.out.println("error:" + e);
		 }

		 close();

		 return personVo;

		}*/
	}

