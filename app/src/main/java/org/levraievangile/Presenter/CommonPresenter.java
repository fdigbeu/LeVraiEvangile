package org.levraievangile.Presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.levraievangile.Model.Annee;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Mois;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;
import org.levraievangile.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class CommonPresenter {

    public static final String KEY_ALL_NEWS_YEARS_LIST = "KEY_ALL_NEWS_YEARS_LIST";
    public static final String KEY_ALL_GOOD_TO_KNOW_LIST = "KEY_ALL_GOOD_TO_KNOW_LIST";
    public static final String KEY_SHORT_CODE = "KEY_SHORT_CODE";
    public static final String KEY_ALL_VIDEOS_LIST = "KEY_ALL_VIDEOS_LIST";
    public static final String KEY_ALL_AUDIOS_LIST = "KEY_ALL_AUDIOS_LIST";
    public static final String KEY_ALL_PDFS_LIST = "KEY_ALL_PDFS_LIST";
    public static final String KEY_ALL_NEWS_MONTH_LIST = "KEY_ALL_NEWS_MONTH_LIST";

    public static final String KEY_VIDEO_PLAYER_SEND_DATA = "KEY_VIDEO_PLAYER_SEND_DATA";
    public static final String KEY_VIDEO_PLAYER_RETURN_DATA = "KEY_VIDEO_PLAYER_RETURN_DATA";
    public static final String KEY_VALUE_POSITION_VIDEO_SELECTED = "KEY_VALUE_POSITION_VIDEO_SELECTED";
    public static final String KEY_VALUE_VIDEO_PLAY_NEXT = "KEY_VALUE_VIDEO_PLAY_NEXT";
    public static final String KEY_VALUE_VIDEO_PLAY_PREVIOUS = "KEY_VALUE_VIDEO_PLAY_PREVIOUS";
    public static final int VALUE_VIDEO_SELECTED_REQUEST_CODE = 11;

    public static final String KEY_NOTIF_AUDIO_TIME_ELAPSED = "KEY_NOTIF_AUDIO_TIME_ELAPSED";
    public static final String KEY_NOTIF_AUDIOS_LIST = "KEY_NOTIF_AUDIOS_LIST";
    public static final String KEY_NOTIF_PLAYER_SELECTED = "KEY_NOTIF_PLAYER_SELECTED";
    public static final String KEY_NOTIF_PLAYER_PLAY_NEXT = "KEY_NOTIF_PLAYER_PLAY_NEXT";
    public static final String KEY_NOTIF_PLAYER_PREVIOUS = "KEY_NOTIF_PLAYER_PREVIOUS";

    public static final String KEY_AUDIO_PLAYER_SEND_DATA = "KEY_AUDIO_PLAYER_SEND_DATA";
    public static final String KEY_AUDIO_PLAYER_RETURN_DATA = "KEY_AUDIO_PLAYER_RETURN_DATA";
    public static final String KEY_VALUE_POSITION_AUDIO_SELECTED = "KEY_VALUE_POSITION_AUDIO_SELECTED";
    public static final String KEY_VALUE_AUDIO_PLAY_NEXT = "KEY_VALUE_AUDIO_PLAY_NEXT";
    public static final String KEY_VALUE_AUDIO_PLAY_PREVIOUS = "KEY_VALUE_AUDIO_PLAY_PREVIOUS";
    public static final String KEY_VALUE_AUDIO_PLAY_NOTIFICATION = "KEY_VALUE_AUDIO_PLAY_NOTIFICATION";
    public static final int VALUE_AUDIO_SELECTED_REQUEST_CODE = 10;


    public CommonPresenter(){}

    public static ArrayList<Video> listeSousMenuVideo(){
        ArrayList<Video> liste = new ArrayList<>();
        liste.add(new Video(R.mipmap.sm_enseignement,"Enseignements", "enseignements"));
        liste.add(new Video(R.mipmap.sm_question_reponse,"Questions/Réponses", "questions-reponses"));
        liste.add(new Video(R.mipmap.sm_temoignage,"Témoignages", "temoignages"));
        liste.add(new Video(R.mipmap.sm_reportage,"Reportages", "reportages"));
        liste.add(new Video(R.mipmap.sm_interview,"Interviews", "interviews"));
        liste.add(new Video(R.mipmap.sm_conversion,"Conversions", "conversions"));
        liste.add(new Video(R.mipmap.sm_confession,"Confessions", "confessions"));
        liste.add(new Video(R.mipmap.sm_jesus_guerit_toujours,"Jésus guérit toujours", "jesus-guerit-toujours"));
        liste.add(new Video(R.mipmap.sm_film,"Films", "films"));
        liste.add(new Video(R.mipmap.sm_dessin_anime,"Déssins animés", "dessins-animes"));
        return liste;
    }

    public static ArrayList<Audio> listeSousMenuAudio(){
        ArrayList<Audio> liste = new ArrayList<Audio>();
        liste.add(new Audio(R.mipmap.sm_enseignement,"Enseignements", "enseignements"));
        liste.add(new Audio(R.mipmap.sm_question_reponse,"Questions/Réponses", "questions-reponses"));
        liste.add(new Audio(R.mipmap.sm_temoignage,"Témoignages", "temoignages"));
        liste.add(new Audio(R.mipmap.sm_interview,"Interviews", "interviews"));
        return liste;
    }

    public static ArrayList<Pdf> listeSousMenuPdf(){
        ArrayList<Pdf> liste = new ArrayList<Pdf>();
        liste.add(new Pdf(R.mipmap.sm_pdf,"Livres", "livres"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Magazines", "magazines"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Bon à savoir", "bon-a-savoir"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Sainte Bible", "sainte-bible"));
        return liste;
    }

    /**
     * Display simple message text
     * @param context
     * @param title
     * @param message
     */
    public static void showMessage(final Context context, String title, String message, final boolean closeActivity){
        Hashtable<String, Integer> resolutionEcran = getScreenSize(context);
        int width = resolutionEcran.get("largeur");
        int height = resolutionEcran.get("hauteur");
        int imgWidth = width <= height ? width : height;
        int newWidth = (int)(imgWidth*0.75f);
        int newHeight = (int)(imgWidth*0.40f);

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setLayout((int)(newWidth*1.30f), ActionBar.LayoutParams.WRAP_CONTENT);

        TextView titre = dialog.findViewById(R.id.title);
        TextView detaille = dialog.findViewById(R.id.msg_detail);

        titre.setText(title);

        buildTextViewToHtmlData(detaille, message);

        Button button_close = dialog.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(closeActivity){
                    ((Activity)context).finish();
                }
            }
        });

        dialog.show();
    }

    /**
     * Build textview to Html
     * @param textView
     * @param textValue
     */
    public static void buildTextViewToHtmlData(TextView textView, String textValue){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(textValue, Html.FROM_HTML_MODE_LEGACY));
        }
        else{
            textView.setText(Html.fromHtml(textValue));
        }
    }

    /**
     * Get mipmap by typeShortcode
     * @param shortcode
     * @return
     */
    public static int getMipmapByTypeShortcode(String shortcode){
        int mipmap = R.mipmap.sm_film;
        switch (shortcode){
            case "enseignements":
                mipmap = R.mipmap.sm_enseignement;
                break;
            case "questions-reponses":
                mipmap = R.mipmap.sm_question_reponse;
                break;
            case "temoignages":
                mipmap = R.mipmap.sm_temoignage;
                break;
            case "reportages":
                mipmap = R.mipmap.sm_reportage;
                break;
            case "interviews":
                mipmap = R.mipmap.sm_interview;
                break;
            case "conversions":
                mipmap = R.mipmap.sm_conversion;
                break;
            case "confessions":
                mipmap = R.mipmap.sm_confession;
                break;
            case "jesus-guerit-toujours":
                mipmap = R.mipmap.sm_jesus_guerit_toujours;
                break;
            case "films":
                mipmap = R.mipmap.sm_film;
                break;
            case "dessins-animes":
                mipmap = R.mipmap.sm_dessin_anime;
                break;
            case "livres":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "magazines":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "bon-a-savoir":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "sainte-bible":
                mipmap = R.mipmap.sm_pdf;
                break;
        }
        return mipmap;
    }



    public static String getLibelleByTypeShortcode(String shortcode){
        String libelle = "Enseignements";
        switch (shortcode){
            case "enseignements":
                libelle = "Enseignements";
                break;
            case "questions-reponses":
                libelle = "Questions/Réponses";
                break;
            case "temoignages":
                libelle = "Témoignages";
                break;
            case "reportages":
                libelle = "Reportages";
                break;
            case "interviews":
                libelle = "Interviews";
                break;
            case "conversions":
                libelle = "Conversions";
                break;
            case "confessions":
                libelle = "Confessions";
                break;
            case "jesus-guerit-toujours":
                libelle = "Jésus guérit toujours";
                break;
            case "films":
                libelle = "Films";
                break;
            case "dessins-animes":
                libelle = "Dessins animés";
                break;
            case "livres":
                libelle = "Livres";
                break;
            case "magazines":
                libelle = "Magazines";
                break;
            case "bon-a-savoir":
                libelle = "Bon à savoir";
                break;
            case "sainte-bible":
                libelle = "Sainte bible";
                break;
        }
        return libelle;
    }


    /**
     * Get string date : Day/Month/Year
     * @param date
     * @return
     */
    public static String getStringDate(String date){
        String tabMonth[] = {"JAN", "FÉV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOÜ", "SEP", "OCT", "NOV", "DÉC"};
        return date.split("-")[2].trim()+" "+tabMonth[Integer.parseInt(date.split("-")[1].trim())-1]+" "+date.split("-")[0].trim();
    }

    /**
     * Change format date : Year - Month - Day
     * @param date
     * @return
     */
    public static String changeFormatDate(String date){
        return date.split("-")[2].trim()+"/"+date.split("-")[1].trim()+"/"+date.split("-")[0].trim();
    }

    public static String changeFormatDuration(String duration){
        String tagDuree[] = duration.split(":");
        return ((Integer.parseInt(tagDuree[0])*60)+Integer.parseInt(tagDuree[1]))+"min";
    }

    /**
     * Get screen size
     * @param context
     * @return
     */
    public static Hashtable<String, Integer> getScreenSize(Context context) {
        Hashtable<String, Integer> dimension = new Hashtable<>();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dimension.put("largeur", width);
        dimension.put("hauteur", height);
        return dimension;
    }


    /**
     * Verify if connection exists
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context){
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Verify if wifi connection exits
     * @param context
     * @return
     */
    public static boolean isMobileWIFIConnected(Context context){
        int networkType = ConnectivityManager.TYPE_WIFI;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }


    public static int getNumberToDisplay(Context context){
        int number = 1;
        Hashtable<String, Integer> screenSize = getScreenSize(context);
        boolean isPortrait = screenSize.get("largeur") <= screenSize.get("hauteur");
        if(isTablette(context)){
            number = isPortrait ? 3 : 5;
        }
        else{
          number = isPortrait ? 2 : 4;
        }
        return number;
    }

    /**
     * Verify if it's a tablet
     * @param context
     * @return
     */
    public static boolean isTablette(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * Save data in share preferences
     * @param context
     * @param key
     * @param contentData
     */
    public static void saveDataInSharePreferences(Context context, String key, String contentData){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, contentData);
            editor.commit();
        }
        catch (Exception ex){}
    }


    /**
     * Retrieve data in share preferences
     * @param context
     * @param key
     * @return
     */
    public static String getDataFromSharePreferences(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


    /**
     * Replace accents by correspondant a letter
     * @param str
     * @return
     */
    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Get all save years
     * @param context
     * @return
     */
    public static ArrayList<Annee> getAllSaveYears(Context context){
        ArrayList<Annee> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_ALL_NEWS_YEARS_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);

            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String annee = jsonObject.getString("annee");
                //--
                mList.add(new Annee(R.mipmap.sm_calendrier, annee));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save good to know
     * @param context
     * @return
     */
    public static ArrayList<BonASavoir> getAllSaveGoodToKnow(Context context){
        ArrayList<BonASavoir> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_ALL_GOOD_TO_KNOW_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                boolean etat = jsonObject.getBoolean("etat");
                int nbvue = jsonObject.getInt("nbvue");
                String type_media = jsonObject.getString("type_media");
                String date = jsonObject.getString("date");
                int mipmap = R.mipmap.sm_infos;
                //--
                mList.add(new BonASavoir(id, titre, nbvue, type_media, etat, date, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save videos saved
     * @param context
     * @return
     */
    public static ArrayList<Video> getAllVideoSavedBy(Context context, String shortCode){
        ArrayList<Video> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_VIDEOS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Video(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save audios saved
     * @param context
     * @return
     */
    public static ArrayList<Audio> getAllAudioSavedBy(Context context, String shortCode){
        ArrayList<Audio> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_AUDIOS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Audio(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all notification audios list saved
     * @param context
     * @return
     */
    public static ArrayList<Audio> getAllNotificationAudios(Context context){
        ArrayList<Audio> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_NOTIF_AUDIOS_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(type_shortcode);
                mList.add(new Audio(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save audios saved
     * @param context
     * @return
     */
    public static ArrayList<Pdf> getAllPdfSavedBy(Context context, String shortCode){
        ArrayList<Pdf> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_PDFS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Pdf(id, urlacces, src, titre, auteur, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all month news saved
     * @param context
     * @return
     */
    public static ArrayList<Mois> getAllNewsMonthSavedBy(Context context, String yearValue){
        ArrayList<Mois> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_NEWS_MONTH_LIST+"-"+yearValue;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int chiffre = jsonObject.getInt("chiffre");
                String lettre = jsonObject.getString("lettre");
                mList.add(new Mois(chiffre, lettre));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get Notification Previous Resource value
     * @param currentValue
     * @param totalResource
     * @return
     */
    public static int getNotifPlayerPreviousValue(int currentValue, int totalResource){
        int position = currentValue > 0 ? currentValue-1 : totalResource-1;
        return position;
    }

    /**
     * Get Notification Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getNotifPlayerNextValue(int currentValue, int totalRessouce){
        int position = currentValue < totalRessouce-1 ? currentValue+1 : 0;
        return position;
    }

    /**
     * Get Previous Ressource value
     * @param currentValue
     * @return
     */
    public static int getPreviousRessourceValue(int currentValue){
        int position = currentValue > 0 ? currentValue-1 : currentValue;
        return position;
    }

    /**
     * Get Scroll Previous Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getScrollToPreviousValue(int currentValue, int totalRessouce){
        return currentValue+2 > totalRessouce-1 ? currentValue+2 : currentValue;
    }

    /**
     * Get Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getNextRessourceValue(int currentValue, int totalRessouce){
        int position = currentValue < totalRessouce-1 ? currentValue+1 : totalRessouce-1;
        return position;
    }

    /**
     * Get Scroll Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getScrollToNextValue(int currentValue, int totalRessouce){
        return currentValue < totalRessouce-2 ? currentValue+2 : currentValue;
    }

    /**
     * Canceled CountDownTimer
     * @param countDownTimer
     */
    public static void cancelCountDownTimer(CountDownTimer countDownTimer){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    /**
     * Stop videoview
     * @param videoView
     */
    public static void stopVideoViewPlayer(VideoView videoView){
        if(videoView != null && videoView.isPlaying()){
            videoView.stopPlayback();
        }
    }


    /**
     * Get app volume
     * @param context
     */
    public static void getApplicationVolume(Context context){
        AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * Stop Media player
     * @param mediaPlayer
     */
    public static void stopMediaPlayer(MediaPlayer mediaPlayer){
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.reset();// It requires again setDataSource for player object.
                mediaPlayer.stop();// Stop it
                mediaPlayer.release();// Release it
                mediaPlayer = null; // Initialize it to null so it can be used later
            }
        }
    }

    public static void saveNotificationParameters(Context context, int positionSelected, int totalAudios){
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, ""+positionSelected);
        int previousPosition = CommonPresenter.getNotifPlayerPreviousValue(positionSelected, totalAudios);
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PREVIOUS, ""+previousPosition);
        int nextPosition = CommonPresenter.getNotifPlayerNextValue(positionSelected, totalAudios);
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, ""+nextPosition);
    }
}
