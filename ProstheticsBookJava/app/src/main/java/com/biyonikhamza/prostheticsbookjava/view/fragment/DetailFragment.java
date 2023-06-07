package com.biyonikhamza.prostheticsbookjava.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biyonikhamza.prostheticsbookjava.R;
import com.biyonikhamza.prostheticsbookjava.databinding.FragmentDetailBinding;
import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDao;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;

    CompositeDisposable disposable = new CompositeDisposable();

    ProstheticDao prostheticDao;
    ProstheticDatabase prostheticDB;

    Prosthetic pModel;



    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        prostheticDB = Room.databaseBuilder(requireActivity() , ProstheticDatabase.class ,"Prosthetics").build();
        prostheticDao = prostheticDB.prostheticDao();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBtn(v);
            }
        });

        int imageId = DetailFragmentArgs.fromBundle(getArguments()).getImageId();

        disposable.add(prostheticDao.id(imageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DetailFragment.this::handleResponseId)
        );
    }

    public void handleResponseId(Prosthetic prosthetic){
        pModel = prosthetic;
        binding.prostheticNameDetail.setText(prosthetic.pName);
        binding.prostheticTrademarkRow.setText(prosthetic.tradeMark);
        binding.prostheticAboutDetail.setText(prosthetic.about);

        Bitmap bitmap = BitmapFactory.decodeByteArray(prosthetic.image , 0 ,prosthetic.image.length);
        binding.imageViewDetail.setImageBitmap(bitmap);

    }

    public void deleteBtn(View view){
        disposable.add(prostheticDao.delete(pModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DetailFragment.this::handleResponse)
        );
    }
    public void handleResponse(){
        NavDirections action = DetailFragmentDirections.actionDetailFragmentToListFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }


}