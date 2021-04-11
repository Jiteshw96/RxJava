package com.example.rxjavapractice.utils

import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


fun SearchView.getQueryTextChangedObservable(): Observable<String> {

    val subject = PublishSubject.create<String>()

    setOnQueryTextListener(object:SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String): Boolean {
           subject.onComplete()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            subject.onNext(newText)
            return true
        }

    })
    
    return subject

}