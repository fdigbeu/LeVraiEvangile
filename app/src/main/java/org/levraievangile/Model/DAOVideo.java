package org.levraievangile.Model;

import android.content.Context;
import android.database.Cursor;

import org.levraievangile.Presenter.CommonPresenter;

import java.util.ArrayList;

/**
 * Created by JESUS EST YAHWEH on 10/12/2017.
 */
public class DAOVideo {

    private Context context;
    private final String TABLE_NAME = "lve_video";

    private final String COL_1 = "id";
    private final String COL_2 = "mipmap";
    private final String COL_3 = "urlacces";
    private final String COL_4 = "src";
    private final String COL_5 = "titre";
    private final String COL_6 = "auteur";
    private final String COL_7 = "duree";
    private final String COL_8 = "date";
    private final String COL_9 = "type_libelle";
    private final String COL_10 = "type_shortcode";

    public DAOVideo(Context context){
        this.context = context;
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+COL_2+" VARCHAR, " +
                ""+COL_3+" VARCHAR, " +
                ""+COL_4+" VARCHAR, " +
                ""+COL_5+" VARCHAR, " +
                ""+COL_6+" VARCHAR, " +
                ""+COL_7+" VARCHAR, " +
                ""+COL_8+" VARCHAR, " +
                ""+COL_9+" VARCHAR, " +
                ""+COL_10+" VARCHAR);";

        CommonPresenter.buildDataBase(context);
        CommonPresenter.getDb().execSQL(sql);
    }

    public void insertData(String mipmap, String urlacces, String src,  String titre,  String auteur,  String duree,  String type_libelle,  String type_shortcode){
        createTable();
        String sql = "INSERT INTO "+TABLE_NAME+" ("+COL_2+", "+COL_3+", "+COL_4+", "+COL_5+", "+COL_6+", "+COL_7+", "+COL_8+", "+COL_9+", "+COL_10+")" +
                " VALUES ('"+mipmap+"', " +
                "'"+urlacces.replace("'","''")+"',  " +
                "'"+src.replace("'","''")+"',  " +
                "'"+titre.replace("'","''")+"',  " +
                "'"+auteur.replace("'","''")+"',  " +
                "'"+duree.replace("'","''")+"',  " +
                "CURRENT_DATE,  " +
                "'"+type_libelle.replace("'","''")+"',  " +
                "'"+type_shortcode.replace("'","''")+"');";
        CommonPresenter.getDb().execSQL(sql);
    }

    public ArrayList<Video> getAllData(){
        createTable();
        ArrayList<Video> resultat = new ArrayList<>();
        Cursor cursor = CommonPresenter.getDb().rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+COL_1+" DESC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            String mipmap = cursor.getString(cursor.getColumnIndex(COL_2));
            String urlacces = cursor.getString(cursor.getColumnIndex(COL_3));
            String src = cursor.getString(cursor.getColumnIndex(COL_4));
            String titre = cursor.getString(cursor.getColumnIndex(COL_5));
            String auteur = cursor.getString(cursor.getColumnIndex(COL_6));
            String duree = cursor.getString(cursor.getColumnIndex(COL_7));
            String date = cursor.getString(cursor.getColumnIndex(COL_8));
            String type_libelle = cursor.getString(cursor.getColumnIndex(COL_9));
            String type_shortcode = cursor.getString(cursor.getColumnIndex(COL_10));
            //--
            int mMipmap = CommonPresenter.getMipmapByTypeShortcode(type_shortcode);
            resultat.add(new Video(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mMipmap));
            //--
            cursor.moveToNext();
        }
        CommonPresenter.getDb().close();
        return resultat;
    }

    public boolean isVideoExists(String src){
        createTable();
        ArrayList<Video> resultat = new ArrayList<>();
        Cursor cursor = CommonPresenter.getDb().rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_4+" LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            String mipmap = cursor.getString(cursor.getColumnIndex(COL_2));
            String urlacces = cursor.getString(cursor.getColumnIndex(COL_3));
            //String src = cursor.getString(cursor.getColumnIndex(COL_4));
            String titre = cursor.getString(cursor.getColumnIndex(COL_5));
            String auteur = cursor.getString(cursor.getColumnIndex(COL_6));
            String duree = cursor.getString(cursor.getColumnIndex(COL_7));
            String date = cursor.getString(cursor.getColumnIndex(COL_8));
            String type_libelle = cursor.getString(cursor.getColumnIndex(COL_9));
            String type_shortcode = cursor.getString(cursor.getColumnIndex(COL_10));
            //--
            int mMipmap = CommonPresenter.getMipmapByTypeShortcode(type_shortcode);
            resultat.add(new Video(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mMipmap));
            //--
            cursor.moveToNext();
        }
        CommonPresenter.getDb().close();
        return (resultat.size() > 0);
    }

    public void dropAllData(){
        createTable();
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        CommonPresenter.getDb().execSQL(sql);
    }

    public void deleteDataBy(int id)
    {
        createTable();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE "+COL_1+" = '"+id+"'";
        CommonPresenter.getDb().execSQL(sql);
    }
}
