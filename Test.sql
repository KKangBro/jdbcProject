select * from EMPLOYEES where employee_id = 100;

select salary 급여, to_char(hire_date,'yyyy/mm/dd') 입사일
from employees
where first_name = initcap('adam');