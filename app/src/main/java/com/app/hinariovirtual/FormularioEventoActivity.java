package com.app.hinariovirtual;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.hinariovirtual.adapter.HinoCheckboxAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Evento;
import com.app.hinariovirtual.model.Hino;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FormularioEventoActivity extends AppCompatActivity {

    private EditText editDescricaoEvento;
    private ListView listViewHinosSelecao;
    private TextView textSemHinos;
    private MaterialButton btnSalvar;
    private MaterialButton btnCancelar;

    private DatabaseHelper dbHelper;
    private HinoCheckboxAdapter hinoCheckboxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_evento);

        View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.novo_evento);

        inicializarComponentes();
        carregarHinos();
        configurarListeners();
    }

    private void inicializarComponentes() {
        editDescricaoEvento = findViewById(R.id.editDescricaoEvento);
        listViewHinosSelecao = findViewById(R.id.listViewHinosSelecao);
        textSemHinos = findViewById(R.id.textSemHinos);
        btnSalvar = findViewById(R.id.btnSalvarEvento);
        btnCancelar = findViewById(R.id.btnCancelarEvento);
        dbHelper = new DatabaseHelper(this);
    }

    private void carregarHinos() {
        List<Hino> todosHinos = dbHelper.listarTodos();

        if (todosHinos.isEmpty()) {
            textSemHinos.setVisibility(View.VISIBLE);
            listViewHinosSelecao.setVisibility(View.GONE);
        } else {
            textSemHinos.setVisibility(View.GONE);
            listViewHinosSelecao.setVisibility(View.VISIBLE);
            hinoCheckboxAdapter = new HinoCheckboxAdapter(this, todosHinos);
            listViewHinosSelecao.setAdapter(hinoCheckboxAdapter);
        }
    }

    private void configurarListeners() {
        btnSalvar.setOnClickListener(v -> salvarEvento());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void salvarEvento() {
        String descricao = editDescricaoEvento.getText().toString().trim();

        if (descricao.isEmpty()) {
            editDescricaoEvento.setError(getString(R.string.descricao_obrigatoria));
            editDescricaoEvento.requestFocus();
            return;
        }

        if (hinoCheckboxAdapter == null || hinoCheckboxAdapter.getHinosSelecionadosIds().isEmpty()) {
            Toast.makeText(this, R.string.selecione_pelo_menos_um, Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar evento
        Evento evento = new Evento(descricao);
        long eventoId = dbHelper.adicionarEvento(evento);

        // Associar hinos
        List<Integer> hinosSelecionados = hinoCheckboxAdapter.getHinosSelecionadosIds();
        for (int hinoId : hinosSelecionados) {
            dbHelper.associarHinoAoEvento((int) eventoId, hinoId);
        }

        Toast.makeText(this, R.string.evento_salvo, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
