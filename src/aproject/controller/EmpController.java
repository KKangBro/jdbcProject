package aproject.controller;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.shinhan.oracle.DateUtil;

import aproject.model.EmpService;
import aproject.view.EmpView;
import aproject.vo.EmpVO;

public class EmpController {

	public static void main(String[] args) {
		EmpService eService = new EmpService();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("1. 모든 직원 조회");
			System.out.println("2. 직원 상세 조회");
			System.out.println("3. 특정 부서의 직원 조회");
			System.out.println("4. 부서, 직책, 급여(이상)인 직원 조회");
			System.out.println("5. 부서의 평균 급여 미만인 직원 조회");
			System.out.println("6. 신규 직원 등록");
			System.out.println("7. 직원 정보 수정");
			System.out.println("8. 직원 정보 삭제");
			System.out.println("9. 특정 직원의 급여 조회(SP)");
			System.out.println("exit.. 종료");
			System.out.print("=== 작업을 선택해주세요>> ");
			String menu = sc.nextLine();

			if (menu.equals("exit"))
				break;

			switch (menu) {
			case "1": {
				EmpView.print(eService.selectAll());
				break;
			}
			case "2": {
				System.out.print("조회할 직원번호>> ");
				int empid = Integer.parseInt(sc.nextLine());
				EmpView.print(eService.selectById(empid));
				break;
			}
			case "3": {
				System.out.print("조회할 부서번호>> ");
				int deptid = Integer.parseInt(sc.nextLine());
				EmpView.print(eService.selectByDept(deptid));
				break;
			}
			case "4": {
				System.out.print("조회할 부서번호, 직책, 급여(이상)>> ");
				int deptid = sc.nextInt();
				String jobid = sc.next();
				double salary = sc.nextDouble();
				sc.nextLine();
				EmpView.print(eService.selectByCondition(deptid, jobid, salary));
				break;
			}
			case "5": {
				List<EmpVO> empList = eService.selectLessDeptAvgSal();
				System.out.println("[Controller] 5번 작업: " + empList.size());
				EmpView.print(empList);
				break;
			}
			case "6": {
				EmpVO emp = makeEmp(sc);
				EmpView.print(eService.empInsert(emp));
				break;
			}
			case "7": {
				System.out.print("== 수정할 직원 ID>> ");
				int empId = Integer.parseInt(sc.nextLine());
				EmpView.print(eService.selectById(empId));

				EmpVO emp = makeEmp2(sc, empId);
				EmpView.print(eService.empUpdate(emp));
				break;
			}
			case "8": {
				System.out.print("== 삭제할 직원 ID>> ");
				int empId = Integer.parseInt(sc.nextLine());

				EmpView.print(eService.selectById(empId));

				System.out.print("정말로 삭제하시겠습니까? [y/n]\t");
				String yesno = sc.nextLine();
				if (yesno.equals("y") || yesno.endsWith("Y"))
					EmpView.print(eService.empDelete(empId));
				break;
			}
			case "9": {
				System.out.print("조회할 직원번호>> ");
				int empid = Integer.parseInt(sc.nextLine());
				EmpView.print("급여는: " + eService.getSalaryFirstName(empid));
				break;
			}
			default:
				System.out.println("다시 입력해주세요..");
			}
			System.out.println();
		}
		System.out.println("수고링..");
		sc.close();
	}

	private static EmpVO makeEmp(Scanner sc) {
		System.out.print("1. 신규 직원의 FIRST_NAME>> ");
		String fName = sc.next();
		System.out.print("2. 신규 직원의 LAST_NAME>> ");
		String lName = sc.next();
		System.out.print("3. 신규 직원의 EMAIL>> ");
		String email = sc.next();
		System.out.print("4. 신규 직원의 PHONE_NUMBER>> ");
		String phoneNum = sc.next();
		System.out.print("5. 신규 직원의 HIRE_DATE(yyyy/mm/dd)>> ");
		String hireDateStr = sc.next();
		Date hireDate = DateUtil.convertToDate(hireDateStr); // String -> Date
		System.out.print("6. 신규 직원의 job_id>> ");
		String jobId = sc.next();
		System.out.print("7. 신규 직원의 SALARY>> ");
		double salary = sc.nextDouble();
		System.out.print("8. 신규 직원의 COMMISSION_PCT>> ");
		double commPct = sc.nextDouble();
		System.out.print("9. 신규 직원의 MANAGER_ID>> ");
		int managerId = sc.nextInt();
		System.out.print("10. 신규 직원의 DEPARTMENT_ID>> ");
		int deptId = sc.nextInt();

		EmpVO emp = new EmpVO();
		emp.setFirst_name(fName);
		emp.setLast_name(lName);
		emp.setEmail(email);
		emp.setPhone_number(phoneNum);
		emp.setHire_date(hireDate);
		emp.setJob_id(jobId);
		emp.setSalary(salary);
		emp.setCommission_pct(commPct);
		emp.setManager_id(managerId);
		emp.setDepartment_id(deptId);
		sc.nextLine();
		return emp;
	}

	private static EmpVO makeEmp2(Scanner sc, int empId) {
		System.out.print("1. 수정할 직원의 EMAIL>> ");
		String email = sc.next();
		System.out.print("2. 수정할 직원의 job_id>> ");
		String jobId = sc.next();
		System.out.print("3. 수정할 직원의 SALARY>> ");
		double salary = sc.nextDouble();
		System.out.print("4. 수정할 직원의 DEPARTMENT_ID>> ");
		int deptId = sc.nextInt();
		System.out.print("5. 신규 직원의 HIRE_DATE(yyyy/mm/dd)>> ");
		String hireDateStr = sc.next();
		Date hireDate = DateUtil.convertToDate(hireDateStr); // String -> Date
		System.out.print("6. 신규 직원의 MANAGER_ID>> ");
		int managerId = sc.nextInt();

		EmpVO emp = new EmpVO();
		emp.setEmployee_id(empId);
		emp.setEmail(email);
		emp.setJob_id(jobId);
		emp.setSalary(salary);
		emp.setDepartment_id(deptId);
		emp.setHire_date(hireDate);
		emp.setManager_id(managerId);

		sc.nextLine();
		return emp;
	}

}
