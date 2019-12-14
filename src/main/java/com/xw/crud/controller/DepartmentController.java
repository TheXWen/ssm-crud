package com.xw.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xw.crud.bean.Department;
import com.xw.crud.bean.Msg;
import com.xw.crud.service.DepartmentService;

/**
 * ����Ͳ����йص�����
 * @author xw
 *
 */
@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * �������еĲ�����Ϣ
	 */
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts() {
		//��������в�����Ϣ
		List<Department> list = departmentService.getDepts();
		return Msg.success().add("depts", list);
	}

}
