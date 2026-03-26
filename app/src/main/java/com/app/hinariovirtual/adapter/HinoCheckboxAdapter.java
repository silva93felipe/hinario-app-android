package com.app.hinariovirtual.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.hinariovirtual.R;
import com.app.hinariovirtual.model.Hino;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter para seleção de hinos com checkbox (usado no formulário de evento)
 */
public class HinoCheckboxAdapter extends ArrayAdapter<Hino> {

    private Context context;
    private List<Hino> hinos;
    private Set<Integer> hinosSelecionados;

    public HinoCheckboxAdapter(Context context, List<Hino> hinos) {
        super(context, R.layout.item_hino_checkbox, hinos);
        this.context = context;
        this.hinos = hinos;
        this.hinosSelecionados = new HashSet<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_hino_checkbox, parent, false);

            holder = new ViewHolder();
            holder.checkbox = convertView.findViewById(R.id.checkboxHino);
            holder.textCodigo = convertView.findViewById(R.id.textHinoCodigoCheck);
            holder.textTitulo = convertView.findViewById(R.id.textHinoTituloCheck);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Hino hino = hinos.get(position);
        holder.textCodigo.setText(hino.getCodigo());
        holder.textTitulo.setText(hino.getTitulo());
        holder.checkbox.setChecked(hinosSelecionados.contains(hino.getId()));

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hinosSelecionados.add(hino.getId());
            } else {
                hinosSelecionados.remove(hino.getId());
            }
        });

        // Clique na row toda também marca/desmarca
        View finalConvertView = convertView;
        convertView.setOnClickListener(v -> {
            holder.checkbox.setChecked(!holder.checkbox.isChecked());
        });

        return convertView;
    }

    public List<Integer> getHinosSelecionadosIds() {
        return new ArrayList<>(hinosSelecionados);
    }

    static class ViewHolder {
        CheckBox checkbox;
        TextView textCodigo;
        TextView textTitulo;
    }
}
