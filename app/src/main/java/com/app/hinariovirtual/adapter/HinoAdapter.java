package com.app.hinariovirtual.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hinariovirtual.R;
import com.app.hinariovirtual.model.Hino;

import java.util.List;

/**
 * Adapter customizado para exibir os hinos na lista
 */
public class HinoAdapter extends ArrayAdapter<Hino> {

    private Context context;
    private List<Hino> cadastros;
    private OnFavoritoClickListener favoritoListener;

    public interface OnFavoritoClickListener {
        void onFavoritoClick(Hino hino, int position);
    }

    public HinoAdapter(Context context, List<Hino> cadastros) {
        super(context, R.layout.item_cadastro, cadastros);
        this.context = context;
        this.cadastros = cadastros;
    }

    public void setOnFavoritoClickListener(OnFavoritoClickListener listener) {
        this.favoritoListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_cadastro, parent, false);

            holder = new ViewHolder();
            holder.textCodigo = convertView.findViewById(R.id.textCodigo);
            holder.textTitulo = convertView.findViewById(R.id.textTitulo);
            holder.iconFavorito = convertView.findViewById(R.id.iconFavorito);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Hino cadastro = cadastros.get(position);
        holder.textCodigo.setText(cadastro.getCodigo());
        holder.textTitulo.setText(cadastro.getTitulo());

        // Atualiza ícone de favorito
        if (cadastro.isFavorito()) {
            holder.iconFavorito.setImageResource(R.drawable.ic_favorite);
            holder.iconFavorito.setColorFilter(context.getResources().getColor(R.color.favorite_active));
        } else {
            holder.iconFavorito.setImageResource(R.drawable.ic_favorite_border);
            holder.iconFavorito.setColorFilter(context.getResources().getColor(R.color.white_soft));
        }

        // Listener de favorito
        holder.iconFavorito.setOnClickListener(v -> {
            if (favoritoListener != null) {
                favoritoListener.onFavoritoClick(cadastro, position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textCodigo;
        TextView textTitulo;
        ImageView iconFavorito;
    }

    public void atualizarLista(List<Hino> novaLista) {
        this.cadastros.clear();
        this.cadastros.addAll(novaLista);
        notifyDataSetChanged();
    }
}