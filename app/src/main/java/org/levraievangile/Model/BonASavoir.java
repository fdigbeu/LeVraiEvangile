package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class BonASavoir {
    @SerializedName("id")
    private int id;
    @SerializedName("titre")
    private String titre;
    @SerializedName("nbvue")
    private int nbvue;
    @SerializedName("type_media")
    private String type_media;
    @SerializedName("etat")
    private boolean etat;
    @SerializedName("date")
    private String date;
    protected int mipmap;

    public BonASavoir(int id, String titre, int nbvue, String type_media, boolean etat, String date, int mipmap) {
        this.id = id;
        this.titre = titre;
        this.nbvue = nbvue;
        this.type_media = type_media;
        this.etat = etat;
        this.date = date;
        this.mipmap = mipmap;
    }

    @Override
    public String toString() {
        return "{\"date\":\"" + date + "\"," +
                "\"type_media\":\"" + type_media +
                "\",\"titre\":\"" + titre +
                "\",\"mipmap\":\"" + mipmap +
                "\",\"nbvue\":\"" + nbvue +
                "\",\"id\":\"" + id +
                "\",\"etat\":\"" + etat + "\"}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getNbvue() {
        return nbvue;
    }

    public void setNbvue(int nbvue) {
        this.nbvue = nbvue;
    }

    public String getType_media() {
        return type_media;
    }

    public void setType_media(String type_media) {
        this.type_media = type_media;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMipmap() {
        return mipmap;
    }

    public void setMipmap(int mipmap) {
        this.mipmap = mipmap;
    }
}
