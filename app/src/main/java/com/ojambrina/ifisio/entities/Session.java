package com.ojambrina.ifisio.entities;

import java.io.Serializable;

public class Session implements Serializable {

    private String date;
    private String reasonTitle;
    private String explorationTitle;
    private String treatmentTitle;
    private Visit visit;
    private Exploration exploration;
    private Treatment treatment;

    public Session() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReasonTitle() {
        return reasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        this.reasonTitle = reasonTitle;
    }

    public String getExplorationTitle() {
        return explorationTitle;
    }

    public void setExplorationTitle(String explorationTitle) {
        this.explorationTitle = explorationTitle;
    }

    public String getTreatmentTitle() {
        return treatmentTitle;
    }

    public void setTreatmentTitle(String treatmentTitle) {
        this.treatmentTitle = treatmentTitle;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Exploration getExploration() {
        return exploration;
    }

    public void setExploration(Exploration exploration) {
        this.exploration = exploration;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
