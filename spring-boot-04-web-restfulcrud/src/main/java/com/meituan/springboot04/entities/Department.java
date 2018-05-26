package com.meituan.springboot04.entities;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: cab
 * \* Date: 2018/5/22
 * \* Time: 19:20
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class Department {
    private Integer id;
    private String departmentName;

    public Department() {
    }

    public Department(Integer id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
