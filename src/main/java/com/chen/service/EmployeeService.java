package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.pojo.Employee;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


public interface EmployeeService extends IService<Employee> {

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> logout(HttpServletRequest request);

    R<String> updateEmployeeById(HttpServletRequest request, Employee employee);

    R<String> saveEmployee(HttpServletRequest request, Employee employee);

    R<Page> pageByEmployee(int page, int pageSize, String name);

    R<String> deleteEmployee(HttpServletRequest request, Long id);

    R<Employee> getEmployeeById(Long id);

}
