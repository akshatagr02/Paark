package com.company.paark;


import java.util.Dictionary;
import java.util.Hashtable;

public class profileData {

    public String T20,T30,T45,Tperh,latefeeT;
    public String f20,f30,f45,fperh,latefeeF;


    public profileData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public profileData(String T20, String T30,String T45,String Tperh, String f20,String f30,String f45, String fperh,String latefeeT,String latefeeF) {
        this.T20 = T20;
        this.T30 = T30;
        this.T45 = T45;
        this.Tperh = Tperh;
        this.latefeeT = latefeeT;


        this.f20 = f20;
        this.f30 = f30;
        this.f45 = f45;
        this.fperh = fperh;
        this.latefeeF =latefeeF;
    }

}