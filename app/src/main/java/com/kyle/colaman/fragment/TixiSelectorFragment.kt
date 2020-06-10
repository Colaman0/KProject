package com.kyle.colaman.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.layoutmanager.WrapLinearlayoutManager
import kotlinx.android.synthetic.main.layout_tixi_selector.*

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系一级二级选项fragment
 */
class TixiSelectorFragment(val viewmodel: TixiViewModel) : BottomSheetDialogFragment() {

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
//        rv_frist.init(
//            firstAdapter,
//            WrapLinearlayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
//        )
//        firstAdapter.registerClick(OnItemClickListener { position, _ -> selectHelper.check(position) })
//        viewmodel.tixiItems.observe(viewLifecycleOwner, object : RxDataObserver<List<TixiEntity>> {
//            override fun onSuccess(data: List<TixiEntity>) {
//            }
//
//            override fun onDone() {
//                super.onDone()
//                if (viewmodel.tixiItems.lastData != null) {
//                    updateList(viewmodel.tixiItems.lastData!!)
//                }
//            }
//        })
    }

    private fun updateList(data: List<TixiEntity>) {
        firstAdapter.clear()
        data.forEach { firstLevel ->
            val item =
                ItemFirstTixiVModel(entity = firstLevel) {
                    showSecondLevel(firstLevel)
                }
            firstAdapter.add(item)
            selectHelper.add(item)
            if (viewmodel.firstItem.get() === firstLevel.name) {
                selectHelper.singleCheck(item)
                (rv_frist.layoutManager as LinearLayoutManager).scrollToPosition(
                    data.indexOf(
                        firstLevel
                    )
                )
                showSecondLevel(firstLevel)
            }
        }
        firstAdapter.notifyDataSetChanged()
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