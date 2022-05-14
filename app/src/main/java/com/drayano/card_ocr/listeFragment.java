package com.drayano.card_ocr;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.drayano.card_ocr.database.Entreprise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class listeFragment extends Fragment
{
    RecyclerView recyclerView;
    EntrepriseViewModel entrepriseViewModel;
    ProgressBar loading;
    private String URL_GET_BUSINESS = "http://testcard.aeadz.net/android/getBusiness.php";
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_liste , container , false);

        loading = rootView.findViewById(R.id.loading_last_business);
        recyclerView = rootView.findViewById(R.id.recyclerview_liste_total_entreprise);
        final MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        SharedPreferences userDetails = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        email = userDetails.getString("username","");

        entrepriseViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(EntrepriseViewModel.class);

//        entrepriseViewModel.getAll().observe(this, new Observer<List<Entreprise>>()
//        {
//            @Override
//            public void onChanged(List<Entreprise> entreprises)
//            {
//                myAdapter.setEntreprise(entreprises);
//            }
//        });
        getAllByEmail(myAdapter);

        return rootView;
    }

    public void getAllByEmail(final MyAdapter adapter)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_BUSINESS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONArray jsonArray = new JSONArray(response);
                            List<Entreprise> liste = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Entreprise entreprise = new Entreprise();
                                entreprise.setEntrepriseId(jsonObject.getInt("id"));
                                entreprise.setName((jsonObject.getString("name")));
                                entreprise.setContact(jsonObject.getString("contact_name"));
                                entreprise.setFacebook(jsonObject.getString("facebook"));
                                entreprise.setLocation(jsonObject.getString("address"));
                                entreprise.setImgPath(jsonObject.getString("logo"));
                                entreprise.setDoubleTel(jsonObject.getString("mobile"));
                                entreprise.setEmail(jsonObject.getString("email"));
                                entreprise.setWebsite(jsonObject.getString("website"));
                                entreprise.setTelFixe(jsonObject.getString("fixe"));
                                entreprise.setFax(jsonObject.getString("fax"));
                                entreprise.setWilaya(jsonObject.getString("state"));
                                entreprise.setCommune(jsonObject.getString("city"));
                                entreprise.setSecteurActivites(jsonObject.getString("categories"));

                                liste.add(entreprise);
                            }

                            if (!liste.isEmpty())
                            {
                                adapter.setEntreprise(liste);
                                Log.i("TAILLE LISTE", String.valueOf(liste.size()));
                            }

                            loading.setVisibility(View.GONE);

                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        loading.setVisibility(View.GONE);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("email",email);

                        return params;
                    }
                };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
