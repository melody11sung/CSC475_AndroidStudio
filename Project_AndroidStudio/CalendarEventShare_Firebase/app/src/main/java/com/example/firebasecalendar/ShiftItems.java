package com.example.firebasecalendar;

public class ShiftItems {
    private String id;
    private String name;
    private String shift;
    private String writeDate;
    private String requestDate;
    private String phone;

    public ShiftItems(){
    }
    public ShiftItems(String id, String name, String duty, String requestDate, String currentDate, String phone){
        this.id = id;
        this.name = name;
        this.shift = duty;
        this.requestDate = requestDate;
        this.writeDate = currentDate;
        this.phone = phone;
    }

    public void setShift(String shift){
        this.shift = shift;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setWriteDate(String writeDate){
        this.writeDate = writeDate;
    }
    public void setRequestDate(String requestDate){
        this.requestDate = requestDate;
    }

    public String getShift() {return shift;}

    public String getName() {return name;}
    public String getWriteDate() {return writeDate;}
    public String getRequestDate() {return requestDate;}
    public String getPhone() {return phone;}
    public String getID() {return id;}


}
