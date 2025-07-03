package com.company.paark;

import java.time.LocalDateTime;

public class Data {
    public String state, district, alpha ,  num;
    public String iddate ,fddate,fare,vtype;
    public  String phoneno;
    public boolean isWithdrawan;

    public  Data(){

    }

    public Data(String state, String district,String alpha , String num, String iddate,String fddate,String fare,String vtype,Boolean isWithdrawan,String phoneno) {
        this.state = state;
        this.district = district;
        this.alpha= alpha;
        this.phoneno = phoneno;
        this.num = num;
        this.iddate = iddate;
        this.fddate = fddate;
        this.fare=fare;
        this.vtype=vtype;
        this.isWithdrawan = isWithdrawan;

    }
}
