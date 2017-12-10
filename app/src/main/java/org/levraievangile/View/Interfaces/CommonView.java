package org.levraievangile.View.Interfaces;

import android.content.Context;

/**
 * Created by Maranatha on 17/11/2017.
 */

public class CommonView {
    public interface ICommonPresenter{
        public void onSendContactFormFinished(Context context, String returnCode);
    }
}
