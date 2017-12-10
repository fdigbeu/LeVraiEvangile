package org.levraievangile.Model;

/**
 * Created by Maranatha on 10/12/2017.
 */

public class JsonReturn {
    private String code_retour;
    private String code_message;
    //--
    public JsonReturn(String code_retour, String code_message) {
        this.code_retour = code_retour;
        this.code_message = code_message;
    }
    //--

    public String getCode_retour() {
        return code_retour;
    }

    public String getCode_message() {
        return code_message;
    }
}