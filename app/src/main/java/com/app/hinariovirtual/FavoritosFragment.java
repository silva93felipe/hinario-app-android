package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.hinariovirtual.adapter.HinoAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Hino;

import java.util.List;

public class FavoritosFragment extends Fragment {

    private ListView listViewFavoritos;
    private TextView textFavoritosVazio;
    private HinoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Hino> listaFavoritos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        listViewFavoritos = view.findViewById(R.id.listViewFavoritos);
        textFavoritosVazio = view.findViewById(R.id.textFavoritosVazio);
        dbHelper = new DatabaseHelper(requireContext());

        configurarListeners();
        carregarFavoritos();

        return view;
    }

    private void configurarListeners() {
        listViewFavoritos.setOnItemClickListener((parent, view, position, id) -> {
            Hino hinoSelecionado = listaFavoritos.get(position);
            Intent intent = new Intent(requireContext(), DetalhesActivity.class);
            intent.putExtra("cadastro_id", hinoSelecionado.getId());
            startActivity(intent);
        });
    }

    private void carregarFavoritos() {
        listaFavoritos = dbHelper.listarFavoritos();
        adapter = new HinoAdapter(requireContext(), listaFavoritos);
        adapter.setOnFavoritoClickListener((hino, position) -> {
            dbHelper.toggleFavorito(hino.getId());
            carregarFavoritos();
        });
        listViewFavoritos.setAdapter(adapter);
        atualizarVisibilidade();
    }

    private void atualizarVisibilidade() {
        if (listaFavoritos.isEmpty()) {
            textFavoritosVazio.setVisibility(View.VISIBLE);
            listViewFavoritos.setVisibility(View.GONE);
        } else {
            textFavoritosVazio.setVisibility(View.GONE);
            listViewFavoritos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarFavoritos();
    }
}
