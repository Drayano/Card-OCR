package com.drayano.card_ocr.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Entreprise
{
    @PrimaryKey(autoGenerate = true)
    private int entrepriseId;

    private String name;
    private String location;
    private String email;
    private String website;
    private String motsCles;
    private String secteurActivites;
    private String facebook;
    private String fax;

    @ColumnInfo(name = "first_mobile")
    private String firstMobile;

    @ColumnInfo(name = "second_mobile")
    private String secondMobile;

    @ColumnInfo(name = "tel_fixe")
    private String telFixe;

    @ColumnInfo(name = "info_add")
    private String infoAdditionnelles;

    @ColumnInfo(name = "note")
    private float note;

    @ColumnInfo(name = "partenariat")
    private String partenariat;

    private String ImgPath;
    private String Wilaya;
    private String commune;
    private String contact;

    public Entreprise(String name, String email, String website, String firstMobile, String secondMobile, String telFixe, String infoAdditionnelles, float note, String partenariat)
    {
        this.name = name;
        this.email = email;
        this.website = website;
        this.firstMobile = firstMobile;
        this.secondMobile = secondMobile;
        this.telFixe = telFixe;
        this.infoAdditionnelles = infoAdditionnelles;
        this.note = note;
        this.partenariat = partenariat;
    }

    @Ignore
    public Entreprise()
    {

    }


    public int getEntrepriseId()
    {
        return entrepriseId;
    }

    public void setEntrepriseId(int id)
    {
        this.entrepriseId = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String site)
    {
        this.website = site;
    }

    public String getFirstMobile()
    {
        return firstMobile;
    }

    public void setFirstMobile(String firstPhone)
    {
        this.firstMobile = firstPhone;
    }

    public String getSecondMobile()
    {
        return secondMobile;
    }

    public void setSecondMobile(String secondPhone)
    {
        this.secondMobile = secondPhone;
    }

    public String getTelFixe()
    {
        return telFixe;
    }

    public void setTelFixe(String thirdPhone)
    {
        this.telFixe = thirdPhone;
    }

    public String getInfoAdditionnelles()
    {
        return infoAdditionnelles;
    }

    public void setInfoAdditionnelles(String infoAdditionnelles)
    {
        this.infoAdditionnelles = infoAdditionnelles;
    }

    public void setWilaya(String wilaya)
    {
        Wilaya = wilaya;
    }

    public String getWilaya()
    {
        return Wilaya;
    }

    public void setImgPath(String imgPath)
    {
        ImgPath = imgPath;
    }

    public String getImgPath()
    {
        return ImgPath;
    }

    public float getNote()
    {
        return note;
    }

    public void setNote(float notation)
    {
        this.note = notation;
    }

    public String getPartenariat()
    {
        return partenariat;
    }

    public void setPartenariat(String partenariat)
    {
        this.partenariat = partenariat;
    }

    public String getMotsCles()
    {
        return motsCles;
    }

    public String getSecteurActivites()
    {
        return secteurActivites;
    }

    public String getFacebook()
    {
        return facebook;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public void setMotsCles(String motsCles)
    {
        this.motsCles = motsCles;
    }

    public void setSecteurActivites(String secteurActivites)
    {
        this.secteurActivites = secteurActivites;
    }

    public void setFacebook(String facebook)
    {
        this.facebook = facebook;
    }

    public String getCommune()
    {
        return commune;
    }

    public void setCommune(String commune)
    {
        this.commune = commune;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    @Override
    public String toString()
    {
        return
                 name +
                "_" + location +
                "_" + email +
                "_" + website +
                "_" + firstMobile +
                "_" + secondMobile +
                "_" + telFixe;
    }

    public String getTelString()
    {
        String telString ="";

        if (this.firstMobile != null)
        {
            telString = firstMobile;
        }

        if (this.secondMobile != null)
        {
            telString = telString + "," + secondMobile;
        }

        return telString;
    }

    public void setDoubleTel(String doubleTel)
    {
        if (!doubleTel.isEmpty())
        {
            if (doubleTel.contains(","))
            {
                String[] telephones = doubleTel.split(",",2);
                setFirstMobile(telephones[0]);
                setSecondMobile(telephones[1]);

            }

            else
            {
                setFirstMobile(doubleTel);
            }
        }
    }
}
