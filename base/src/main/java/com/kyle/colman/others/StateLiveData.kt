package com.kyle.colman.others

import androidx.lifecycle.*
import com.kyle.colman.helper.toKError
import com.kyle.colman.network.KError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

/**
 * Author   : kyle
 * Date     : 2020/6/10
 * Function : 在原有livedata基础上增加发射状态事件，在[observe]方法中使用[StateObserver]来代替默认的[Observer]
 */
class StateLiveData<T> : LiveData<STATE<T?>>() {
    /**
     * 这里都用[setValue]方式去发射是为了避免发射间隙太短导致前面的值被覆盖
     *
     */
    fun emitLoading() {
        value = LOADING
    }

    fun emitError(throwable: KError) {
        value = FAIL(throwable)
    }

    fun emitSuccess(data: T) {
        value = SUCCESS(data)
    }

    fun emitCompleted() {
        value = COMPLETED
    }

    fun emit(state: STATE<T>) {
        value = state
    }
}

/**
 * 可以发射[STATE]的livedata 和[liveData]创建方式一样
 * 通过[flow]发射数据并且转成livedata,并且在对应事件周期发射[STATE],用[StateObserver]订阅之后可以收到对应的方法
 * 比如[StateObserver.success],[StateObserver.loading]等...
 *
 * @param T
 * @param context
 * @param block
 * @return
 */
@ExperimentalCoroutinesApi
fun <T> stateLivedata(
    context: CoroutineContext = Dispatchers.IO,
    block: suspend FlowCollector<STATE<T>>.() -> Unit
): LiveData<STATE<T>> {
    return flow(block = block)
        .onStart { emit(LOADING) }
        .catch { emit(FAIL(it.toKError())) }
        .onCompletion { emit(COMPLETED) }
        .flowOn(context)
        .asLiveData()
}

/**
 * 在[flow]上绑定一个[StateLiveData]
 * 在[flow]对应的周期事件里会用[StateLiveData]发射事件
 * 可以配合[StateObserver]使用
 *
 * 和[stateLivedata]的区别不大，但是这个方法可以绑定一个已有的livedata去使用
 * [stateLivedata]每次都会创建一个新的livedata
 * 如果需要用到[switchMap]去监听另一个livedata然后再创建一个新的livedata的话就用[stateLivedata]
 * @param T
 * @param liveData
 * @return
 */
@ExperimentalCoroutinesApi
@JvmOverloads
suspend fun <T> Flow<T>.bindLivedata(
    liveData: StateLiveData<T>
): StateLiveData<T> {
    this.onStart {
        liveData.emitLoading()
    }.catch {
        liveData.emitError(it.toKError())
    }.onCompletion {
        liveData.emitCompleted()
    }.onEach {
        liveData.emitSuccess(it)
    }.collect()
    return liveData
}

suspend fun <T> FlowCollector<STATE<T>>.emitSuccess(data: T) {
    emit(SUCCESS(data))
}

suspend fun <T> LiveDataScope<STATE<T>>.emitLoading() {
    emit(LOADING)
}

suspend fun <T> LiveDataScope<STATE<T>>.emitError(throwable: KError) {
    emit(LOADING)
}


/**
 * 表明Livedata事件的状态密封类
 *
 * @param T 数据
 */
sealed class STATE<out T>
object LOADING : STATE<Nothing>()
object COMPLETED : STATE<Nothing>()
class FAIL(val throwable: KError) : STATE<Nothing>()
class SUCCESS<T>(val data: T) : STATE<T>()
