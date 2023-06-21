package com.example.cypics;

public class writecomplaint {
    public String incident_occured,time_occured,date_occured,reporting_delay,additional_info,Scenario,Category,SubCategory,status;
    public writecomplaint(String a1, String b1, String c1, String d1, String f1, String p1, String selectedCategory, String selectedSubCategory) {
        this.incident_occured=a1;
        this.time_occured =b1;
        this.date_occured=c1;
        this.reporting_delay=d1;
        this.additional_info=f1;
        this.Scenario=p1;
        this.Category=selectedCategory;
        this.SubCategory=selectedSubCategory;
        this.status= "Investigation on process";

    }


}
