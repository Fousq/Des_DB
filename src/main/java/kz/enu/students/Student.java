package kz.enu.students;

import java.time.LocalDate;

public class Student {
    private int id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String studyGroup;
    private String phone;
    private String country;
    private String city;
    private String address;
    private LocalDate birthday;
    private String gender;
    private double CNAverageScore;
    private double NMAverageScore;
    private double MMAverageScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(String studyGroup) {
        this.studyGroup = studyGroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getCNAverageScore() {
        return CNAverageScore;
    }

    public void setCNAverageScore(double CNAverageScore) {
        this.CNAverageScore = CNAverageScore;
    }

    public double getNMAverageScore() {
        return NMAverageScore;
    }

    public void setNMAverageScore(double NMAverageScore) {
        this.NMAverageScore = NMAverageScore;
    }

    public double getMMAverageScore() {
        return MMAverageScore;
    }

    public void setMMAverageScore(double MMAverageScore) {
        this.MMAverageScore = MMAverageScore;
    }
}
