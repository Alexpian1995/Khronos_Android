package com.example.khronos;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Abrir el menú al iniciar la aplicación
        drawerLayout.openDrawer(GravityCompat.START);

        // Establecer el fragmento inicial solo si no hay un estado guardado
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Establecer el fragmento inicial
        }

        // Configurar el BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment replaceFragment = null;

            if (item.getItemId() == R.id.nav_agregar) {
                replaceFragment = new AgregarFragment(); // Cambia esto al formulario de agregar empleado
            } else if (item.getItemId() == R.id.nav_empleados) {
                replaceFragment = new EmpleadosFragment();
            } else if (item.getItemId() == R.id.nav_asistencia) {
                replaceFragment = new AboutFragment(); // Cambia esto al fragmento correcto
            }

            return loadFragment(replaceFragment);
        });

        // Configurar el FloatingActionButton
        fab.setOnClickListener(v -> loadFragment(new AddItemFragment()));

        // Configurar el NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            drawerLayout.closeDrawers(); // Cerrar el Drawer

            Fragment replaceFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_agregar) {
                replaceFragment = new AgregarFragment(); // Cambia esto al formulario de agregar empleado
            } else if (item.getItemId() == R.id.nav_empleados) {
                replaceFragment = new EmpleadosFragment(); // Cambia esto al fragmento correcto
            } else if (item.getItemId() == R.id.nav_asistencia) {
                replaceFragment = new AsistenciaFragment();
            } else if (item.getItemId() == R.id.nav_logout) { // Asegúrate de tener el ID correcto para "Cerrar sesión"
                // Cerrar sesión en Firebase y redirigir al LoginActivity
                FirebaseAuth.getInstance().signOut(); // Cerrar sesión si usas Firebase Auth
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finalizar MainActivity
                return true;
            }

            return loadFragment(replaceFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit(); // No se añade a la pila
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Si el menú de navegación está abierto, ciérralo
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Si no hay fragmentos en la pila, abre el menú de navegación
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                drawerLayout.openDrawer(GravityCompat.START); // Abre el menú de navegación
            } else {
                // Si hay fragmentos en la pila, regresa al anterior
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
