package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maranatha on 05/12/2017.
 */

public class Annee {

    private int mipmap;
    @SerializedName("annee")
    private String annee;

    public Annee(int mipmap, String annee) {
        this.mipmap = mipmap;
        this.annee = annee;
    }

    @Override
    public String toString() {
        return "{\"annee\":\"" + annee +
                "\",\"mipmap\":\"" + mipmap + "\"}";
    }

    public int getMipmap() {
        return mipmap;
    }

    public void setMipmap(int mipmap) {
        this.mipmap = mipmap;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
