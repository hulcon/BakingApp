package com.example.irshad.bakingapp.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.irshad.bakingapp.Model.Recipe;
import com.example.irshad.bakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements Player.EventListener {

    public static final String PARCELABLE_EXTRA_STEP_DETAIL = "step_detail";
    public static final String TAG = StepDetailsFragment.class.getSimpleName();

    @BindView(R.id.textview_step_description)
    TextView textviewStepDescription;

    @BindView(R.id.textview_step_short_description)
    TextView textviewStepShortDescription;

    @BindView(R.id.exoplayer_step_video)
    PlayerView mPlayerView;

    @BindView((R.id.progressbar_video_loading))
    ProgressBar progressBarVideoLoading;

    private Recipe.Step mRecipeStep;

    private SimpleExoPlayer mExoPlayer;



    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this,rootView);
        Log.d(TAG,"Fragment Created for step detail ");
        //mPlayerView = rootView.findViewById(R.id.exoplayer_step_video);
        if(mRecipeStep != null){
            updateUI();
        }



        //mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
               // (getResources(), R.drawable.ic_serving));

        //initializeMediaSession();


        return rootView;
    }

    public void setStep(Recipe.Step recipeStep){
        mRecipeStep = recipeStep;
        Log.d(TAG,"Description is " + mRecipeStep.getDescription());
    }

    public void updateUI(){
        textviewStepDescription.setText(mRecipeStep.getDescription());
        textviewStepShortDescription.setText(mRecipeStep.getShortDescription());
        String videoUrl = mRecipeStep.getVideoUrl();
        if(!TextUtils.isEmpty(videoUrl)){
            initializePlayer(Uri.parse(videoUrl));
            mPlayerView.setVisibility(View.VISIBLE);
            Log.d(TAG,"Video url is " + videoUrl);
        } else {
            Log.d(TAG,"Empty URL encountered");
            mPlayerView.setVisibility(View.GONE);
        }

    }

   /* *//**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     *//*
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        //mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }
*/
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        Context context = getContext();
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, getResources().getString(R.string.app_name));
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,userAgent);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            /*MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);*/
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);

            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }

        if(playbackState == Player.STATE_BUFFERING){
            progressBarVideoLoading.setVisibility(View.VISIBLE);
        }

        if(playbackState == Player.STATE_ENDED){
            progressBarVideoLoading.setVisibility(View.GONE);
        }

        if(playbackState == Player.STATE_IDLE) {
            progressBarVideoLoading.setVisibility(View.GONE);
        }

        if(playbackState == Player.STATE_READY){
            progressBarVideoLoading.setVisibility(View.GONE);
        }
        //mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     *//*
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        //mMediaSession.setActive(false);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
