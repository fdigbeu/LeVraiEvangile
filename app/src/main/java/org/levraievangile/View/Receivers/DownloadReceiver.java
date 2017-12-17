package org.levraievangile.View.Receivers;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import org.levraievangile.Presenter.HomePresenter;

/**
 * Created by Maranatha on 30/08/2017.
 */

public class DownloadReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                HomePresenter homePresenter = new HomePresenter();
                homePresenter.fileIsDownloadSuccessFully(context);
            }
        }
        catch (Exception ex){}
    }
}