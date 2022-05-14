package com.drayano.card_ocr.traitement;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.drayano.card_ocr.EntrepriseViewModel;
import com.drayano.card_ocr.database.AnnuaireDatabase;
import com.drayano.card_ocr.database.Entreprise;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportCSV
{
    private Context context;
    private Activity activity;
    private LifecycleOwner lifecycleOwner;
    private EntrepriseViewModel entrepriseViewModel;
    private List<Entreprise> listeEntreprises;
    private LiveData<List<Entreprise>> listEntreprise;

    public ExportCSV(Context context,Activity activity, LifecycleOwner lifecycleOwner) throws IOException
    {
        this.context = context;
        this.activity = activity;
        this.lifecycleOwner = lifecycleOwner;
        entrepriseViewModel = new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()).create(EntrepriseViewModel.class);
        listEntreprise = entrepriseViewModel.getAll();
        listeEntreprises = listEntreprise.getValue();
    }

    private File createCSVFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
        String CsvFileName = "Excel_"+ timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File csvFile = File.createTempFile(
                CsvFileName,    /* prefix */
                ".csv",   /* suffix */
                storageDir      /* directory */
        );

        return csvFile;
    }

    public Uri exportCsv()
    {
        File csvFile = null;

        try
        {
            csvFile = createCSVFile();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        final File finalCsvFile = csvFile;
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                AnnuaireDatabase annuaireDatabase = AnnuaireDatabase.getInstance(context);
                listeEntreprises = annuaireDatabase.entrepriseDao().getAllCsv();

                if (finalCsvFile != null)
                {
                    try
                    {
                        CSVWriter csvWriter = new CSVWriter(new FileWriter(finalCsvFile));
                        String entete [] = "Nom_Adresse_Email_Site Web_TELEPHONE 1_TELEPHONE 2_FIXE".split("_");
                        csvWriter.writeNext("sep=,".split(" ")); // Tell Excel to use , as separator
                        csvWriter.writeNext(entete);

                        Entreprise entreprise;
                        int i = 0;
                        for (i = 0; i < listeEntreprises.size(); i++)
                        {
                            entreprise = listeEntreprises.get(i);
                            String arrStr [] = entreprise.toString().split("_");
                            csvWriter.writeNext(arrStr);
                        }

                        csvWriter.close();
                    }

                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        Uri csvUri = FileProvider.getUriForFile(context,"com.drayano.card_ocr.fileprovider", csvFile);
        return csvUri;
    }
}
