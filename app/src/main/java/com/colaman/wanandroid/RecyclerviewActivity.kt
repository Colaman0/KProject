package com.colaman.kproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.colaman.kproject.viewmodel.ItemTextViewmodel
import com.colaman.kyle.common.recyclerview.adapter.KAdapter
import com.colaman.kyle.common.recyclerview.layoutmanager.WrapLinearlayoutManager
import kotlinx.android.synthetic.main.activity_recyclerview.*

class RecyclerviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)


        val adapter = KAdapter(this).apply {
            bindRecyclerView(this@RecyclerviewActivity.recyclerview)
        }
        recyclerview.apply {
            this.adapter = adapter
            layoutManager = WrapLinearlayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        add.setOnClickListener {
            for (index in 0..5) {
                adapter.add(ItemTextViewmodel(adapter.size().toString()))
            }
            adapter.disableLoadmore(true)
            adapter.diffNotifydatasetchanged()
        }
        loadmore.setOnClickListener {
            adapter.disableLoadmore(!adapter.disableLoadmore)
        }

        adapter.addLoadMoreListener {
            Log.d("kyle", "loadmore")
        }

        remove.setOnClickListener {
            for (index in 0..5) {
                adapter.remove(0)
            }
            adapter.diffNotifydatasetchanged()
            adapter.disableLoadmore(false)
        }

    }
}
