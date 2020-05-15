package com.etl.jiangxiappraisal.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue.IdleHandler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 键盘工具
 * Created by jing on 2016/8/19.
 */
object TKeyboard {
    /**
     * 打开键盘
     * 废弃，使用 [.openKeybord]
     *
     * @param mEditText
     * @param mContext
     */
    @Deprecated("")
    fun openKeybord(mEditText: EditText, mContext: Context?) {
        openKeybord(mEditText)
    }

    /**
     * 打开键盘
     *
     * @param mEditText
     */
    fun openKeybord(mEditText: EditText) {
//        InputMethodManager imm = (InputMethodManager) MainApplication.getAppContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
        val imm = mEditText.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 关闭软键盘
     * 废弃，使用 [.closeKeybord]
     *
     * @param mEditText
     * @param mContext
     */
    @Deprecated(
        "", ReplaceWith(
            "closeKeybord(mEditText)",
            "com.etl.jiangxiappraisal.utils.TKeyboard.closeKeybord"
        )
    )
    fun closeKeybord(mEditText: EditText, mContext: Context?) {
        closeKeybord(mEditText)
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     */
    fun closeKeybord(mEditText: EditText) {
        val imm = mEditText.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0) //强制隐藏键盘
    }

    /**
     * 销毁键盘时候调用
     *
     * @param destContext
     * @param activity
     */
    //    public static void fixInputMethodManagerLeak(Context destContext) {
    //        if (destContext == null) {
    //            return;
    //        }
    //
    //        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    //        if (imm == null) {
    //            return;
    //        }
    //
    //        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
    //        Field f = null;
    //        Object obj_get = null;
    //        for (int i = 0;i < arr.length;i ++) {
    //            String param = arr[i];
    //            try{
    //                f = imm.getClass().getDeclaredField(param);
    //                if (f.isAccessible() == false) {
    //                    f.setAccessible(true);
    //                } // author: sodino mail:sodino@qq.com
    //                obj_get = f.get(imm);
    //                if (obj_get != null && obj_get instanceof View) {
    //                    View v_get = (View) obj_get;
    //                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
    //                        f.set(imm, null); // 置空，破坏掉path to gc节点
    //                    } else {
    //                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
    //                        break;
    //                    }
    //                }
    //            }catch(Throwable t){
    //                t.printStackTrace();
    //            }
    //        }
    //    }
    fun hideKeyBoard(activity: FragmentActivity?) {
        //拿到InputMethodManager
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //如果window上view获取焦点 && view不为空
        if (imm.isActive && activity.currentFocus != null) {
            //拿到view的token 不为空
            if (activity.currentFocus!!.windowToken != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    /**
     * 在使用完调用
     * Fix for https://code.google.com/p/android/issues/detail?id=171190 .
     *
     *
     * When a view that has focus gets detached, we wait for the main thread to be idle and then
     * check if the InputMethodManager is leaking a view. If yes, we tell it that the decor view got
     * focus, which is what happens if you press home and come back from recent apps. This replaces
     * the reference to the detached view with a reference to the decor view.
     *
     *
     * Should be called from [Activity.onCreate] )}.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun fixFocusedViewLeak(application: Application) {

        // Don't know about other versions yet.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 || Build.VERSION.SDK_INT > 23) {
            return
        }
        val inputMethodManager =
            application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val mServedViewField: Field
        val mHField: Field
        val finishInputLockedMethod: Method
        val focusInMethod: Method
        try {
            mServedViewField =
                InputMethodManager::class.java.getDeclaredField("mServedView")
            mServedViewField.isAccessible = true
            mHField =
                InputMethodManager::class.java.getDeclaredField("mServedView")
            mHField.isAccessible = true
            finishInputLockedMethod =
                InputMethodManager::class.java.getDeclaredMethod("finishInputLocked")
            finishInputLockedMethod.isAccessible = true
            focusInMethod =
                InputMethodManager::class.java.getDeclaredMethod(
                    "focusIn",
                    View::class.java
                )
            focusInMethod.isAccessible = true
        } catch (unexpected: NoSuchMethodException) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
            return
        } catch (unexpected: NoSuchFieldException) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
            return
        }
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(
                activity: Activity,
                bundle: Bundle
            ) {
            }

            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle
            ) {
                val cleaner = ReferenceCleaner(
                    inputMethodManager, mHField, mServedViewField,
                    finishInputLockedMethod
                )
                val rootView = activity.window.decorView.rootView
                val viewTreeObserver = rootView.viewTreeObserver
                viewTreeObserver.addOnGlobalFocusChangeListener(cleaner)
            }
        })
    }

    internal class ReferenceCleaner(
        private val inputMethodManager: InputMethodManager,
        private val mHField: Field,
        private val mServedViewField: Field,
        private val finishInputLockedMethod: Method
    ) : IdleHandler, View.OnAttachStateChangeListener, OnGlobalFocusChangeListener {
        override fun onGlobalFocusChanged(
            oldFocus: View,
            newFocus: View
        ) {
            oldFocus?.removeOnAttachStateChangeListener(this)
            Looper.myQueue().removeIdleHandler(this)
            newFocus.addOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(v: View) {}
        override fun onViewDetachedFromWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)
            Looper.myQueue().removeIdleHandler(this)
            Looper.myQueue().addIdleHandler(this)
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        override fun queueIdle(): Boolean {
            clearInputMethodManagerLeak()
            return false
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private fun clearInputMethodManagerLeak() {
            try {
                val lock = mHField[inputMethodManager]
                // This is highly dependent on the InputMethodManager implementation.
                synchronized(lock) {
                    val servedView =
                        mServedViewField[inputMethodManager] as View
                    val servedViewAttached =
                        servedView.windowVisibility != View.GONE
                    if (servedViewAttached) {
                        // The view held by the IMM was replaced without a global focus change. Let's make
                        // sure we get notified when that view detaches.

                        // Avoid double registration.
                        servedView.removeOnAttachStateChangeListener(this)
                        servedView.addOnAttachStateChangeListener(this)
                    } else {
                        // servedView is not attached. InputMethodManager is being stupid!
                        val activity = extractActivity(servedView.context)
                        if (activity == null || activity.window == null) {
                            // Unlikely case. Let's finish the input anyways.
                            finishInputLockedMethod.invoke(inputMethodManager)
                        } else {
                            val decorView =
                                activity.window.peekDecorView()
                            val windowAttached =
                                decorView.windowVisibility != View.GONE
                            if (!windowAttached) {
                                finishInputLockedMethod.invoke(inputMethodManager)
                            } else {
                                decorView.requestFocusFromTouch()
                            }
                        }
                    }
                }
            } catch (unexpected: IllegalAccessException) {
                Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
            } catch (unexpected: InvocationTargetException) {
                Log.e("IMMLeaks", "Unexpected reflection exception", unexpected)
            }
        }

        private fun extractActivity(context: Context): Activity? {
            var context = context
            while (true) {
                context = when (context) {
                    is Application -> {
                        return null
                    }
                    is Activity -> {
                        return context
                    }
                    is ContextWrapper -> {
                        val baseContext =
                            context.baseContext
                        // Prevent Stack Overflow.
                        if (baseContext === context) {
                            return null
                        }
                        baseContext
                    }
                    else -> {
                        return null
                    }
                }
            }
        }

    }
}