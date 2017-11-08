package com.whereyou.techease.controllers;

/**
 * Created by kaxhiftaj on 4/3/17.
 */

public class Contestents {

    private String locationName;
    private Double locationLatLng;
    private String locationCurrent;
    private String locationComment;
    private String loctionDistance;


    public String getLoctionDistance() {
        return loctionDistance;
    }

    public String getLocationName() {return locationName;}

    public Double getLocationLatLng() {
        return locationLatLng;
    }

    public String getLocationComment() {
        return locationComment;
    }

    public String getLocationCurrent() {
        return locationCurrent;
    }


    public void setLocationName(String locationName) {

        this.locationName = locationName;
    }

    public void setLocationComment(String locationComment) {
        this.locationComment = locationComment;
    }

    public void setLocationCurrent(String locationCurrent) {
        this.locationCurrent = locationCurrent;
    }


    public void setLoctionDistance(String loctionDistance) {
        this.loctionDistance = loctionDistance;
    }

    public void setLocationLatLng(Double locationLatLng) {
        this.locationLatLng = locationLatLng;
    }
}
