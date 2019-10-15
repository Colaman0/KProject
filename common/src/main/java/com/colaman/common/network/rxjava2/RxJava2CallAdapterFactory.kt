/*
 * Copyright (C) 2015 Square, Inc.
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
package com.colaman.common.network.rxjava2

import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit

/**
 * A [call adapter][CallAdapter.Factory] which uses RxJava 2 for creating observables.
 *
 *
 * Adding this class to [Retrofit] allows you to return an [Observable],
 * [Flowable], `Single`, [Completable] or [Maybe] from service methods.
 * <pre>`
 * interface MyService {
 * &#64;GET("user/me")
 * Observable<User> getUser()
 * }
`</pre> *
 * There are three configurations supported for the `Observable`, `Flowable`,
 * `Single`, and `Maybe` type parameter:
 *
 *  * Direct body (e.g., `Observable<User>`) calls `onNext` with the deserialized body
 * for 2XX responses and calls `onError` with [HttpException] for non-2XX responses and
 * [IOException] for network errors.
 *  * Response wrapped body (e.g., `Observable<Response<User>>`) calls `onNext`
 * with a [Response] object for all HTTP responses and calls `onError` with
 * [IOException] for network errors
 *  * Result wrapped body (e.g., `Observable<Result<User>>`) calls `onNext` with a
 * [Result] object for all HTTP responses and errors.
 *
 */
class RxJava2CallAdapterFactory private constructor(private val scheduler: Scheduler?) :
    CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = CallAdapter.Factory.getRawType(returnType)

        if (rawType == Completable::class.java) {
            // Completable is not parameterized (which is what the rest of this method deals with) so it
            // can only be created with a single configuration.
            return RxJava2CallAdapter(
                Void::class.java,
                scheduler,
                false,
                true,
                false,
                false,
                false,
                true
            )
        }

        val isFlowable = rawType == Flowable::class.java
        val isSingle = rawType == Single::class.java
        val isMaybe = rawType == Maybe::class.java
        if (rawType != Observable::class.java && !isFlowable && !isSingle && !isMaybe) {
            return null
        }

        var isResult = false
        var isBody = false
        val responseType: Type
        if (returnType !is ParameterizedType) {
            val name = if (isFlowable) "Flowable" else if (isSingle) "Single" else "Observable"
            throw IllegalStateException(
                name + " return type must be parameterized"
                        + " as " + name + "<Foo> or " + name + "<? extends Foo>"
            )
        }

        val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType)
        val rawObservableType = CallAdapter.Factory.getRawType(observableType)
        if (rawObservableType == Response::class.java) {
            if (observableType !is ParameterizedType) {
                throw IllegalStateException("Response must be parameterized" + " as Response<Foo> or Response<? extends Foo>")
            }
            responseType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
        } else if (rawObservableType == Result::class.java) {
            if (observableType !is ParameterizedType) {
                throw IllegalStateException("Result must be parameterized" + " as Result<Foo> or Result<? extends Foo>")
            }
            responseType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
            isResult = true
        } else {
            responseType = observableType
            isBody = true
        }

        return RxJava2CallAdapter(
            responseType, scheduler, isResult, isBody, isFlowable,
            isSingle, isMaybe, false
        )
    }

    companion object {
        /**
         * Returns an instance which creates synchronous observables that do not operate on any scheduler
         * by default.
         */
        fun create(): RxJava2CallAdapterFactory {
            return RxJava2CallAdapterFactory(null)
        }

        /**
         * Returns an instance which creates synchronous observables that
         * [subscribe on][Observable.subscribeOn] `scheduler` by default.
         */
        fun createWithScheduler(scheduler: Scheduler?): RxJava2CallAdapterFactory {
            if (scheduler == null) throw NullPointerException("scheduler == null")
            return RxJava2CallAdapterFactory(scheduler)
        }
    }
}
