package com.example.evaluacion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IncidenceModel> incidenceModelArrayList;

    public CustomAdapter(Context context, ArrayList<IncidenceModel> incidenceModelArrayList) {
        this.context = context;
        this.incidenceModelArrayList = incidenceModelArrayList;
    }

    @Override
    public int getCount() {
        return incidenceModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return incidenceModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);

            holder.tvId =convertView.findViewById(R.id.id);
            holder.tvLaboratory = convertView.findViewById(R.id.laboratory);
            holder.tvRut = convertView.findViewById(R.id.rut);
            holder.tvName = convertView.findViewById(R.id.name);
            holder.tvIncidenceBody = convertView.findViewById(R.id.incidenceBody);
            holder.tvDate = convertView.findViewById(R.id.date);
            holder.tvTime = convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        } else {
            // El método getTag devuelve el objeto ViewHolder establecido como una etiqueta en la vista
            holder = (ViewHolder) convertView.getTag();
        }

        // Mostrar información en la interfaz de usuario
        holder.tvId.setText("Registro N°: "+ incidenceModelArrayList.get(position).getId());
        holder.tvLaboratory.setText("Laboratorio: " + incidenceModelArrayList.get(position).getLaboratory());
        holder.tvRut.setText("RUT: " + incidenceModelArrayList.get(position).getRut());
        holder.tvName.setText("Nombre: " + incidenceModelArrayList.get(position).getName());
        holder.tvIncidenceBody.setText("Descripción: " + incidenceModelArrayList.get(position).getIncidenceBody());
        holder.tvDate.setText("Fecha: " + incidenceModelArrayList.get(position).getDate());
        holder.tvTime.setText("Hora: " + incidenceModelArrayList.get(position).getTime());

        return convertView;
    }

    private static class ViewHolder {
        protected TextView tvLaboratory, tvRut, tvName, tvIncidenceBody, tvDate, tvTime, tvId;
    }
}