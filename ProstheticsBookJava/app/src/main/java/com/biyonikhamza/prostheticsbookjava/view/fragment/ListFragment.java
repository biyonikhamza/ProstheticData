package com.biyonikhamza.prostheticsbookjava.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biyonikhamza.prostheticsbookjava.R;
import com.biyonikhamza.prostheticsbookjava.adapter.ProstheticListAdapter;
import com.biyonikhamza.prostheticsbookjava.databinding.FragmentListBinding;
import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDao;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListFragment extends Fragment {

    ProstheticDatabase prostheticDB;
    ProstheticDao prostheticDao;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private FragmentListBinding binding;



    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        prostheticDB = Room.databaseBuilder(requireContext() ,ProstheticDatabase.class ,"Prosthetics").build();
        prostheticDao = prostheticDB.prostheticDao();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       incomingData();

        super.onViewCreated(view, savedInstanceState);
    }
    public void incomingData(){
        compositeDisposable.add(prostheticDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ListFragment.this::handleResponse)
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    public void handleResponse(List<Prosthetic> prostheticList){
        binding.prostheticRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        ProstheticListAdapter adapter = new ProstheticListAdapter(prostheticList);
        binding.prostheticRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        compositeDisposable.clear();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu , @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_prosthetic){
            NavDirections action = ListFragmentDirections.actionListFragmentToProstheticAddFragment();
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }
}