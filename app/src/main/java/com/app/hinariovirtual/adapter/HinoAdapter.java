package com.app.hinariovirtual.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.hinariovirtual.R;
import com.app.hinariovirtual.model.Hino;

import java.util.List;

/**
 * Adapter customizado para exibir os cadastros na lista
 */
public class HinoAdapter extends ArrayAdapter<Hino> {

    private Context context;
    private List<Hino> cadastros;

    public HinoAdapter(Context context, List<Hino> cadastros) {
        super(context, R.layout.item_cadastro, cadastros);
        this.context = context;
        this.cadastros = cadastros;
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Hino cadastro = cadastros.get(position);
        holder.textCodigo.setText(cadastro.getCodigo());
        holder.textTitulo.setText(cadastro.getTitulo());

        return convertView;
    }

    static class ViewHolder {
        TextView textCodigo;
        TextView textTitulo;
    }

    public void atualizarLista(List<Hino> novaLista) {
        this.cadastros.clear();
        this.cadastros.addAll(novaLista);
        notifyDataSetChanged();
    }
}