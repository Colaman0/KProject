package com.kyle.colaman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.colaman.wanandroid.viewmodel.ItemTextViewmodel
import com.kyle.colaman.databinding.ItemTextBinding
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder
import com.kyle.colman.view.recyclerview.adapter.CommonAdapter
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.recyclerview
import kotlinx.android.synthetic.main.activity_list2.*

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list2)
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val adapter = CommonAdapter(this)
        adapter.bindRecyclerView(recyclerview)
        adapter.loadmoreAdapter?.addLoadmoreCallback {
            LogUtils.d("startloadmore")
        }
        var size = 0
        btn_1.setOnClickListener {
            size++
            adapter.getEditableItems().add(0, ItemText(size.toString()))
            adapter.diffNotify()
        }
        btn_2.setOnClickListener {
            adapter.getEditableItems().removeAt(0)
            adapter.diffNotify()
        }
        btn_3.setOnClickListener {
            adapter.loadmoreAdapter?.disableLoadmore(
                !adapter.loadmoreAdapter?.disableLoadmore!!
            )
        }
    }
}

class ItemText(val text: String) : PagingItemView<ItemText, ItemTextBinding>(R.layout.item_text) {
    override fun onBindView(holder: PagingVHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textview).text = text
    }

    override fun isUISame(data: ItemText): Boolean {
        return data.text == text

    }

    override fun isItemSame(data: ItemText): Boolean {
        return data.text == text

    }
}