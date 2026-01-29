package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * Activity para exibir os detalhes completos de um cadastro
 */
public class DetalhesActivity extends AppCompatActivity {
    private TextView textTexto;
    private DatabaseHelper dbHelper;
    private Hino cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Habilita botão de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inicializarComponentes();
        carregarCadastro();
    }

    private void inicializarComponentes() {
        textTexto = findViewById(R.id.textDetalheTexto);
        dbHelper = new DatabaseHelper(this);
    }

    private void carregarCadastro() {
        int cadastroId = getIntent().getIntExtra("cadastro_id", -1);

        if (cadastroId != -1) {
            cadastro = dbHelper.buscarPorId(cadastroId);

            if (cadastro != null) {
                textTexto.setText(cadastro.getTexto());
                setTitle(cadastro.getTitulo());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_editar) {
            // Abre tela de edição
            Intent intent = new Intent(DetalhesActivity.this, FormularioActivity.class);
            intent.putExtra("cadastro_id", cadastro.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os dados ao voltar da edição
        carregarCadastro();
    }
}