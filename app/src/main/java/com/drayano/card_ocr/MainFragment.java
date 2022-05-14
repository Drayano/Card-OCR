package com.drayano.card_ocr;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Objects;

public class MainFragment extends Fragment
{
    private LinearLayout ajouter,scaner;

    public String info_all;
    Menu menu;
    MenuItem listeEntrepriseItem, listePartenairItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_main, container , false);

        scaner = rootView.findViewById(R.id.layout_scanner);
        ajouter = rootView.findViewById(R.id.layout_ajouter);
        ajouter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent newInfo = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext() , NewEntreprise.class);
                newInfo.putExtra("call","manual");
                startActivity(newInfo);
            }
        });
        scaner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent newInfo = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext() , NewEntreprise.class);
                newInfo.putExtra("call","scan");
                startActivity(newInfo);
            }
        });

        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.drawer_view,menu);
//        listeEntrepriseItem = menu.findItem(R.id.listeEntreprise);
//        listePartenairItem = menu.findItem(R.id.listePartenaires);
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem menuItem = menu.findItem(R.id.listePartenaires);
//    }

    //    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
//        ajouter = getView().findViewById(R.id.layout_ajouter);
//        ajouter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent new_info = new Intent(getActivity().getApplicationContext() , NewEntreprise.class);
//                startActivity(new_info);
//            }
//        });
//    }
}


