package com.colaman.common.common.rx;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.colaman.common.imp.IRxConsumer;

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/3/2
 *     desc   : 封装处理了Livedata发射过来的RxData类型数据并且根据不同状态回调不同方法，具体分发处理逻辑在这个类处理
 * </pre>
 */
public abstract class RxConsumer<T> implements Observer<RxData<T>>, IRxConsumer<T> {
    private RxData.STATUS mCurrentStatus;

    @Override
    public void onChanged(@Nullable RxData<T> tRxData) {
        if (tRxData == null) {
            return;
        }
        mCurrentStatus = tRxData.getStatus();
        switch (tRxData.getStatus()) {
            case SUCCESS:
                onNext(tRxData.getData());
                break;
            case FAIL:
                onError(tRxData.getThrowable());
                break;
            case COMPLETE:
                onComplete();
                break;
            case UNSUSCRIBE:
                onUnsucrible();
                break;
            case SUSCRIBERIB:
                onSuscrible();
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onUnsucrible() {

    }

    @Override
    public void onSuscrible() {

    }

    public RxData.STATUS getCurrentStatus() {
        return mCurrentStatus;
    }

    public boolean isSuccess() {
        return mCurrentStatus.equals(RxData.STATUS.SUCCESS);
    }

    public boolean isFail() {
        return mCurrentStatus.equals(RxData.STATUS.FAIL);
    }

}
