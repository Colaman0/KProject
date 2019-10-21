/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.colaman.kyle.network.rxjava2


import com.colaman.kyle.common.rx.initHttp

import java.lang.reflect.Type

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Scheduler
import retrofit2.Call
import retrofit2.CallAdapter

internal class RxJava2CallAdapter(
    private val responseType: Type,
    private val scheduler: Scheduler?,
    private val isResult: Boolean,
    private val isBody: Boolean,
    private val isFlowable: Boolean,
    private val isSingle: Boolean,
    private val isMaybe: Boolean,
    private val isCompletable: Boolean
) : CallAdapter<Any, Any> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<Any>): Any {
        val responseObservable = CallObservable(call)

        var observable: Observable<*>
        if (isResult) {
            observable = ResultObservable(responseObservable)
        } else if (isBody) {
            observable = BodyObservable(responseObservable)
        } else {
            observable = responseObservable
        }

        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler)
        }

        if (isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST)
        }
        if (isSingle) {
            return observable.singleOrError()
        }
        if (isMaybe) {
            return observable.singleElement()
        }
        observable.initHttp(call.request())
        return if (isCompletable) {
            observable.ignoreElements()
        } else observable
    }


}
