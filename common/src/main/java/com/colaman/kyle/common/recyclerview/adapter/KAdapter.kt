package com.colaman.kyle.common.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.common.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.base.recyclerview.adapter.BaseRecyclerViewAdapter
import com.colaman.kyle.base.viewmodel.BaseLoadmoreViewModel
import com.colaman.kyle.base.viewmodel.CommonLoadMoreVModel
import com.colaman.kyle.common.rx.binLife
import com.colaman.kyle.common.recyclerview.CommonDiffCallBack
import com.colaman.kyle.common.rx.fullSubscribe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/30
 *     desc   : 带有一些额外功能的adapter
 *
 * 1.diffutils刷新，只要把viewmodel添加到adpater中，然后调用[diffNotifydatasetchanged]进行刷新就OK了
 *  adapter中的itemviewmodel需要对应重写[RecyclerItemViewModel.isSame]方法去判断两个viewmodel内容是否一致
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
    datas: MutableList<RecyclerItemViewModel<out ViewDataBinding, out Any>?> = mutableListOf()
) :
    BaseRecyclerViewAdapter(context, datas) {
    val oldDatas = mutableListOf<RecyclerItemViewModel<out ViewDataBinding, out Any>?>()

    // diffutil的callback
    protected val diffCallback by lazy {
        CommonDiffCallBack(oldDatas, viewmodels)
    }


    // 头部view，不随adapter的remove/add改动
    val headers = mutableListOf<RecyclerItemViewModel<*, *>?>()

    // 底部view，不随adapter的remove/add改动
    val footers = mutableListOf<RecyclerItemViewModel<*, *>?>()

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
    protected var loadMoreItemViewModel: BaseLoadmoreViewModel<*, *>? = null
        get() {
            if (field == null) {
                field = initLoadMoreItemViewModel()
            }
            return field
        }

    init {
        if (datas.size > 0) {
            oldDatas.addAll(datas)
        }
    }

    /**
     * 初始化一个默认的loading样式item
     * @return RecyclerItemViewModel<*, *>
     */
    fun initLoadMoreItemViewModel(): BaseLoadmoreViewModel<*, *> {
        val viewmodel = CommonLoadMoreVModel()
        viewmodel.bindAdapter(this)
        return viewmodel
    }


    override fun getItemCount(): Int {

        /**
         * 避免第一次加载的时候如果开启loadmore，默认显示了loadingItem
         */
        if (getDatas().size == 0) {
            return 0
        }
        return getDatas().size + if (disableLoadmore && getAdapterSize() > 0) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (disableLoadmore && position == itemCount - 1) {
            return loadMoreItemViewModel!!.initLayoutRes()
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            if (disableLoadmore && holder.itemType == loadMoreItemViewModel!!.initLayoutRes()) {

                holder.bindViewModel(
                    loadMoreItemViewModel!! as RecyclerItemViewModel<ViewDataBinding, Any>,
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
        ToastUtils.showShort("加载更多")
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
            disableLoadmore = disable
            recyclerView?.post {
                notifyItemChanged(itemCount)
            }
        }
    }


    /**
     * diffutils 刷新adapter，itemviewmodel需要实现对应的接口
     */
    @SuppressLint("CheckResult")
    fun diffNotifydatasetchanged(disableLoadmore: Boolean = false) {
        this.disableLoadmore = disableLoadmore

        /**
         * 清空数据的时候直接刷新列表，减少计算差异，并且避免loadmore item的存在导致数据不一致崩溃
         */
        if (getDatas().size == 0) {
            loadmoreIng = false
            notifyDataSetChanged()
            return
        }
        /**
         * 数据量比较小的时候可以不用切换线程，线程的切换也是需要耗时
         */
        if (getDatas().size > 500) {
            Observable.just("")
                .subscribeOn(Schedulers.computation())
                .map { DiffUtil.calculateDiff(diffCallback, false) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { diffResult ->
                        diffResult.dispatchUpdatesTo(this)
                }
                .doOnComplete {
                    loadmoreIng = false
                    oldDatas.clear()
                    oldDatas.addAll(viewmodels)
                }
                .binLife(lifecycleOwner!!)
                .fullSubscribe()
        } else {
            val result = DiffUtil.calculateDiff(diffCallback, false)
            Observable.just(result)
                .doOnNext {
                        result.dispatchUpdatesTo(this)
                }
                .doOnComplete {
                    loadmoreIng = false
                    oldDatas.clear()
                    oldDatas.addAll(viewmodels)
                }
                .binLife(lifecycleOwner!!)
                .fullSubscribe()
        }
    }


    /**
     * diffutils 刷新adapter，itemviewmodel需要实现对应的接口
     */
    @SuppressLint("CheckResult")
    fun diffNotifydatasetchanged() {

        /**
         * 清空数据的时候直接刷新列表，减少计算差异，并且避免loadmore item的存在导致数据不一致崩溃
         */
        if (getDatas().size == 0) {
            loadmoreIng = false
            notifyDataSetChanged()
            return
        }
        /**
         * 数据量比较小的时候可以不用切换线程，线程的切换也是需要耗时
         */
        if (getDatas().size > 500) {
            Observable.just("")
                .subscribeOn(Schedulers.computation())
                .map { DiffUtil.calculateDiff(diffCallback, false) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { diffResult ->
                        diffResult.dispatchUpdatesTo(this)
                }
                .doOnComplete {
                    loadmoreIng = false
                    oldDatas.clear()
                    oldDatas.addAll(viewmodels)
                }
                .binLife(lifecycleOwner!!)
                .fullSubscribe()
        } else {
            val result = DiffUtil.calculateDiff(diffCallback, false)
            Observable.just(result)
                .doOnNext {
                        result.dispatchUpdatesTo(this)
                }
                .doOnComplete {
                    loadmoreIng = false
                    oldDatas.clear()
                    oldDatas.addAll(viewmodels)
                }
                .binLife(lifecycleOwner!!)
                .fullSubscribe()
        }
    }


    /**
     * 是否开启loadmore
     *
     * @param disableLoadmore 是否允许loadmore
     * @param refreshNow 是否立刻刷新，默认为false，手动调用[diffNotifydatasetchanged]刷新视觉效果比较同步
     */
    fun switchLoadMore(disable: Boolean, refreshNow: Boolean = false) {
        if (disableLoadmore != disable) {
            disableLoadmore = disable
            if (disable) {
                loadMoreItemViewModel?.state = LOADMORE_STATE.NOTHING
            }
            if (refreshNow) {
                recyclerView?.post {
                    notifyItemChanged(itemCount)
                }
            }
        }
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
    fun setLoadMoreItem(item: BaseLoadmoreViewModel<ViewDataBinding, Any>?) {
        loadMoreItemViewModel = item
    }


    /**
     * 添加loadmore回调
     * @param listener OnLoadMoreListener
     */
    fun addLoadmoreListener(listener: OnLoadMoreListener) {
        loadMoreListeners.add(listener)
    }

    fun addLoadMoreListener(callback: () -> Unit) {
        loadMoreItemViewModel?.addLoadMoreListener(callback)
    }

    /**
     * 添加底部view
     * @param viewmodel RecyclerItemViewModel<*, *>?
     */
    fun addFooter(viewmodel: RecyclerItemViewModel<*, *>?) {
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
    fun addHeader(viewmodel: RecyclerItemViewModel<*, *>?) {
        headers.add(viewmodel)
        getDatas().add(0, viewmodel)
    }

    override fun add(viewModel: RecyclerItemViewModel<out ViewDataBinding, out Any>) {
        super.add(viewModel, getDatas().size - footers.size)
    }

    override fun addAll(
        list: Collection<RecyclerItemViewModel<out ViewDataBinding, out Any>>,
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

    fun move(from: Int, to: Int) {
        Collections.swap(getDatas(), from, to)
        diffNotifydatasetchanged()
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