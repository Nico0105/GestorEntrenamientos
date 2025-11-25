package com.example.gestorentrenamientos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorentrenamientos.database.AppDatabase;
import com.example.gestorentrenamientos.database.DatabaseClient;
import com.example.gestorentrenamientos.database.User;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private ListView listUsers;
    private AppDatabase database;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listUsers = findViewById(R.id.listUsers);
        database = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();

        loadUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers(){
        new Thread(() -> {
            List<User> usuarios = database.userDao().getAllUsers();

            runOnUiThread(() -> {
                adapter = new UsersAdapter(AdminActivity.this, usuarios);
                listUsers.setAdapter(adapter);

                // Mantener apretado para eliminar al usuario
                listUsers.setOnItemLongClickListener((parent, view, position, id) -> {
                    User u = adapter.getItem(position);
                    confirmDelete(u);
                    return true;
                });

                // Click nommal para editar
                listUsers.setOnItemClickListener((parent, view, position, id) -> {
                    User u = adapter.getItem(position);
                    Intent i = new Intent(AdminActivity.this, EditUserActivity.class);
                    i.putExtra("userId", u.getId());
                    startActivity(i);
                });
            });
        }).start();
    }

    private void confirmDelete(User user){
        new AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que deseas eliminar a: " + user.getUsername() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteUser(User user){
        new Thread(() -> {
            database.userDao().deleteUser(user);

            runOnUiThread(() -> {
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                loadUsers();
            });
        }).start();
    }
}
