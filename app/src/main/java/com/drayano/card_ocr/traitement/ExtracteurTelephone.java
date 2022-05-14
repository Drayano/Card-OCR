package com.drayano.card_ocr.traitement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurTelephone
{
    private String regexTel = "((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?(((\\.|-)?\\d){5,15})";
    private String telephone, telephone2, fixe;
    private String[] indicatif =
        {
            "21","23","24","25","26","27","29","31","32","33","34","35","36","37","38","41","43","45","46", "48","49"
        };
    private String regexFixPrefix = "((\\+)?(\\d{2,3}))?(\\(?(0)(\\)|1)?)?";
    private String regexFixSufix = "(\\d{6})";

    public void extractMobile(String textBrut)
    {
        Pattern pattern = Pattern.compile(regexTel);
        Matcher matcher = pattern.matcher(textBrut.replace(" ",""));

        int i = 1;
        while (matcher.find())
        {
            if (i == 1)
            {
                telephone = CorrectMobile(matcher.group());
            }

            if (i == 2)
            {
                telephone2 = CorrectMobile(matcher.group());
            }

            i++;
        }

        for (i=1 ; i < indicatif.length ; i++)
        {
            pattern = Pattern.compile(regexFixPrefix+indicatif[i]+regexFixSufix);
            matcher = pattern.matcher(textBrut.replace(" ",""));

            while (matcher.find())
            {
                fixe = CorrectMobile(matcher.group());
            }
        }
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

        if (mobileBrute.contains("("))
        {
            int indexBegin = mobileBrute.indexOf("(");
            int indexEnd = mobileBrute.indexOf(")");
            finalMobile = mobileBrute.substring(0, indexBegin) + mobileBrute.substring(indexEnd + 1);

            return finalMobile;
        }

        if (mobileBrute.contains(".") || mobileBrute.contains("-"))
        {
            finalMobile = mobileBrute.replaceAll("(\\.|-)","");
            return finalMobile;
        }

        else
        {
            return mobileBrute;
        }
    }

    public String getTelephone()
    {
        return telephone;
    }

    public String getTelephone2()
    {
        return telephone2;
    }

    public String getFixe()
    {
        return fixe;
    }
}


