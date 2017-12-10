package org.questionsreponses.View.Interfaces;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Maranatha on 17/11/2017.
 */

public class CommonView {
    public interface ICommonPresenter{
        public void onSendContactFormFinished(Context context, String returnCode);
    }
}
