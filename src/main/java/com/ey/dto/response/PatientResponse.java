package com.ey.dto.response;

public class PatientResponse {

private Long patientId;
   private String name;
   private int age;
   private String gender;
   private String phone;
   private String address;
   private boolean active;
   public Long getPatientId() {
	return patientId;
   }
   public void setPatientId(Long patientId) {
	this.patientId = patientId;
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
   public String getGender() {
	return gender;
   }
   public void setGender(String gender) {
	this.gender = gender;
   }
   public String getPhone() {
	return phone;
   }
   public void setPhone(String phone) {
	this.phone = phone;
   }
   public String getAddress() {
	return address;
   }
   public void setAddress(String address) {
	this.address = address;
   }
   public boolean isActive() {
	return active;
   }
   public void setActive(boolean active) {
	this.active = active;
   }
   
   

}
