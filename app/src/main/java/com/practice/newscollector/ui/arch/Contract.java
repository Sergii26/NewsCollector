package com.practice.newscollector.ui.arch;

public class Contract {
    public interface Presenter<V extends View> {
        /**
         * call in onStart()
         *
         * @param view - activity/fragment/view
         */
        void subscribe(V view);

        /**
         * call in onStop()
         */
        void unsubscribe();

        void destroy();
    }

    public interface View {
        String getString(int strResId, Object... args);

        void showToast(int strResId);

        void showToast(String message);

    }

    public interface Host {

    }

}
