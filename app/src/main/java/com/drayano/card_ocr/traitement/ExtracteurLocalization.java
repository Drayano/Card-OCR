package com.drayano.card_ocr.traitement;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurLocalization
{
    private String localization = "";
    private String[] ville =
        {
            "Adrar","adrar","ADRAR","CHLEF","Chlef","chlef","LAGHOUAT","Laghouat","laghouat",
            "OUM EL BOUAGHI","Oum El Bouaghi","oum el bouaghi","BATNA","Batna","batna","BEJAIA","Béjaia","béjaia","bejaia","Béjaïa","béjaïa",
            "BISKRA","Biskra","biskra","BECHAR","Béchar","Bechar","béchar","bechar","BLIDA","Blida","blida",
            "BOUIRA","Bouira","bouira","TAMANRASSET","Tamanrasset","tamanrasset","TEBESSA","Tébessa","Tebessa","tebessa",
            "TLEMCEN","Tlemcen","tlemcen","TIARET","Tiaret","tiaret","TIZI OUZOU","Tizi Ousou","tizi ousou",
            "ALGER$","Alger$","alger$","DJELFA","Djelfa","djelfa","JIJEL","Jijel","jijel","SETIF","Sétif","Setif","setfi",
            "SAIDA","Saida","saida","saïda","SKIKDA","Skikda","skikda","SIDI BEL ABBES","Sidi Bel Abbèss","Sidi Bel Abbes","sidi bel abbes","sidi bel abbès",
            "ANNABA","Annaba","annaba","GUELMA","Guelma","guelma","CONSTANTINE","Constantine","constantine","MEDEA","Médéa","médéa","medea",
            "MOSTAGANEM","Mostaganem","mostaganem","M'SILA","M'Sila","m'sila","MASCARA","Mascara","mascara","OUARGLA","Ouargla","ouargla",
            "ORAN","Oran","oran","EL BAYADH","El Bayadh","el bayadh","ILLIZI","Illizi","illizi","BORDJ BOU ARRERIJ","Bordj Bou Arrerij",
            "BOUMERDES","Boumerdès","boumerdès","boumerdes","EL TARF","El Tarf","el tarf","TINDOUF","Tindouf","tindouf",
            "TISSEMSILT","Tissemsilt","tissemsilt","EL OUED","El Oued","el oued","KHENCHELA","Khenchela","khenchela",
            "SOUK AHRAZ","Souk Ahraz","souk ahraz","TIPAZA","Tipaza","tipaza","MILA","Mila","mila","AIN DEFLA","Aïn Defla","aïn defla","Ain Defla","ain defla",
            "NAAMA","Naâma","Naama","naâma","naama","AIN TEMOUCHENT","Aïn Témouchent","Ain Témouchent","Ain Temouchent","aïn témouchent","ain témouchent","ain temouchent",
            "GHARDAIA","Ghardaïa","Ghardaia","ghardaïa","ghardaia","RELIZANE","Relizane","relizane","paris","tunisie$"
        };

    private String regexLoc = "(N°)?(\\d{2,5})?(\\s|,|-)?(Street|rue|cite|cité|boulevard|avenue|hai)(.+)";

    @Nullable
    public String extraireLocalization(String textBrut)
    {
        int i;
        Pattern pattern;
        Matcher matcher, verificateur;

        for (i = 0; i < ville.length ; i++)
        {
            pattern = Pattern.compile(regexLoc+"("+ville[i]+")"+"?", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(textBrut);

            while (matcher.find())
            {
                if (localization.isEmpty() )
                {
                    localization = textBrut;
                }
            }
        }

        for (i = 0; i < ville.length; i++)
        {
            pattern = Pattern.compile(ville[i], Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(textBrut);

            while (matcher.find())
            {
                boolean existe = false;

                if (localization.isEmpty())
                {
                    localization = textBrut;
                }

                if (localization != null && localization.length() > 0)
                {
                    verificateur = pattern.matcher(localization);

                    while (verificateur.find())
                    {
                        existe = true;
                    }

                    if (!existe)
                    {
                        localization = localization +" "+ textBrut;
                    }
                }
            }
        }

        return localization ;
    }

    public String getLocalization()
    {
        return localization;
    }
}
