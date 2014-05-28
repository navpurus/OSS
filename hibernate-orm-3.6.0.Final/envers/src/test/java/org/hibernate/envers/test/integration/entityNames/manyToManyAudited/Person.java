package org.hibernate.envers.test.integration.entityNames.manyToManyAudited;

import java.util.List;

import org.hibernate.envers.Audited;

/**
 * @author Hern�n Chanfreau
 * 
 */

@Audited
public class Person {
	
	private long id;
	
	private String name;
	
	private int age;
	
	private List<Car> cars;
	
	public Person(){ }
	
	public Person(String name, int age){
		this.name = name;
		this.age = age;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

}
