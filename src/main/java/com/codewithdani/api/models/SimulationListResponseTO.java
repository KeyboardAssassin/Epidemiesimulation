package com.codewithdani.api.models;

public class SimulationListResponseTO {
    private String uuid;
    private String date;

    public SimulationListResponseTO(String uuid, String date) {
        this.uuid = uuid;
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDate() {
        return date;
    }
}