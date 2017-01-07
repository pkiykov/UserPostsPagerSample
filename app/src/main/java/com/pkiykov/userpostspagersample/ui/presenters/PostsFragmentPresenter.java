package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Environment;

import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;

import java.io.File;
import java.io.IOException;

public class PostsFragmentPresenter extends BasePresenter<PostsFragment> {

    public void saveLogs() {
        new Thread(() -> {
            try {
                final File path = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                Runtime.getRuntime().exec(
                        "logcat  -d -f " + path + File.separator
                                + "dbo_logcat"
                                + ".txt");
            } catch (IOException ignored) {
            }
        }).start();
    }
}
