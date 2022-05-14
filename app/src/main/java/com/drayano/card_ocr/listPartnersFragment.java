package com.drayano.card_ocr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drayano.card_ocr.database.Entreprise;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link listPartnersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class listPartnersFragment extends Fragment
{
    private RecyclerView recyclerView;
    private EntrepriseViewModel entrepriseViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_list_partners , container , false);

        recyclerView = rootView.findViewById(R.id.recyclerview_liste_partner);
        final MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        entrepriseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(EntrepriseViewModel.class);
        entrepriseViewModel.getAllPartners().observe(this, new Observer<List<Entreprise>>()
        {
            @Override
            public void onChanged(List<Entreprise> entreprises)
            {
                myAdapter.setEntreprise(entreprises);
            }
        });

        return rootView;
    }
}
