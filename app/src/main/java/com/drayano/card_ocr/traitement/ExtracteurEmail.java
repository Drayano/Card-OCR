package com.drayano.card_ocr.traitement;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurEmail
{
    private String regexEmail = "[A-Za-z0-9+_.-]+@([a-zA-Z0-9]\\-?)+\\.[a-z|A-Z]{2,5}";
    private String email;

    @Nullable
    public String extractEmail(String line)
    {
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find())
        {
            email = matcher.group();
        }

        return email;
    }

    public String getEmail()
    {
        return email;
    }
}
