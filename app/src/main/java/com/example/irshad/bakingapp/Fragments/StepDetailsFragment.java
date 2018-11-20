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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements Player.EventListener {

    public static final String PARCELABLE_EXTRA_STEP_ARRAY_LIST = "step_detail";
    public static final String CURRENT_STEP_INDEX = "current_step_index";
    public static final String TAG = StepDetailsFragment.class.getSimpleName();

    public static final String EXTRA_KEY_PLAYER_WHEN_READY = "playerWhenReady";
    public static final String EXTRA_KEY_PLAYER_CURRENT_POSITION = "playerCurrentPosition";

    @BindView(R.id.textview_step_description)
    TextView textviewStepDescription;

    @BindView(R.id.textview_step_short_description)
    TextView textviewStepShortDescription;

    @BindView(R.id.exoplayer_step_video)
    PlayerView mPlayerView;

    @BindView((R.id.progressbar_video_loading))
    ProgressBar progressBarVideoLoading;

    @BindView(R.id.imageview_thumbnail)
    ImageView imageViewThumbnail;

    private Recipe.Step mRecipeStep;

    private SimpleExoPlayer mExoPlayer;

    private boolean playWhenReady = true;
    private long exoplayerCurrentPosition = 0;



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
        if(savedInstanceState != null){
            playWhenReady = savedInstanceState.getBoolean(EXTRA_KEY_PLAYER_WHEN_READY);
            exoplayerCurrentPosition = savedInstanceState.getLong(EXTRA_KEY_PLAYER_CURRENT_POSITION);
            Log.d(TAG,"Retrieving exoplayer position and playstate... CURRENT POSITION IS " + exoplayerCurrentPosition);
        }
        if(mRecipeStep != null){
            updateUI();
        } else {
            Log.d(TAG,"Step is NULL!!!!!!!!!!!!!!!!!!!!");
        }
        return rootView;
    }


    public void setStepsArrayList(ArrayList<Recipe.Step> recipeStepsArrayList,int currentStepIndex){
        mRecipeStep = recipeStepsArrayList.get(currentStepIndex);
        Log.d(TAG,"Description is " + mRecipeStep.getDescription());
    }

    public void updateUI(){
        textviewStepDescription.setText(mRecipeStep.getDescription());
        textviewStepShortDescription.setText(mRecipeStep.getShortDescription());
        String videoUrl = mRecipeStep.getVideoUrl();
        String thumbnailUrl = mRecipeStep.getThumbnailUrl();
        if(!TextUtils.isEmpty(videoUrl)){
            initializePlayer(Uri.parse(videoUrl));
            imageViewThumbnail.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            Log.d(TAG,"Video url is " + videoUrl);
        } else if(!TextUtils.isEmpty(thumbnailUrl)){
            mPlayerView.setVisibility(View.GONE);
            imageViewThumbnail.setVisibility(View.VISIBLE);
            Picasso.get().load(thumbnailUrl).placeholder(R.drawable.recipe).into(imageViewThumbnail);
        }
        else {
            Log.d(TAG,"Empty URL encountered");
            mPlayerView.setVisibility(View.GONE);
            imageViewThumbnail.setVisibility(View.GONE);
        }

    }


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
            mExoPlayer.setPlayWhenReady(playWhenReady);
            Log.d(TAG,"Seeking to .................................................." + exoplayerCurrentPosition);
            mExoPlayer.seekTo(exoplayerCurrentPosition);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null){
            playWhenReady = mExoPlayer.getPlayWhenReady();
            exoplayerCurrentPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        /*if(mExoPlayer != null){*/
           /* playWhenReady = mExoPlayer.getPlayWhenReady();
            exoplayerCurrentPosition = mExoPlayer.getCurrentPosition();*/
            outState.putBoolean(EXTRA_KEY_PLAYER_WHEN_READY,playWhenReady);
            outState.putLong(EXTRA_KEY_PLAYER_CURRENT_POSITION,exoplayerCurrentPosition);
            Log.d(TAG,"Saving exoplayer position and playstate... CURRENT POSITION SAVED IS " + exoplayerCurrentPosition);
        /*} else {
            Log.d(TAG,"Exoplayer is NULL!!!!!!!");
        }*/
    }
}
