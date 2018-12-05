/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Spy
 */
public class TournamentFacade extends Observable {

    public enum TypeNotif {
        INIT, TOURNAMENT_SELECTED, PLAYER_ONE_SELECTED, PLAYER_TWO_SELECTED, ADD_MATCH, REMOVE_MATCH
    }

    public List<Tournament> tournamentList = new ArrayList<>();

    public int indexValue = 0;

    Player p1 = new Player("Philippe");
    Player p2 = new Player("Khadija");
    Player p3 = new Player("spyridon");
    Player p4 = new Player("chaffi");
    Player p5 = new Player("lindsay");
    Player p6 = new Player("rodolphe");
    Match m = new Match(p1, p2, RESULTS.DRAW);
    Match m2 = new Match(p2, p1, RESULTS.DRAW);
    Match m3 = new Match(p1, p4, RESULTS.DRAW);
    Match m4 = new Match(p4, p1, RESULTS.DRAW);
    Match m5 = new Match(p6, p2, RESULTS.DRAW);
    Match m6 = new Match(p3, p6, RESULTS.DRAW);
    public Player p = p1;
    public Player actual;

    public TournamentFacade() {
        Tournament t1 = new Tournament("E-Sport");
        Tournament t2 = new Tournament("T2");

        addPlayersT1(t1);
        addPlayerst2(t2);
        addMatch1(t1);
        addMatch2(t2);
        tournamentList.add(t1);
        tournamentList.add(t2);
    }

    public Tournament getTournois() {
        return tournamentList.get(indexValue);

    }

    public void setIndex(int index) {
        this.indexValue = index;
        notif(TypeNotif.TOURNAMENT_SELECTED);
    }

    public void SetPlayer(Player player) {
        this.actual = player;
        notif(TypeNotif.PLAYER_ONE_SELECTED);

    }
    
    public void createNewMatch(Player p1, Player p2, RESULTS res){
        Match m = new Match(p1, p2, res);
        System.out.println(m);
        getTournois().addMatch(m);
        notif(TypeNotif.ADD_MATCH);
    }

    public List<Player> getValidPlayerList() {
        List<Player> inscrit = getTournois().getSubscribersList();
        List<Player> list = inscrit.stream()
                .filter(p -> !addOpponentInvalidList()
                .contains(p)).collect(toList());

        return list;

    }
    
    public void removeMatch(Match m){
    getTournois().getMatchList().remove(m);
    notif(TypeNotif.REMOVE_MATCH);
   
    }

    // ajouter les match deja jouer par le player p dans matchPlayed.
    public List<Match> addMatchPlayed() {
        List<Match> matchPlayed = new ArrayList<>();
        for (Match m : getTournois().getMatchList()) {
            if (m.getPlayer1() == actual || m.getPlayer2() == actual) {
                matchPlayed.add(m);
            }
        }

        return matchPlayed;
    }

    // ajouter les adversaire de player p dans la liste opponentsListInvalid(les joueur qui n'ont deja jouer contre player p et recois aussi la liste des matchs deja jouer par player p)
    public List<Player> addOpponentInvalidList() {
        List<Player> playerInvalid = new ArrayList<>();
        for (Match m : addMatchPlayed()) {
            if (!m.getPlayer1().equals(actual)) {
                playerInvalid.add(m.getPlayer1());
            }
            if (!m.getPlayer2().equals(actual)) {
                playerInvalid.add(m.getPlayer2());

            }
        }
        return playerInvalid;
    }

    public List<Player> addOppponentValidList() {
        List<Player> playerValid = new ArrayList<>();
        for (Player s : getTournois().getSubscribersList()) {
            if (!addOpponentInvalidList().contains(s) && !s.equals(actual)) {
                playerValid.add(s);
            }
        }
        return playerValid;
    }

    public void add(Tournament tournois) {
        tournamentList.add(tournois);
    }

    public Tournament getTournamantList(String name) {
        Tournament value = null;
        for (int i = 0; i <= tournamentList.size() - 1; ++i) {
            if (tournamentList.get(i).equals(name)) {
                value = tournamentList.get(i);
            }
        }
        return value;
//        if (tournamentList.contains(t)) {
//            return t;
//        }
//        return null;

    }

    public void addPlayersT1(Tournament tournois) {
        tournois.addPlayer(p1);
        tournois.addPlayer(p2);
        tournois.addPlayer(p3);
        tournois.addPlayer(p4);
        tournois.addPlayer(p5);
        tournois.addPlayer(p6);

    }

    public void addPlayerst2(Tournament tournois) {
        tournois.addPlayer(p1);
        tournois.addPlayer(p2);
        tournois.addPlayer(p3);

    }

    public void addMatch1(Tournament tournois) {
        tournois.addMatch(m);
        tournois.addMatch(m2);
        tournois.addMatch(m3);
        tournois.addMatch(m4);
        tournois.addMatch(m5);
        tournois.addMatch(m6);
    }

    public void addMatch2(Tournament tournois) {
        tournois.addMatch(m);
        tournois.addMatch(m2);
        tournois.addMatch(m3);

    }

    public List<Tournament> getTournamentList() {
        return tournamentList;
    }

    public List<Player> getSubscrib() {
        Tournament tournois = getTournois();
        return tournois.getSubscribersList();
    }

    public Set<Match> getMatchList() {
        Tournament tournois = getTournois();
        return tournois.getMatchList();

    }

    public Player createPlayer(String name) {
        return new Player(name);
    }

    public Tournament createTournament(String name) {
        return new Tournament(name);

    }

    public void notif(TypeNotif typeNotif) {
        setChanged();// cette methode renvoi un boullean et permet de faire des notif uniquement quand un changement a eux lieux.
        notifyObservers(typeNotif);
    }

}
