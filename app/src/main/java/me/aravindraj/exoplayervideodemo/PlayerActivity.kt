package me.aravindraj.exoplayervideodemo

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.custom_playback_control.*


class PlayerActivity : AppCompatActivity() {


    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var componentListener: ComponentListener
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var loadControl: DefaultLoadControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        componentListener = ComponentListener()

        exo_subtitle_checkbox.setOnCheckedChangeListener { exo_subtitle_checkbox, isChecked ->
            if (isChecked) {
                trackSelector.setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
            } else {
                trackSelector.setRendererDisabled(C.TRACK_TYPE_VIDEO, true)
            }

        }
    }


    private fun initializePlayer() {
        if (player == null) {
            trackSelector = DefaultTrackSelector()
            loadControl = DefaultLoadControl()
            player = ExoPlayerFactory.newSimpleInstance(
                    DefaultRenderersFactory(this),
                    trackSelector,
                    loadControl)
            player!!.addListener(componentListener)
            player!!.addVideoDebugListener(componentListener)
            player!!.addAudioDebugListener(componentListener)
            video_view.setPlayer(player)
            player!!.setPlayWhenReady(playWhenReady)
            val uri = Uri.parse(getString(R.string.media_url_mp4))
            val mediaSource = buildMediaSource(uri)
            val mergerMediaSource = MergingMediaSource(mediaSource, buildSubtitleSource(getString(R.string.media_url_srt)))
            player!!.prepare(mergerMediaSource, true, false)
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }

    private fun buildSubtitleSource(path: String): MediaSource {
        val subtitleFormat = Format.createTextSampleFormat(
                null,
                MimeTypes.APPLICATION_SUBRIP,
                C.SELECTION_FLAG_DEFAULT, null)
        return SingleSampleMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(Uri.parse(path), subtitleFormat, C.TIME_UNSET)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.getCurrentPosition()
            currentWindow = player!!.getCurrentWindowIndex()
            playWhenReady = player!!.getPlayWhenReady()
            player!!.removeListener(componentListener)
            player!!.removeVideoDebugListener(componentListener);
            player!!.removeAudioDebugListener(componentListener);
            player!!.release()
            player = null
        }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
        } else {
            hideSystemUi()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUiFullScreen() {
        video_view!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


}
