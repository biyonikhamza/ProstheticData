package com.biyonikhamza.prostheticsbookjava.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.biyonikhamza.prostheticsbookjava.R;
import com.biyonikhamza.prostheticsbookjava.databinding.ShowProstheticBinding;
import com.biyonikhamza.prostheticsbookjava.view.fragment.ListFragment;
import com.biyonikhamza.prostheticsbookjava.view.fragment.ListFragmentDirections;
import com.biyonikhamza.prostheticsbookjava.view.fragment.ProstheticAddFragment;


public class ProstheticShow extends AppCompatActivity {

    ShowProstheticBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShowProstheticBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }


}