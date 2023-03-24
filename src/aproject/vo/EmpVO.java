package aproject.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// JavaBeans기술 : 변수 접근지정자는 private로 한다. setter/getter, 기본생성자가 있어야한다.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpVO {
	private int employee_id;
	private String first_name;
	private String last_name;
	private String email;
	private String phone_number;
	private Date hire_date;
	private String job_id;
	private double salary;
	private double commission_pct;
	private int manager_id;
	private int department_id;
}
