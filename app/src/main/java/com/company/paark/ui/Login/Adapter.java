package com.company.paark.ui.Login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.company.paark.ui.Login.Fragments.LoginFragment;
import com.company.paark.ui.Login.Fragments.SignupFragment;

public class Adapter extends FragmentStateAdapter {
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LoginFragment();
            case 1:
                return new SignupFragment();
            default:
                return new LoginFragment();
        }


    }

    public Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
