package com.app.hinariovirtual.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hinariovirtual.DetalhesActivity;
import com.app.hinariovirtual.R;
import com.app.hinariovirtual.db.DatabaseHelper;
import com.app.hinariovirtual.model.Evento;
import com.app.hinariovirtual.model.Hino;

import java.util.List;

/**
 * Adapter para exibir eventos na lista com expansão para mostrar hinos
 */
public class EventoAdapter extends ArrayAdapter<Evento> {

    private Context context;
    private List<Evento> eventos;
    private DatabaseHelper dbHelper;
    private OnEventoDeleteListener deleteListener;

    public interface OnEventoDeleteListener {
        void onEventoDelete(Evento evento, int position);
    }

    public EventoAdapter(Context context, List<Evento> eventos) {
        super(context, R.layout.item_evento, eventos);
        this.context = context;
        this.eventos = eventos;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void setOnEventoDeleteListener(OnEventoDeleteListener listener) {
        this.deleteListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Não reutilizar convertView para evitar problemas com estado de expansão
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_evento, parent, false);

        Evento evento = eventos.get(position);

        TextView textDescricao = view.findViewById(R.id.textEventoDescricao);
        ImageView iconExpandir = view.findViewById(R.id.iconExpandir);
        ImageView btnExcluir = view.findViewById(R.id.btnExcluirEvento);
        LinearLayout layoutHinos = view.findViewById(R.id.layoutHinosEvento);
        LinearLayout layoutHeader = view.findViewById(R.id.layoutEventoHeader);

        textDescricao.setText(evento.getDescricao());

        // Toggle expansão
        layoutHeader.setOnClickListener(v -> {
            if (layoutHinos.getVisibility() == View.GONE) {
                // Expandir - carregar hinos do evento
                layoutHinos.setVisibility(View.VISIBLE);
                iconExpandir.setImageResource(R.drawable.ic_expand_less);

                // Remove hinos antigos (mantém o divider)
                if (layoutHinos.getChildCount() > 1) {
                    layoutHinos.removeViews(1, layoutHinos.getChildCount() - 1);
                }

                List<Hino> hinos = dbHelper.listarHinosDoEvento(evento.getId());

                if (hinos.isEmpty()) {
                    TextView textVazio = new TextView(context);
                    textVazio.setText("Nenhum hino associado");
                    textVazio.setTextColor(context.getResources().getColor(R.color.white_soft));
                    textVazio.setTextSize(14);
                    textVazio.setPadding(0, 8, 0, 8);
                    layoutHinos.addView(textVazio);
                } else {
                    for (Hino hino : hinos) {
                        View hinoView = inflater.inflate(R.layout.item_hino_evento, layoutHinos, false);
                        TextView textCodigo = hinoView.findViewById(R.id.textHinoCodigo);
                        TextView textTitulo = hinoView.findViewById(R.id.textHinoTitulo);

                        textCodigo.setText(hino.getCodigo());
                        textTitulo.setText(hino.getTitulo());

                        // Clique no hino abre detalhes
                        hinoView.setOnClickListener(hinoV -> {
                            Intent intent = new Intent(context, DetalhesActivity.class);
                            intent.putExtra("cadastro_id", hino.getId());
                            context.startActivity(intent);
                        });

                        layoutHinos.addView(hinoView);
                    }
                }
            } else {
                // Colapsar
                layoutHinos.setVisibility(View.GONE);
                iconExpandir.setImageResource(R.drawable.ic_expand_more);
            }
        });

        // Botão excluir
        btnExcluir.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onEventoDelete(evento, position);
            }
        });

        return view;
    }

    public void atualizarLista(List<Evento> novaLista) {
        this.eventos.clear();
        this.eventos.addAll(novaLista);
        notifyDataSetChanged();
    }
}
