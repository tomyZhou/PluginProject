package com.plugin.stander;

import android.app.Activity;
import android.os.Bundle;

public interface ActivityInterface {
    void insertAppContext(Activity appActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onStop();

    void onDestory();
}
