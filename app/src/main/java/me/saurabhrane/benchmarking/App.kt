package me.saurabhrane.benchmarking

import android.app.Activity
import android.app.Application
import android.os.*
import android.os.Process.getStartUptimeMillis
import android.util.Log
import androidx.annotation.RequiresApi
import me.saurabhrane.benchmarking.NextDrawListener.Companion.onNextDraw
import me.saurabhrane.benchmarking.WindowDelegateCallback.Companion.onDecorViewReady

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        var firstDraw = false
        val handler = Handler(Looper.getMainLooper())

        registerActivityLifecycleCallbacks(
                object : ActivityLifecycleCallbacks {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onActivityCreated(
                            activity: Activity,
                            savedInstanceState: Bundle?
                    ) {
                        if (firstDraw) return
                        val name = activity::class.java.simpleName
                        val window = activity.window
                        window.onDecorViewReady {
                            window.decorView.onNextDraw {
                                if (firstDraw) return@onNextDraw
                                firstDraw = true
                                handler.postAtFrontOfQueue {
                                    val start = Process.getStartUptimeMillis()
                                    val now = SystemClock.uptimeMillis()
                                    val startDurationMs = now.minus(start)
                                    Log.d(
                                            "AppStart",
                                            "Displayed $name in $startDurationMs ms"
                                    )
                                }
                            }
                        }
                    }

                    override fun onActivityStarted(activity: Activity) {

                    }

                    override fun onActivityResumed(activity: Activity) {

                    }

                    override fun onActivityPaused(activity: Activity) {

                    }

                    override fun onActivityStopped(activity: Activity) {

                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                    }

                    override fun onActivityDestroyed(activity: Activity) {

                    }
                })
    }
}