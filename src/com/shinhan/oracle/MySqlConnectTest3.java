package com.shinhan.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MySqlConnectTest3 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.print("조회하고자하는 직원이름입력>> ");
		Scanner sc = new Scanner(System.in);
		String inputName = sc.nextLine();
		String sql = "select first_name 이름, last_name 성, salary 급여, hire_date 입사일\r\n"
				+ "from employees\r\n"
				+ "where lower(first_name) = lower('" + inputName + "')";
		
		// 1. driver load
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("1. driver load 성공");
		
		// 2. connection
		String url = "jdbc:mysql://localhost/hr"; // localhost == 127.0.0.1
		String userid = "hr", pass = "hr";
		Connection conn = DriverManager.getConnection(url, userid, pass);
		System.out.println("2. connection 성공");
		
		// 3. statement를 통해서 SQL전송
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()) {
			System.out.println(rs.getString("이름"));
			System.out.println(rs.getString("성"));
			System.out.println(rs.getInt("급여"));
			System.out.println(rs.getDate("입사일"));
			System.out.println("------------");
		}
		rs.close();
		st.close();
		conn.close();	// 자원 반납 
	}

}
