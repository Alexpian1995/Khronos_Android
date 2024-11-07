package com.example.khronos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmpleadosFragment extends Fragment {

    private FirebaseFirestore db;
    private TableLayout tableLayout;
    private ArrayList<Employee> employees;
    private String editingEmployeeId = null;
    private EditText editName, editDocumento;
    private Button saveButton, cancelButton;

    public EmpleadosFragment() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_empleados, container, false);

        tableLayout = rootView.findViewById(R.id.tableLayout);
        editName = rootView.findViewById(R.id.editName);
        editDocumento = rootView.findViewById(R.id.editDocumento);
        saveButton = rootView.findViewById(R.id.saveButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);

        // Inicialmente, deshabilitar los campos y ocultar botones de edición
        clearEditFields();

        // Configurar el evento de los botones
        saveButton.setOnClickListener(this::onSaveClicked);
        cancelButton.setOnClickListener(this::onCancelClicked);

        // Cargar empleados al inicio
        fetchEmployees();

        return rootView;
    }

    private void fetchEmployees() {
        db.collection("empleados")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        employees = new ArrayList<>();
                        tableLayout.removeAllViews();
                        QuerySnapshot snapshot = task.getResult();

                        for (QueryDocumentSnapshot document : snapshot) {
                            Employee employee = document.toObject(Employee.class);
                            employee.setId(document.getId());
                            employees.add(employee);
                            addEmployeeRow(employee);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error obteniendo empleados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addEmployeeRow(Employee employee) {
        TableRow row = new TableRow(getContext());

        TextView nameTextView = new TextView(getContext());
        nameTextView.setText(employee.getNombre());

        TextView documentoTextView = new TextView(getContext());
        documentoTextView.setText(employee.getDocumento());

        Button editButton = new Button(getContext());
        editButton.setText("Editar");
        editButton.setOnClickListener(v -> editEmployee(employee));

        Button deleteButton = new Button(getContext());
        deleteButton.setText("Eliminar");
        deleteButton.setOnClickListener(v -> deleteEmployee(employee.getId()));

        row.addView(nameTextView);
        row.addView(documentoTextView);
        row.addView(editButton);
        row.addView(deleteButton);

        tableLayout.addView(row);
    }

    private void editEmployee(Employee employee) {
        editingEmployeeId = employee.getId();
        editName.setText(employee.getNombre());
        editDocumento.setText(employee.getDocumento());

        // Habilitar los campos de edición y mostrar botones
        editName.setEnabled(true);
        editDocumento.setEnabled(true);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private void saveEdit() {
        String name = editName.getText().toString().trim();
        String documento = editDocumento.getText().toString().trim();

        if (name.isEmpty() || documento.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingEmployeeId != null) {
            DocumentReference docRef = db.collection("empleados").document(editingEmployeeId);
            docRef.update("nombre", name, "documento", documento)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Empleado actualizado", Toast.LENGTH_SHORT).show();
                        fetchEmployees(); // Actualiza la lista de empleados
                        clearEditFields(); // Limpia los campos de edición
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error al actualizar el empleado", e);
                        Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void clearEditFields() {
        editingEmployeeId = null;
        editName.setText("");
        editDocumento.setText("");

        // Deshabilitar campos de edición y ocultar botones
        editName.setEnabled(false);
        editDocumento.setEnabled(false);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

    private void deleteEmployee(String employeeId) {
        db.collection("empleados").document(employeeId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Empleado eliminado", Toast.LENGTH_SHORT).show();
                    fetchEmployees();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error al eliminar el empleado", e);
                    Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }

    public void onSaveClicked(View view) {
        saveEdit();
    }

    public void onCancelClicked(View view) {
        clearEditFields();
    }

    // Clase interna para representar a un empleado
    public static class Employee {
        private String id;
        private String nombre;
        private String documento;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDocumento() {
            return documento;
        }

        public void setDocumento(String documento) {
            this.documento = documento;
        }
    }
}
