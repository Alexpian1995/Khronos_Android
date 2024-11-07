package com.example.khronos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class AsistenciaFragment extends Fragment {

    private FirebaseFirestore db;
    private TableLayout tableLayout;
    private ArrayList<Attendance> attendanceRecords;
    private String editingAttendanceId = null;
    private Spinner employeeSpinner, attendanceTypeSpinner; // Spinner para tipo de asistencia
    private ArrayList<Employee> employees; // Lista de empleados
    private ArrayAdapter<String> employeeAdapter;

    public AsistenciaFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_asistencia, container, false);

        tableLayout = rootView.findViewById(R.id.tableLayout);
        employeeSpinner = rootView.findViewById(R.id.employeeSpinner);
        attendanceTypeSpinner = rootView.findViewById(R.id.attendanceTypeSpinner); // Spinner para el tipo de asistencia

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this::onSaveClicked);

        Button cancelButton = rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this::onCancelClicked);

        // Inicializar y cargar los empleados en el Spinner
        employees = new ArrayList<>();
        employeeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSpinner.setAdapter(employeeAdapter);
        fetchEmployees();

        // Cargar registros de asistencia al inicio
        fetchAttendanceRecords();

        return rootView;
    }

    private void fetchEmployees() {
        db.collection("empleados")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        employees.clear();
                        employeeAdapter.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class);
                            employee.setId(document.getId());
                            employees.add(employee);
                            employeeAdapter.add(employee.getNombre());
                        }
                        employeeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error obteniendo empleados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchAttendanceRecords() {
        db.collection("asistencias")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        attendanceRecords = new ArrayList<>();
                        tableLayout.removeAllViews();
                        QuerySnapshot snapshot = task.getResult();

                        for (QueryDocumentSnapshot document : snapshot) {
                            Attendance attendance = document.toObject(Attendance.class);
                            attendance.setId(document.getId());
                            attendanceRecords.add(attendance);
                            addAttendanceRow(attendance);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error obteniendo asistencia", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addAttendanceRow(Attendance attendance) {
        TableRow row = new TableRow(getContext());

        // Obtener el nombre del empleado
        Employee employee = findEmployeeById(attendance.getEmployeeId());
        TextView employeeNameTextView = new TextView(getContext());
        employeeNameTextView.setText(employee != null ? employee.getNombre() : "Empleado desconocido");

        // Hora de entrada
        TextView entryTimeTextView = new TextView(getContext());
        entryTimeTextView.setText(attendance.getEntryTime() != null ? attendance.getEntryTime() : "No registrada");

        // Hora de salida
        TextView exitTimeTextView = new TextView(getContext());
        exitTimeTextView.setText(attendance.getExitTime() != null ? attendance.getExitTime() : "No registrada");

        // Hora extra
        TextView extraHoursTextView = new TextView(getContext());
        extraHoursTextView.setText(attendance.getExtraHours() != null ? attendance.getExtraHours() : "0 horas");

        // Ausencia
        TextView absenceTextView = new TextView(getContext());
        absenceTextView.setText(attendance.isAbsent() ? "Sí" : "No");

        // Tipo de asistencia (Hora Extra/Ausencia)
        TextView typeTextView = new TextView(getContext());
        typeTextView.setText(attendance.getAttendanceType());

        // Agregar las vistas a la fila
        row.addView(employeeNameTextView);
        row.addView(entryTimeTextView);
        row.addView(exitTimeTextView);
        row.addView(extraHoursTextView);
        row.addView(absenceTextView);
        row.addView(typeTextView);

        // Agregar la fila a la tabla
        tableLayout.addView(row);
    }

    private Employee findEmployeeById(String employeeId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(employeeId)) {
                return employee;
            }
        }
        return null;
    }

    private void saveEdit() {
        int selectedEmployeePosition = employeeSpinner.getSelectedItemPosition();
        int selectedTypePosition = attendanceTypeSpinner.getSelectedItemPosition();

        if (selectedEmployeePosition == -1 || selectedTypePosition == -1) {
            Toast.makeText(getContext(), "Todos los campos deben ser completos", Toast.LENGTH_SHORT).show();
            return;
        }

        String employeeId = employees.get(selectedEmployeePosition).getId();
        String attendanceType = (String) attendanceTypeSpinner.getSelectedItem();

        if (editingAttendanceId != null) {
            DocumentReference docRef = db.collection("asistencias").document(editingAttendanceId);
            docRef.update("employeeId", employeeId, "attendanceType", attendanceType)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Asistencia editada", Toast.LENGTH_SHORT).show();
                        fetchAttendanceRecords();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error editando asistencia", Toast.LENGTH_SHORT).show());
        }
    }

    private void onSaveClicked(View view) {
        saveEdit();
    }

    private void onCancelClicked(View view) {
        // Cancelar edición
        editingAttendanceId = null;
        employeeSpinner.setEnabled(true);
        attendanceTypeSpinner.setEnabled(true);
    }
}
