package org.levraievangile.View.Interfaces;

import org.levraievangile.Model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maranatha on 06/12/2017.
 */

public class VideoView {
    public interface IVideo{
        public void initialize();
        public void events();
        public void loadVideoData(ArrayList<Video> videos, int numberColumns);
        public void scrollVideoDataToPosition(int positionScroll);
        public void instanciateIVideoRecycler(VideoView.IVideoRecycler iVideoRecycler);
        public void launchVideoToPlay(Video video, int position);
        public void progressBarVisibility(int visibility);
        public void askPermissionToSaveFile();
        public void modifyHeaderInfos(String typeLibelle);
        public void closeActivity();
    }

    // Presenter interface
    public interface IPresenter{}

    // VideoRecyclerAdapter interface
    public interface IVideoRecycler{
        public void playNextVideo();
        public void playPreviousVideo();
    }

    public interface IApiRessource {
        @GET("webservice/videos/{TYPE}/")
        Call<List<Video>> getAllVideos(@Path(value = "TYPE") String keyWord);
    }

}
