package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.myview.VpAdapter
import kotlinx.android.synthetic.main.first_fragment_layout.*

class FirstFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.first_fragment_layout,container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var inflaters=layoutInflater

        var vpAdapter=VpAdapter(listOf(inflaters.inflate(R.layout.pagerview,null,false)
                ,inflaters.inflate(R.layout.pagerview,null,false)
                ,inflaters.inflate(R.layout.pagerview,null,false)
                ,inflaters.inflate(R.layout.pagerview,null,false)))

        my_banner.adapter=vpAdapter

    }
}