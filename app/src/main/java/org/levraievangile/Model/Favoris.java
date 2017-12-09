package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class Favoris implements Serializable{
    private int id;
    private String type;
    private String urlacces;
    private String src;
    private String titre;
    private String auteur;
    private String duree;
    private String date;
    private String type_libelle;
    private String type_shortcode;
    private int mipmap;
    private int ressource_id; // VideoId, AudioId

    public Favoris(int id, String type, String urlacces, String src, String titre, String auteur, String duree, String date, String type_libelle, String type_shortcode, int mipmap, int ressource_id) {
        this.id = id;
        this.type = type;
        this.urlacces = urlacces;
        this.src = src;
        this.titre = titre;
        this.auteur = auteur;
        this.duree = duree;
        this.date = date;
        this.type_libelle = type_libelle;
        this.type_shortcode = type_shortcode;
        this.mipmap = mipmap;
        this.ressource_id = ressource_id;
    }

    @Override
    public String toString() {
        return "{\"auteur\":\""+auteur+"\"," +
                "\"date\":\""+date+"\"," +
                "\"type\":\""+type+"\"," +
                "\"duree\":\""+duree+"\"," +
                "\"urlacces\":\""+urlacces+"\"," +
                "\"type_shortcode\":\""+type_shortcode+"\"," +
                "\"src\":\""+src+"\"," +
                "\"titre\":\""+titre+"\"," +
                "\"type_libelle\":\""+type_libelle+"\"," +
                "\"mipmap\":"+mipmap+"," +
                "\"ressource_id\":"+ressource_id+"," +
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRessource_id() {
        return ressource_id;
    }

    public void setRessource_id(int ressource_id) {
        this.ressource_id = ressource_id;
    }

    public int getId() {
        return id;
    }
}
