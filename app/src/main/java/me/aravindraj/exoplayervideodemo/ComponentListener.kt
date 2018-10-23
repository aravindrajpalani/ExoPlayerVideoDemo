package me.aravindraj.exoplayervideodemo

import android.util.Log
import android.view.Surface
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.DefaultEventListener
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.video.VideoRendererEventListener


class ComponentListener : DefaultEventListener(), VideoRendererEventListener, AudioRendererEventListener {
    override fun onAudioSinkUnderrun(bufferSize: Int, bufferSizeMs: Long, elapsedSinceLastFeedMs: Long) {
    }

    override fun onAudioEnabled(counters: DecoderCounters?) {
    }

    override fun onAudioInputFormatChanged(format: Format?) {
    }

    override fun onAudioSessionId(audioSessionId: Int) {
    }

    override fun onAudioDecoderInitialized(decoderName: String?, initializedTimestampMs: Long, initializationDurationMs: Long) {
    }

    override fun onAudioDisabled(counters: DecoderCounters?) {
    }

    override fun onDroppedFrames(count: Int, elapsedMs: Long) {
    }

    override fun onVideoEnabled(counters: DecoderCounters?) {
    }

    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
    }

    override fun onVideoDisabled(counters: DecoderCounters?) {
    }

    override fun onVideoDecoderInitialized(decoderName: String?, initializedTimestampMs: Long, initializationDurationMs: Long) {
    }

    override fun onVideoInputFormatChanged(format: Format?) {
    }

    override fun onRenderedFirstFrame(surface: Surface?) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean,
                                      playbackState: Int) {
        val stateString: String
        when (playbackState) {
            Player.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -"
            Player.STATE_BUFFERING -> stateString = "ExoPlayer.STATE_BUFFERING -"
            Player.STATE_READY -> stateString = "ExoPlayer.STATE_READY     -"
            Player.STATE_ENDED -> stateString = "ExoPlayer.STATE_ENDED     -"
            else -> stateString = "UNKNOWN_STATE             -"
        }
        Log.d("ExoPlayer2", "changed state to " + stateString
                + " playWhenReady: " + playWhenReady)
    }
}