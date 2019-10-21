package com.colaman.kyle.common.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/3/2
 *     desc   : 空订阅，实际操作内容放在RxConsumer中
 * </pre>
 */
public class RxObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

}
