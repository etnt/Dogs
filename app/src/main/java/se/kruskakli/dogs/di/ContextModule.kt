package se.kruskakli.dogs.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
   This module tells Hilt how to provide a Context dependency.
   Whenever a Context is requested, Hilt will provide the application context,
   and the same instance of the context will be used every time it's injected.
 */

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    /*
       @Provides tells Hilt that this function is providing a dependency.
       @Singleton tells Hilt that the provided dependency should be scoped
       to the application, meaning that the same instance of the dependency
       will be used every time it's injected.

       The provideContext function takes an appContext parameter annotated
       with @ApplicationContext. This is a qualifier that tells Hilt to inject
       the application context, not any other Context.

       The provideContext function returns the appContext that it receives as
       a parameter. This means that whenever a Context is requested, this
       function will provide it.
     */
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}
