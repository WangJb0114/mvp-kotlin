package com.wuba.weizhang.mvp

import android.app.Activity

abstract class BaseMvpActivity<out T : IBasePresenter<out IBaseView>> : Activity() {

    protected val mPresenter: T by lazy { createPresenter() }

    protected abstract fun createPresenter(): T

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}
