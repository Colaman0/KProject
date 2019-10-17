package com.colaman.common.common.expand

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.colaman.common.base.recyclerview.adapter.FeaturesRecyclerViewAdapter
import com.colaman.common.common.manager.ImageManager
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 *
 *     author : kyle
 *     time   : 2019/10/17
 *     desc   : bindingadapter
 *
 */

@BindingAdapter("bindSize")
fun View.bindSize(size: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {
    layoutParams.width = dp2px(context, size)
    layoutParams.height = dp2px(context, size)
}


@BindingAdapter("bindWidth", "bindHeight", requireAll = true)
fun View.bindWidthHeight(
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) {
    layoutParams.width = dp2px(context, width)
    layoutParams.height = dp2px(context, height)
}

/**
 * 绑定textview字体大小
 * @receiver TextView
 * @param context Context
 * @param textSize Int
 */
@BindingAdapter("bindTextSize")
fun TextView.bindTextSize(textSize: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
}


/**
 * 给view加上点击事件
 * @receiver View
 * @param action Function0<Unit>
 */
@BindingAdapter("onclick")
fun View.click(action: () -> Unit) {
    setOnClickListener {
        action.invoke()
    }
}

/**
 * 监听EditText内容变化
 *
 * @param action [onTextChangeListener]接口，在xml中只需要绑定viewmodel中对应一个方法就行了，需要有一个string类型参数
 */
@BindingAdapter("textchange")
fun EditText.textChange(action: onTextChangeListener) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            action.onTextChange(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * 加载网络图片拓展方法
 * @receiver ImageView
 * @param url String 网络url
 */
@BindingAdapter("loadUrl")
fun ImageView.loadUrl(url: String) {
    ImageManager.getImageLoader().loadImage(context, url, this)
}


/**
 * 加载圆形网络图片
 * @receiver ImageView
 * @param url String 网络url
 */
@BindingAdapter("loadCircleUrl")
fun ImageView.loadCircleUrl(url: String) {
    ImageManager.getImageLoader().loadCircleImage(context, url, this)
}

@BindingAdapter("bindRecyclerViewAdapter")
fun RecyclerView.bindAdapter(adapter: FeaturesRecyclerViewAdapter) {
    bindLinearAdapter(context, adapter)
}