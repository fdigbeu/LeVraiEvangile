package crud;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import classes.Audio;
import classes.Connexion;
import classes.Favoris;
import classes.Pdf;
import classes.Video;

/**
 * Created by JESUS EST YAHWEH on 07/01/2017.
 */

public class CRUDFavoris {

    private final String table_name = "lve_favoris";
    private Connexion connexion;
    private Context context;
    private Favoris favoris;
    private String orderSql = "DESC";

    public CRUDFavoris(Context context){
        this.context = context;
    }

    public CRUDFavoris(Context context, Favoris favoris){
        this.context = context;
        this.favoris = favoris;
    }


    public void creerTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR, mipmap VARCHAR, urlacces VARCHAR, src VARCHAR, titre VARCHAR, auteur VARCHAR, duree VARCHAR, date VARCHAR, type_libelle VARCHAR, type_shortcode VARCHAR, ressource_id VARCHAR);";
        connexion = new Connexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void ajouter(){
        creerTable();
        String sql = "";
        switch (favoris.getType()){
            case "video":
                sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                        " VALUES ('"+favoris.getType()+"', '"+favoris.getVideo().getMipmap()+"', '"+favoris.getVideo().getUrlacces()+"', '"+favoris.getVideo().getSrc()+"', '"+favoris.getVideo().getTitre().replace("'", "''")+"', '"+favoris.getVideo().getAuteur().replace("'", "''")+"', '"+favoris.getVideo().getDuree()+"', '"+favoris.getVideo().getDate()+"', '"+favoris.getVideo().getType_libelle()+"', '"+favoris.getVideo().getType_shortcode()+"', '"+favoris.getVideo().getId()+"');";
                break;
            case "audio":
                sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                        " VALUES ('"+favoris.getType()+"', '"+favoris.getAudio().getMipmap()+"', '"+favoris.getAudio().getUrlacces()+"', '"+favoris.getAudio().getSrc()+"', '"+favoris.getAudio().getTitre().replace("'", "''")+"', '"+favoris.getAudio().getAuteur().replace("'", "''")+"', '"+favoris.getAudio().getDuree()+"', '"+favoris.getAudio().getDate()+"', '"+favoris.getAudio().getType_libelle()+"', '"+favoris.getAudio().getType_shortcode()+"', '"+favoris.getAudio().getId()+"');";
                break;
            case "pdf":
                sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                        " VALUES ('"+favoris.getType()+"', '"+favoris.getPdf().getMipmap()+"', '"+favoris.getPdf().getUrlacces()+"', '"+favoris.getPdf().getSrc()+"', '"+favoris.getPdf().getTitre().replace("'", "''")+"', '"+favoris.getPdf().getAuteur().replace("'", "''")+"', '00:00:00', '"+favoris.getPdf().getDate()+"', '"+favoris.getPdf().getType_libelle()+"', '"+favoris.getPdf().getType_shortcode()+"', '"+favoris.getPdf().getId()+"');";
                break;
        }
        connexion.getDb().execSQL(sql);
    }

    public void ajouterVideo(String type, String mipmap, String urlacces, String src, String titre, String auteur, String duree, String date, String type_libelle, String type_shortcode, String ressource_id){
        creerTable();
        String sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                " VALUES ('"+type+"', '"+mipmap+"', '"+urlacces+"', '"+src+"', '"+titre.replace("'", "''")+"', '"+auteur.replace("'", "''")+"', '"+duree+"', '"+date+"', '"+type_libelle+"', '"+type_shortcode+"', '"+ressource_id+"');";
        connexion.getDb().execSQL(sql);
    }

    public void ajouterAudio(String type, String mipmap, String urlacces, String src, String titre, String auteur, String duree, String date, String type_libelle, String type_shortcode, String ressource_id){
        creerTable();
        String sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                " VALUES ('"+type+"', '"+mipmap+"', '"+urlacces+"', '"+src+"', '"+titre.replace("'", "''")+"', '"+auteur.replace("'", "''")+"', '"+duree+"', '"+date+"', '"+type_libelle+"', '"+type_shortcode+"', '"+ressource_id+"');";
        connexion.getDb().execSQL(sql);
    }

    public void ajouterPdf(String type, String mipmap, String urlacces, String src, String titre, String auteur, String date, String type_libelle, String type_shortcode, String ressource_id){
        creerTable();
        String sql = "INSERT INTO " + table_name + " (type, mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, ressource_id)" +
                " VALUES ('"+type+"', '"+mipmap+"', '"+urlacces+"', '"+src+"', '"+titre.replace("'", "''")+"', '"+auteur.replace("'", "''")+"', '00:00:00', '"+date+"', '"+type_libelle+"', '"+type_shortcode+"', '"+ressource_id+"');";
        connexion.getDb().execSQL(sql);
    }

    public ArrayList<Favoris> listerVideo(){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'video' ORDER BY id "+orderSql, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            resultat.add(new Favoris(Integer.parseInt(id), type, new Video(Integer.parseInt(ressource_id), Integer.parseInt(mipmap), urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode)));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public ArrayList<Favoris> listerAudio(){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'audio' ORDER BY id "+orderSql, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            resultat.add(new Favoris(Integer.parseInt(id), type, new Audio(Integer.parseInt(ressource_id), Integer.parseInt(mipmap), urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode)));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public ArrayList<Favoris> listerPdf(){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'pdf' ORDER BY id "+orderSql, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            resultat.add(new Favoris(Integer.parseInt(id), type, new Pdf(Integer.parseInt(ressource_id), Integer.parseInt(mipmap), urlacces, src, titre, auteur, date, type_libelle, type_shortcode)));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public Favoris infosBy(String src){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE '"+favoris.getType()+"' AND src LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            //String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            switch (favoris.getType()){
                case "video":
                    favoris.getVideo().setMipmap(Integer.parseInt(mipmap));
                    favoris.getVideo().setUrlacces(urlacces);
                    favoris.getVideo().setSrc(src);
                    favoris.getVideo().setTitre(titre);
                    favoris.getVideo().setAuteur(auteur);
                    favoris.getVideo().setDuree(duree);
                    favoris.getVideo().setDate(date);
                    favoris.getVideo().setType_libelle(type_libelle);
                    favoris.getVideo().setType_shortcode(type_shortcode);
                    favoris.getVideo().setId(Integer.parseInt(ressource_id));
                    resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getVideo()));
                    break;
                case "audio":
                    favoris.getAudio().setMipmap(Integer.parseInt(mipmap));
                    favoris.getAudio().setUrlacces(urlacces);
                    favoris.getAudio().setSrc(src);
                    favoris.getAudio().setTitre(titre);
                    favoris.getAudio().setAuteur(auteur);
                    favoris.getAudio().setDuree(duree);
                    favoris.getAudio().setDate(date);
                    favoris.getAudio().setType_libelle(type_libelle);
                    favoris.getAudio().setType_shortcode(type_shortcode);
                    favoris.getAudio().setId(Integer.parseInt(ressource_id));
                    resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getAudio()));
                    break;
                case "pdf":
                    favoris.getPdf().setMipmap(Integer.parseInt(mipmap));
                    favoris.getPdf().setUrlacces(urlacces);
                    favoris.getPdf().setSrc(src);
                    favoris.getPdf().setTitre(titre);
                    favoris.getPdf().setAuteur(auteur);
                    favoris.getPdf().setDate(date);
                    favoris.getPdf().setType_libelle(type_libelle);
                    favoris.getPdf().setType_shortcode(type_shortcode);
                    favoris.getPdf().setId(Integer.parseInt(ressource_id));
                    resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getPdf()));
                    break;
            }
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }

    public Favoris infosAudioBy(String src){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'audio' AND src LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            //String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            favoris.getAudio().setMipmap(Integer.parseInt(mipmap));
            favoris.getAudio().setUrlacces(urlacces);
            favoris.getAudio().setSrc(src);
            favoris.getAudio().setTitre(titre);
            favoris.getAudio().setAuteur(auteur);
            favoris.getAudio().setDuree(duree);
            favoris.getAudio().setDate(date);
            favoris.getAudio().setType_libelle(type_libelle);
            favoris.getAudio().setType_shortcode(type_shortcode);
            favoris.getAudio().setId(Integer.parseInt(ressource_id));
            resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getAudio()));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }

    public Favoris infosVideoBy(String src){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'video' AND src LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            //String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            favoris.getVideo().setMipmap(Integer.parseInt(mipmap));
            favoris.getVideo().setUrlacces(urlacces);
            favoris.getVideo().setSrc(src);
            favoris.getVideo().setTitre(titre);
            favoris.getVideo().setAuteur(auteur);
            favoris.getVideo().setDuree(duree);
            favoris.getVideo().setDate(date);
            favoris.getVideo().setType_libelle(type_libelle);
            favoris.getVideo().setType_shortcode(type_shortcode);
            favoris.getVideo().setId(Integer.parseInt(ressource_id));
            resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getVideo()));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }

    public Favoris infosPdfBy(String src){
        creerTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE type LIKE 'pdf' AND src LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            //String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            String ressource_id = cursor.getString(cursor.getColumnIndex("ressource_id"));
            //--
            favoris.getPdf().setMipmap(Integer.parseInt(mipmap));
            favoris.getPdf().setUrlacces(urlacces);
            favoris.getPdf().setSrc(src);
            favoris.getPdf().setTitre(titre);
            favoris.getPdf().setAuteur(auteur);
            favoris.getPdf().setDate(date);
            favoris.getPdf().setType_libelle(type_libelle);
            favoris.getPdf().setType_shortcode(type_shortcode);
            favoris.getPdf().setId(Integer.parseInt(ressource_id));
            resultat.add(new Favoris(Integer.parseInt(id), type, favoris.getPdf()));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }


    public void vider(){
        creerTable();
        String sql = "DROP TABLE IF EXISTS " + table_name;
        connexion.getDb().execSQL(sql);
    }

    public void supprimer()
    {
        creerTable();
        String sql = "DELETE FROM " + table_name + " WHERE id LIKE '%"+favoris.getId()+"%'";
        connexion.getDb().execSQL(sql);
    }

    public Favoris getFavoris() {
        return favoris;
    }

    public void setFavoris(Favoris favoris) {
        this.favoris = favoris;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }
}
