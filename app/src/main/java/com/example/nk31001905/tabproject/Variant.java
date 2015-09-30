package com.example.nk31001905.tabproject;

/**
 * Created by nk91008743 on 30-09-2015.
 */
public class Variant {
    public String id;
    public String title;
    public String link;
    public boolean isLocal=false;
    public Variant(String id, String title,String link, boolean isLocal){
        this.id = id;
        this.title = title;
        this.link = link;
        this.isLocal = isLocal;
    }
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Variant))
            return false;
        if (obj == this)
            return true;

        Variant v = (Variant) obj;
        if(v.id.equals(this.id))
            return true;
        return false;
    }

}
