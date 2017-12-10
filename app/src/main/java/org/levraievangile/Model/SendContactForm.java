package org.levraievangile.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.levraievangile.R;
import org.levraievangile.View.Interfaces.CommonView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maranatha on 10/12/2017.
 */

public class SendContactForm  extends AsyncTask<Void, Void, String> {

    private Context context;
    private ProgressBar progressBar;
    private  String codeRetour = "";
    private URL url = null;
    private String actionForm;
    private HttpURLConnection httpURLConnection;
    private HashMap<String, String> postDataParams;
    private CommonView.ICommonPresenter iPresenter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {

        try{
            url = new URL(actionForm);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line=br.readLine()) != null){
                    codeRetour+=line;
                }
            }
        }
        catch (Exception e)
        {
            return context.getResources().getString(R.string.unstable_connection);
        }

        httpURLConnection.disconnect();

        return codeRetour;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //--
        iPresenter.onSendContactFormFinished(context, s);
    }

    public void initializeData(Context context, HashMap<String, String> postDataParams, String actionForm, ProgressBar progressBar, CommonView.ICommonPresenter iPresenter){
        this.context = context;
        this.postDataParams = postDataParams;
        this.actionForm = actionForm;
        this.progressBar = progressBar;
        this.iPresenter = iPresenter;
    }



    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder resultat = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first){
                first = false;
            }
            else {
                resultat.append("&");
            }
            resultat.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            resultat.append("=");
            resultat.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return resultat.toString();
    }
}

