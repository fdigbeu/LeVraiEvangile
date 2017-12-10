package org.levraievangile.Model;

/**
 * Created by Maranatha on 28/11/2017.
 */

public class Setting {
    private String title;
    private String libelle;
    private boolean choice;

    public Setting(String title, String libelle, boolean choice) {
        this.title = title;
        this.libelle = libelle;
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "{\"title\":\""+title+"\"," +
                "\"libelle\":\""+libelle+"\"," +
                "\"choice\":"+choice+"}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean getChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }
}
