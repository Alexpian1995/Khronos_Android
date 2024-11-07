package com.example.khronos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AgregarFragment extends Fragment {

    private EditText etNombre;
    private EditText etDocumento;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Referencia los elementos de la interfaz
        etNombre = view.findViewById(R.id.etNombre);
        etDocumento = view.findViewById(R.id.etDocumento);
        Button btnAgregarEmpleado = view.findViewById(R.id.btnAgregarEmpleado);

        // Configura el botón de agregar empleado
        btnAgregarEmpleado.setOnClickListener(v -> agregarEmpleado());

        return view;
    }

    private void agregarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String documento = etDocumento.getText().toString().trim();

        Log.d("AgregarFragment", "Intentando agregar empleado: " + nombre + ", Documento: " + documento);

        if (!nombre.isEmpty() && !documento.isEmpty()) {
            Map<String, Object> empleado = new HashMap<>();
            empleado.put("nombre", nombre);
            empleado.put("documento", documento);

            db.collection("empleados")
                    .add(empleado)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Empleado agregado", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                        etDocumento.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AgregarFragment", "Error al agregar el empleado", e);
                        Toast.makeText(getContext(), "Error al agregar el empleado", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
