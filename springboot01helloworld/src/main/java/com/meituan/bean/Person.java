package com.meituan.bean;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.*;


/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties :告诉Spring Boot将本类中的所有属性和配置文件中相关的配置进行绑定
 * 	perfix = "person" : 配置文件中哪个下面的所有属性进行一一映射
 * 	 只要该组件是容器中的组件，才能使用容器中的功能
 *
 * 	 @ConfigurationProperties(prefix="person)  默认从全局配置文件中获取值
 */
@Component
@ConfigurationProperties(prefix = "person")
@PropertySource(value = {"classpath:person.properties"})
public class Person {
	/**
	 * <bean class="Person">
	 * <property name="lastName" value="字面量/${key}/#{SpEL}从环境变量、配置文件中取值" />
	 * </bean>
	 */
//    @Value("${person.last-name}")
	//lastName必须是email
	private String lastName;
	private Integer age;
	private Boolean boss;
	private Date birth;

	private Map<String, Object> maps;
	private List<Object> lists;
	private Dog dog;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getBoss() {
		return boss;
	}

	public void setBoss(Boolean boss) {
		this.boss = boss;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Map<String, Object> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, Object> maps) {
		this.maps = maps;
	}

	public List<Object> getLists() {
		return lists;
	}

	public void setLists(List<Object> lists) {
		this.lists = lists;
	}

	public Dog getDog() {
		return dog;
	}

	public void setDog(Dog dog) {
		this.dog = dog;
	}

	@Override
	public String toString() {
		return "Person{" +
				"lastName='" + lastName + '\'' +
				", age=" + age +
				", boss=" + boss +
				", birth=" + birth +
				", maps=" + maps +
				", lists=" + lists +
				", dog=" + dog +
				'}';
	}
}