package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.hinariovirtual.adapter.HinoAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewCadastros;
    private FloatingActionButton fabAdicionar;
    private HinoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Hino> listaCadastros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarListeners();
        carregarCadastros();
    }

    private void inicializarComponentes() {
        listViewCadastros = findViewById(R.id.listViewCadastros);
        fabAdicionar = findViewById(R.id.fabAdicionar);
        dbHelper = new DatabaseHelper(this);
    }

    private void configurarListeners() {
        // Clique no FAB para adicionar novo cadastro
        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        // Clique em um item da lista para ver detalhes
        listViewCadastros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hino cadastroSelecionado = listaCadastros.get(position);

                Intent intent = new Intent(MainActivity.this, DetalhesActivity.class);
                intent.putExtra("cadastro_id", cadastroSelecionado.getId());
                startActivity(intent);
            }
        });

        // Long click para editar
        listViewCadastros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Hino cadastroSelecionado = listaCadastros.get(position);

                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
                intent.putExtra("cadastro_id", cadastroSelecionado.getId());
                startActivity(intent);

                return true;
            }
        });
    }

    private void carregarCadastros() {
        listaCadastros = dbHelper.listarTodos();
        adapter = new HinoAdapter(this, listaCadastros);
        listViewCadastros.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a lista sempre que voltar para esta tela
        listaCadastros = dbHelper.listarTodos();
        if (adapter != null) {
            adapter.atualizarLista(listaCadastros);
        }
    }
}