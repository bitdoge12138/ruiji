package com.chen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.R;
import com.chen.pojo.Employee;
import com.chen.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());


        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);


        if (emp == null) {
            return R.error("登录失败");

        }

        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }




    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工的信息：{}" , employee.toString());

        // 设置初始密码，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//
//        Long empID = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);

        employeeService.save(employee);

        return R.success("添加员工成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={}, pageSize={}, name={]", page, pageSize, name);


        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();   // wrapper 封装器

        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);


        employeeService.page(pageInfo, queryWrapper);


        return R.success(pageInfo);
    }


    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

//        Long empID = (Long) request.getSession().getAttribute("employee");
//
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empID);

        employeeService.updateById(employee);


        return R.success("员工修改信息完成");

    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null) return R.success(employee);
        return R.error("没有查到该员工的信息");
    }





}
