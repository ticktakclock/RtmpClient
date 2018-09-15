package com.github.ticktakclock.rtmpclient

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.takusemba.rtmppublisher.Publisher
import com.takusemba.rtmppublisher.PublisherListener
import com.takusemba.rtmppublisher.RtmpPublisher
import timber.log.Timber

private const val STREAM_KEY = "stream_key"
private const val IP_ADDRESS = "ip_address"

/**
 * A simple [Fragment] subclass.
 * Use the [PublisherFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PublisherFragment : Fragment(), PublisherListener {
    companion object {
        /**
         * create new instance of PublisherFragment.
         * @return A new instance of fragment PublisherFragment.
         */
        @JvmStatic
        fun newInstance(ipAddress: String, streamKey: String) =
                PublisherFragment().apply {
                    arguments = Bundle().apply {
                        putString(STREAM_KEY, streamKey)
                        putString(IP_ADDRESS, ipAddress)
                    }
                }
    }

    private var streamKey: String? = null
    private var ipAddress: String? = null
    private lateinit var unbinder: Unbinder
    @BindView(R.id.fragment_publisher_glv)
    lateinit var glView: GLSurfaceView
    @BindView(R.id.fragment_publisher_publish_btn)
    lateinit var publishButton: Button
    private var publisher: RtmpPublisher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        arguments?.let {
            streamKey = it.getString(STREAM_KEY)
            ipAddress = it.getString(IP_ADDRESS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")
        val view = inflater.inflate(R.layout.fragment_publisher, container, false)
        unbinder = ButterKnife.bind(this, view);
        preparePublisher()
        updateControl()
        return view
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume. ipAddress=\"$ipAddress\", streamkey=\"$streamKey\"")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    /**
     * PublisherListener.onFailedToConnect()
     * */
    override fun onFailedToConnect() {
        Timber.e("onFailedToConnect()")
        showCaution(R.string.fragment_publisher_caution_failed_to_connect)
        updateControl()
    }

    /**
     * PublisherListener.onDisconnected()
     * */
    override fun onDisconnected() {
        Timber.e("onDisconnected()")
        showCaution(R.string.fragment_publisher_caution_disconnected)
        updateControl()
    }

    /**
     * PublisherListener.onStarted()
     * */
    override fun onStarted() {
        Timber.d("onStarted()")
        showCaution(R.string.fragment_publisher_caution_started)
        updateControl()
    }

    /**
     * PublisherListener.onStopped()
     * */
    override fun onStopped() {
        Timber.d("onStopped()")
        showCaution(R.string.fragment_publisher_caution_stopped)
        updateControl()
    }

    /**
     * 配信ボタンのイベントリスナ
     * */
    @OnClick(R.id.fragment_publisher_publish_btn)
    fun publishButtonPressed() {
        if (isPublishing()) stopPublish()
        else startPublish()
    }

    /**
     * 配信開始する
     * */
    private fun startPublish() = publisher?.startPublishing()

    /**
     * 配信停止する
     * */
    private fun stopPublish() = publisher?.stopPublishing()

    /**
     * preparePublisher
     *
     * onResume()前に呼ぶ必要がある。ライブラリの制約
     * */
    private fun preparePublisher() {
        // rtmp://xxx.xxx.xxx.xxx/live/stream_key
        val url = "rtmp://$ipAddress/live/$streamKey"
        // 転送不可を下げるため動画サイズは320x240とする
        publisher = Publisher.Builder(activity as AppCompatActivity)
                .setGlView(glView)
                .setUrl(url)
                .setSize(320, 240)
                .setAudioBitrate(Publisher.Builder.DEFAULT_AUDIO_BITRATE)
                .setVideoBitrate(Publisher.Builder.DEFAULT_VIDEO_BITRATE)
                .setCameraMode(Publisher.Builder.DEFAULT_MODE)
                .setListener(this)
                .build()
    }

    /**
     * 配信中かどうか判断し、UIの表示を更新する
     * */
    private fun updateControl() =
            if (isPublishing()) publishButton.setText(R.string.fragment_publisher_btn_stop_publish)
            else publishButton.setText(R.string.fragment_publisher_btn_start_publish)

    /**
     * 配信中かどうか判定する
     * */
    private fun isPublishing() = publisher?.isPublishing ?: false

    /**
     * トーストを表示する
     *
     * @param resId メッセージID
     * */
    private fun showCaution(resId: Int) = Toast.makeText(context, getString(resId), Toast.LENGTH_SHORT)
            .apply { setGravity(Gravity.CENTER, 0, 0) }
            .run { show() }
}
