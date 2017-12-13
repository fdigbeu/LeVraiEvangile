package org.tv2vie.View.Services;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import org.tv2vie.Presenter.Home.HomePresenter;

/**
 * Created by Maranatha on 30/08/2017.
 */

public class DownloadService extends Service
{
    private static BroadcastReceiver downloadReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(downloadReceiver);
        downloadReceiver = null;
    }

    private void registerScreenOffReceiver()
    {
        downloadReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                HomePresenter.retrieveDataFromDownloadService(context, intent);
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }
}
