package com.example.khronos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.khronos.databinding.FragmentHomeBinding; // Asegúrate de que la ruta sea correcta

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño utilizando View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Configurar el contenido del fragmento
        setupContent();


        binding.getRoot().setBackgroundColor(getResources().getColor(R.color.white));

        return binding.getRoot();
    }

    private void setupContent() {
        // Título de bienvenida
        binding.textViewTitle.setText("Bienvenidos a Khronos");

        // Descripción del proyecto
        binding.textViewDescription.setText("Khronos es una aplicación de gestión de horarios diseñada para facilitar...");

        // Lista de características
        binding.listFeatures.setText("Registro de Asistencia\nGestión de Ausencias\nAprobación de Registros\nExportación a PDF");

        // Lista de tecnologías
        binding.listTechnologies.setText("Frontend: React.js\nBackend: Firebase");

        // Objetivo del proyecto
        binding.textViewObjective.setText("Optimizar la gestión del tiempo laboral...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evitar memory leaks
    }
}
