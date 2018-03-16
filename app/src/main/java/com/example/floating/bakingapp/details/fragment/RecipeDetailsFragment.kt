package com.example.floating.bakingapp.details.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.model.Steps
import com.example.floating.bakingapp.recipes.RecipeListActivity.Companion.steps
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import org.parceler.Parcels

import java.util.ArrayList

/**
 * Created by Abdulkarim on 6/14/2017.
 */

/**
 * The dummy content this fragment is presenting.
 */
//private DummyContent.DummyItem mItem;

/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */


class RecipeDetailsFragment : Fragment(), ExoPlayer.EventListener {
    private var mPosition: Long = 0
    private var autoplay = false
    internal lateinit var mStepDetails: View
    internal var currVideoIndex: Int = 0
    private var mExoPlayer: SimpleExoPlayer? = null
    private var mStepHeaderTextView: TextView? = null
    private var mStepDescriptionTextView: TextView? = null
    private var progressBar: ProgressBar? = null
    private var mPlayerView: SimpleExoPlayerView? = null
    private var details: CardView? = null


    internal val isLandscape: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(ITEM_ID)
            mTwoPane = savedInstanceState.getBoolean(PANES)
            mPosition = savedInstanceState.getLong(POSITION, 0)
            steps_list = savedInstanceState.getParcelableArrayList(STEPS)
            autoplay = savedInstanceState.getBoolean(AUTOPLAY, false)
            currVideoIndex = savedInstanceState.getInt(CURRENT_WINDOW, 0)
        } else {
            mIndex = arguments!!.getInt(ITEM_ID)
            mTwoPane = arguments!!.getBoolean(PANES)
            steps_list = arguments!!.getParcelableArrayList(RECIPE_STEPS)
        }

        val rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        mStepDetails = rootView.findViewById(R.id.recipe_step_details_container)
        mPlayerView = rootView.findViewById(R.id.video_player)
        progressBar = rootView.findViewById(R.id.progress_bar)
        mStepDescriptionTextView = rootView.findViewById(R.id.recipe_step_description)
        mStepHeaderTextView = rootView.findViewById(R.id.recipe_step_header)
        details = rootView.findViewById(R.id.description)

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        showViewsPhone()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer(mediaUri: Uri) {
        mExoPlayer = null
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultTrackSelector(),
                DefaultLoadControl())

        mExoPlayer!!.addListener(this)
        mPlayerView!!.player = mExoPlayer
        mExoPlayer!!.playWhenReady = true

        mExoPlayer!!.seekTo(currVideoIndex, mPosition)

        val userAgent = Util.getUserAgent(context, "The Baking App")

        val mediaSource = ExtractorMediaSource(mediaUri,
                DefaultDataSourceFactory(
                        activity!!, userAgent),
                DefaultExtractorsFactory(), null, null)

        mExoPlayer!!.prepare(mediaSource)
    }

    internal fun showViewsPhone() {
        if (!mTwoPane && isLandscape) {
            hideSystemUI()
            details!!.visibility = View.GONE
            playInstructionVideo(mIndex)
            mStepHeaderTextView!!.text = steps[mIndex].shortDescription
            mStepDescriptionTextView!!.text = steps[mIndex].description
        } else {
            details!!.visibility = View.VISIBLE
            mStepHeaderTextView!!.text = steps[mIndex].shortDescription
            mStepDescriptionTextView!!.text = steps[mIndex].description
            playInstructionVideo(mIndex)
        }
    }

    private fun playInstructionVideo(index: Int) {
        mPlayerView!!.visibility = View.VISIBLE
        mPlayerView!!.requestFocus()
        val thumbnailUrl = steps[index].thumbnailURL
        val videoUrl = steps[index].videoURL

        if (!videoUrl!!.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl))
        } else if (!thumbnailUrl!!.isEmpty()) {
            initializePlayer(Uri.parse(thumbnailUrl))
        } else {
            mPlayerView!!.visibility = View.GONE
        }
    }

    private fun releasePlayer() {
        if (mExoPlayer != null) {
            mPosition = mExoPlayer!!.currentPosition
            currVideoIndex = mExoPlayer!!.currentWindowIndex
            mExoPlayer!!.stop()
            mExoPlayer!!.release()
            mExoPlayer = null
        }
    }

    @SuppressLint("InlineApi")
    private fun hideSystemUI() {
        val view = activity!!.window.decorView

        view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (mExoPlayer != null) {
            outState.putLong(PLAYBACK_POSITION, mPosition)
            outState.putInt(CURRENT_WINDOW, currVideoIndex)
            outState.putBoolean(AUTOPLAY, autoplay)
        }
        outState.putBoolean(PANES, mTwoPane)
        outState.putInt(ITEM_ID, mIndex)
        outState.putParcelable(STEPS, Parcels.wrap(steps_list))
    }

    override fun onTimelineChanged(timeline: Timeline, manifest: Any) {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            progressBar!!.visibility = View.GONE
        } else if (playbackState == Player.STATE_BUFFERING && playWhenReady) {
            progressBar!!.visibility = View.VISIBLE
        } else if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            mPosition = mExoPlayer!!.currentPosition
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onPlayerError(error: ExoPlaybackException) {

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

    }

    override fun onSeekProcessed() {

    }

    companion object {

        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        private val TAG = RecipeDetailsFragment::class.java.simpleName
        val ITEM_ID = "item_id"
        val POSITION = "position"
        val RECIPE_STEPS = "recipe_steps"
        val PANES = "panes"
        val STEPS = "steps"
        val AUTOPLAY = "autoplay"
        val CURRENT_WINDOW = "current_window"
        val PLAYBACK_POSITION = "playback_position"


        lateinit var steps_list: ArrayList<Steps>
        var mIndex = 0
        var mTwoPane: Boolean = false
    }
}
