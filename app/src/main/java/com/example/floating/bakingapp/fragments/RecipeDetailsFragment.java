package com.example.floating.bakingapp.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
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
    public static final String RECIPE_STEPS = "recipe_steps";
    public static final String PANES = "panes";
    public static final String STEPS = "steps";
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW = "current_window";
    public static final String PLAYBACK_POSITION = "playback_position";


    public static ArrayList<Steps> steps_list;
    public static int mIndex = 0;
    private long mPosition;
    private boolean autoplay = false;
    public static boolean mTwoPane;
    View mStepDetails;
    int currVideoIndex;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepHeaderTextView, mStepDescriptionTextView;
    private SimpleExoPlayerView mPlayerView;
    private CardView details;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(ITEM_ID);
            mTwoPane = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getLong(POSITION,0);
            steps_list = savedInstanceState.getParcelableArrayList(STEPS);
            autoplay = savedInstanceState.getBoolean(AUTOPLAY, false);
            currVideoIndex = savedInstanceState.getInt(CURRENT_WINDOW, 0);
        }else {
            mIndex = getArguments().getInt(ITEM_ID);
            mTwoPane = getArguments().getBoolean(PANES);
            steps_list = getArguments().getParcelableArrayList(RECIPE_STEPS);
        }

        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        mStepDetails = (View) rootView.findViewById(R.id.recipe_step_details_container);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_player);
        mStepDescriptionTextView = (TextView) rootView.findViewById(R.id.recipe_step_description);
        mStepHeaderTextView = (TextView) rootView.findViewById(R.id.recipe_step_header);
        details = (CardView) rootView.findViewById(R.id.description);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        showViewsPhone();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        mExoPlayer = null;
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getContext(),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mExoPlayer.addListener(this);
        mPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);

        mExoPlayer.seekTo(currVideoIndex, mPosition);

        String userAgent = Util.getUserAgent(getContext(), "The Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(
                        getActivity(), userAgent),
                new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
    }

    void showViewsPhone() {
        if (!mTwoPane && isLandscape()) {
            hideSystemUI();
            details.setVisibility(View.GONE);
            playInstructionVideo(mIndex);
            mStepHeaderTextView.setText(steps.get(mIndex).getShortDescription());
            mStepDescriptionTextView.setText(steps.get(mIndex).getDescription());
        } else {
            details.setVisibility(View.VISIBLE);
            mStepHeaderTextView.setText(steps.get(mIndex).getShortDescription());
            mStepDescriptionTextView.setText(steps.get(mIndex).getDescription());
            playInstructionVideo(mIndex);
        }
    }

    private void playInstructionVideo(int index) {
        mPlayerView.setVisibility(View.VISIBLE);
        mPlayerView.requestFocus();
        String thumbnailUrl = steps.get(index).getThumbnailURL();
        String videoUrl = steps.get(index).getVideoURL();

        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else if (!thumbnailUrl.isEmpty()) {
            initializePlayer(Uri.parse(thumbnailUrl));
        }else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPosition = mExoPlayer.getCurrentPosition();
            currVideoIndex = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    boolean isLandscape() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    @SuppressLint("InlineApi")
    private void hideSystemUI() {
        View view = getActivity().getWindow().getDecorView();

        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null){
            outState.putLong(PLAYBACK_POSITION, mPosition);
            outState.putInt(CURRENT_WINDOW, currVideoIndex);
            outState.putBoolean(AUTOPLAY, autoplay);
        }
        outState.putBoolean(PANES, mTwoPane);
        outState.putInt(ITEM_ID, mIndex);
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
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady){
            Toast.makeText(getActivity(), "Playing", Toast.LENGTH_LONG).show();
        }
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            mPosition = mExoPlayer.getCurrentPosition();
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
