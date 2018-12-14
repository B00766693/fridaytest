package com.aisling;

import com.vaadin.data.PropertySet;

class Bus
{
    private int id;
    private String destination;
    private String feature;
    private Boolean accessible;
    private int capacity;

    Bus(int pId, String pDestination, String pFeature, Boolean pAccesible, int pCapacity){
        this.id = pId;
        this.destination = pDestination;
        this.feature = pFeature;
        this.accessible = pAccesible;
        this.capacity = pCapacity;
    }//constructor
    
    //get & 
    
    public int getId(){
        return id;
    }

    public void setId(int pId){
        this.id = pId;
    }

    public String getDestination(){
        return destination;
    }

    public void setDestination(String pDestination){
        this.destination = pDestination;
    }

    public String getFeature(){
        return feature;
    }

    public void setFeature(String pFeature){
        this.feature= pFeature;
    }

    public Boolean getIsAccessible(){
        return accessible;
    }

    public void setIsAccessible(Boolean pAccessible){
        this.accessible = pAccessible;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int pCapacity){
        this.capacity =pCapacity;
    }

}//class
