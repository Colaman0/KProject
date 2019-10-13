package com.colaman.common.common.rx;

import com.colaman.common.imp.IRxData;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * Create by kyle on 2018/12/25
 * Function : 绑定生命周期相关
 */
public class RxLife {

    private RxLife() {

    }

    /**
     * 绑定IRxData,把observable的几个事件操作处理让IRxData来处理
     *
     * @param rxData
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> bindLiveData(IRxData<T> rxData) {
        return upstream -> upstream
                .doOnSubscribe(disposable -> rxData.onSuscrible())
                .doOnNext(rxData::onNext)
                .doOnError(rxData::onError)
                .doOnComplete(rxData::onComplete)
                .doAfterTerminate((Action) rxData::onUnsucrible);
    }


}
