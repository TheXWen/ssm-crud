package com.xw.crud.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.xw.crud.bean.Employee;
import com.xw.crud.dao.DepartmentMapper;
import com.xw.crud.dao.EmployeeMapper;

/**
 * ����dao��Ĺ���
 * @author xw
 * �Ƽ�Spring����Ŀ�Ϳ���ʹ��Spring�ĵ�Ԫ���ԣ������Զ�ע��������Ҫ�����
 * 1������SpringTestģ��
 * 2��@ContextConfigurationָ��Spring�����ļ���λ��
 * 3��ֱ��autowiredҪʹ�õ��������
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	SqlSession sqlSession;
	
	/**
	 * ����DepartmentMapper
	 */
	@Test
	public void testCRUD() {
		System.out.println(departmentMapper);
		
		//1�����뼸������
//		departmentMapper.insertSelective(new Department(null, "������"));
//		departmentMapper.insertSelective(new Department(null, "���Բ�"));
		
		//2������Ա������,����Ա������
//		employeeMapper.insertSelective(new Employee(null, "M", "Jerry@xw.com", "Jerry", 1));
		
		//3������������Ա��,������ʹ�ÿ���ִ������������sqlSession
		/*for(){
			employeeMapper.insertSelective(new Employee(null, "M", "Jerry@xw.com", "Jerry", 1));
		}*/
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		for(int i = 0; i < 1000; i++){
			String uid = UUID.randomUUID().toString().substring(0, 5) + i;
			mapper.insertSelective(new Employee(null, "M", uid + "@xw.com", uid, 1));
		}
		System.out.println("�������");
	}

}
