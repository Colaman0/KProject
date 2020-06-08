package com.kyle.colman.view.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colaman.base.viewmodel.BaseLoadmoreView
import com.kyle.colman.viewmodel.CommonLoadMoreV
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.CommonDiffCallBack
import kotlinx.coroutines.*
import java.util.*

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/30
 *     desc   : 带有一些额外功能的adapter
 *
 * 1.diffutils刷新，只要把viewmodel添加到adpater中，然后调用[diffNotifydatasetchanged]进行刷新就OK了
 *  adapter中的itemviewmodel需要对应重写[RecyclerItemView.isSame]方法去判断两个viewmodel内容是否一致
 *  一般可以根据viewmodel内实体类或者直接判断两个viewmodel是否相同
 *
 * 2.LoadMore 样式设置，开启LoadMore需要调用[switchLoadMore] 或者直接[disableLoadmore]赋值，默认是不开启的，开启之后会有一个默认的样式，如果要自定义样式
 * 调用[setLoadMoreItem]
 *
 * 3.添加Header/Footer ,通过[addHeader] [addFooter]来添加，[headers] [footers]不受刷新影响，实现方式其实是把[headers] [footers]对应添加到原本[viewmodels]
 * 里的头部和底部，并且把对应[viewmodels]的[add] [remove] [clear]方法进行处理，避免操作到了头部和底部view
 *
 * </pre>
 */


class KAdapter(
    context: Context?,
    data: MutableList<RecyclerItemView<out ViewDataBinding, out Any>?> = mutableListOf()
) :
    BaseRecyclerViewAdapter(context, data) {
    val oldDatas = mutableListOf<RecyclerItemView<out ViewDataBinding, out Any>?>()

    // diffutil的callback
    protected val diffCallback by lazy {
        CommonDiffCallBack(oldDatas, viewmodels)
    }


    // 头部view，不随adapter的remove/add改动
    val headers = mutableListOf<RecyclerItemView<*, *>?>()

    // 底部view，不随adapter的remove/add改动
    val footers = mutableListOf<RecyclerItemView<*, *>?>()

    // 是否允许loadmore的标记
    var disableLoadmore = false

    // loadmore的回调集合
    var loadMoreListeners = mutableListOf<OnLoadMoreListener>()

    var loadmoreIng = false


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                }
            }
        }
    }

    /**
     * loadmore的item
     */
    protected var loadMoreItemViewModel: BaseLoadmoreView<*, *>? = null
        get() {
            if (field == null) {
                field = initLoadMoreItemViewModel()
            }
            return field
        }

    init {
        if (data.size > 0) {
            oldDatas.addAll(data)
        }
    }

    /**
     * 初始化一个默认的loading样式item
     * @return RecyclerItemViewModel<*, *>
     */
    fun initLoadMoreItemViewModel(): BaseLoadmoreView<*, *> {
        return CommonLoadMoreV()
    }


    override fun getItemCount(): Int {

        /**
         * 避免第一次加载的时候如果开启loadmore，默认显示了loadingItem
         */
        if (getDatas().size == 0) {
            return 0
        }
        Log.d("cola", "getItemCount = ${getDatas().size + if (disableLoadmore) 1 else 0}")

        return getDatas().size + if (disableLoadmore) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("cola", "getItemViewType = $position")
        if (disableLoadmore && position == itemCount - 1) {
            return loadMoreItemViewModel!!.layoutRes
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        Log.d("cola", "onBindViewHolder = $position")
        if (position != RecyclerView.NO_POSITION) {
            if (disableLoadmore && holder.itemType == loadMoreItemViewModel!!.layoutRes) {
                holder.bindViewModel(
                    loadMoreItemViewModel!! as RecyclerItemView<ViewDataBinding, Any>,
                    position
                )
                if (disableLoadmore && !loadmoreIng) {
                    loadmore()
                }
            } else {
                super.onBindViewHolder(holder, position)
            }
        }
    }

    fun loadmore() {
        loadmoreIng = true
        loadMoreListeners.forEach {
            it.onLoadMore()
        }
    }

    fun finishLoadmore() {
        loadmoreIng = false
    }

    fun disableLoadmore(disable: Boolean) {
        finishLoadmore()
        if (disableLoadmore != disable) {
            recyclerView?.post {
                disableLoadmore = disable
                notifyItemChanged(itemCount)
            }
        }
    }


    /**
     * diffutils 刷新adapter，itemviewmodel需要实现对应的接口
     */
    @SuppressLint("CheckResult")
    suspend fun diffNotifydatasetchanged(
        newData: List<RecyclerItemView<out ViewDataBinding, out Any>?>
    ) {
        withContext(Dispatchers.Default) {
            val result = DiffUtil.calculateDiff(CommonDiffCallBack(oldDatas, newData), true)
            recyclerView?.post {
                result.dispatchUpdatesTo(this@KAdapter)
                getDatas().clear()
                getDatas().addAll(newData)
                oldDatas.clear()
                oldDatas.addAll(newData)
            }
        }
        finishLoadmore()
    }

    override fun bindRecyclerView(recyclerView: RecyclerView?) {
        super.bindRecyclerView(recyclerView)
        val layoutManager = recyclerView?.layoutManager
        recyclerView?.addOnScrollListener(scrollListener)
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if ((disableLoadmore && position == itemCount - 1) ||
                        (position < headers.size) ||
                        (position >= headers.size + getAdapterSize())
                    ) {
                        return layoutManager.spanCount
                    }
                    return 1
                }
            }
        }
    }

    /**
     * 设置loading的itemviewmodel,设置null的时候自动关闭loadmore
     *
     * @param item BaseLoadmoreViewModel<*, *>?
     */
    fun setLoadMoreItem(item: BaseLoadmoreView<ViewDataBinding, Any>?) {
        loadMoreItemViewModel = item
    }


    /**
     * 添加loadmore回调
     * @param listener OnLoadMoreListener
     */
    fun addLoadmoreListener(listener: OnLoadMoreListener) {
        loadMoreListeners.add(listener)
    }

    fun removeLoadmoreListener(listener: OnLoadMoreListener) {
        loadMoreListeners.remove(listener)
    }

    /**
     * 添加底部view
     * @param viewmodel RecyclerItemViewModel<*, *>?
     */
    fun addFooter(viewmodel: RecyclerItemView<*, *>?) {
        footers.add(viewmodel)
        getDatas().add(viewmodel)
    }

    /**
     * 移除底部view
     * @param index Int
     */
    fun removeHeader(index: Int) {
        if (index >= headers.size || index < 0) {
            return
        }
        headers.removeAt(index)
        getDatas().removeAt(index)
    }

    /**
     * 移除底部view
     * @param index Int
     */
    fun removeFooter(index: Int) {
        if (index < 0 || index >= footers.size) {
            return
        }
        val viewModel = footers.get(index)
        if (viewModel != null) {
            getDatas().remove(viewModel)
            footers.removeAt(index)
        }
    }

    /**
     * 添加头部view
     * @param viewmodel RecyclerItemViewModel<*, *>?
     */
    fun addHeader(viewmodel: RecyclerItemView<*, *>?) {
        headers.add(viewmodel)
        getDatas().add(0, viewmodel)
    }

    override fun add(view: RecyclerItemView<out ViewDataBinding, out Any>) {
        super.add(view, getDatas().size - footers.size)
    }

    override fun addAll(
        list: Collection<RecyclerItemView<out ViewDataBinding, out Any>>,
        index: Int
    ) {
        super.addAll(list, getDatas().size - footers.size)
    }

    override fun remove(index: Int) {
        val realIndex = headers.size + index
        if (realIndex > getDatas().size - footers.size - headers.size) {
            return
        }
        super.remove(realIndex)
    }

    /**
     * 获取adapter中除了[headers][footers]以外的viewmodels长度
     * @return Int
     */
    fun getAdapterSize() = getDatas().size - footers.size - headers.size


    override fun clear() {
        super.clear()
        getDatas().addAll(headers)
        getDatas().addAll(footers)
    }

    suspend fun move(from: Int, to: Int) {
        Collections.swap(getDatas(), from, to)
//        diffNotifydatasetchanged(oldDatas, getDatas())
    }

}

interface OnLoadMoreListener {
    fun onLoadMore()
}

/**
 * loadmore item 的状态，可以在刷新数据之后根据状态来回调给loadmore item
 */
enum class LOADMORE_STATE {
    LOADING, SUCCESS, FAILED, NOTHING
}

/**
 * 刷新数据后的状态枚举
 */
enum class NOTIFY_STATE {
    SUCCESS, FAILED
}