package com.xw.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xw.crud.bean.Employee;
import com.xw.crud.bean.Msg;
import com.xw.crud.service.EmployeeService;

/**
 * ����Ա��CRUD����
 * @author xw
 *
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	/**
	 * ������������һ
	 * ����ɾ��:1-2-3
	 * ����ɾ��:1
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{ids}", method=RequestMethod.DELETE)
	public Msg deleteEmp(@PathVariable("ids")String ids) {
		//����ɾ��
		if(ids.contains("-")){
			List<Integer> del_ids = new ArrayList<Integer>();
			String[] str_ids = ids.split("-");
			//��װid
			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else{
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}
	
	/**
	 * ���ֱ�ӷ���ajax=PUT��ʽ������
	 * ��װ������
	 * Employee [empId=1013, gender=null, email=null, empName=null, dId=null, department=null]
	 * 
	 * ����:
	 * ��������������
	 * ����Emplloyee�����װ����
	 * update tbl_emp	where emp_id = 1014
	 * 
	 * ԭ��:
	 * Tomcat:
	 * 		1�����������е�����,��װһ��map
	 * 		2��request.getParameter("empName")�ͻ�����map��ȡֵ
	 * 		3��SpeingMVC��װPOJO�����ʱ��
	 * 				���POJO��ÿ�����Ե�ֵ:request.getParameter("email")
	 * AJAX����PUT����������Ѫ��:
	 * 		PUT����,�������е�����,request.getParameter("email")�ò���
	 * 		Tomcatһ����PUT�����װ�������е�����Ϊmap,ֻ��POST��ʽ������ŷ�װ������Ϊmap
	 * 
	 * �������:
	 * ����Ҫ��֧��ֱ�ӷ���PUT֮��������з�װ�������е�����
	 * 1��������HttpPutFormContentFilter
	 * 2����������:���������е����ݽ�����װ��һ��map
	 * 3��request�����°�װ,request.getParameter()����д,�ͻ���Լ���װ��map��ȡ����
	 * Ա�����·���
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{empId}", method=RequestMethod.PUT)
	public Msg saveEmp(Employee employee, HttpServletRequest request) {
		System.out.println("�������е�ֵ:" + request.getParameter("gender"));
		System.out.println("��Ҫ���µ�Ա�����ݣ�" + employee);
		Employee employee2 = employeeService.getEmp(employee.getEmpId());
		//���ж��û����Ƿ��ǺϷ��ı���ʽ
		String empName = employee2.getEmpName();
				String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
				if(!empName.matches(regx)){
					return Msg.fail().add("va_msg", "�û�������6-16λ���ֺ���ĸ����ϻ���2-5λ����");
				}
					employeeService.updateEmp(employee);
					return Msg.success();
			
	}
	
	/**
	 * ����id��ѯԱ��
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}
	
	/**
	 * ����û����Ƿ����
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkuser")
	public Msg checkuser(@RequestParam("empName")String empName) {
		//���ж��û����Ƿ��ǺϷ��ı���ʽ
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		boolean b = employeeService.checkUser(empName);
		if(!empName.matches(regx)){
			return Msg.fail().add("va_msg", "�û�������6-16λ���ֺ���ĸ����ϻ���2-5λ����");
		}
		
		//���ݿ��û����ظ�У��
		if(b){
			return Msg.success();
		}else {
			return Msg.fail().add("va_msg", "�û���������");
		}
	}
	
	/**
	 * Ա������
	 * @return
	 */
	@RequestMapping(value="/emp", method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result){
		if(result.hasErrors()){
			//У��ʧ��,Ӧ�÷���ʧ��,��ģ̬������ʾУ��ʧ�ܵ���ʾ��Ϣ
			Map<String, Object> map = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("������ֶ���" + fieldError.getField());
				System.out.println("������Ϣ" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}
	
	/**
	 * ����jackson��
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn", defaultValue="1")Integer pn) {
		//�ⲻ��һ����ҳ��ѯ
		//����PageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ����,����ҳ��,�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		//ʹ��PageInfo��װ��ѯ��Ľ��,ֻ��Ҫ��PageInfo����ҳ�������
		//��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ����������,����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo", page);
	}
	
	/**
	 * ��ѯԱ������(��ҳ��ѯ)
	 * @return
	 */
//	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn", defaultValue="1")Integer pn, Model model) {
		//�ⲻ��һ����ҳ��ѯ
		//����PageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ����,����ҳ��,�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startPage��������������ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		//ʹ��PageInfo��װ��ѯ��Ľ��,ֻ��Ҫ��PageInfo����ҳ�������
		//��װ����ϸ�ķ�ҳ��Ϣ,���������ǲ�ѯ����������,����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps, 5);
		model.addAttribute("pageInfo", page);
		
		return "list";
	}

}