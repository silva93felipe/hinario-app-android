package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.hinariovirtual.adapter.HinoAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ListView listViewHinos;
    private TextView textListaVazia;
    private FloatingActionButton fabAdicionar;
    private HinoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Hino> listaHinos;
    private List<Hino> listaHinosFilter;
    private EditText editPesquisa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listViewHinos = view.findViewById(R.id.listViewHinos);
        textListaVazia = view.findViewById(R.id.textListaVazia);
        fabAdicionar = view.findViewById(R.id.fabAdicionar);
        editPesquisa = view.findViewById(R.id.editPesquisa);
        dbHelper = new DatabaseHelper(requireContext());

        configurarListeners();
        carregarHinos();
        pesquisar();

        return view;
    }

    private void limparCampoPesquisa(){
        editPesquisa.setText("");
    }

    private void aplicarFiltroNoAdapter(){
        adapter = new HinoAdapter(requireContext(), listaHinos);
        adapter.notifyDataSetChanged();
        listViewHinos.setAdapter(adapter);
    }

    private void pesquisar(){
        editPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Hino> hinoAux = new ArrayList<>();
                for (Hino hino : listaHinos){
                    if(hino.getTitulo().toLowerCase().contains((charSequence.toString().toLowerCase()))){
                        hinoAux.add(hino);
                    }
                }
                listaHinos = hinoAux;
                aplicarFiltroNoAdapter();
                if(listaHinos.isEmpty()){
                    listaHinos = listaHinosFilter;
                    for (Hino hino : listaHinos){
                        if(hino.getTitulo().toLowerCase().contains((charSequence.toString().toLowerCase()))){
                            hinoAux.add(hino);
                        }
                    }
                    listaHinos = hinoAux;
                    aplicarFiltroNoAdapter();
                }

                if(editPesquisa.getText().toString().isEmpty()){
                    listaHinos = listaHinosFilter;
                    aplicarFiltroNoAdapter();
                }
            }
        });
    }

    private void configurarListeners() {
        fabAdicionar.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FormularioActivity.class);
            startActivity(intent);
        });

        listViewHinos.setOnItemClickListener((parent, view, position, id) -> {
            Hino hinoSelecionado = listaHinos.get(position);
            Intent intent = new Intent(requireContext(), DetalhesActivity.class);
            intent.putExtra("cadastro_id", hinoSelecionado.getId());
            startActivity(intent);
            limparCampoPesquisa();
        });

        listViewHinos.setOnItemLongClickListener((parent, view, position, id) -> {
            Hino hinoSelecionado = listaHinos.get(position);
            Intent intent = new Intent(requireContext(), FormularioActivity.class);
            intent.putExtra("cadastro_id", hinoSelecionado.getId());
            startActivity(intent);
            limparCampoPesquisa();
            return true;
        });
    }

    private void carregarHinos() {
        listaHinos = dbHelper.listarTodos();
        listaHinosFilter = listaHinos;
        adapter = new HinoAdapter(requireContext(), listaHinos);
        adapter.setOnFavoritoClickListener((hino, position) -> {
            dbHelper.toggleFavorito(hino.getId());
            carregarHinos();
        });
        listViewHinos.setAdapter(adapter);
        atualizarVisibilidade();
    }

    private void atualizarVisibilidade() {
        textListaVazia.setVisibility(View.GONE);
        listViewHinos.setVisibility(View.VISIBLE);
        if (listaHinos.isEmpty()) {
            textListaVazia.setVisibility(View.VISIBLE);
            listViewHinos.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarHinos();
    }
}
