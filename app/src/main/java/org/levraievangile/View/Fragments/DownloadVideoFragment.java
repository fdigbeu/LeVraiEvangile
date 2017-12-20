package org.levraievangile.View.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.Video;
import org.levraievangile.Presenter.DownloadPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Activities.DownloadActivity;
import org.levraievangile.View.Activities.VideoPlayerActivity;
import org.levraievangile.View.Adapters.DownloadRecyclerAdapter;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;

import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_POSITION_VIDEO_SELECTED;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_NEXT;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VALUE_VIDEO_PLAY_PREVIOUS;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA;
import static org.levraievangile.Presenter.CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA;
import static org.levraievangile.Presenter.CommonPresenter.VALUE_VIDEO_SELECTED_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadVideoFragment extends Fragment implements DownloadView.IDownloadVideoView{
    // Ref Download interface
    private DownloadView.IDownload iDownload;
    // Ref DownloadRecycler interface for videos list
    private DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler;
    // Ref widgets
    private RecyclerView downloadRecyclerView;
    private ProgressBar downloadProgressBar;
    // Presenter
    private DownloadPresenter downloadPresenter;

    public DownloadVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_download_video, container, false);
        downloadPresenter = new DownloadPresenter(this);
        downloadPresenter.loadDownloadVideoFragData(rootView);
        return rootView;
    }

    @Override
    public void initialize(View rootView) {
        downloadRecyclerView = rootView.findViewById(R.id.downloadRecyclerView);
        downloadProgressBar = rootView.findViewById(R.id.downloadProgressBar);
    }

    @Override
    public void events() {

    }

    @Override
    public void loadDownloadVideoData(ArrayList<DownloadFile> downloads, int numberColumns) {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), numberColumns);
        downloadRecyclerView.setLayoutManager(gridLayout);
        downloadRecyclerView.setHasFixedSize(true);
        DownloadRecyclerAdapter adapter = new DownloadRecyclerAdapter(downloads, this);
        downloadRecyclerView.setAdapter(adapter);
    }

    @Override
    public void progressBarVisibility(int visibility) {
        downloadProgressBar.setVisibility(visibility);
    }

    @Override
    public void scrollVideoDataToPosition(int positionScroll) {
        downloadRecyclerView.scrollToPosition(positionScroll);
    }

    @Override
    public void instanciateIDownloadVideoRecycler(DownloadView.IDownloadVideoRecycler iDownloadVideoRecycler) {
        this.iDownloadVideoRecycler = iDownloadVideoRecycler;
    }

    @Override
    public void launchVideoToPlay(Video video, int position) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra(KEY_VIDEO_PLAYER_SEND_DATA, video);
        intent.putExtra(KEY_VALUE_POSITION_VIDEO_SELECTED, position);
        startActivityForResult(intent, VALUE_VIDEO_SELECTED_REQUEST_CODE);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VALUE_VIDEO_SELECTED_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(KEY_VIDEO_PLAYER_RETURN_DATA);
                switch (result){
                    case KEY_VALUE_VIDEO_PLAY_NEXT:
                        downloadPresenter.playNextVideoInPlayer(iDownloadVideoRecycler);
                        break;

                    case KEY_VALUE_VIDEO_PLAY_PREVIOUS:
                        downloadPresenter.playPreviousVideoInPlayer(iDownloadVideoRecycler);
                        break;
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+requestCode);
                Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = "+resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DownloadPresenter downloadPresenter = new DownloadPresenter(iDownload, this);
        downloadPresenter.downloadVideoFragmentAttachSuccess(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        iDownload =(DownloadView.IDownload) context;
        ((DownloadActivity)context).instanciateIDownloadVideoView(this);
    }
}
