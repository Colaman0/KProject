package com.colaman.kyle.common.rx;

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/3/2
 *     desc   : livedata与rxjava联动中传递的数据基类，包括数据状态，数据具体值
 * </pre>
 */
public class RxData<T> {

    public enum STATUS {
        SUCCESS, FAIL, COMPLETE, UNSUSCRIBE,SUSCRIBERIB
    }

    private STATUS mSTATUS;
    private T t;
    private Throwable mThrowable;


    public RxData(T t) {
        this(STATUS.SUCCESS, t);
    }

    public RxData(Throwable throwable) {
        this(STATUS.FAIL, throwable);
    }

    public RxData(STATUS STATUS, T t) {
        setStatus(STATUS);
        this.t = t;
    }

    public RxData(STATUS STATUS) {
        setStatus(STATUS);
    }

    public RxData(STATUS STATUS, Throwable throwable) {
        setStatus(STATUS);
        setThrowable(throwable);
    }

    public T getData() {
        return t;
    }

    public RxData setData(T t) {
        this.t = t;
        return this;
    }

    public STATUS getStatus() {
        return mSTATUS;
    }

    public RxData setStatus(STATUS STATUS) {
        mSTATUS = STATUS;
        return this;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }


}
