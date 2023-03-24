package aproject.model;

import java.util.List;

import aproject.vo.EmpVO;

// Service : 업무로직담당
public class EmpService {
	EmpDAO empDao = new EmpDAO();

	// sp 호출
	public EmpVO getSalaryFirstName(int empId) {
		return empDao.getSalaryFirstName(empId);
	}

	public List<EmpVO> selectAll() {
		return empDao.selectAll();
	}

	public List<EmpVO> selectLessDeptAvgSal() {
		List<EmpVO> emplist = empDao.selectLessDeptAvgSal();
		System.out.println("[EmpService] 실행건수: " + emplist.size());
		return emplist;
	}

	public EmpVO selectById(int empid) {
		return empDao.selectById(empid);
	}

	public List<EmpVO> selectByDept(int deptid) {
		return empDao.selectByDept(deptid);
	}

	public List<EmpVO> selectByCondition(int deptid, String jobid, double salary) {
		return empDao.selectByCondition(deptid, jobid, salary);
	}

	// 신규 정보 등록 (insert)
	public String empInsert(EmpVO emp) {
		int rst = empDao.empInsert(emp);
		return rst > 0 ? "입력 성공" : "입력 실패";
	}

	// 직원 정보 수정 (update)
	public String empUpdate(EmpVO emp) {
		int rst = empDao.empUpdate(emp);
		return rst > 0 ? "수정 성공" : "수정 실패";
	}

	// 한 건의 직원 삭제 (delete)
	public String empDelete(int empId) {
		int rst = empDao.empDelete(empId);
		return rst > 0 ? "삭제 성공" : "삭제 실패";
	}
}
