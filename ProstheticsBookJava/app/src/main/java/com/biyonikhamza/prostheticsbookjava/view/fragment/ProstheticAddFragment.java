package com.biyonikhamza.prostheticsbookjava.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biyonikhamza.prostheticsbookjava.R;
import com.biyonikhamza.prostheticsbookjava.databinding.FragmentProstheticAddBinding;
import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDao;
import com.biyonikhamza.prostheticsbookjava.service.ProstheticDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProstheticAddFragment extends Fragment {

    ActivityResultLauncher<Intent> aResultLauncher;
    ActivityResultLauncher<String> aPermissionLauncher;
    Bitmap choosenBitmap;

    ProstheticDatabase prostheticDB;
    ProstheticDao prostheticDao;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private FragmentProstheticAddBinding binding;

    public ProstheticAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        registerLauncherFun();

        prostheticDB = Room.databaseBuilder(requireActivity() , ProstheticDatabase.class , "Prosthetics").build();
        prostheticDao = prostheticDB.prostheticDao();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProstheticAddBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGallery(v);
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnSaveBtn(v);
            }
        });

    }
    public void clickOnSaveBtn(View view){

                String pName = binding.eTextPnameAdd.getText().toString();
                String tradeMark = binding.eTextTrademarkAdd.getText().toString();
                String about = binding.eTextAboutAdd.getText().toString();

                if(pName.isEmpty()){
                    binding.eTextPnameAdd.setError("Protezin ismini girmen gerek.");
                }
                else if(tradeMark.isEmpty()){
                    binding.eTextTrademarkAdd.setError("Protezin markasını girmen gerek.");
                }
                else if(about.isEmpty()){
                    binding.eTextAboutAdd.setError("Düşüncelerini merak ediyoruz. :)");
                }
                else{
                    Bitmap image = makeSmallBitmap(choosenBitmap , 300);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG , 50 , outputStream);

                    byte[] byteArray = outputStream.toByteArray();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.length);
                    binding.imageViewAdd.setImageBitmap(bitmap);

                    Prosthetic prosthetic = new Prosthetic(pName ,tradeMark , about ,byteArray);

                    compositeDisposable.add( (Disposable) prostheticDao.insertAll(prosthetic)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(ProstheticAddFragment.this::handleResponse)
                    );
                }
    }

    public void handleResponse(){
        NavDirections action = ProstheticAddFragmentDirections.actionProstheticAddFragmentToListFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    public Bitmap makeSmallBitmap(Bitmap image , int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1){
            // LandScape -> Resim Yataydır.
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }
        else{
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(choosenBitmap , width , height , true);
    }

    public void clickOnGallery(View view){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    //Android 33+ -> READ_MEDIA_IMAGES

                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES)
                            != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                                Manifest.permission.READ_MEDIA_IMAGES )){
                            Snackbar.make(requireActivity() , view ,
                                    "Galeriye gitmemiz gerek !" ,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Peki Gidelim inşaAllah", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Eğer izin verilmediyse , izni tekrar istiyecez.
                                    aPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                                }
                            }).show();
                        }
                        else {
                            // Eğer izin verilmediyse , izin alıcaz.
                            aPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }
                    else{
                        // Galeriye gidicez.
                        Intent intentGallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        aResultLauncher.launch(intentGallery);
                    }
                }
                //Android 32- -> READ_EXTERNAL_STORAGE
                else{
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE )){
                            Snackbar.make(requireActivity() , view ,
                                            "Galeriye gitmemiz gerek !" ,
                                            Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Peki Gidelim inşaAllah", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Eğer izin verilmediyse , izni tekrar istiyecez.
                                            aPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                                        }
                                    }).show();
                        }
                        else {
                            // Eğer izin verilmediyse , izin alıcaz.
                            aPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }
                    else{
                        // Galeriye gidicez.
                        Intent intentGallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        aResultLauncher.launch(intentGallery);
                    }
                }
    }

    public void registerLauncherFun(){

        aResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intentForResult = result.getData();
                            if (intentForResult != null) {
                                Uri imageViewData = intentForResult.getData();
                                try {
                                    if (Build.VERSION.SDK_INT >= 28) {
                                        ImageDecoder.Source source = ImageDecoder
                                                .createSource(requireActivity().getContentResolver(), imageViewData);
                                        choosenBitmap = ImageDecoder.decodeBitmap(source);
                                        binding.imageViewAdd.setImageBitmap(choosenBitmap);
                                    } else {
                                        choosenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageViewData);
                                        binding.imageViewAdd.setImageBitmap(choosenBitmap);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
        aPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result){
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            aResultLauncher.launch(galleryIntent);
                        }
                        else{
                            // Permission verilmemiş ise
                            Toast.makeText(requireActivity() , "İzni Vermediniz" , Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        compositeDisposable.clear();
    }
}