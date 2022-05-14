package com.drayano.card_ocr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.drayano.card_ocr.database.Entreprise;

public class EntrepriseInformations extends AppCompatActivity
{
    private int id;
    private MaterialToolbar toolbar;
    private LiveData<Entreprise> entrepriseLiveData;
    private EntrepriseViewModel entrepriseViewModel;
    private Entreprise entrepriseActuelle;
    private TextView nameView, tel1View, tel2View, fixView, emailView, websiteView, locationTextView;
    private RatingBar ratingBar;
    private ImageButton btnCallTel1, btnCallTel2, btnMsgTel1, btnMsgTel2, btnCallFixe, btnEmail, btnWebsite, btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreprise_informations);
        nameView = findViewById(R.id.affichage_nom);
        tel1View = findViewById(R.id.affichage_tel);
        tel2View = findViewById(R.id.affichage_tel2);
        fixView = findViewById(R.id.affichage_fix);
        emailView = findViewById(R.id.affichage_email);
        websiteView = findViewById(R.id.affichage_website);
        locationTextView = findViewById(R.id.affichage_localization);
        ratingBar = findViewById(R.id.ratingBar);
        toolbar = findViewById(R.id.entreprise_info_toolbar);
        btnCallTel1 = findViewById(R.id.btn_call_tel1);
        btnCallTel2 = findViewById(R.id.btn_call_tel2);
        btnCallFixe = findViewById(R.id.btn_call_fix);
        btnMsgTel1 = findViewById(R.id.btn_msg_tel1);
        btnMsgTel2 = findViewById(R.id.btn_msg_tel2);
        btnEmail = findViewById(R.id.btn_email);
        btnWebsite = findViewById(R.id.btn_website);
        btnLocation = findViewById(R.id.btn_location);
        setSupportActionBar(toolbar);

//        btnCallTel1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String uri;
//                uri = "tel:" + tel1View.getText();
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse(uri));
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    Log.i("Mobile call : ","Error !");
//                    return;
//                }
//                startActivity(intent);
//            }
//        });

        btnCallTel1.setOnClickListener(allEventsListener);
        btnCallTel2.setOnClickListener(allEventsListener);
        btnMsgTel1.setOnClickListener(allEventsListener);
        btnMsgTel2.setOnClickListener(allEventsListener);
        btnCallFixe.setOnClickListener(allEventsListener);
        btnEmail.setOnClickListener(allEventsListener);
        btnWebsite.setOnClickListener(allEventsListener);
        btnLocation.setOnClickListener(allEventsListener);

        id = getIntent().getIntExtra("id",0);


        entrepriseViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EntrepriseViewModel.class);
        setDataFromIntent();
        //getData();
    }

    private void getData()
    {
        entrepriseLiveData = entrepriseViewModel.getEntreprise(id);
        setData();
    }

    private void setDataFromIntent()
    {
        nameView.setText(getIntent().getStringExtra("name"));
        tel1View.setText(getIntent().getStringExtra("tel1"));
        tel2View.setText(getIntent().getStringExtra("tel2"));
        fixView.setText(getIntent().getStringExtra("fix"));
        emailView.setText(getIntent().getStringExtra("email"));
    }


    // Affectation de la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.entreprise_info_app_bar,menu);
        return true;
    }

    // Toolbar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.i("onOptionsItemSelected", "Ca marche !");
        switch (item.getItemId())
        {
            case R.id.delete :
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Est-vous sur de vouloir supprimer l'entreprise ?");
                alertDialogBuilder.setPositiveButton("Annuler",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Log.i("Suppression :", "Annuler");
                            }
                        });

                final LifecycleOwner lifecycleOwner = this;
                alertDialogBuilder.setNegativeButton("Confirmer",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int id = getIntent().getIntExtra("id",0);

                                if (entrepriseLiveData.hasObservers())
                                {
                                    entrepriseLiveData.removeObservers(lifecycleOwner);
                                }

                                deleteEntreprise();
                                Log.i("onOptionsItemSelected","Suppression aboutie");
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;

            case R.id.edit :
                Intent i = new Intent(getApplicationContext(),EditEntreprise.class);
                i.putExtra("IdEntreprise",id);
                startActivity(i);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteEntreprise()
    {
        entrepriseViewModel.deleteEntreprise(entrepriseActuelle);
    }

    View.OnClickListener allEventsListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String uri;
            Intent intent;

            switch (v.getId())
            {
                case R.id.btn_call_tel1:
                    if (tel1View.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        uri = "tel:" + tel1View.getText();
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        {
                            Log.i("Mobile call : ","Error !");
                            return;
                        }

                        startActivity(intent);
                    }

                    break;

                case R.id.btn_call_tel2:
                    if (tel2View.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        uri = "tel:" + tel2View.getText();
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        {
                            Log.i("Mobile call : ","Error !");
                            return;
                        }

                        else startActivity(intent);
                    }

                    break;

                case R.id.btn_call_fix:
                    if (fixView.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        uri = "tel:" + fixView.getText();
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        {
                            Log.i("Mobile call : ","Error !");
                            return;
                        }

                        else startActivity(intent);
                    }

                    break;

                case R.id.btn_msg_tel1:
                    if (tel1View.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        uri = "smsto:" + tel1View.getText();
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse(uri));

                        if (intent.resolveActivity(getPackageManager()) != null)
                        {
                            startActivity(intent);
                        }
                    }

                    break;

                case R.id.btn_msg_tel2:
                    if (tel2View.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})") )
                    {
                        uri = "smsto:" + tel2View.getText();
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse(uri));

                        if (intent.resolveActivity(getPackageManager()) != null)
                        {
                            startActivity(intent);
                        }
                    }

                    break;

                case R.id.btn_email:
                    if (emailView.getText().toString().matches("[A-Za-z0-9+_.-]+@(.+)"))
                    {
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + emailView.getText()));

                        if (intent.resolveActivity(getPackageManager()) != null)
                        {
                            startActivity(intent);
                        }
                    }

                    break;

                case R.id.btn_website:
                    if (websiteView.getText().toString().matches("(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})"))
                    {
                        String url = websiteView.getText().toString();

                        if ( !url.startsWith("https://") && !url.startsWith("http://") )
                        {
                            url = "http://" + url;
                        }

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        if (intent.resolveActivity(getPackageManager()) != null)
                        {
                            startActivity(intent);
                        }
                    }

                    break;

                case R.id.btn_location:
                    if (!locationTextView.getText().toString().isEmpty())
                    {
                        Log.i("MAPS INTENT "," Button clické");
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+locationTextView.getText().toString());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void setData()
    {
        entrepriseLiveData.observe(this, new Observer<Entreprise>()
        {
            @Override
            public void onChanged(Entreprise entreprise)
            {
                if (!entreprise.getName().isEmpty())
                {
                    nameView.setText(entreprise.getName());
                }

                if (!entreprise.getFirstMobile().isEmpty())
                {
                    tel1View.setText(entreprise.getFirstMobile());
                }

                if (!entreprise.getSecondMobile().isEmpty())
                {
                    tel2View.setText(entreprise.getSecondMobile());
                }

                if (!entreprise.getTelFixe().isEmpty())
                {
                    fixView.setText(entreprise.getTelFixe());
                }

                if (!entreprise.getEmail().isEmpty())
                {
                    emailView.setText(entreprise.getEmail());
                }

                if (!entreprise.getWebsite().isEmpty())
                {
                    websiteView.setText(entreprise.getWebsite());
                }

                if (entreprise.getLocation() != null && !entreprise.getLocation().isEmpty())
                {
                    locationTextView.setText(entreprise.getLocation());
                }

                ratingBar.setRating(entreprise.getNote());
                entrepriseActuelle = entreprise;
            }
        });
    }
}
