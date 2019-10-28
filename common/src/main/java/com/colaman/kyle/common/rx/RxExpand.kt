package com.colaman.kyle.common.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.colaman.kyle.impl.IExceptionAdapter
import com.colaman.kyle.base.recyclerview.PageHelper
import com.colaman.kyle.common.network.KErrorExceptionFactory
import com.colaman.kyle.common.param.KError
import com.colaman.kyle.entity.HttpModel
import com.colaman.kyle.entity.PageDTO
import com.colaman.kyle.impl.IKResponse
import com.colaman.kyle.impl.IRxConsumer
import com.colaman.kyle.impl.IStatus
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Request

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : rxjava 拓展方法
 * </pre>
 */


/**
 * 处理网络请求response
 *
 * @param T 实际data， 上游是由INetworkResponse包裹起来的data
 * @return
 */
fun <T, C : IKResponse<T>> Observable<C>.analysisResponse(): Observable<T> =
    flatMap {
        //  reponse如果是成功的话，就直接返回data
        if (it.isSuccess() && it.getData() != null) {
            Observable.just(it.getData())
        } else {
            Observable.empty()
        }
    }


/**
 * 拓展出一个属性出来，用来添加KError的错误适配器
 */
val <T> Observable<T>.kErrorAdapters by lazy {
    return@lazy mutableListOf<IExceptionAdapter<KError>>()
}


/**
 * 拓展出一个属性出来，用来添加[IRxConsumer]
 */
val <T> Observable<T>.rxConsumers by lazy {
    return@lazy mutableListOf<IRxConsumer<T>>()
}

/**
 * 拓展出一个属性出来，用来添加网络请求request
 */
val <T> Observable<T>.httpRequest by lazy {
    return@lazy HttpModel()
}

fun <T> Observable<T>.initHttp(request: Request?) {
    httpRequest.request = request
}

/**
 * 拓展出一个属性出来，用来添加RxLivedata
 */
val <T> Observable<T>.rxLiveDatas by lazy {
    return@lazy mutableListOf<RxLivedata<T>>()
}

/**
 * 拓展出一个属性出来，用来添加IStatus实现类
 */
val <T> Observable<T>.statusImpls by lazy {
    return@lazy mutableListOf<IStatus?>()
}

/**
 * 拓展出一个属性出来，用来添加IStatus实现类
 */
val <T> Observable<T>.kErrorCallback by lazy {
    return@lazy KErrorLamdaRunnable()
}


/**
 * 添加一个KErrorAdatper
 *
 * @param T
 * @param factory
 * @return
 */
fun <T> Observable<T>.addKErrorAdatpers(vararg factory: IExceptionAdapter<KError>): Observable<T> {
    kErrorAdapters.addAll(factory)
    return this
}

/**
 * 用于添加接受Observable中回调，可以当作一个中转的作用，把对应事件分发出去，让实现了[IRxConsumer]的类在对应方法里做处理
 * @receiver Observable<T>
 * @param receiver Array<out IRxConsumer<T>> 实现了[IRxConsumer]的类
 * @return Observable<T>
 */
fun <T> Observable<T>.addConsumerReceiver(vararg receiver: IRxConsumer<T>): Observable<T> {
    rxConsumers.addAll(receiver)
    return this
}


/**
 * 添加实现了IStatus的包裹类，在特定的事件回调中去回调包裹类的正确方法
 *
 * @param T
 * @param status
 * @return
 */
fun <T> Observable<T>.bindStatusImpl(vararg status: IStatus): Observable<T> {
    status.forEach {
        addConsumerReceiver(StatusTransformer(it))
    }
    return this
}


/**
 * 向不同的livedata中绑定当前Observable,之后livedata的实际发射数据动作由当前Observable来托管
 * 在viewmodel层只需要把一个RxLivdata 通过bindRxLivedata来绑定到一个Observable上，具体数据发射不用管
 * observable会处理，model层只需要订阅这个RxLivedata就行了，然后可以把IStatus的实例通过RxLiveData
 * 中保存的Observable来绑定在Observable上，这样一些view比如dialog loading的状态回调就自动托管
 *
 * @param T
 * @param livedata
 * @return
 */
fun <T> Observable<T>.bindRxLivedata(vararg livedata: RxLivedata<T>): Observable<T> {
    rxConsumers.addAll(livedata)
    livedata.forEach {
        it.bindObservable(this)
    }
    return this
}


/**
 * 拓展出一个方法，用来接收KError类型的doOnError， 实际上这个方式是在onError调用时调用的
 *
 * @param T
 * @param error
 * @return
 */
fun <T> Observable<T>.doOnKError(callback: (error: KError) -> Unit): Observable<T> {
    kErrorCallback.lamdaRunnable = callback
    return this
}

/**
 * 补充订阅的所有方法,在这里把所有status对应调用正确的方法回调
 *
 * @receiver Observable<T>
 */
fun <T> Observable<T>.fullSubscribe() =
    doFinally {
        rxConsumers.forEach {
            it.onFinally()
        }
    }.subscribe({ data ->
        rxConsumers.forEach {
            it.onNext(data)
        }
    }, { throwable ->
        // 分析过滤错误发射出去
        val kThrowable = analysisExcetpion(throwable)
        rxConsumers.forEach {
            it.onError(kThrowable)
        }
    }, {
        rxConsumers.forEach {
            it.onComplete()
        }
    }, {
        rxConsumers.forEach {
            it.onSuscrible()
        }
    })

/**
 * 分析过滤错误 在onError里实现,onNext里发生错误不会调用doOnError，所以需要在onError里实现
 *
 * @param T
 * @param throwable
 */
fun <T> Observable<T>.analysisExcetpion(throwable: Throwable): KError {
    // 实例化一个KErrorExceptionFactory去过滤筛选错误
    val exceptionFactory = KErrorExceptionFactory()
    kErrorAdapters.forEach {
        exceptionFactory.addExceptionCreator(it)
    }
    val error = exceptionFactory.analysisExcetpion(throwable = throwable)
    kErrorCallback.callError(error)
    return error
}


/**
 * 基础切换线程
 * @receiver Observable<T>
 */
fun <T> Observable<T>.switchApiThread(): Observable<T> {
    return subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}


/**
 * 自动解绑rxjava流，统一使用这个拓展方法来处理，方便后续使用其他方式来解绑
 * 默认是destory之后解绑
 *
 * @property life  传入一个Lifecycle，在activity以及viewmodel中都能get到
 */
fun <T> Observable<T>.binLife(
    lifecycleOwner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): Observable<T> {
    return bindToLifecycle(lifecycleOwner)
}


/**
 * 自动解绑rxjava流，统一使用这个拓展方法来处理，方便后续使用其他方式来解绑
 * 默认是destory之后解绑
 *
 * @property life  传入一个Lifecycle，在activity以及viewmodel中都能get到
 */
fun <T> Observable<T>.bindUntil(
    lifecycleOwner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): Observable<T> {
    return bindUntil(lifecycleOwner, event)
}


/**
 * 自动处理分页信息
 * @receiver Observable<PageDTO<T>>
 * @param pageHelper PageHelper
 * @return Observable<T>
 */
fun <T> Observable<PageDTO<T>>.handlePage(pageHelper: PageHelper): Observable<T> {
    return flatMap {
        // 记录分页信息
        pageHelper.currentPage = it.getCurrentPage()
        pageHelper.totalPage = it.getTotalPageSize()
        // 如果是最后一页，自动将loadmore关闭
        pageHelper.adapter?.switchLoadMore(pageHelper.isLastPage)
        if (it.data != null) {
            Observable.just(it.data)
        } else {
            // 数据为null也关闭loadmore
            pageHelper.adapter?.switchLoadMore(pageHelper.isLastPage)
            Observable.empty()
        }
    }
}

/**
 * 包裹一个KError的lamda回调，因为拓展属性没法像正常属性一样延迟赋值lamda函数，所以添加这个类来处理
 *
 * @property lamdaRunnable
 */
class KErrorLamdaRunnable(var lamdaRunnable: ((error: KError) -> Unit)? = null) {
    /**
     * 执行lamda函数
     */
    fun callError(error: KError) {
        lamdaRunnable?.invoke(error)
    }
}
