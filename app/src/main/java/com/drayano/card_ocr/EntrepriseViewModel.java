package com.drayano.card_ocr;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.drayano.card_ocr.database.AnnuaireDatabase;
import com.drayano.card_ocr.database.Entreprise;
import com.drayano.card_ocr.database.EntrepriseDao;

import java.util.List;

public class EntrepriseViewModel extends AndroidViewModel
{
    private LiveData<Integer> nombreEntreprise;
    private LiveData<Integer> nombrePartenaires;
    private LiveData<List<Entreprise>> listeEntreprise;
    private LiveData<List<Entreprise>> listePartenaires;
    private LiveData<Entreprise> entreprise;
    private LiveData<Entreprise> entrepriseAsupprimer;
    private String TAG = this.getClass().getSimpleName();
    private AnnuaireDatabase mydb;
    private EntrepriseDao entrepriseDao;
    private Application app;

    public EntrepriseViewModel(@NonNull Application application)
    {
        super(application);
        Log.i(TAG,"Super passed");
        app = application;
        mydb = AnnuaireDatabase.getInstance(application);
        entrepriseDao = mydb.entrepriseDao();

        nombreEntreprise = entrepriseDao.getNubmerOfEntreprise();
        nombrePartenaires = entrepriseDao.getNumberPartner();
        listeEntreprise = entrepriseDao.getAll();
        listePartenaires = entrepriseDao.getAllPart();
    }

    public LiveData<Integer> getNombreEntreprise()
    {
        return nombreEntreprise;
    }

    public  LiveData<Integer> getNombrePartenaires()
    {
        return nombrePartenaires;
    }

    public LiveData<List<Entreprise>> getAll()
    {
        return listeEntreprise;
    }

    public LiveData<List<Entreprise>> getAllPartners()
    {
        return listePartenaires;
    }

    public LiveData<Entreprise> getEntreprise(int id)
    {
        entreprise = entrepriseDao.getEntreprise(id);
        return entreprise;
    }

    public void deleteEntreprise(Entreprise entreprise)
    {
        new DeleteAsyncTask(entreprise).execute();
    }

    private class DeleteAsyncTask extends AsyncTask<Entreprise,Void,Void>
    {
        Entreprise mEntreprise;
        DeleteAsyncTask(Entreprise entreprise)
        {
            this.mEntreprise = entreprise;
        }

        @Override
        protected Void doInBackground(Entreprise... entreprises)
        {
            entrepriseDao.deleteEntreprise(mEntreprise);
            return null;
        }
    }
}
