package com.drayano.card_ocr;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.drayano.card_ocr.traitement.ExtracteurEmail;
import com.drayano.card_ocr.traitement.ExtracteurLocalization;
import com.drayano.card_ocr.traitement.ExtracteurNom;
import com.drayano.card_ocr.traitement.ExtracteurSiteWeb;
import com.drayano.card_ocr.traitement.ExtracteurTelephone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTextRecognizer
{
    private EditText nameView;
    private EditText tel1View;
    private EditText tel2View;
    private EditText fixeView;
    private EditText emailView;
    private EditText websiteView;
    private EditText location;
    private EditText additionalInfoView;
    private String TAG ="MyTextRecognizer";
    private FirebaseVisionImage image;
    private FirebaseVisionTextRecognizer textRecognizer;
    private ArrayList<String> infoLines;
    private Window window;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private LinearLayout progressBarLinearLayout;
    private String[] wilaya =
        {
            "Adrar","adrar","ADRAR","CHLEF","Chlef","chlef","LAGHOUAT","Laghouat","laghouat",
            "OUM EL BOUAGHI","Oum El Bouaghi","oum el bouaghi","BATNA","Batna","batna","BEJAIA","Béjaia","béjaia","bejaia","Béjaïa","béjaïa",
            "BISKRA","Biskra","biskra","BECHAR","Béchar","Bechar","béchar","bechar","BLIDA","Blida","blida",
            "BOUIRA","Bouira","bouira","TAMANRASSET","Tamanrasset","tamanrasset","TEBESSA","Tébessa","Tebessa","tebessa",
            "TLEMCEN","Tlemcen","tlemcen","TIARET","Tiaret","tiaret","TIZI OUZOU","Tizi Ousou","tizi ousou",
            "ALGER","Alger","alger","DJELFA","Djelfa","djelfa","JIJEL","Jijel","jijel","SETIF","Sétif","Setif","setfi",
            "SAIDA","Saida","saida","saïda","SKIKDA","Skikda","skikda","SIDI BEL ABBES","Sidi Bel Abbèss","Sidi Bel Abbes","sidi bel abbes","sidi bel abbès",
            "ANNABA","Annaba","annaba","GUELMA","Guelma","guelma","CONSTANTINE","Constantine","constantine","MEDEA","Médéa","médéa","medea",
            "MOSTAGANEM","Mostaganem","mostaganem","M'SILA","M'Sila","m'sila","MASCARA","Mascara","mascara","OUARGLA","Ouargla","ouargla",
            "ORAN","Oran","oran","EL BAYADH","El Bayadh","el bayadh","ILLIZI","Illizi","illizi","BORDJ BOU ARRERIJ","Bordj Bou Arrerij",
            "BOUMERDES","Boumerdès","boumerdès","boumerdes","EL TARF","El Tarf","el tarf","TINDOUF","Tindouf","tindouf",
            "TISSEMSILT","Tissemsilt","tissemsilt","EL OUED","El Oued","el oued","KHENCHELA","Khenchela","khenchela",
            "SOUK AHRAZ","Souk Ahraz","souk ahraz","TIPAZA","Tipaza","tipaza","MILA","Mila","mila","AIN DEFLA","Aïn Defla","aïn defla","Ain Defla","ain defla",
            "NAAMA","Naâma","Naama","naâma","naama","AIN TEMOUCHENT","Aïn Témouchent","Ain Témouchent","Ain Temouchent","aïn témouchent","ain témouchent","ain temouchent",
            "GHARDAIA","Ghardaïa","Ghardaia","ghardaïa","ghardaia","RELIZANE","Relizane","relizane"
        };

    private ExtracteurTelephone extracteurTelephone;
    private ExtracteurEmail extracteurEmail;
    private ExtracteurSiteWeb extracteurSiteWeb;
    private ExtracteurLocalization extracteurLocalization;
    private ExtracteurNom extracteurNom;

    public MyTextRecognizer(Context context, Uri imageURI)
    {
        try
        {
            image = FirebaseVisionImage.fromFilePath(context, imageURI);
        }

        catch (IOException e)
        {
            Log.d(TAG,"Erreur instanciation du firebaseVisionImage");
            e.printStackTrace();
        }

        textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        infoLines = new ArrayList<>();
        extracteurTelephone = new ExtracteurTelephone();
        extracteurEmail = new ExtracteurEmail();
        extracteurSiteWeb = new ExtracteurSiteWeb();
        extracteurLocalization = new ExtracteurLocalization();
        extracteurNom = new ExtracteurNom();
    }

    // Remplit les info extrais dans une arraylist
    public void AnalyserImage()
    {
        final Task<FirebaseVisionText> resultat = textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>()
                {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText)
                    {
                        Log.d(TAG," textRecognizer success !");
                        progressBar.setVisibility(View.VISIBLE);
                        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks())
                        {
                            // Récuperation des blocs de texte
                            //Log.i("Block :", block.getText());

                            for (FirebaseVisionText.Line line : block.getLines())
                            {
                                // Récuperations des lignes de texte

                                extraireInformations(line.getText());
                                //extractName(line.getText());
                                //extractEmail(line.getText());
                                //extractMobile(line.getText());
                                //extractWebAdresse(line.getText());
                                //extractLocation(line.getText());

                                infoLines.add(line.getText());
                                Log.i("Ligne : ",line.getText());
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBarLayout.removeView(progressBarLinearLayout);
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d(TAG, "Probleme extraction du text!");

                    }
                });
    }

    public void setWindow(Window window)
    {
        this.window = window;
    }

    public void setProgressBar(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    public void setProgressBarLayout(RelativeLayout progressBarLayout)
    {
        this.progressBarLayout = progressBarLayout;
    }

    public void setProgressBarLinearLayout(LinearLayout progressBarLinearLayout)
    {
        this.progressBarLinearLayout = progressBarLinearLayout;
    }

    private void extractName(String line)
    {
        String webadresse = "(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})";

        Pattern pattern = Pattern.compile(webadresse);
        Matcher matcher = pattern.matcher(line.toLowerCase());

        while (matcher.find())
        {
            String inter = matcher.group();
            String a = inter.replaceAll("www\\." , "");
            String b = a.replaceAll("\\.([a-z]{2,3})","");
            String nomFinal = b.substring(0,1).toUpperCase()+b.substring(1);
            nameView.setText(nomFinal);
        }
    }

    private void extractEmail(String line)
    {
        String email = "[A-Za-z0-9+_.-]+@([a-z|A-Z|0-9|_|\\-|\\.]+)\\.[a-z|A-Z]{2,5}";
        Pattern pattern = Pattern.compile(email);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find())
        {
            emailView.setText(matcher.group());
        }
    }

    private void extractMobile(String line)
    {
        String telNumber = "((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(\\d{8,9})";

        Pattern pattern = Pattern.compile(telNumber);
        Matcher matcher = pattern.matcher(line.replace(" ",""));

        int i = 1;
        while (matcher.find())
        {
            if (i == 1)
            {
                tel1View.setText(CorrectMobile(matcher.group()));
            }

            if (i == 2)
            {
                tel2View.setText(CorrectMobile(matcher.group()));
            }

            i++;
        }
    }

    private void extractWebAdresse(String line)
    {
        String webadresse = "(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})";

        Pattern pattern = Pattern.compile(webadresse);
        Matcher matcher = pattern.matcher(line.toLowerCase());

        while (matcher.find())
        {
            websiteView.setText(matcher.group());
        }
    }

    private void extractLocation(String line)
    {
        int i;

        Pattern pattern;
        Matcher matcher;

        for(i = 0; i < wilaya.length; i++)
        {
            pattern = Pattern.compile(wilaya[i]);
            matcher = pattern.matcher(line);

            while (matcher.find())
            {
                location.setText(line);
            }
        }

        // Code postal numero de wilya + [0-9]{3}
    }

    private String CorrectMobile(String mobileBrute)
    {
        String interm1, interm2, finalMobile = null;
        int methode = 0;

        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("\\+(\\d{2,3})\\(");
        matcher = pattern.matcher(mobileBrute);

        if (matcher.find())
        {
            int indexBegin = mobileBrute.indexOf("(");
            finalMobile = mobileBrute.substring(0,indexBegin) + mobileBrute.substring(indexBegin + 3);
            methode += 1;

            return finalMobile;
        }

        if(mobileBrute.contains("("))
        {
            int indexBegin = mobileBrute.indexOf("(");
            int indexEnd = mobileBrute.indexOf(")");
            finalMobile = mobileBrute.substring(0, indexBegin) + mobileBrute.substring(indexEnd + 1);

            return finalMobile;
        }

        else
        {
            return mobileBrute;
        }
    }

    private void extraireInformations(String line)
    {
        String telephone1, telephone2, fixe, email, siteWeb, localization, nomEntreprise;

        if (line != null)
        {
            extracteurTelephone.extractMobile(line);
            telephone1 = extracteurTelephone.getTelephone();
            telephone2 = extracteurTelephone.getTelephone2();
            fixe = extracteurTelephone.getFixe();

            if (telephone1 != null && tel1View.getText().toString().isEmpty())
            {
                tel1View.setText(extracteurTelephone.getTelephone());
            }

            else if (!tel1View.getText().toString().equals(telephone1) && tel2View.getText().toString().isEmpty())
            {
                tel2View.setText(telephone1);
            }

            if (telephone2 != null && tel2View.getText().toString().isEmpty())
            {
                tel2View.setText(telephone2);
            }

            if (fixe != null)
            {
                fixeView.setText(fixe);
            }

            if (telephone1 != null && fixe != null && fixeView.getText().toString().equals(tel1View.getText().toString()) && !telephone1.equals(fixe))
            {
                tel1View.setText(extracteurTelephone.getTelephone());
            }

            email = extracteurEmail.extractEmail(line);

            if (email != null)
            {
                emailView.setText(email);
            }

            siteWeb = extracteurSiteWeb.extraireSiteWeb(line);

            if (siteWeb != null)
            {
                websiteView.setText(siteWeb);
            }

            localization = extracteurLocalization.extraireLocalization(line);

            if (localization != null)
            {
                location.setText(localization);
            }

            extracteurNom.extraireNom(line);
            nomEntreprise = extracteurNom.getNomEntreprise();

            if (nomEntreprise != null)
            {
                nameView.setText(nomEntreprise);
            }
        }
    }

    public void setNameView(EditText nameView)
    {
        this.nameView = nameView;
    }

    public void setTel1View(EditText tel1View)
    {
        this.tel1View = tel1View;
    }

    public void setTel2View(EditText tel2View)
    {
        this.tel2View = tel2View;
    }

    public void setFixeView(EditText fixeView)
    {
        this.fixeView = fixeView;
    }

    public void setEmailView(EditText emailView)
    {
        this.emailView = emailView;
    }

    public void setWebsiteView(EditText websiteView)
    {
        this.websiteView = websiteView;
    }

    public void setAdditionalInfoView(EditText additionalInfoView)
    {
        this.additionalInfoView = additionalInfoView;
    }

    public void setLocation(EditText location)
    {
        this.location = location;
    }
}
