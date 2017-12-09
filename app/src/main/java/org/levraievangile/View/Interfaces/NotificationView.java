package org.levraievangile.View.Interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.levraievangile.R;


/**
 * Created by Maranatha on 30/11/2017.
 */

public class NotificationView {
    public interface ACTION {
        public static final String MAIN_ACTION = "org.levraievangile.action.main";
        public static final String INIT_ACTION = "org.levraievangile.action.init";
        public static final String PREVIOUS_ACTION = "org.levraievangile.action.previous";
        public static final String PLAY_ACTION = "org.levraievangile.action.play";
        public static final String NEXT_ACTION = "org.levraievangile.action.next";
        public static final String PAUSE_ACTION = "org.levraievangile.action.pause";
        public static final String STARTFOREGROUND_ACTION = "org.levraievangile.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "org.levraievangile.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static final int FOREGROUND_SERVICE = 125;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.logo, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}

