package com.colaman.kyle.common.expand

import com.blankj.utilcode.util.LogUtils
import io.reactivex.internal.operators.completable.CompletableDoFinally
import kotlin.Exception

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/11/6
 *     desc   :
 * </pre>
 */
typealias RunBlock = () -> Unit


inline fun catchError(block: RunBlock) = try {
    block.invoke()
} catch (e: Exception) {
    LogUtils.e(e)
}


inline fun catchError(block: RunBlock, finallyBlock: RunBlock) {
    try {
        block.invoke()
    } catch (e: Exception) {
        LogUtils.e(e)
    } finally {
        finallyBlock.invoke()
    }
}

fun retry(time: Int = Int.MAX_VALUE,block: RunBlock) {

}