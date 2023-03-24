package aproject.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

import com.shinhan.dbutil.OracleUtil;

import aproject.vo.EmpVO;

// DAO(Data Access Object): DB업무.. CRUD.. insert, select, update, delete
public class EmpDAO {
	Connection conn;
	Statement st;
	PreparedStatement pst; // ? 지원
	CallableStatement cst; // sp 지원
	ResultSet rs;
	int rstCount; // insert, update, delete 건수

	// sp 호출
	public EmpVO getSalaryFirstName(int empId) {
		String sql = "{call sp_salary ( ?, ?, ? )}";
		conn = OracleUtil.getConnection();
		EmpVO emp = new EmpVO();
		try {
			cst = conn.prepareCall(sql);
			cst.setInt(1, empId);
			cst.registerOutParameter(2, Types.DOUBLE);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.execute(); // resultset 있으면 true, 없으면 false
//			System.out.println("[DAO execute]");
			emp.setSalary(cst.getDouble(2));
			emp.setFirst_name(cst.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return emp;
	}

	public List<EmpVO> selectAll() {
//		String sql = "select * from employees";
		String sql = """
				select	employee_id,
						first_name,
						last_name,
						email,
						phone_number,
						hire_date,
						job_id,
						f_tax(salary) salary,
						commission_pct,
						manager_id,
						department_id
				from employees
				order by 1
				""";
		
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for (int i = 1; i <= count; i++) {
				System.out.println("칼럼이름:" + meta.getColumnName(i));
			}

			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

	// 자신의 속한 부서의 평균 급여보다 더 적은 급여를 받는 직원들을 조회하시오
	public List<EmpVO> selectLessDeptAvgSal() {
		String sql = """
				select e.employee_id, e.first_name, e.salary, e.department_id
				from employees e, (select department_id, avg(salary) avgsal
				                    from employees
				                    group by department_id) deptavg
				where e.department_id = deptavg.department_id
				and e.salary < deptavg.avgsal
				""";
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpVO emp = makeEmp2(rs);
				emplist.add(emp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

	// 특정 직원 조회
	public EmpVO selectById(int empid) {
		String sql = "select * from employees where employee_id = " + empid;
		EmpVO emp = null;
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			if (rs.next()) {
				emp = makeEmp(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emp;
	}

	// 특정 부서의 직원 조회
	public List<EmpVO> selectByDept(int deptid) {
		String sql = "select * from employees where department_id = " + deptid;
		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, st, conn);
		}

		return emplist;
	}

	// 특정 부서, jobid, salary이상 직원 조회
	public List<EmpVO> selectByCondition(int deptid, String jobid, double salary) {
		String sql = "select * from employees where department_id = ? and job_id = ? and salary >= ?";

		List<EmpVO> emplist = new ArrayList<>();
		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, deptid);
			pst.setString(2, jobid);
			pst.setDouble(3, salary);
			rs = pst.executeQuery();

			while (rs.next()) {
				EmpVO emp = makeEmp(rs);
				emplist.add(emp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(rs, pst, conn); // PreparedStatement가 Statement의 자식이라서 가능
		}

		return emplist;
	}

	// 신규 직원 등록 (insert)
	public int empInsert(EmpVO emp) {
		Scanner sc = new Scanner(System.in);
		String sql = """
				insert into employees values(seq_employee.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, emp.getFirst_name());
			pst.setString(2, emp.getLast_name());
			pst.setString(3, emp.getEmail());
			pst.setString(4, emp.getPhone_number());
			pst.setDate(5, emp.getHire_date());
			pst.setString(6, emp.getJob_id());
			pst.setDouble(7, emp.getSalary());
			pst.setDouble(8, emp.getCommission_pct());
			pst.setInt(9, emp.getManager_id());
			pst.setInt(10, emp.getDepartment_id());

			rstCount = pst.executeUpdate(); // DML문장 실행한다. 영향받은 건수가 return

		} catch (SQLException e) {
			rstCount = -1;
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		System.out.println("insert결과: " + rstCount);
		return rstCount;
	}

	// 직원 정보 수정 (Update)
	public int empUpdate(EmpVO emp) {
		Scanner sc = new Scanner(System.in);
		String sql = """
				update employees
				set email = ?, department_id = ?, job_id = ?, salary = ?,
					hire_date = ?, manager_id = ?
				where employee_id = ?
				""";

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, emp.getEmail());
			pst.setInt(2, emp.getDepartment_id());
			pst.setString(3, emp.getJob_id());
			pst.setDouble(4, emp.getSalary());
			pst.setDate(5, emp.getHire_date());
			pst.setInt(6, emp.getManager_id());
			pst.setInt(7, emp.getEmployee_id());

			rstCount = pst.executeUpdate(); // DML문장 실행한다. 영향받은 건수가 return

		} catch (SQLException e) {
			rstCount = -1;
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		System.out.println("update결과: " + rstCount);
		return rstCount;
	}

	// 한 건의 직원 삭제 (Delete)
	public int empDelete(int empId) {
		String sql = """
				delete from employees
				where employee_id = ?
				""";

		conn = OracleUtil.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, empId);

			rstCount = pst.executeUpdate(); // DML문장 실행한다. 영향받은 건수가 return

		} catch (SQLException e) {
			rstCount = -1;
			e.printStackTrace();
		} finally {
			OracleUtil.dbDisconnect(null, pst, conn);
		}

		System.out.println("delete결과: " + rstCount);
		return rstCount;
	}

	private EmpVO makeEmp(ResultSet rs) throws SQLException {
		EmpVO emp = new EmpVO();
		emp.setCommission_pct(rs.getDouble("Commission_pct"));
		emp.setDepartment_id(rs.getInt("Department_id"));
		emp.setEmail(rs.getString("Email"));
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setHire_date(rs.getDate("Hire_date"));
		emp.setJob_id(rs.getString("Job_id"));
		emp.setLast_name(rs.getString("Last_name"));
		emp.setManager_id(rs.getInt("Manager_id"));
		emp.setPhone_number(rs.getString("Phone_number"));
		emp.setSalary(rs.getDouble("Salary"));

		return emp;
	}

	private EmpVO makeEmp2(ResultSet rs) throws SQLException {
		EmpVO emp = new EmpVO();
		emp.setDepartment_id(rs.getInt("Department_id"));
		emp.setEmployee_id(rs.getInt("Employee_id"));
		emp.setFirst_name(rs.getString("First_name"));
		emp.setSalary(rs.getDouble("Salary"));

		return emp;
	}

}
