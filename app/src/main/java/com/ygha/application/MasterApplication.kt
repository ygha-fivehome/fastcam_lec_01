package com.ygha.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class MasterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.d("testt","onActivityCreated, " + activity.localClassName)
            }

            override fun onActivityStarted(activity: Activity) {
                Log.d("testt","onActivityStarted, " + activity.localClassName)
            }

            override fun onActivityResumed(activity: Activity) {
                Log.d("testt","onActivityResumed, " + activity.localClassName)
            }

            override fun onActivityPaused(activity: Activity) {
                Log.d("testt","onActivityPaused, " + activity.localClassName)
            }

            override fun onActivityStopped(activity: Activity) {
                Log.d("testt","onActivityStopped, " + activity.localClassName)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.d("testt","onActivitySaveInstanceState, " + activity.localClassName)
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.d("testt","onActivityDestroyed, " + activity.localClassName)
            }
        })

    }
}