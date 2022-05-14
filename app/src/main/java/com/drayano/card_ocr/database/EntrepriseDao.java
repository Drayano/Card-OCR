package com.drayano.card_ocr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntrepriseDao
{
    @Query("SELECT * FROM entreprise")
    LiveData<List<Entreprise>> getAll();

    @Query("SELECT * FROM entreprise")
    List<Entreprise> getAllCsv();

    @Query("SELECT * FROM entreprise WHERE partenariat LIKE 'oui'")
    LiveData<List<Entreprise>> getAllPart();

    @Insert
    void insertEntreprise(Entreprise entreprise);

    @Delete
    void deleteEntreprise(Entreprise entreprise);

    @Query("SELECT COUNT (*) FROM entreprise WHERE partenariat LIKE 'oui'")
    LiveData<Integer> getNumberPartner();

    @Query("SELECT COUNT (*) FROM entreprise")
    LiveData<Integer> getNubmerOfEntreprise();

    @Query("SELECT * FROM entreprise WHERE entrepriseId LIKE :id")
    LiveData<Entreprise> getEntreprise(int id);

    @Query("SELECT * FROM entreprise WHERE entrepriseId LIKE :id")
    Entreprise getEntrepriseParId(int id);

    @Update
    void updateEntreprise(Entreprise entreprise);
}
