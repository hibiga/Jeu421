package com.project.jeu421.entity;

import javax.persistence.*;

@Entity
@Table(name = "game_player")
public class GamePlayerEntity {
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

    @Column(name = "nb_jet_charge")
    private int nbJetCharge;

    @Column(name = "nb_jet_decharge")
    private int nbJetDecharge;

    @Column(name = "status")
    private int status;

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

    public int getNbJetCharge() {
        return nbJetCharge;
    }

    public void setNbJetCharge(int nbJetCharge) {
        this.nbJetCharge = nbJetCharge;
    }

    public int getNbJetDecharge() {
        return nbJetDecharge;
    }

    public void setNbJetDecharge(int nbJetDecharge) {
        this.nbJetDecharge = nbJetDecharge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
