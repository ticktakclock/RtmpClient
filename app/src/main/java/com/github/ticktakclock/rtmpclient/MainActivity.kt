package com.github.ticktakclock.rtmpclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

private const val STREAM_KEY = "android"
// TODO: your IP Address here. xxx.xxx.xxx.xxx
private const val IP_ADDRESS = ""

class MainActivity : AppCompatActivity(), MainFragment.OnMainFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToMain()
    }

    /**
     * OnMainFragmentInteractionListener.onNavigatePlayer()
     * */
    override fun onNavigatePlayer() = navigateToPlayer()

    /**
     * OnMainFragmentInteractionListener.onNavigatePublisher()
     * */
    override fun onNavigatePublisher() = navigateToPublisher()

    /**
     * メイン画面に遷移する
     * */
    private fun navigateToMain() {
        val fragment = MainFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_container_fl, fragment)
                .commit()
    }

    /**
     * 視聴画面に遷移する
     * */
    private fun navigateToPlayer() {
        val fragment = PlayerFragment.newInstance(IP_ADDRESS, STREAM_KEY)
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_container_fl, fragment)
                .addToBackStack(null)
                .commit()
    }

    /**
     * 配信画面に遷移する.
     *
     * 配信画面は別Activityで起動する
     * */
    private fun navigateToPublisher() =
            startActivity(PublisherActivity.newInstance(this, IP_ADDRESS, STREAM_KEY))
}
