package com.drayano.card_ocr;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drayano.card_ocr.database.Entreprise;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private List<Entreprise> listEntreprise;
    Context context;

    public MyAdapter(Context ct)
    {
        context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.liste_element , parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final Entreprise entreprise = listEntreprise.get(position);
        holder.nomEntreprise.setText(entreprise.getName());
        holder.description.setText(String.valueOf(entreprise.getNote()));
        holder.elementLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context.getApplicationContext(), NewEntreprise.class);
                intent.putExtra("id",entreprise.getEntrepriseId());
                intent.putExtra("name",entreprise.getName());
                intent.putExtra("tel1",entreprise.getFirstMobile());
                intent.putExtra("tel2",entreprise.getSecondMobile());
                intent.putExtra("fix",entreprise.getTelFixe());
                intent.putExtra("fax",entreprise.getFax());
                intent.putExtra("email",entreprise.getEmail());
                intent.putExtra("website",entreprise.getWebsite());
                intent.putExtra("address",entreprise.getLocation());
                intent.putExtra("wilaya",entreprise.getWilaya());
                intent.putExtra("commune",entreprise.getCommune());
                intent.putExtra("facebook",entreprise.getFacebook());
                intent.putExtra("secteurs",entreprise.getSecteurActivites());
                intent.putExtra("commune",entreprise.getCommune());
                intent.putExtra("contact",entreprise.getContact());
                intent.putExtra("img_path",entreprise.getImgPath());
                intent.putExtra("call","update");
                Log.i("Get ID :", String.valueOf(entreprise.getEntrepriseId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (listEntreprise != null)
        {
            return listEntreprise.size();
        }

        else
        {
            return 0;
        }
    }

    public void setEntreprise(List<Entreprise> entreprises)
    {
        listEntreprise = entreprises;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nomEntreprise, description;
        RelativeLayout elementLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nomEntreprise = itemView.findViewById(R.id.nom_entreprise);
            description = itemView.findViewById(R.id.description_entreprise);
            elementLayout = itemView.findViewById(R.id.list_element);
        }
    }
}
