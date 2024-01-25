package com.project.jeu421.entity;

import javax.persistence.*;

@Entity
@Table(name = "game")

public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom_game")
    private String nomGame;

    @Column(name = "num_charge")
    private int numCharge;

    @Column(name = "num_decharge")
    private int numDecharge;

    public Long getId() {
        return id;
    }

    public String getNomGame() {
        return nomGame;
    }

    public void setNomGame(String nomGame) {
        this.nomGame = nomGame;
    }

    public int getNumCharge() {
        return numCharge;
    }

    public void setNumCharge(int numCharge) {
        this.numCharge = numCharge;
    }

    public int getNumDecharge() {
        return numDecharge;
    }

    public void setNumDecharge(int numDecharge) {
        this.numDecharge = numDecharge;
    }
}
