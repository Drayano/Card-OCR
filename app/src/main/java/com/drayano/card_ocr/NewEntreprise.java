package com.drayano.card_ocr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.drayano.card_ocr.database.Entreprise;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;


public class NewEntreprise extends AppCompatActivity
{
    private MaterialToolbar toolbar;
    private EditText name;
    private EditText tel1;
    private EditText tel2;
    private EditText fixe;
    private EditText infoAdd;
    private EditText email;
    private EditText website;
    private EditText location;
    private EditText facebook;
    private EditText motscles;
    private EditText nomContact;
    private EditText commune;
    private EditText anneeCreation;
    private EditText fax;
    private MaterialButton choisirLogo;
    private AutoCompleteTextView secteurActivites1,secteurActivites2,secteurActivites3,wilaya;
    private RatingBar ratingBar;
    private ProgressBar loading;
    private String partenaire;
    private ImageView imgNewCompany;
    private int erreurRemplissage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static Uri photoURI,uploadURI;
    //private String URL_NEW_BUSINESS = "https://ismail-houari.000webhostapp.com/new_business.php";
    //private String URL_NEW_BUSINESS = "http://card.aeadz.net/android/newbusiness.php";
    private String URL_NEW_BUSINESS = "http://testcard.aeadz.net/android/newbusiness.php";
    private String URL_UPDATE_BUSINESS = "http://testcard.aeadz.net/android/update_business.php";
    private String MODE = "INSERT";
    private String createdBy;

    private final String[] wilaya_list =
            {
                "Adrar","Chlef","Laghouat",
                "Oum El Bouaghi","Batna","Béjaia","Biskra","Béchar","Blida",
                "Bouira","Tamanrasset","Tébessa","Tlemcen","Tiaret","Tizi Ousou",
                "Alger","Djelfa","Jijel","Sétif","Saida","Skikda","Sidi Bel Abbes",
                "Annaba","Guelma","Constantine","Médéa","Mostaganem","M'Sila","Mascara","Ouargla",
                "Oran","El Bayadh","Illizi","Bordj Bou Arrerij","Boumerdès","El Tarf","Tindouf",
                "Tissemsilt","El Oued","Khenchela","Souk Ahraz","Tipaza","Mila","Aïn Defla",
                "Naâma","Aïn Témouchent","Ghardaïa","Relizane"
            };
    String[] SECTEURS;
    String[] SECTEURS_NUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entreprise);

        toolbar = findViewById(R.id.new_info_toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.champ_nom);
        tel1 = findViewById(R.id.champ_tel);
        tel2 = findViewById(R.id.champ_tel2);
        fixe = findViewById(R.id.champ_fixe);
        email = findViewById(R.id.champ_email);
        website = findViewById(R.id.champ_website);
        infoAdd = findViewById(R.id.champ_mots_cles);
        location = findViewById(R.id.champ_location);
        loading = findViewById(R.id.loading_new_business);
        imgNewCompany = findViewById(R.id.img_new_company);
        fax = findViewById(R.id.champ_fax);
        facebook = findViewById(R.id.champ_facebook);
        secteurActivites1 = findViewById(R.id.secteur_activites1);
        secteurActivites2 = findViewById(R.id.secteur_activites2);
        secteurActivites3 = findViewById(R.id.secteur_activites3);
        motscles = findViewById(R.id.champ_mots_cles);
        commune = findViewById(R.id.champ_commune);
        nomContact = findViewById(R.id.champ_nom_contact);
        anneeCreation = findViewById(R.id.champ_creation);
        wilaya = findViewById(R.id.champ_wilaya);
        choisirLogo = findViewById(R.id.btn_logo_retry);

        SharedPreferences userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        createdBy = userDetails.getString("username","");

        if (getIntent().getIntExtra("id",0) != 0)
        {
            MODE = "UPDATE";
            setDataFromIntent();
        }

        SECTEURS= getResources().getStringArray(R.array.secteur_activities_array);
        SECTEURS_NUMBER = getResources().getStringArray(R.array.secteur_activité_number);

        ArrayAdapter<String> adapterSecteur = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SECTEURS);
        secteurActivites1.setAdapter(adapterSecteur);
        secteurActivites2.setAdapter(adapterSecteur);
        secteurActivites3.setAdapter(adapterSecteur);

        ArrayAdapter<String> adapterWilaya = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,wilaya_list);
        wilaya.setAdapter(adapterWilaya);

        choisirLogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(photoURI != null)
                    startCorp(photoURI);
            }
        });

        // Vérfification des données entrées dans chaque champ (FIN JUSQU'AU PROCHAIN COMMENTAIRE)
        erreurRemplissage = 0;
        name.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (name.getText().toString().isEmpty() || !name.getText().toString().matches("([a-zA-Z0-9]{2,})(\\s([a-zA-Z0-9]{2,}))*"))
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
                if (!website.getText().toString().isEmpty() && !website.getText().toString().matches("(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})"))
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

        if (getIntent().getStringExtra("call").equals("scan")){
            dispatchTakePictureIntent();
        }
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
        wilaya.setText(getIntent().getStringExtra("wilaya"));
        fax.setText(getIntent().getStringExtra("fax"));
        nomContact.setText(getIntent().getStringExtra("contact"));
        facebook.setText(getIntent().getStringExtra("facebook"));
        commune.setText(getIntent().getStringExtra("commune"));

        if(!getIntent().getStringExtra("img_path").isEmpty())
        {
            Picasso.get().load(getIntent().getStringExtra("img_path")).into(imgNewCompany);
        }
    }

    // Remplissage de l'objet a ajouter dans la base
    private void remplirInformations(Entreprise entreprise)
    {
        entreprise.setName(name.getText().toString());
        entreprise.setFirstMobile(tel1.getText().toString());
        entreprise.setSecondMobile(tel2.getText().toString());
        entreprise.setTelFixe(fixe.getText().toString());
        entreprise.setEmail(email.getText().toString());
        entreprise.setWebsite(website.getText().toString());
        entreprise.setLocation(location.getText().toString());
        entreprise.setFacebook(facebook.getText().toString());
        entreprise.setSecteurActivites(secteurActivites1.getText().toString());
        entreprise.setFax(fax.getText().toString());
        entreprise.setMotsCles(motscles.getText().toString());

        Log.i("Information",entreprise.getName() + "**" +entreprise.getTelString() + "**" +entreprise.getTelFixe() +
                "**" +entreprise.getFax() + "**" +entreprise.getEmail() + "**" +entreprise.getLocation()+"**"+entreprise.getFacebook() +
                "**" +entreprise.getSecteurActivites() + "**"+entreprise.getWebsite());
    }

    // Obtention de la valeur des radioButton
//    public void onRadioButtonClicked(View view)
//    {
//        boolean checked = ((RadioButton) view).isChecked();
//
//        switch (view.getId())
//        {
//            case R.id.partner :
//                if (checked)
//                {
//                    partenaire = "oui";
//                }
//
//                break;
//
//            case R.id.not_partner :
//                if (checked)
//                {
//                    partenaire = "non";
//                }
//
//                break;
//        }
//    }

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
                    final Entreprise newEntreprise = new Entreprise();
                    remplirInformations(newEntreprise);
                    envoyerRequete();
//                    if (){
//                        AsyncTask.execute(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                AnnuaireDatabase mydb = AnnuaireDatabase.getInstance(getApplicationContext());
//                                mydb.entrepriseDao().insertEntreprise(newEntreprise);
//                                // better use html text for better formatting
//                                String msg = "<h3>Votre entreprise " +newEntreprise.getName()+ " a bien été enregistrée chez Les Annuaires Ecomnomiques Algeriens,</h3></br><h3>Merci.</h3>";
//                                // add       Email                 Password  (security problem : the email will be available in all devices and for this it's better using php mailing in server side)
//                                if (newEntreprise.getEmail() != null )
//                                    email("ismailsgh10@gmail.com","messi1075",newEntreprise.getEmail(),"Enregistrement AnnuairesDz",msg,"gmail",getApplicationContext());
//
//                            }
//                        });


//                        if (photoURI != null){
//                            Intent toLogo = new Intent(getApplicationContext(),ImageCropperActivity.class);
//                            toLogo.putExtra("imageUri",photoURI.toString());
//                            startActivity(toLogo);
//                        }else{
//                            Intent main = new Intent(getApplicationContext(),MainActivity.class);
//                            startActivity(main);
//
//                        }

//                    }


                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Veuillez entrer des informations valides", Toast.LENGTH_LONG).show();
                }

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

    private void envoyerRequete()
    {
        choisirLogo.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        String URL = URL_NEW_BUSINESS;
        if (MODE.equals("UPDATE")) URL = URL_UPDATE_BUSINESS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1"))
                            {
                                Toast.makeText(NewEntreprise.this,"Enregistrer avec succée",Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                                choisirLogo.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(NewEntreprise.this,MainActivity.class);
                                startActivity(intent);


                            }

                            else
                            {
                                Toast.makeText(NewEntreprise.this,"Echec de l'enregistrement ",Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                                choisirLogo.setVisibility(View.VISIBLE);
                            }
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(NewEntreprise.this,"Erreur dans le format JSON",Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            choisirLogo.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError){
                            Toast.makeText(NewEntreprise.this,"Oups, Serveur en maintenance",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(NewEntreprise.this,"Veuillez verifier votre connectivité",Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace();
                        loading.setVisibility(View.GONE);
                        choisirLogo.setVisibility(View.VISIBLE);

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name.getText().toString());
                        params.put("nom_contact",nomContact.getText().toString());
                        params.put("tel",  getTelString());
                        params.put("email", email.getText().toString());
                        params.put("fix", fixe.getText().toString());
                        params.put("website", website.getText().toString());
                        params.put("adresse", location.getText().toString());
                        params.put("wilaya",wilaya.getText().toString());
                        params.put("commune",commune.getText().toString());
                        params.put("fax",fax.getText().toString());
                        params.put("secteurActivites",getSecteurActivite());
                        params.put("facebook",facebook.getText().toString());
                        params.put("annee_creation",anneeCreation.getText().toString());
                        params.put("mots_cles",motscles.getText().toString());
                        params.put("img_uploaded",getStringImage());

                        if (getIntent().getStringExtra("img_path") != null)
                        {
                            params.put("img_path",getIntent().getStringExtra("img_path"));
                        }

                        params.put("created_by",createdBy);

                        if (getIntent().getIntExtra("id",0) != 0)
                        {
                           params.put("id",String.valueOf(getIntent().getIntExtra("id",0)));
                        }

                        return params;
                    }
                };

//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });

        RequestQueue requestQueue = Volley.newRequestQueue(NewEntreprise.this);
        requestQueue.add(stringRequest);
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

            if (photoFile != null)
            {
                // Continue only if file was successfully created
                photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
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
            myTextRecognizer.setAdditionalInfoView(infoAdd);
            myTextRecognizer.setLocation(location);

            myTextRecognizer.setProgressBar(progressBar);
            myTextRecognizer.setWindow(getWindow());
            myTextRecognizer.setProgressBarLayout(layout);
            myTextRecognizer.setProgressBarLinearLayout(progressBarLayout);
            myTextRecognizer.AnalyserImage();
            choisirLogo.setVisibility(View.VISIBLE);
            imgNewCompany.setImageURI(photoURI);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                uploadURI = result.getUri();
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }

        if ( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                imgNewCompany.setImageURI(result.getUri());
                uploadURI = result.getUri();
            }
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

    // You call the function with the sender email, his password, the receiver email, the subject title, the message content, and the email provider of the sender (only gmail and yahoo supported).
    private static void email(String from, String password, String to, String subject, String message, String provider,Context context)
    {
        //    From  -   Password    -   To  -   Subject -   Message - Provider (either gmail or yahoo)
        send(from, password, to, subject, message, provider,context);
        System.out.println("Done");
    }

    static void send(final String from, final String password, String to, String sub, String msg, String provider, Context context)
    {
        // Get properties object
        Properties props = new Properties();

        if (provider.equalsIgnoreCase("gmail"))
        {
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.socketFactory.port", "465");
//            props.put("mail.smtp.socketFactory.class",
//                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
        }

        else if (provider.equalsIgnoreCase("yahoo"))
        {
            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
        }

        // Get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(from, password);
                    }
                });

        // Compose message
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(sub);
            message.setContent(msg,"text/html");

            // Send message
            Transport.send(message);
            System.out.println("Email Sent Successfully");
        }

        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private String getTelString()
    {
        String telString ="";
        if (!tel1.getText().toString().isEmpty())
        {
            telString = tel1.getText().toString();
        }

        if (!tel2.getText().toString().isEmpty())
        {
            telString = telString + "," + tel2.getText().toString();
        }

        return telString;
    }

    private String getSecteurActivite()
    {
        String secteurs = "";

        if (!secteurActivites1.getText().toString().isEmpty())
        {
            for (int i = 1; i < SECTEURS.length; i++)
            {
                if(SECTEURS[i].equals(secteurActivites1.getText().toString()))
                    secteurs = SECTEURS_NUMBER[i];
            }
        }

        if (!secteurActivites2.getText().toString().isEmpty())
        {
            for (int i = 1; i < SECTEURS.length; i++)
            {
                if(SECTEURS[i].equals(secteurActivites2.getText().toString()))
                {
                    secteurs = secteurs + "," + SECTEURS_NUMBER[i];
                }
            }
        }

        if (!secteurActivites3.getText().toString().isEmpty())
        {
            for (int i = 1; i < SECTEURS.length ; i++)
            {
                if(SECTEURS[i].equals(secteurActivites3.getText().toString()))
                {
                    secteurs = secteurs + "," + SECTEURS_NUMBER[i];
                }
            }
        }

        return secteurs;
    }

    private void startCorp(Uri imageUri)
    {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private String getStringImage()
    {
        String encodedImage ="";

        if (uploadURI != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(uploadURI.getPath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }

        return encodedImage;
    }
}
