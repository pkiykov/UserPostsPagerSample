package com.pkiykov.userpostspagersample.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pkiykov.userpostspagersample.utils.Injector;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusFragment;

public class BaseFragment<P extends Presenter> extends NucleusFragment<P> {

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle bundle) {
        final PresenterFactory<P> superFactory = super.getPresenterFactory();
        setPresenterFactory(superFactory == null ? null : (PresenterFactory<P>) () -> {
            P presenter = superFactory.createPresenter();
            ((Injector) BaseFragment.this.getActivity().getApplication()).inject(presenter);
            return presenter;
        });
        super.onCreate(bundle);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
