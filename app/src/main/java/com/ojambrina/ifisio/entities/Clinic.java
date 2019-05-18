package com.ojambrina.ifisio.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Clinic implements Serializable {

    private String name;
    private String password;
    private String direction;
    private String identityNumber;
    private String description;
    private String image;
    private List<Patient> patientList = new ArrayList<>();
    private HashMap<String, Clinic> clinicHashMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public HashMap<String, Clinic> getClinicHashMap() {
        return clinicHashMap;
    }

    public void setClinicHashMap(HashMap<String, Clinic> clinicHashMap) {
        this.clinicHashMap = clinicHashMap;
    }

    public void getClinic() {
        String clinicName = clinicHashMap.get(name).toString();
        String clinicPassword = clinicHashMap.get(password).toString();
        String clinicDirection = clinicHashMap.get(direction).toString();
        String clinicIdentityNumber = clinicHashMap.get(identityNumber).toString();
        String clinicCescription = clinicHashMap.get(description).toString();
        String clinicImage = clinicHashMap.get(image).toString();
    }
}
