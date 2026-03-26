package com.app.hinariovirtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.hinariovirtual.adapter.EventoAdapter;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EventosFragment extends Fragment {

    private ListView listViewEventos;
    private TextView textEventosVazio;
    private FloatingActionButton fabAdicionarEvento;
    private EventoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Evento> listaEventos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos, container, false);

        listViewEventos = view.findViewById(R.id.listViewEventos);
        textEventosVazio = view.findViewById(R.id.textEventosVazio);
        fabAdicionarEvento = view.findViewById(R.id.fabAdicionarEvento);
        dbHelper = new DatabaseHelper(requireContext());

        configurarListeners();
        carregarEventos();

        return view;
    }

    private void configurarListeners() {
        fabAdicionarEvento.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FormularioEventoActivity.class);
            startActivity(intent);
        });
    }

    private void carregarEventos() {
        listaEventos = dbHelper.listarEventos();
        adapter = new EventoAdapter(requireContext(), listaEventos);
        adapter.setOnEventoDeleteListener((evento, position) -> {
            // Confirmação antes de excluir
            new AlertDialog.Builder(requireContext())
                    .setTitle("Excluir Evento")
                    .setMessage("Deseja excluir o evento \"" + evento.getDescricao() + "\"?")
                    .setPositiveButton("Excluir", (dialog, which) -> {
                        dbHelper.removerEvento(evento.getId());
                        Toast.makeText(requireContext(), R.string.evento_excluido, Toast.LENGTH_SHORT).show();
                        carregarEventos();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        listViewEventos.setAdapter(adapter);
        atualizarVisibilidade();
    }

    private void atualizarVisibilidade() {
        if (listaEventos.isEmpty()) {
            textEventosVazio.setVisibility(View.VISIBLE);
            listViewEventos.setVisibility(View.GONE);
        } else {
            textEventosVazio.setVisibility(View.GONE);
            listViewEventos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarEventos();
    }
}
