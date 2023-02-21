package com.ux.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ux.common.R;
import com.ux.entiry.Employee;
import com.ux.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // 接受前端传来的json 数据，需要使用RequestBody
    // 用HttpServletRequest request的原因是会儿登录成功之后，需要把employee对象的ID存到session里，表示登录成功，这样像获取当前登录用户
    // 就可以获取出来，就可以通过request对象来get一个Session

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        // 1、将页面提交的密码password进行md5加密处强
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // 对mad5 进行加密

        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw); // 使用one是因为在数据库索引里对字段加了唯一约束，查出来的数据是唯一的。

        // 3、如果没有查询到则返回登录失败结果
        if (emp == null ) {
            return R.error("登录失败");
        }

        // 4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }


        // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果 0 是禁用，1 是启用
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 清理session 中保存的当前员工ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        log.info("新增员工,{}",employee.toString());

        // 设置初始密码 123456，需要进行md5 加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 记录创建时间
        // employee.setCreateTime(LocalDateTime.now());
        // 更新时间
        // employee.setUpdateTime(LocalDateTime.now());
        // 创建人  获取当前登录用的id
        // Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
        // employee.setCreateUser(id);
        // 更新人
        // employee.setUpdateUser(id);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        // 分页构造器
        Page info = new Page(page,pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 过滤条件  将isEmpty 改成了hasLength
        queryWrapper.like(StringUtils.hasLength(name),Employee::getName,name);
        // 排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        employeeService.page(info,queryWrapper);
        return R.success(info);
    }

    /**
     * 更新
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        // log.info(employee.toString());
        // Long employee1 = (Long) httpServletRequest.getSession().getAttribute("employee");
        // employee.setUpdateUser(employee1);
        // employee.setUpdateTime(LocalDateTime.now());
        Long threadId = Thread.currentThread().getId();
        log.info("线程ID,{}",threadId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getId(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee byId = employeeService.getById(id);
        if (byId != null){
            return R.success(byId);
        }
        return R.error("没有查询到对应信息");
    }
}
