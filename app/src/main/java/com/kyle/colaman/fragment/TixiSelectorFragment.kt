package com.kyle.colaman.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.kyle.colaman.R
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.viewmodel.ItemFirstTixiVModel
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.helper.RecyclerViewHelper
import com.kyle.colman.helper.SELECT_TYPE
import com.kyle.colman.helper.SelectHelper
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.layoutmanager.WrapLinearlayoutManager
import kotlinx.android.synthetic.main.layout_tixi_selector.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系一级二级选项fragment
 */
@ExperimentalCoroutinesApi
class TixiSelectorFragment() : BottomSheetDialogFragment() {
    var checkPosition = 0

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
        updateList(viewmodel.tixis)
    }


    private fun updateList(data: List<TixiEntity>) {
        firstAdapter.clear()
        selectHelper.datas.clear()
        data.forEachIndexed { index, firstLevel ->
            val item = ItemFirstTixiVModel(entity = firstLevel) {
                selectHelper.check(index)
                showSecondLevel(firstLevel)
            }
            firstAdapter.add(item)
            selectHelper.add(item)
            firstLevel.children?.forEach {
                if (viewmodel.lastIdValue == it.id) {
                    checkPosition = index
                    showSecondLevel(firstLevel)
                }
            }
            if (firstLevel.name?.equals(viewmodel.firstItem.get())!!) {
                checkPosition = index
            }
        }
        firstAdapter.notifyDataSetChanged()
        selectHelper.check(checkPosition)
        RecyclerViewHelper.scrollToPosition(rv_frist, checkPosition)
    }

    override fun onResume() {
        super.onResume()
        if (firstAdapter.getDatas().isNotEmpty()) {
            RecyclerViewHelper.scrollToPosition(rv_frist, checkPosition)
        }
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("ResourceType")
    private fun showSecondLevel(entity: TixiEntity) {
        chip_group.removeAllViews()
        entity.children?.forEach { second ->
            chip_group.addView(Chip(context).apply {
                text = second.name
                if (viewmodel.lastIdValue == second.id) {
                    setChipBackgroundColorResource(R.color.colorPrimary)
                    setTextColor(Color.WHITE)
                }
                setOnClickListener {
                    /**
                     * 点击某一个体系之后，更新[TixiFragment]中header的显示，刷新对应文章列表
                     */
                    viewmodel.updateNewItemInfo(entity.name ?: "", second.name ?: "")
                    // 更新文章
                    viewmodel.refreshArticles(second.id!!)
                    dismiss()
                }
            })
        }
    }
}