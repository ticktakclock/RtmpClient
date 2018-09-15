package com.github.ticktakclock.rtmpclient

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import timber.log.Timber

private const val STREAM_KEY = "stream_key"
private const val IP_ADDRESS = "ip_address"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerFragment : Fragment() {
    companion object {
        /**
         * create new instance of PlayerFragment.
         * @return A new instance of fragment PlayerFragment.
         */
        @JvmStatic
        fun newInstance(ipAddress: String, streamKey: String) =
                PlayerFragment().apply {
                    arguments = Bundle().apply {
                        putString(STREAM_KEY, streamKey)
                        putString(IP_ADDRESS, ipAddress)
                    }
                }
    }

    private var streamKey: String? = null
    private var ipAddress: String? = null
    private lateinit var unbinder: Unbinder
    @BindView(R.id.fragment_player_exp)
    lateinit var playerView: SimpleExoPlayerView
    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        arguments?.let {
            streamKey = it.getString(STREAM_KEY)
            ipAddress = it.getString(IP_ADDRESS)
        }

        streamKey
                ?: Timber.w(context.toString() + " must call PlayerFragment.newInstance() and streamKey must not be empty.")
        ipAddress
                ?: Timber.w(context.toString() + " must call PlayerFragment.newInstance() and ipAddress must not be empty.")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        unbinder = ButterKnife.bind(this, view);
        return view
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume. ipAddress=\"$ipAddress\", streamKey=\"$streamKey\"")
        playStart()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        playStop()
    }

    /**
     * 更新ボタンを押したときのコールバック
     * */
    @OnClick(R.id.fragment_player_reload_btn)
    fun reloadButtonPressed() {
        Timber.d("reload button pressed.")
        playStop()
        playStart()
    }

    /**
     * 再生を開始する
     * */
    private fun playStart() {
        // rtmp://xxx.xxx.xxx.xxx/live/stream_key
        val uri = Uri.parse("rtmp://$ipAddress/live/$streamKey")

        val player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(context), DefaultTrackSelector(), DefaultLoadControl())
        playerView.player = player
        // rtmpプロトコルを使用する場合はRtmpDataSourceFactory()を使用する
        val mediaSource = ExtractorMediaSource(uri, RtmpDataSourceFactory(), DefaultExtractorsFactory(), null, null)
        player.prepare(mediaSource)
        player.playWhenReady = true
        this.player = player
    }

    /**
     * 再生を停止する
     * */
    private fun playStop() =
            player?.let {
                it.playWhenReady = false
                it.release()
            }
}
