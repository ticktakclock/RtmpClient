package com.github.ticktakclock.rtmpclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.lang.RuntimeException

private const val STREAM_KEY = "stream_key"
private const val IP_ADDRESS = "ip_address"

class PublisherActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun newInstance(context: Context, ipAddress: String, streamKey: String) =
                Intent(context, PublisherActivity::class.java).apply {
                    putExtra(STREAM_KEY, streamKey)
                    putExtra(IP_ADDRESS, ipAddress)
                }
    }

    private lateinit var streamKey: String
    private lateinit var ipAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publisher)
        val bundle = intent?.extras ?: throw RuntimeException("must call PublisherActivity.newInstance().")
        streamKey = bundle.getString(STREAM_KEY) ?: throw RuntimeException("streamKey must be not null or empty.")
        ipAddress = bundle.getString(IP_ADDRESS) ?: throw RuntimeException("ipAddress must be not null or empty.")
        navigateToPublisher()
    }

    /**
     * 配信画面に遷移する
     * */
    private fun navigateToPublisher() {
        val fragment = PublisherFragment.newInstance(ipAddress, streamKey)
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_publisher_container_fl, fragment)
                .commit()
    }
}
