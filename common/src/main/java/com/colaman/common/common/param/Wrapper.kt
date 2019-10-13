package com.colaman.common.common.param

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/27
 *     desc   : 包裹类，用于某些场景下不能直接传递对象
 * </pre>
 */
class Wrapper<T> {
    var data: T? = null
}