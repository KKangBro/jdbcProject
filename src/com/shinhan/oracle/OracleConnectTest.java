package com.shinhan.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleConnectTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String sql = "select * from EMPLOYEES ";
		
		// 1. driver load
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("1. driver load 성공");
		
		// 2. connection
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userid = "hr", pass = "hr";
		Connection conn = DriverManager.getConnection(url, userid, pass);
		System.out.println("2. connection 성공");
		
		// 3. statement를 통해서 SQL전송
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			System.out.println(rs.getInt(1));
			System.out.println(rs.getString(2));
			System.out.println(rs.getString(3));
			System.out.println(rs.getInt("Salary"));
			System.out.println("------------");
		}
		rs.close();
		st.close();
		conn.close();	// 자원 반납 
	}

}
