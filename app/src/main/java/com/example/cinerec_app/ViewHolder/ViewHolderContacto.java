package com.example.cinerec_app.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinerec_app.R;

public class ViewHolderContacto extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderContacto.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position); //SE EJECUTA AL PRESIONAR EL ITEM
        void onItemLongClick(View view, int position); //SE EJECUTA AL MANTENER PRESIONADO EL ITEM
    }

    public void setOnClickListener(ViewHolderContacto.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderContacto(@NonNull View itemView) {
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

    public void SetearDatosContacto(Context context,
                                    String id_contacto, String uid_contacto, String nombre, String apellidos, String correo,
                                    String telefono, String edad, String direccion, String imagen){

        ImageView Imagen_contacto_Item;
        TextView id_contacto_item, uid_contacto_item, nombre_contacto_item, apellidos_contacto_item,
                correo_contacto_item, telefono_contacto_item, edad_contacto_item, direccion_contacto_item;

        Imagen_contacto_Item = mView.findViewById(R.id.Imagen_contacto_Item);
        id_contacto_item = mView.findViewById(R.id.id_contacto_item);
        uid_contacto_item = mView.findViewById(R.id.uid_contacto_item);
        nombre_contacto_item = mView.findViewById(R.id.nombre_contacto_item);
        apellidos_contacto_item = mView.findViewById(R.id.apellidos_contacto_item);
        correo_contacto_item = mView.findViewById(R.id.correo_contacto_item);
        telefono_contacto_item = mView.findViewById(R.id.telefono_contacto_item);
        edad_contacto_item = mView.findViewById(R.id.edad_contacto_item);
        direccion_contacto_item = mView.findViewById(R.id.direccion_contacto_item);

        id_contacto_item.setText(id_contacto);
        uid_contacto_item.setText(uid_contacto);
        nombre_contacto_item.setText(nombre);
        apellidos_contacto_item.setText(apellidos);
        correo_contacto_item.setText(correo);
        telefono_contacto_item.setText(telefono);
        edad_contacto_item.setText(edad);
        direccion_contacto_item.setText(direccion);

        try {

            Glide.with(context)
                    .load(imagen)
                    .placeholder(R.drawable.usuario)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(Imagen_contacto_Item);
        } catch (Exception e) {

            Glide.with(context)
                    .load(R.drawable.usuario)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(Imagen_contacto_Item);
        }





    }
}
