package com.ljb.mvp.kotlin.presenter

import com.ljb.mvp.kotlin.common.LoginUser
import com.ljb.mvp.kotlin.common.rx.subscribeNet
import com.ljb.mvp.kotlin.contract.EventsContract
import com.ljb.mvp.kotlin.protocol.http.IReposHttpProtocol
import com.ljb.mvp.kotlin.protocol.http.IUserHttpProtocol
import com.ljb.mvp.kotlin.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.presenter.BaseMvpPresenter
import mvp.ljb.kt.presenter.getContextEx
import net.ljb.kt.client.HttpFactory

/**
 * @Author:Kotlin MVP Plugin
 * @Date:2019/04/20
 * @Description input description
 **/
class EventPresenter : BaseMvpPresenter<EventsContract.IView>(), EventsContract.IPresenter {

    private var mPage = 1

    override fun onLoadMore() {
        mPage++
        getDataFromNet(mPage)
    }

    override fun onRefresh() {
        mPage = 1
        getDataFromNet(mPage)
    }

    private fun getDataFromNet(page: Int) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getEventsByName(LoginUser.login, page)
                .compose(RxUtils.bindToLifecycle(getMvpView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNet(getContextEx()) {
                    onNextEx { getMvpView().showPage(it, page) }
                    onErrorEx { getMvpView().errorPage(it, page) }
                }
    }

    override fun getReposFromUrl(url: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .getReposFromUrl(url)
                .compose(RxUtils.bindToLifecycle(getMvpView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNet(getContextEx()) {
                    onNextEx { getMvpView().setRepos(it) }
                    onErrorEx { getMvpView().setRepos(null) }
                }
    }

}