package com.practice.newscollector.ui.arch;

import com.practice.newscollector.model.logger.Logger;

import androidx.annotation.CallSuper;
import io.reactivex.disposables.CompositeDisposable;

/**
 * minimal implementation of presenter
 *
 * @param <V> - interface of activity/fragment/service/task
 */

public abstract class MvpPresenter<V extends Contract.View> implements Contract.Presenter<V> {
    protected final CompositeDisposable onStopDisposable = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposable = new CompositeDisposable();

    /**
     * link to activity/fragment/service/task
     */
    protected V view;

    @Override
    @CallSuper
    public final void subscribe(V view) {
        this.view = view;
    }

    @Override
    @CallSuper
    public void unsubscribe() {
        onStopDisposable.clear();
        view = null;
    }

    protected boolean hasView() {
        return view != null;
    }

    @Override
    @CallSuper
    public void destroy() {
        onDestroyDisposable.clear();
    }

}
