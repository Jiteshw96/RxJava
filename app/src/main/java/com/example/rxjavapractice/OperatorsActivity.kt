package com.example.rxjavapractice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.rxjavapractice.model.ApiUser
import com.example.rxjavapractice.model.User
import com.example.rxjavapractice.utils.AppConstant
import com.example.rxjavapractice.utils.Utils
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.internal.operators.observable.ObservableSubscribeOn
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import java.util.*
import java.util.concurrent.TimeUnit

class OperatorsActivity : AppCompatActivity() {

    private lateinit var textView:TextView
    companion object{
        private const val TAG = "OperatorsActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators)
        textView = findViewById(R.id.result)
    }



    //Create Simple Observable
    private fun getSimpleObservable():Observable<String>{
        return Observable.just("Cricket","footbal")
    }

    private fun getSimpleObserver():Observer<String>{
        return object:Observer<String>{
            override fun onSubscribe(d: Disposable) {
              Log.d(TAG," OnSubscribe "+d.isDisposed)
            }

            override fun onNext(t: String) {
                textView.append(" On Next Value :  $t")
                textView.append(AppConstant.LINE_SEPARATOR)

            }

            override fun onError(e: Throwable) {
                textView.append(" On Error :  $e")
                textView.append(AppConstant.LINE_SEPARATOR)
            }

            override fun onComplete() {
                textView.append(" On Complete")
                textView.append(AppConstant.LINE_SEPARATOR)
            }
        }
    }



    /* Create Observable with Map Operator to
     * Convert the APIUser to User.
     */

    private fun getMapObservable():Observable<List<ApiUser>>{
        /*return Observable.create{e ->
            if(!e.isDisposed){
                e.onNext(Utils.getApiUserList())
                e.onComplete()
            }
        }*/
        return Observable.create(object: ObservableOnSubscribe<List<ApiUser>>{
            override fun subscribe(emitter: ObservableEmitter<List<ApiUser>>) {
               emitter.onNext(Utils.getApiUserList())
               emitter.onComplete()
            }

        })
    }

    private fun getMapObserver():Observer<List<User>>{
        return object:Observer<List<User>>{
            override fun onSubscribe(d: Disposable) {
               Log.e(TAG,"OnSubscribed" + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
               textView.append("OnNext")
               textView.append(AppConstant.LINE_SEPARATOR)
               for (user in userList){
                   textView.append(" FirstName: ${user.firstName}")
                   textView.append(AppConstant.LINE_SEPARATOR)
               }
                Log.d(TAG, " onNext" + userList.size)
            }

            override fun onError(e: Throwable) {
               textView.append(" OnError " + e.message)
               textView.append(AppConstant.LINE_SEPARATOR)
               Log.e(TAG," onError" +e.message)
            }

            override fun onComplete() {
                textView.append(" OnComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.e(TAG," OnComplete")
            }

        }
    }

    fun doSomeWorkMap(view: View) {
        //Call the observables here to test
        getMapObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { apiUsers-> return@map Utils.convertApiUserListToUserList(apiUsers) }
            .map { it.filter { user -> user.isFollowing  } }
            .subscribe(getMapObserver())
    }






    /*
     Observable With Zip Operator
     */

    private fun getCricketFansObservable():Observable<List<User>>{
        return Observable.create(ObservableOnSubscribe<List<User>>{ emitter ->
            if(!emitter.isDisposed){
                emitter.onNext(Utils.getUserWhoLovesCricket())
                emitter.onComplete()
            }

        }).subscribeOn(Schedulers.io())
    }

    private fun getFootballFansObservable():Observable<List<User>>{
        return Observable.create(ObservableOnSubscribe<List<User>>{ emitter->
           if(!emitter.isDisposed){
               emitter.onNext(Utils.getUserWhoLovesFootball())
               emitter.onComplete()
           }
        })
    }

    private fun getZipObserver():Observer<List<User>>{
        return object:Observer<List<User>>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(userList: List<User>) {
                textView.append(" onNext")
                textView.append(AppConstant.LINE_SEPARATOR)
                for (user in userList) {
                    textView.append(" firstname : ${user.firstName}")
                    textView.append(AppConstant.LINE_SEPARATOR)
                }
                Log.d(TAG, " onNext : " + userList.size)
            }

            override fun onError(e: Throwable) {
                textView.append(" onError : " + e.message)
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onError : " + e.message)
            }

            override fun onComplete() {
                textView.append(" onComplete")
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onComplete")
            }

        }
    }

    fun doSomeWorkZip(view: View) {
        //Call the observables here to test
        Observable.zip(getFootballFansObservable(),getCricketFansObservable(),
            BiFunction<List<User>,List<User> ,List<User>>{
                footballFans, cricketFans ->  return@BiFunction Utils.filterUserWhoLovesBoth(cricketFans,footballFans)
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getZipObserver())

    }


    /*
    Observable Merge and Concat Operator Example
    Merge may not maintain the order
    Concat maintains the order
     */

    fun doSomeWorkConcat(view: View){
        val observableA = Observable.fromArray("A1","A8","A2","A3","A4","A5","2")
        val observableB = Observable.fromArray("B1","B2,","B3","B4","B5")

       // Observable.merge(observableA,observableB)
        Observable.concat(observableA,observableB)
            .subscribeOn(Schedulers.io())
           // .delay(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getMergerObserver())
    }

    private fun getMergerObserver():Observer<String>{
        return object:Observer<String>{
            override fun onSubscribe(d: Disposable) {

            }
            override fun onNext(value: String) {
                textView.append(" onNext : value : $value")
                textView.append(AppConstant.LINE_SEPARATOR)
                Log.d(TAG, " onNext : value : $value")
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
            }

        }
    }


    /*
    Observable fromCallable
        Exception will occur if we use Observable.Just as function will execute first
        If we use fromCallable then it will execute whenever observer is attached so observer will handle the error
     */

    fun doSomeWork(view: View){
            Observable.fromCallable<Int>{return@fromCallable getNumber()}.subscribe(object:Observer<Int>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(value: Int) {
                    textView.append(" onNext : value : $value")
                    textView.append(AppConstant.LINE_SEPARATOR)
                    Log.d(TAG, " onNext : value : $value")
                }

                override fun onError(e: Throwable) {
                    textView.append(" onError  : ${e.message + e.localizedMessage}")
                }

                override fun onComplete() {

                }

            })

    }

    fun getNumber() = 1/0


}