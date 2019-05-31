package com.ojambrina.ifisio.entities;

import java.io.Serializable;
import java.util.List;

public class Session implements Serializable {

    private String date;
    private List<String> reasonList;
    private List<String> explorationList;
    private List<String> treatmentList;

    public Session() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<String> reasonList) {
        this.reasonList = reasonList;
    }

    public List<String> getExplorationList() {
        return explorationList;
    }

    public void setExplorationList(List<String> explorationList) {
        this.explorationList = explorationList;
    }

    public List<String> getTreatmentList() {
        return treatmentList;
    }

    public void setTreatmentList(List<String> treatmentList) {
        this.treatmentList = treatmentList;
    }
}
