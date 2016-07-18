package ru.katkalov.android.yamobdev2016;

import android.app.Fragment;
import android.content.Context;
import com.squareup.leakcanary.RefWatcher;
import ru.katkalov.android.yamobdev2016.Application;
import android.util.Log;


public aspect LeakCanaryWatcher {
    pointcut getFragmentsLeak(): execution(* Fragment+.onDestroy());

    before(): getFragmentsLeak() {
        Fragment fragment = (Fragment) thisJoinPoint.getTarget();
        if (fragment.getActivity() != null){
            RefWatcher refWatcher = Application.getRefWatcher(fragment.getActivity());
            refWatcher.watch(fragment);
        }
    }
}
