package com.hongdthaui.babysleep.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdthaui.babysleep.databinding.FragmentSouthBinding;
import com.hongdthaui.babysleep.model.SongOnline;
import com.hongdthaui.babysleep.view.activity.MainActivity;
import com.hongdthaui.babysleep.R;
import com.hongdthaui.babysleep.model.Song;
import com.hongdthaui.babysleep.utils.ItemClickSupport;
import com.hongdthaui.babysleep.view.adapter.SongAdapter;
import com.hongdthaui.babysleep.viewmodel.SouthViewModel;

import java.util.List;

public class SouthFragment extends Fragment {
    private SouthViewModel viewModel;
    FragmentSouthBinding binding;
    public SouthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SouthViewModel.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_south,container,false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        final SongAdapter songAdapter = new SongAdapter();

        binding.fragmentSouthRv.setAdapter(songAdapter);
        binding.fragmentSouthRv.setLayoutManager(linearLayoutManager);

        viewModel.getSouthList().observe(getViewLifecycleOwner(), new Observer<List<SongOnline>>() {
            @Override
            public void onChanged(List<SongOnline> songs) {
                songAdapter.setSongList(songs);
                songAdapter.notifyDataSetChanged();
            }
        });
        ItemClickSupport.addTo(binding.fragmentSouthRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                activity.onPlay(position,songAdapter.getSongList());
            }
        });
    }
}
