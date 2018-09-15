package com.github.ticktakclock.rtmpclient

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnMainFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {
    companion object {
        /**
         * create new instance of MainFragment.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private var listener: OnMainFragmentInteractionListener? = null
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView")
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        unbinder = ButterKnife.bind(this, view);
        return view
    }

    /**
     * 視聴ボタンを押したときのコールバック
     * */
    @OnClick(R.id.fragment_main_play_btn)
    fun playButtonPressed() {
        listener?.onNavigatePlayer()
    }

    /**
     * 配信ボタンを押したときのコールバック
     * */
    @OnClick(R.id.fragment_main_publish_btn)
    fun publishButtonPressed() {
        listener?.onNavigatePublisher()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach")
        if (context is OnMainFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView")
        unbinder.unbind()
    }

    /**
     * Fragmentのイベントリスナ
     */
    interface OnMainFragmentInteractionListener {
        fun onNavigatePlayer()
        fun onNavigatePublisher()
    }
}
