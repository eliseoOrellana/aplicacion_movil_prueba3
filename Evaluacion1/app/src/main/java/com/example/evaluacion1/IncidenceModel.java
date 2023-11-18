package com.example.evaluacion1;

import java.io.Serializable;

public class IncidenceModel implements Serializable {
    private String name, rut, laboratory, incidenceBody, time, date;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIncidenceBody() {
        return incidenceBody;
    }

    public void setIncidenceBody(String incidenceBody) {
        this.incidenceBody = incidenceBody;
    }

}

