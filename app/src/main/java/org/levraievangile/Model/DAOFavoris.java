package org.levraievangile.Model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.levraievangile.Presenter.CommonPresenter;

import java.util.ArrayList;

/**
 * Created by JESUS EST YAHWEH on 07/01/2017.
 */

public class DAOFavoris {

    private Context context;
    private final String TABLE_NAME = "lve_favoris";

    private final String COL_1 = "id";
    // type = audio, download_audio, video, download_video, pdf, download_pdf/ notif_audio_today, notif_video_today
    private final String COL_2 = "type";
    private final String COL_3 = "mipmap";
    private final String COL_4 = "urlacces";
    private final String COL_5 = "src";
    private final String COL_6 = "titre";
    private final String COL_7 = "auteur";
    private final String COL_8 = "duree";
    private final String COL_9 = "date";
    private final String COL_10 = "type_libelle";
    private final String COL_11 = "type_shortcode";
    private final String COL_12 = "ressource_id";

    public DAOFavoris(Context context){
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
                ""+COL_10+" VARCHAR, " +
                ""+COL_11+" VARCHAR, " +
                ""+COL_12+" VARCHAR);";

        CommonPresenter.buildDataBase(context);
        CommonPresenter.getDb().execSQL(sql);
    }

    public void insertData(Favoris favoris){
        createTable();
        String sql = "INSERT INTO "+TABLE_NAME+" ("+COL_2+", "+COL_3+", "+COL_4+", "+COL_5+", "+COL_6+", "+COL_7+", "+COL_8+", "+COL_9+", "+COL_10+", "+COL_11+", "+COL_12+")" +
                " VALUES ('"+favoris.getType()+"', " +
                "'"+favoris.getMipmap()+"',  " +
                "'"+favoris.getUrlacces().replace("'","''")+"',  " +
                "'"+favoris.getSrc().replace("'","''")+"',  " +
                "'"+favoris.getTitre().replace("'","''")+"',  " +
                "'"+favoris.getAuteur().replace("'","''")+"',  " +
                "'"+favoris.getDuree().replace("'","''")+"',  " +
                "CURRENT_DATE,  " +
                "'"+favoris.getType_libelle().replace("'","''")+"',  " +
                "'"+favoris.getType_shortcode().replace("'","''")+"',  " +
                "'"+favoris.getRessource_id()+"');";
        CommonPresenter.getDb().execSQL(sql);
        Log.i("TAG_INSERT_FAVORIS", "favoris.getType_shortcode() = "+favoris.getType_shortcode());
    }

    public ArrayList<Favoris> getAllData(String type){
        createTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = CommonPresenter.getDb().rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+" LIKE '"+type+"' ORDER BY "+COL_1+" DESC", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            //String type = cursor.getString(cursor.getColumnIndex(COL_2));
            String mipmap = cursor.getString(cursor.getColumnIndex(COL_3));
            String urlacces = cursor.getString(cursor.getColumnIndex(COL_4));
            String src = cursor.getString(cursor.getColumnIndex(COL_5));
            String titre = cursor.getString(cursor.getColumnIndex(COL_6));
            String auteur = cursor.getString(cursor.getColumnIndex(COL_7));
            String duree = cursor.getString(cursor.getColumnIndex(COL_8));
            String date = cursor.getString(cursor.getColumnIndex(COL_9));
            String type_libelle = cursor.getString(cursor.getColumnIndex(COL_10));
            String type_shortcode = cursor.getString(cursor.getColumnIndex(COL_11));
            String ressourec_id = cursor.getString(cursor.getColumnIndex(COL_12));
            //--
            int mMipmap = CommonPresenter.getMipmapByTypeShortcode(type_shortcode);
            resultat.add(new Favoris(id, type, mMipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, Integer.parseInt(ressourec_id)));
            //--
            cursor.moveToNext();
        }
        CommonPresenter.getDb().close();
        return resultat;
    }

    public boolean isFavorisExists(String src, String typeResource){
        createTable();
        ArrayList<Favoris> resultat = new ArrayList<>();
        Cursor cursor = CommonPresenter.getDb().rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+" LIKE '"+typeResource+"' AND "+COL_5+" LIKE '"+src+"'", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        //--
        for(Integer j=0; j<count; j++){
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            String type = cursor.getString(cursor.getColumnIndex(COL_2));
            String mipmap = cursor.getString(cursor.getColumnIndex(COL_3));
            String urlacces = cursor.getString(cursor.getColumnIndex(COL_4));
            //String src = cursor.getString(cursor.getColumnIndex(COL_5));
            String titre = cursor.getString(cursor.getColumnIndex(COL_6));
            String auteur = cursor.getString(cursor.getColumnIndex(COL_7));
            String duree = cursor.getString(cursor.getColumnIndex(COL_8));
            String date = cursor.getString(cursor.getColumnIndex(COL_9));
            String type_libelle = cursor.getString(cursor.getColumnIndex(COL_10));
            String type_shortcode = cursor.getString(cursor.getColumnIndex(COL_11));
            String ressourec_id = cursor.getString(cursor.getColumnIndex(COL_12));
            //--
            int mMipmap = CommonPresenter.getMipmapByTypeShortcode(type_shortcode);
            resultat.add(new Favoris(id, type, mMipmap, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, Integer.parseInt(ressourec_id)));
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
