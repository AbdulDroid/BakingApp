package com.example.floating.bakingapp.fragments;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Ingredients;
import com.example.floating.bakingapp.data.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.floating.bakingapp.ui.RecipeListActivity.steps;

/**
 * Created by Abdulkarim on 6/14/2017.
 */

public class RecipeDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    public static final String ITEM_ID = "item_id";
    public static final String POSITION = "position";
    public static final String PANES = "panes";
    public static final String STEPS = "steps";

    public static ArrayList<Steps> steps_list;
    private ArrayList<Ingredients> ingredients_list;
    public static int mIndex = 0;
    private long mPosition;
    public static boolean mTwoPane;
    private  View mStepDetails;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailsFragment() {
    }
    @BindView(R.id.recipe_step_header) TextView mStepHeaderTextView;
    @BindView(R.id.recipe_step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.video_player) SimpleExoPlayerView mPlayerView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null){
            mIndex = savedInstanceState.getInt(ITEM_ID);
            mTwoPane = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getLong(POSITION);
            steps_list = savedInstanceState.getParcelableArrayList(STEPS);
            if (mPosition != 0)
                intializePlayer(Uri.parse(steps_list.get(mIndex).getVideoURL()));
                restExoPlayer(mPosition, true);
        }else {
            mTwoPane = getArguments().getBoolean(PANES);
            mIndex = getArguments().getInt(ITEM_ID);
            steps_list = steps;
        }
        if (getArguments().containsKey(ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_details);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
            }
        }

        mStepDetails = (View) rootView.findViewById(R.id.recipe_detail_container);
//        intializeMediaSeesion();

        intializePlayer(Uri.parse(steps_list.get(mIndex).getVideoURL()));
        mStepHeaderTextView.setText(steps_list.get(mIndex).getShortDescription());
        mStepDescriptionTextView.setText(steps_list.get(mIndex).getDescription());

        return rootView;
    }

//    private void intializeMediaSeesion() {
//        mMediaSession = new MediaSessionCompat(getContext(), TAG);
//        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
//                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//        mMediaSession.setMediaButtonReceiver(null);
//        mStateBuilder = new PlaybackStateCompat.Builder()
//                .setActions(
//                        PlaybackStateCompat.ACTION_PLAY |
//                                PlaybackStateCompat.ACTION_PAUSE |
//                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
//                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
//        mMediaSession.setPlaybackState(mStateBuilder.build());
//        mMediaSession.setCallback(new MySessionCallback());
//        mMediaSession.setActive(true);
//    }

    private void intializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.cheese_cake_one));
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "RecipeDetailsFragment");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            restExoPlayer(mPosition, false);
        }
    }

    private void restExoPlayer(long position, boolean playWhenReady) {
        this.mPosition = position;
        mExoPlayer.seekTo(position);
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mExoPlayer.setPlayWhenReady(false);
//        mMediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mMediaSession.setActive(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
//        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PANES, mTwoPane);
        outState.putInt(ITEM_ID, mIndex);
        outState.putLong(POSITION, mPosition);
        outState.putParcelableArrayList(STEPS, steps_list);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            mPosition = mExoPlayer.getCurrentPosition();
        }

//        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

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
            restExoPlayer(0, false);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
