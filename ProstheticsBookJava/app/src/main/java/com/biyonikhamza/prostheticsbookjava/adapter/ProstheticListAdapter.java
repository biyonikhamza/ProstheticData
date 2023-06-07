package com.biyonikhamza.prostheticsbookjava.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.biyonikhamza.prostheticsbookjava.databinding.ProstheticRowBinding;
import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;
import com.biyonikhamza.prostheticsbookjava.view.fragment.ListFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class ProstheticListAdapter extends RecyclerView.Adapter<ProstheticListAdapter.ProstheticListVH>{


    List<Prosthetic> prostheticList;


    public ProstheticListAdapter(List<Prosthetic> prostheticList) {
        this.prostheticList = prostheticList;
    }


    class ProstheticListVH extends RecyclerView.ViewHolder{

        private ProstheticRowBinding rowBinding;

        public ProstheticListVH(ProstheticRowBinding binding) {
            super(binding.getRoot());
            this.rowBinding = binding;
        }
    }

    @NonNull
    @Override
    public ProstheticListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProstheticRowBinding prostheticRowBinding = ProstheticRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent , false);
        return new ProstheticListVH(prostheticRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProstheticListVH holder, @SuppressLint("RecyclerView") int position) {
        holder.rowBinding.prostheticNameRow.setText(prostheticList.get(position).pName);
        holder.rowBinding.prostheticTrademarkRow.setText(prostheticList.get(position).tradeMark);

        Bitmap bitmap = BitmapFactory.decodeByteArray(prostheticList.get(position).image , 0 , prostheticList.get(position).image.length);
        holder.rowBinding.prostheticImageRow.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragmentDirections.ActionListFragmentToDetailFragment action = ListFragmentDirections.actionListFragmentToDetailFragment();
                action.setImageId(prostheticList.get(position).id);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }
    @Override
    public int getItemCount() {return prostheticList.size();}

}
