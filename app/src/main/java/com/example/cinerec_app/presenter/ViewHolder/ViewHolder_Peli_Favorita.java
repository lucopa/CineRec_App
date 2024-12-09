package com.example.cinerec_app.presenter.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.R;

public class ViewHolder_Peli_Favorita extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolder_Peli_Favorita.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); //SE EJECUTA AL PRESIONAR EL ITEM
        void onItemLongClick(View view, int position); //SE EJECUTA AL MANTENER PRESIONADO EL ITEM
    }

    public void setOnClickListener(ViewHolder_Peli_Favorita.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Peli_Favorita(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void SetearDatos(Context context, String id_peli, String uid_usuario,
                            String correo_usuario, String fecha_hora_registro,
                            String titulo, String descripcion, String fecha_peli, String estado){

        TextView id_peli_item,Uid_Usuario_item,Correo_Usuario_item,fecha_hora_registro_item,Titulo_item,Descripcion_item,Fecha_item,Estado_item;

        ImageView peli_vista_item, peli_pendiente_item;


        //Establecer la conexion con el item
        id_peli_item= mView.findViewById(R.id.id_peli_item_I);
        Uid_Usuario_item= mView.findViewById(R.id.Uid_Usuario_item_I);
        Correo_Usuario_item= mView.findViewById(R.id.Correo_Usuario_item_I);
        fecha_hora_registro_item= mView.findViewById(R.id.fecha_hora_registro_item_I);
        Titulo_item= mView.findViewById(R.id.Titulo_item_I);
        Descripcion_item= mView.findViewById(R.id.Descripcion_item_I);
        Fecha_item= mView.findViewById(R.id.Fecha_item_I);
        Estado_item= mView.findViewById(R.id.Estado_item_I);
        peli_vista_item= mView.findViewById(R.id.peli_vista_item_I);
        peli_pendiente_item= mView.findViewById(R.id.peli_pendiente_item_I);


        //Setear la informacion dentrod el item
        id_peli_item.setText(id_peli);
        Uid_Usuario_item.setText(uid_usuario);
        Correo_Usuario_item.setText(correo_usuario);
        fecha_hora_registro_item.setText(fecha_hora_registro);
        Titulo_item.setText(titulo);
        Descripcion_item.setText(descripcion);
        Fecha_item.setText(fecha_peli);
        Estado_item.setText(estado);

        if (estado.equals("Visto")){
            peli_vista_item.setVisibility(View.VISIBLE);
        } else {
            peli_pendiente_item.setVisibility(View.VISIBLE);
        }


    }
}
