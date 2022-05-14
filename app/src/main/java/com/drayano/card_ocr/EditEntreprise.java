package com.drayano.card_ocr;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.drayano.card_ocr.database.AnnuaireDatabase;
import com.drayano.card_ocr.database.Entreprise;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEntreprise extends AppCompatActivity
{
    private MaterialToolbar toolbar;
    private int idEntreprise;
    private Entreprise entreprise;
    private LiveData<Entreprise> entrepriseLiveData;
    private EntrepriseViewModel entrepriseViewModel;
    private EditText name;
    private EditText tel1;
    private EditText tel2;
    private EditText fixe;
    private EditText infoAdditionnelles;
    private EditText email;
    private EditText website;
    private EditText location;
    private RadioButton partenaireRadio;
    private RadioButton nonPartenaireRadio;
    private RatingBar ratingBar;
    private String partenaire;
    private int erreurRemplissage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entreprise);

        toolbar = findViewById(R.id.new_info_toolbar);
        setSupportActionBar(toolbar);

        idEntreprise = getIntent().getIntExtra("IdEntreprise",0);

        name = findViewById(R.id.champ_nom);
        tel1 = findViewById(R.id.champ_tel);
        tel2 = findViewById(R.id.champ_tel2);
        fixe = findViewById(R.id.champ_fixe);
        email = findViewById(R.id.champ_email);
        website = findViewById(R.id.champ_website);
        infoAdditionnelles = findViewById(R.id.champ_info);
        location = findViewById(R.id.champ_location);
        ratingBar = findViewById(R.id.ratingBar);
        partenaireRadio = findViewById(R.id.partner);
        nonPartenaireRadio = findViewById(R.id.not_partner);

        entrepriseViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EntrepriseViewModel.class);
        //getData();
    }

    private void getData()
    {
        entrepriseLiveData = entrepriseViewModel.getEntreprise(idEntreprise);
        setData();
    }

    private void setDataFromIntent()
    {
        name.setText(getIntent().getStringExtra("name"));
        tel1.setText(getIntent().getStringExtra("tel1"));
        tel2.setText(getIntent().getStringExtra("tel2"));
        fixe.setText(getIntent().getStringExtra("fix"));
        email.setText(getIntent().getStringExtra("email"));
        website.setText(getIntent().getStringExtra("website"));
        location.setText(getIntent().getStringExtra("address"));
    }

    private void setData()
    {
        entrepriseLiveData.observe(this, new Observer<Entreprise>()
        {
            @Override
            public void onChanged(Entreprise entreprise)
            {
                if (!entreprise.getName().isEmpty())
                {
                    name.setText(entreprise.getName());
                }

                if (!entreprise.getFirstMobile().isEmpty())
                {
                    tel1.setText(entreprise.getFirstMobile());
                }

                if (!entreprise.getSecondMobile().isEmpty())
                {
                    tel2.setText(entreprise.getSecondMobile());
                }

                if (!entreprise.getEmail().isEmpty())
                {
                    email.setText(entreprise.getEmail());
                }

                if (!entreprise.getWebsite().isEmpty())
                {
                    website.setText(entreprise.getWebsite());
                }

                if (!entreprise.getTelFixe().isEmpty())
                {
                    fixe.setText(entreprise.getTelFixe());
                }

                if (!(entreprise.getPartenariat() == null) && entreprise.getPartenariat().equals("oui"))
                {
                    partenaireRadio.setChecked(true);
                    partenaire = "oui";
                }

                else
                {
                    nonPartenaireRadio.setChecked(true);
                    partenaire = "non";
                }

                ratingBar.setRating(entreprise.getNote());
            }
        });

        // Vérification des données entrées dans chaque champ (FIN JUSQU'AU PROCHAIN COMMENTAIRE)
        erreurRemplissage = 0;
        name.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (name.getText().toString().isEmpty() || !name.getText().toString().matches("([a-zA-Z0-9]{2,})"))
                    {
                        name.setError("Veuiller entrer un nom valide");
                        erreurRemplissage += 1;
                    }

                    else
                    {
                        erreurRemplissage -= 1;
                    }
                }
            }
        });

        tel1.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (!tel1.getText().toString().isEmpty() && !tel1.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        tel1.setError("Veuiller entrer un numéro valide");
                        erreurRemplissage += 1;
                    }

                    else
                    {
                        erreurRemplissage -= 1;
                    }
                }
            }
        });

        tel2.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (!tel2.getText().toString().isEmpty() && !tel2.getText().toString().matches("((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})"))
                    {
                        tel2.setError("Veuiller entrer un numéro valide");
                        erreurRemplissage += 1;
                    }

                    else
                    {
                        erreurRemplissage -= 1;
                    }
                }
            }
        });

        fixe.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (!fixe.getText().toString().isEmpty() && !fixe.getText().toString().matches("0((41)|(21))([0-9]{6})"))
                    {
                        erreurRemplissage += 1;
                        fixe.setError("Veuiller entrer un numéro valide");
                    }

                    else
                    {
                        erreurRemplissage -= 1;
                    }
                }
            }
        });

        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!email.getText().toString().isEmpty() && !email.getText().toString().matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"))
                {
                    erreurRemplissage += 1;
                    email.setError("Veuillez entrer un email valide");
                }

                else
                {
                    erreurRemplissage -= 1;
                }
            }
        });

        website.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!website.getText().toString().matches("(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})"))
                {
                    erreurRemplissage += 1;
                    website.setError("Veuillez entrer une adresse valide");
                }

                else
                {
                    erreurRemplissage -= 1;
                }
            }
        });
    }

    // Remplissage de l'objet à ajouter dans la base
    private void remplirInformations(Entreprise entreprise)
    {
        entreprise.setEntrepriseId(idEntreprise);
        entreprise.setName(name.getText().toString());
        entreprise.setFirstMobile(tel1.getText().toString());
        entreprise.setSecondMobile(tel2.getText().toString());
        entreprise.setTelFixe(fixe.getText().toString());
        entreprise.setEmail(email.getText().toString());
        entreprise.setWebsite(website.getText().toString());
        entreprise.setInfoAdditionnelles(infoAdditionnelles.getText().toString());
        entreprise.setPartenariat(partenaire);
        entreprise.setNote(ratingBar.getRating());
    }

    // Obtention de la valeur des radioButton
    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId())
        {
            case R.id.partner :
                if (checked)
                {
                    partenaire = "oui";
                }

                break;

            case R.id.not_partner :
                if (checked)
                {
                    partenaire = "non";
                }

                break;
        }
    }

    // Affectation de la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.new_info_top_app_bar,menu);
        return true;
    }

    // Toolbar listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.confirm_add :
                boolean ok = verifierFiche();
                if (ok)
                {
                    final Entreprise editedEntreprise = new Entreprise();
                    remplirInformations(editedEntreprise);

                    AsyncTask.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            AnnuaireDatabase mydb = AnnuaireDatabase.getInstance(getApplicationContext());
                            mydb.entrepriseDao().updateEntreprise(editedEntreprise);
                        }
                    });

                    Toast.makeText(this,"Entreprise modifiée",Toast.LENGTH_LONG).show();
                    finish();
                }

                else Toast.makeText(getApplicationContext(), "Veuillez entrer des informations valides", Toast.LENGTH_LONG).show();
                return true;

            case R.id.use_camera :
                dispatchTakePictureIntent();
                return true;

            default:
                System.out.println("Problem !!");
                Toast.makeText(getApplicationContext(), "Problem !" , Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    // Verification de la validité des données entrées
    public boolean verifierFiche()
    {
        int ct = 0;
        if (!name.getText().toString().isEmpty()) ct++;
        if (!tel1.getText().toString().isEmpty()) ct++;
        if (!tel2.getText().toString().isEmpty()) ct++;
        if (!fixe.getText().toString().isEmpty()) ct++;
        if (!email.getText().toString().isEmpty()) ct++;
        if (!website.getText().toString().isEmpty()) ct++;

        if (ct < 2)
        {
            return false;
        }

        else
        {
            return true;
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there is a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }

            catch (IOException ex)
            {
                // Error while creating file
                Log.d("Activity New Entreprise", "Echec creation du fichier de la photo");
            }

            if (photoFile != null )
            {
                // Continue only if file was successfully created
                photoURI = FileProvider.getUriForFile(this,
                        "com.drayano.card_ocr.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK)
        {
            Toast.makeText(this, "DE RETOUR !",Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);

            // Creation de la progressBar
            RelativeLayout layout = findViewById(R.id.new_entreprise_r_layout);
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            LinearLayout progressBarLayout = new LinearLayout(this);
            createProgressBarLayout(progressBarLayout,layout,progressBar);

            final MyTextRecognizer myTextRecognizer = new MyTextRecognizer(this, photoURI);
            myTextRecognizer.setNameView(name);
            myTextRecognizer.setTel1View(tel1);
            myTextRecognizer.setTel2View(tel2);
            myTextRecognizer.setFixeView(fixe);
            myTextRecognizer.setEmailView(email);
            myTextRecognizer.setWebsiteView(website);
            myTextRecognizer.setAdditionalInfoView(infoAdditionnelles);
            myTextRecognizer.setLocation(location);

            myTextRecognizer.setProgressBar(progressBar);
            myTextRecognizer.setWindow(getWindow());
            myTextRecognizer.setProgressBarLayout(layout);
            myTextRecognizer.setProgressBarLinearLayout(progressBarLayout);
            myTextRecognizer.AnalyserImage();
        }
    }

    private void createProgressBarLayout(LinearLayout linearLayout,RelativeLayout relativeLayout,ProgressBar progressBar)
    {
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_IN);
        TextView progressBarText =new TextView(this);
        progressBarText.setText("Extraction des informations ...");
        progressBarText.setTextSize(12);
        progressBarText.setTextColor(getColor(R.color.primary_text));
        linearLayout.setBackground(getDrawable(R.drawable.radius_gris));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams parametres = new LinearLayout.LayoutParams(150,150);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(450,150);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textParams.leftMargin = 5;
        parametres.gravity = Gravity.CENTER_VERTICAL;
        parametres.leftMargin = 5;
        linearLayout.addView(progressBarText,textParams);
        linearLayout.addView(progressBar,parametres);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(600, 300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(linearLayout, params);
    }
}
