package com.colaman.common.common


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/19
 *     desc   : 非粘性livedata
 * </pre>
 */
class NotStickyLiveData<T> : MutableLiveData<T>() {
    private var mCurrentVersion = -1

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, CustomObserver(observer))
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(CustomObserver(observer))
    }

    override fun setValue(value: T) {
        mCurrentVersion++
        super.setValue(value)
    }

    override fun postValue(value: T) {
        mCurrentVersion++
        super.postValue(value)
    }

    /**
     * 多包装一层，自己记录version来去除粘性事件
     */
    inner class CustomObserver<T>(val observer: Observer<T>) : Observer<T> {
        var mVersion: Int = mCurrentVersion

        /**
         * Called when the data is changed.
         * @param t  The new data
         */
        override fun onChanged(t: T?) {
            if (mVersion >= mCurrentVersion) {
                return
            }
            mVersion = mCurrentVersion
            observer.onChanged(t)
        }
    }
}



