package com.example.appchat.view.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.appchat.R;
import com.example.appchat.adapters.PostAdapter;
import com.example.appchat.databinding.FragmentFiltrosBinding;
import com.example.appchat.viewmodel.PostViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class FiltrosFragment extends Fragment {
    private FragmentFiltrosBinding binding;
    private PostViewModel postViewModel;
    private ArrayAdapter<String> spinnerAdapter;

    public FiltrosFragment() {
    }

    public static FiltrosFragment newInstance() {
        return new FiltrosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFiltrosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);
        List<String> categorias = new ArrayList<>();
        categorias.add("Naturaleza");
        categorias.add("Montaña");
        categorias.add("Aventura");
        categorias.add("Cultura");
        categorias.add("Playa");
        categorias.add("Religión");
        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategorias.setAdapter(spinnerAdapter);

        // Manejar la selección de una categoría
        binding.spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                cargarPostsPorCategoria(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void cargarPostsPorCategoria(String categoria) {
        postViewModel.getPostsByCategory(categoria).observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                PostAdapter adapter = new PostAdapter(posts);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("FiltrosFragment", "No hay posts en la categoría " + categoria);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
