package com.example.koinandroid

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface RxSingleSchedulers {

    fun <T> applySchedulers(): SingleTransformer<T, T>

    companion object {
        val DEFAULT: RxSingleSchedulers = object : RxSingleSchedulers {
            override fun <T> applySchedulers(): SingleTransformer<T, T> {
                return SingleTransformer {
                    it.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
            }

        }

        val TEST_SCHEDULERS: RxSingleSchedulers = object : RxSingleSchedulers {
            override fun <T> applySchedulers(): SingleTransformer<T, T> {
                return SingleTransformer {
                    it.subscribeOn(Schedulers.trampoline())
                        .observeOn(Schedulers.trampoline())
                }
            }

        }
    }
}