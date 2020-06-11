package com.kyle.colaman.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.kyle.colaman.R
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.viewmodel.ItemFirstTixiVModel
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.helper.SELECT_TYPE
import com.kyle.colman.helper.SelectHelper
import com.kyle.colman.impl.OnItemClickListener
import com.kyle.colman.others.StateObserver
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.layoutmanager.WrapLinearlayoutManager
import kotlinx.android.synthetic.main.layout_tixi_selector.*

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系一级二级选项fragment
 */
class TixiSelectorFragment() : BottomSheetDialogFragment() {
    val viewmodel by lazy {
        ViewModelProvider(parentFragment!!).get(TixiViewModel::class.java)
    }
    val firstAdapter by lazy {
        KAdapter(context)
    }

    val selectHelper = SelectHelper(SELECT_TYPE.SINGLE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_tixi_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_frist.init(
            firstAdapter,
            WrapLinearlayoutManager(context!!, LinearLayoutManager.VERTICAL, false),
            lifecycleOwner = viewLifecycleOwner
        )
        firstAdapter.registerClick(OnItemClickListener { position, _ -> selectHelper.check(position) })
        updateList(viewmodel.tixis)
    }

    private fun updateList(data: List<TixiEntity>) {
        firstAdapter.clear()
        var position = 0
        data.forEach { firstLevel ->
//            (rv_frist.layoutManager as LinearLayoutManager).scrollToPosition(
//                data.indexOf(
//                    it.entity
//                )
//            )


            val item = ItemFirstTixiVModel(entity = firstLevel) {
                showSecondLevel(firstLevel)
            }
            firstAdapter.add(item)
            firstLevel.children?.forEach {
                if (viewmodel.lastIdValue == it.id) {
                    item.isCheck = true
                    position = firstAdapter.getDatas().size
                }
            }
            selectHelper.add(item)
        }
        firstAdapter.getDatas().forEach { it ->

        }
        firstAdapter.notifyDataSetChanged()
//        recyclerview_k.scrollToPosition(position)
    }

    @SuppressLint("ResourceType")
    private fun showSecondLevel(entity: TixiEntity) {
        chip_group.removeAllViews()
        entity.children?.forEach { second ->
            chip_group.addView(Chip(context).apply {
                text = second.name
                if (viewmodel.secondItem.get() === text) {
                    setChipBackgroundColorResource(R.color.colorPrimary)
                    setTextColor(Color.WHITE)
                }
                setOnClickListener {
                    viewmodel.firstItem.set(entity.name)
                    viewmodel.secondItem.set(second.name)
                    // 更新文章
                    viewmodel.getArticle(second.id!!)
                    dismiss()
                }
            })
        }
    }
}