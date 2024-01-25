package com.project.jeu421.entity;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class PlayerEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "ville")
    private String ville;

    @Column(name = "nb_game")
    private float nbGame;

    @Column(name = "nb_win")
    private float nbWin;

    @Column(name = "mean_win")
    private float meanWin;

    @Column(name = "mean_jet_charge")
    private float meanJetCharge;

    @Column(name = "mean_jet_decharge")
    private float meanJetDecharge;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public float getNbGame() {
        return nbGame;
    }

    public void setNbGame(float nbGame) {
        this.nbGame = nbGame;
    }

    public float getNbWin() {
        return nbWin;
    }

    public void setNbWin(float nbWin) {
        this.nbWin = nbWin;
    }

    public float getMeanWin() {
        return meanWin;
    }

    public void setMeanWin(float meanWin) {
        this.meanWin = meanWin;
    }

    public float getMeanJetCharge() {
        return meanJetCharge;
    }

    public void setMeanJetCharge(float meanJetCharge) {
        this.meanJetCharge = meanJetCharge;
    }

    public float getMeanJetDecharge() {
        return meanJetDecharge;
    }

    public void setMeanJetDecharge(float meanJetDecharge) {
        this.meanJetDecharge = meanJetDecharge;
    }
}
