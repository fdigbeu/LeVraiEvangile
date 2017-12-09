package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class Video implements Serializable{

    @SerializedName("id")
    private int id;
    @SerializedName("urlacces")
    private String urlacces;
    @SerializedName("src")
    private String src;
    @SerializedName("titre")
    private String titre;
    @SerializedName("auteur")
    private String auteur;
    @SerializedName("duree")
    private String duree;
    @SerializedName("date")
    private String date;
    @SerializedName("type_libelle")
    private String type_libelle;
    @SerializedName("type_shortcode")
    private String type_shortcode;
    private int mipmap;

    public Video(int mipmap, String type_libelle, String type_shortcode) {
        this.mipmap = mipmap;
        this.type_libelle = type_libelle;
        this.type_shortcode = type_shortcode;
    }

    public Video(int id, String urlacces, String src, String titre, String auteur, String duree, String date, String type_libelle, String type_shortcode, int mipmap) {
        this.id = id;
        this.urlacces = urlacces;
        this.src = src;
        this.titre = titre;
        this.auteur = auteur;
        this.duree = duree;
        this.date = date;
        this.type_libelle = type_libelle;
        this.type_shortcode = type_shortcode;
        this.mipmap = mipmap;
    }

    @Override
    public String toString() {
        return "{\"auteur\":\""+auteur+"\"," +
                "\"date\":\""+date+"\"," +
                "\"duree\":\""+duree+"\"," +
                "\"urlacces\":\""+urlacces+"\"," +
                "\"type_shortcode\":\""+type_shortcode+"\"," +
                "\"src\":\""+src+"\"," +
                "\"titre\":\""+titre+"\"," +
                "\"type_libelle\":\""+type_libelle+"\"," +
                "\"mipmap\":"+mipmap+"," +
                "\"id\":"+id+"}";
    }

    public int getMipmap() {
        return mipmap;
    }

    public void setMipmap(int mipmap) {
        this.mipmap = mipmap;
    }

    public String getUrlacces() {
        return urlacces;
    }

    public void setUrlacces(String urlacces) {
        this.urlacces = urlacces;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType_libelle() {
        return type_libelle;
    }

    public void setType_libelle(String type_libelle) {
        this.type_libelle = type_libelle;
    }

    public String getType_shortcode() {
        return type_shortcode;
    }

    public void setType_shortcode(String type_shortcode) {
        this.type_shortcode = type_shortcode;
    }
}
