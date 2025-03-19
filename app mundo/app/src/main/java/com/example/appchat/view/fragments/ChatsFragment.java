package com.example.appchat.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appchat.R;
import com.example.appchat.adapters.PostAdapter;
import com.example.appchat.databinding.FragmentTopsBinding;
import com.example.appchat.view.HomeActivity;
import com.example.appchat.view.MainActivity;
import com.example.appchat.view.PostActivity;
import com.example.appchat.viewmodel.AuthViewModel;
import com.example.appchat.viewmodel.PostViewModel;

import java.util.ArrayList;

public class TopsFragment extends Fragment {
    private FragmentTopsBinding binding;
    private PostViewModel postViewModel;
    private AuthViewModel authViewModel; //  autenticación
    private PostAdapter postAdapter;

    public TopsFragment() {
    }

    public static TopsFragment newInstance() {
        return new TopsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //barra de herramientas
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);


        // RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postViewModel.getTopPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                // Log.d("HomeFragment", "Número de posts: " + posts.size());
                PostAdapter adapter = new PostAdapter(posts);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ((HomeActivity) requireActivity()).hideProgressBar();
            } else {
                Log.d("HomeFragment", "No hay posts disponibles.");
                ((HomeActivity) requireActivity()).hideProgressBar();
            }
        });


        setupMenu();
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    onLogout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void onLogout() {
        authViewModel.logout().observe(getViewLifecycleOwner(), logoutResult -> {
            if (logoutResult != null && logoutResult) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {

                Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
