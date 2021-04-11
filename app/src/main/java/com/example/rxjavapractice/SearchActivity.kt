package com.example.rxjavapractice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import com.example.rxjavapractice.utils.getQueryTextChangedObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {
   lateinit var searchView:SearchView
   lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
         searchView = findViewById<SearchView>(R.id.searchView)
         textView = findViewById<TextView>(R.id.textViewResult)
        setUpSearchObservable()

    }

    @SuppressLint("CheckResult")
    private fun setUpSearchObservable() {
        searchView.getQueryTextChangedObservable()
            .debounce(5, TimeUnit.SECONDS)
            .filter { text ->
                if (text.isEmpty()) {
                    textView.text = ""
                    return@filter false
                } else {
                    return@filter true
                }
            }.distinctUntilChanged()
            .flatMap { query ->
                dataFromNetwork(query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result -> textView.text = result + textView.text
            }
    }
    private fun dataFromNetwork(query: String): Observable<String> {
        return Observable.just(true)
            .delay(2,TimeUnit.SECONDS)
            .map {
                query
            }


    }
}