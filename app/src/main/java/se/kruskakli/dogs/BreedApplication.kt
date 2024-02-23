package se.kruskakli.dogs

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BreedApplication : Application() {

    init {
        instance = this
    }

    /*
        You can access the application context from any part of your application
        without needing an activity or service by extending the Application class
        and providing a static method or property to access the context.
     */
    companion object {
        private var instance: Application? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

}
