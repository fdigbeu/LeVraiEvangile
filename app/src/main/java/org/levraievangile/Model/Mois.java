package org.levraievangile.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class Mois {
    @SerializedName("mois_chiffre")
    private int chiffre;
    @SerializedName("mois_lettre")
    private String lettre;

    public Mois(int chiffre, String lettre) {
        this.chiffre = chiffre;
        this.lettre = lettre;
    }

    public int getChiffre() {
        return chiffre;
    }

    public void setChiffre(int chiffre) {
        this.chiffre = chiffre;
    }

    public String getLettre() {
        return lettre;
    }

    public void setLettre(String lettre) {
        this.lettre = lettre;
    }
}
