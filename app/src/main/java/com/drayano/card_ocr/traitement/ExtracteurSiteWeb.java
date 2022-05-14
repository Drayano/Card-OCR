package com.drayano.card_ocr.traitement;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurSiteWeb
{
    private String regexSiteWeb = "(www\\.)([a-zA-Z0-9]\\-?)+\\.([a-z]{2,3})";
    private String siteWeb;

    @Nullable
    public String extraireSiteWeb(String line)
    {
        Pattern pattern = Pattern.compile(regexSiteWeb);
        Matcher matcher = pattern.matcher(line.toLowerCase());

        while (matcher.find())
        {
            siteWeb = matcher.group();
        }

        return siteWeb;
    }

    public String getSiteWeb()
    {
        return siteWeb;
    }
}
