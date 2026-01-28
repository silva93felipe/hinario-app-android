package com.app.hinariovirtual;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;

public class FormularioActivity extends AppCompatActivity {

    private EditText editCodigo;
    private EditText editTitulo;
    private EditText editTexto;
    private Button btnSalvar;
    private Button btnCancelar;
    private Button btnExcluir;

    private DatabaseHelper dbHelper;
    private Hino cadastroAtual;
    private boolean isEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        // Habilita botão de voltar na ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inicializarComponentes();
        verificarModoEdicao();
        configurarListeners();
    }

    private void inicializarComponentes() {
        editCodigo = findViewById(R.id.editCodigo);
        editTitulo = findViewById(R.id.editTitulo);
        editTexto = findViewById(R.id.editTexto);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnExcluir = findViewById(R.id.btnExcluir);

        dbHelper = new DatabaseHelper(this);
    }

    private void verificarModoEdicao() {
        int cadastroId = getIntent().getIntExtra("cadastro_id", -1);

        if (cadastroId != -1) {
            // Modo edição
            isEdicao = true;
            setTitle("Editar Cadastro");
            btnExcluir.setVisibility(View.VISIBLE);

            cadastroAtual = dbHelper.buscarPorId(cadastroId);

            if (cadastroAtual != null) {
                editCodigo.setText(cadastroAtual.getCodigo());
                editTitulo.setText(cadastroAtual.getTitulo());
                editTexto.setText(cadastroAtual.getTexto());
            }
        } else {
            // Modo criação
            isEdicao = false;
            setTitle("Novo Cadastro");
            btnExcluir.setVisibility(View.GONE);
        }
    }

    private void configurarListeners() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarCadastro();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirCadastro();
            }
        });
    }

    private void salvarCadastro() {
        String codigo = editCodigo.getText().toString().trim();
        String titulo = editTitulo.getText().toString().trim();
        String texto = editTexto.getText().toString().trim();

        // Validações
        if (codigo.isEmpty()) {
            editCodigo.setError("Campo obrigatório");
            editCodigo.requestFocus();
            return;
        }

        if (titulo.isEmpty()) {
            editTitulo.setError("Campo obrigatório");
            editTitulo.requestFocus();
            return;
        }

        if (isEdicao) {
            // Atualizar cadastro existente
            cadastroAtual.setCodigo(codigo);
            cadastroAtual.setTitulo(titulo);
            cadastroAtual.setTexto(texto);

            dbHelper.atualizar(cadastroAtual);
            Toast.makeText(this, "Cadastro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            // Criar novo cadastro
            Hino novoCadastro = new Hino(codigo, titulo, texto);
            dbHelper.adicionar(novoCadastro);
            Toast.makeText(this, "Cadastro criado com sucesso!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void excluirCadastro() {
        if (cadastroAtual != null) {
            dbHelper.remover(cadastroAtual.getId());
            Toast.makeText(this, "Cadastro excluído com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
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