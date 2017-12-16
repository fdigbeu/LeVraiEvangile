package org.levraievangile.View.Interfaces;

/**
 * Created by Maranatha on 16/12/2017.
 */

public class SplashView {
    public interface ISplash{
        public void initialize();
        public void events();
        public void launchHomeActivity();
        public void startAlarmService();
        public void stopAlarmService();
    }
}
