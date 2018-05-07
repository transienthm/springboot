package com.meituan.bean;

public class Dog {
	private String name;
	private Integer age;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAge() {
		return this.age;
	}

	@Override
	public String toString() {
		return this.name + " : " + this.age + ".";
	}
}