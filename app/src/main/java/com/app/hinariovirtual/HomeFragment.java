package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.hinariovirtual.adapter.HinoAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private ListView listViewHinos;
    private TextView textListaVazia;
    private FloatingActionButton fabAdicionar;
    private HinoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Hino> listaHinos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listViewHinos = view.findViewById(R.id.listViewHinos);
        textListaVazia = view.findViewById(R.id.textListaVazia);
        fabAdicionar = view.findViewById(R.id.fabAdicionar);
        dbHelper = new DatabaseHelper(requireContext());

        configurarListeners();
        carregarHinos();

        return view;
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
        });

        listViewHinos.setOnItemLongClickListener((parent, view, position, id) -> {
            Hino hinoSelecionado = listaHinos.get(position);
            Intent intent = new Intent(requireContext(), FormularioActivity.class);
            intent.putExtra("cadastro_id", hinoSelecionado.getId());
            startActivity(intent);
            return true;
        });
    }

    private void carregarHinos() {
        listaHinos = dbHelper.listarTodos();
        adapter = new HinoAdapter(requireContext(), listaHinos);
        adapter.setOnFavoritoClickListener((hino, position) -> {
            dbHelper.toggleFavorito(hino.getId());
            carregarHinos();
        });
        listViewHinos.setAdapter(adapter);
        atualizarVisibilidade();
    }

    private void atualizarVisibilidade() {
        if (listaHinos.isEmpty()) {
            textListaVazia.setVisibility(View.VISIBLE);
            listViewHinos.setVisibility(View.GONE);
        } else {
            textListaVazia.setVisibility(View.GONE);
            listViewHinos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarHinos();
    }
}
