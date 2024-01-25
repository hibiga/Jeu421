package com.project.jeu421.entity;

import javax.persistence.*;

@Entity
@Table(name = "game_round")
public class GameRoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_game")
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "id_player")
    private PlayerEntity player;

    @Column(name = "dice_val_1")
    private int diceValue1;

    @Column(name = "dice_val_2")
    private int diceValue2;

    @Column(name = "dice_val_3")
    private int diceValue3;

    @Column(name = "nb_round")
    private int nbRound;

    @Column(name = "phase")
    private int phase;

    @Column(name = "relancement")
    private int relancement;

    public Long getId() {
        return id;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public int getDiceValue1() {
        return diceValue1;
    }

    public void setDiceValue1(int diceValue1) {
        this.diceValue1 = diceValue1;
    }

    public int getDiceValue2() {
        return diceValue2;
    }

    public void setDiceValue2(int diceValue2) {
        this.diceValue2 = diceValue2;
    }

    public int getDiceValue3() {
        return diceValue3;
    }

    public void setDiceValue3(int diceValue3) {
        this.diceValue3 = diceValue3;
    }

    public int getNbRound() {
        return nbRound;
    }

    public void setNbRound(int nbRound) {
        this.nbRound = nbRound;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getRelancement() {
        return relancement;
    }

    public void setRelancement(int relancement) {
        this.relancement = relancement;
    }
}
