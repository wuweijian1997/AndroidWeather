package com.logic.weather.ui.place

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.logic.weather.MainActivity
import com.logic.weather.WeatherActivity
import com.logic.weather.databinding.FragmentPlaceBinding
import com.logic.weather.util.TimerUtil

class PlaceFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java] }
    private lateinit var binding: FragmentPlaceBinding

    private lateinit var adapter: PlaceAdapter
    private val updateText = 1
    private val handle = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                updateText -> viewModel.searchPlaces(msg.obj.toString())
            }
        }
    }
    ///为Fragment创建视图时调用
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater)
        return binding.root
    }

    ///确保与Fragment相关联的Activity已经创建完毕时调用.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            WeatherActivity.open(context!!, place.location.lng, place.location.lat, place.name)
            activity?.finish()
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        val searchPlaceEdit = binding.searchPlaceEdit
        val bgImageView = binding.bgImageView
        searchPlaceEdit.addTextChangedListener { text: Editable? ->
            val content = text.toString()
            if (content.isNotEmpty()) {
                TimerUtil().antiShake({
                    val msg = Message()
                    msg.what = updateText
                    msg.obj = content
                    handle.sendMessage(msg)
                })
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}