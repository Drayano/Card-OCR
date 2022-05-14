package com.drayano.card_ocr.traitement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurNom
{
    private String nomEntreprise;
    private ExtracteurSiteWeb extracteurSiteWeb;

    private String regexNom = "^([a-z|A-Z]{3,9}\\s){1,2}$";
    private String[] noms =
        {
                "drayano","djawed","nabil","mohammed","mohamed","mohammad","houari","aness","adam",
        };

    public ExtracteurNom()
    {
        extracteurSiteWeb = new ExtracteurSiteWeb();
    }

    public void extraireNom(String line)
    {
        Pattern pattern = Pattern.compile(regexNom);
        Matcher matcher = pattern.matcher(line);
        String siteWeb = extracteurSiteWeb.extraireSiteWeb(line);

        if (siteWeb != null && !siteWeb.isEmpty())
        {
            String a = siteWeb.replaceAll("www\\." , "");
            String b = a.replaceAll("\\.([a-z]{2,3})","");
            nomEntreprise = b.substring(0,1).toUpperCase()+b.substring(1);
        }

        if (nomEntreprise == null || nomEntreprise.isEmpty())
        {
            while (matcher.find())
            {
                nomEntreprise = matcher.group();
            }
        }
    }

    public String getNomEntreprise()
    {
        return nomEntreprise;
    }
}
