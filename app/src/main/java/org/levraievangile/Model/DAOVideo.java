package crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import classes.Animation;
import classes.Connexion;
import classes.Parametrage;
import classes.SousMenu;
import classes.Video;


/**
 * Created by JESUS EST YAHWEH on 21/03/2016.
 */
public class CRUDVideo {

    private final String table_name = "lve_video";
    private Connexion connexion;
    private Context context;
    private Video video;
    private String orderSql = "DESC";

    public CRUDVideo(Context context){
        this.context = context;
    }

    public CRUDVideo(Context context, Video video){
        this.context = context;
        this.video = video;
    }

    public void creerTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT, mipmap VARCHAR, urlacces VARCHAR, src VARCHAR, titre VARCHAR, auteur VARCHAR, duree VARCHAR, date VARCHAR, type_libelle VARCHAR, type_shortcode VARCHAR);";
        connexion = new Connexion(context);
        connexion.getDb().execSQL(sql);
    }

    public void ajouter(){
        creerTable();
        String sql = "INSERT INTO " + table_name + " (mipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode)" +
                " VALUES ('"+video.getMipmap()+"', '"+video.getUrlacces()+"', '"+video.getSrc()+"', '"+video.getTitre().replace("'", "''")+"', '"+video.getAuteur().replace("'", "''")+"', '"+video.getDuree()+"', '"+video.getDate()+"', '"+video.getType_libelle()+"', '"+video.getType_shortcode()+"');";
        connexion.getDb().execSQL(sql);
    }

    public void modifier(Video video)
    {
        creerTable();
        ContentValues values = new ContentValues();
        values.put("mipmap", video.getMipmap());
        values.put("urlacces", video.getUrlacces());
        values.put("src", video.getSrc());
        values.put("titre", video.getTitre().replace("'", "''"));
        values.put("auteur", video.getAuteur().replace("'", "''"));
        values.put("duree", video.getDuree());
        values.put("date", video.getDate());
        values.put("type_libelle", video.getType_libelle());
        values.put("type_shortcode", video.getType_shortcode());
        connexion.getDb().update(table_name, values, "id LIKE ?", new String[]{""+video.getId()});
    }

    public ArrayList<Video> lister(){
        creerTable();
        ArrayList<Video> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " ORDER BY id "+orderSql, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            resultat.add(new Video(Integer.parseInt(id), Integer.parseInt(mipmap), urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return resultat;
    }

    public Video infosBy(String src){
        creerTable();
        ArrayList<Video> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE src LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            resultat.add(new Video(Integer.parseInt(id), Integer.parseInt(mipmap), urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }

    public Video infos(int id){
        creerTable();
        ArrayList<Video> resultat = new ArrayList<>();
        Cursor cursor = connexion.getDb().rawQuery("Select * FROM " + table_name + " WHERE id = '"+id+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            String mipmap = cursor.getString(cursor.getColumnIndex("mipmap"));
            String urlacces = cursor.getString(cursor.getColumnIndex("urlacces"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String auteur = cursor.getString(cursor.getColumnIndex("auteur"));
            String duree = cursor.getString(cursor.getColumnIndex("duree"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type_libelle = cursor.getString(cursor.getColumnIndex("type_libelle"));
            String type_shortcode = cursor.getString(cursor.getColumnIndex("type_shortcode"));
            resultat.add(new Video(id, Integer.parseInt(mipmap), urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
            cursor.moveToNext();
        }
        connexion.getDb().close();
        return (resultat.size() > 0 ? resultat.get(0) : null);
    }

    public ArrayList<Video> getFromAssetByRecherche(String motclef){
        ArrayList<Video> resultat = new ArrayList<>();
        String srcFichier = "videos.json";
        try
        {
            Parametrage parametrage = new Parametrage();
            parametrage.setContext(context);
            Animation animation = new Animation();
            SousMenu sousMenu = new SousMenu();
            //--
            JSONArray jsonArray = new JSONArray(parametrage.loadJSONFromAsset(srcFichier));
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String titre = jsonObject.getString("titre");
                String auteur = jsonObject.getString("auteur");
                if(motclef != null && (titre.toLowerCase().contains(motclef.trim().toLowerCase()) || auteur.toLowerCase().contains(motclef.trim().toLowerCase()))){
                    int id = jsonObject.getInt("id");
                    String duree = jsonObject.getString("duree");
                    String urlacces = jsonObject.getString("urlacces");
                    String src = jsonObject.getString("src");
                    String type_libelle = jsonObject.getString("type_libelle");
                    String type_shortcode = jsonObject.getString("type_shortcode");
                    String date = animation.getDateStringByDate(jsonObject.getString("date"));
                    int imageMipmap = sousMenu.getMipmapByTypeShortcode(type_shortcode);
                    resultat.add(new Video(id, imageMipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return resultat;
    }


    public ArrayList<Video> getFromAssetBy(String shortcode){
        ArrayList<Video> resultat = new ArrayList<>();
        String srcFichier = "videos.json";
        try
        {
            Parametrage parametrage = new Parametrage();
            parametrage.setContext(context);
            Animation animation = new Animation();
            SousMenu sousMenu = new SousMenu();
            //--
            JSONArray jsonArray = new JSONArray(parametrage.loadJSONFromAsset(srcFichier));
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(shortcode != null && shortcode.equalsIgnoreCase(jsonObject.getString("type_shortcode"))){
                    int id = jsonObject.getInt("id");
                    String titre = jsonObject.getString("titre");
                    String duree = jsonObject.getString("duree");
                    String urlacces = jsonObject.getString("urlacces");
                    String src = jsonObject.getString("src");
                    String auteur = jsonObject.getString("auteur");
                    String type_libelle = jsonObject.getString("type_libelle");
                    String type_shortcode = jsonObject.getString("type_shortcode");
                    String date = animation.getDateStringByDate(jsonObject.getString("date"));
                    int imageMipmap = sousMenu.getMipmapByTypeShortcode(type_shortcode);
                    resultat.add(new Video(id, imageMipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
                }
                else{
                    if(shortcode == null){
                        int id = jsonObject.getInt("id");
                        String titre = jsonObject.getString("titre");
                        String duree = jsonObject.getString("duree");
                        String urlacces = jsonObject.getString("urlacces");
                        String src = jsonObject.getString("src");
                        String auteur = jsonObject.getString("auteur");
                        String type_libelle = jsonObject.getString("type_libelle");
                        String type_shortcode = jsonObject.getString("type_shortcode");
                        String date = animation.getDateStringByDate(jsonObject.getString("date"));
                        int imageMipmap = sousMenu.getMipmapByTypeShortcode(type_shortcode);
                        resultat.add(new Video(id, imageMipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode));
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return resultat;
    }


    // Chargement du fichier json depuis le dossier assets
    public String loadJSONFromAsset(String srcFichier) {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open(srcFichier);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void vider(){
        creerTable();
        String sql = "DROP TABLE IF EXISTS " + table_name;
        connexion.getDb().execSQL(sql);
    }

    public void supprimer()
    {
        creerTable();
        String sql = "DELETE FROM " + table_name + " WHERE id LIKE '%"+video.getId()+"%'";
        connexion.getDb().execSQL(sql);
    }

    public Connexion getConnexion() {
        return connexion;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }
}
