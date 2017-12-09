package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class Actualite {
    @SerializedName("id")
    private int id;
    @SerializedName("titre")
    private String titre;
    @SerializedName("url_src")
    private String urlSrc;
    @SerializedName("date")
    private String date;
    @SerializedName("lien1")
    private String urlImage;
    private int mipmap;

    public Actualite(){}

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

    public String getUrlSrc() {
        return urlSrc;
    }

    public void setUrlSrc(String urlSrc) {
        this.urlSrc = urlSrc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getMipmap() {
        return mipmap;
    }

    public void setMipmap(int mipmap) {
        this.mipmap = mipmap;
    }
}
