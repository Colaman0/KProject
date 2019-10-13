package com.colaman.common.imp

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/23
 *     desc   : 可选择的view，比如底部顶部导航，一切可以选择的view都可以实现，主要用来和导航条做绑定
 * </pre>
 */
interface ISelectable {


    fun onSeletct(position: Int)
}