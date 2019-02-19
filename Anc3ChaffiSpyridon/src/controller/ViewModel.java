package controller;

import java.io.FileNotFoundException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import model.Match;
import model.Player;
import model.Question;
import model.Tournament;
import model.TournamentFacade;
import model.RESULTS;
import view.PopUpDelete;
import view.ViewGamePlayer1;
import view.ViewGamePlayer2;

/**
 *
 * @author Spy
 */
public final class ViewModel {

    TournamentFacade facade;
    private ListProperty<Player> subscribeList;
    private ObservableList<Player> oppList = FXCollections.observableArrayList();
    public IntegerProperty indexTournament = new SimpleIntegerProperty();
    private ObjectProperty<Player> actualPlayer = new SimpleObjectProperty<Player>();
    private ObjectProperty<Player> cb1 = new SimpleObjectProperty<>();
    private ObjectProperty<Player> cb2 = new SimpleObjectProperty<>();
    private ObjectProperty<String> cb3 = new SimpleObjectProperty<>();
    private IntegerProperty indexMatch = new SimpleIntegerProperty();
    public ObjectProperty<Match> matchSelected = new SimpleObjectProperty<>();
    private ObservableList<Question> selectedQuestionList = FXCollections.observableArrayList();
    private ObjectProperty<Question> selectedQuestion = new SimpleObjectProperty<>();
    private IntegerProperty IndexQuestion = new SimpleIntegerProperty();
    private IntegerProperty pointTotaux = new SimpleIntegerProperty();
    private IntegerProperty cptPoint = new SimpleIntegerProperty();
    private final int MAX_POINT = 10;
    private StringProperty questionName = new SimpleStringProperty();
    private StringProperty questionPoint = new SimpleStringProperty();
    public StringProperty res1 = new SimpleStringProperty();
    public StringProperty res2 = new SimpleStringProperty();
    public StringProperty res3 = new SimpleStringProperty();
    public StringProperty res4 = new SimpleStringProperty();
    private BooleanProperty disable = new SimpleBooleanProperty();
    public ObjectProperty<Question> currentQuestion = new SimpleObjectProperty<>();
    public IntegerProperty cptFillQuestions = new SimpleIntegerProperty();
    public IntegerProperty indexQuestion = new SimpleIntegerProperty();

    public ViewModel(TournamentFacade facade) {
        this.facade = facade;
        addPoint();
        System.out.println(pointTotaux.get());
    }

    public StringProperty getRes1() {
        return res1;
    }

    public StringProperty getRes2() {
        return res2;
    }

    public StringProperty getRes3() {
        return res3;
    }

    public StringProperty getRes4() {
        return res4;
    }

    public IntegerProperty cptPointProperty() {
        return cptPoint;
    }

    public IntegerProperty pointTotauxProperty() {
        return pointTotaux;
    }

    public IntegerProperty getIndexQuestion() {
        return IndexQuestion;
    }

    public ObjectProperty<Question> getSelectedQuestion() {
        return selectedQuestion;
    }

    public StringProperty questionNameProperty() {
        return questionName;
    }

    public StringProperty questionPointProperty() {
        return questionPoint;
    }

    public SimpleListProperty<Question> selectedQuestionProperty() {
        return new SimpleListProperty<>(selectedQuestionList);
    }

    public ObservableList<Question> selectedQuestionList() {
        return selectedQuestionList;
    }

    public void setAttributQuetion(Question q) {
        this.questionName.set(q.getName().get());
        this.questionPoint.set(q.pointsProperty().getValue().toString());
        setReponse(q);
        this.currentQuestion.set(q);
    }

    public void setReponse(Question q) {
        res1.set(q.getResponses().get(0));
        res2.set(q.getResponses().get(1));
        res3.set(q.getResponses().get(2));
        res4.set(q.getResponses().get(3));
    }

    public void addQuestionforOpp(Question q) {
        if (cptPoint.get() + q.pointsProperty().get() <= MAX_POINT && !selectedQuestionList.contains(q)) {
            selectedQuestion.set(q);
            if (!selectedQuestionList.contains(getSelectedQuestion().get()) && getSelectedQuestion().get() != null) {
                this.selectedQuestionList.add(getSelectedQuestion().get());
                cptPoint.set(cptPoint.get() + q.pointsProperty().get());
                cptFillQuestions.set(selectedQuestionList.size());
            }
            pointTotaux.set(pointTotaux.get() - q.pointsProperty().get());
            setReponse(selectedQuestion.get());
        }

    }

    public void deleteQuestionForOpp(Question q) {
        if (selectedQuestionList.contains(q)) {
            selectedQuestion.set(q);
            cptPoint.set(cptPoint.get() - selectedQuestion.get().pointsProperty().get());
            this.selectedQuestionList.remove(q);
            pointTotaux.set(pointTotaux.get() + q.pointsProperty().get());
            cptFillQuestions.set(selectedQuestionList.size());
        }
    }

    public SimpleListProperty<Question> quetionsProperty() {
        return new SimpleListProperty<>(facade.getQuestion());
    }

    public void setTournois() {
        facade.indexTournamentProperty().set(indexTournament.get());
    }

    public void clearOppList() {
        this.oppList().clear();
    }

    public ObservableList<Player> oppList() {
        return this.oppList;
    }

    public SimpleListProperty<Player> subscribesListProperty() {
        return new SimpleListProperty<>(facade.getTournamentSubsList());
    }

    public ObjectProperty<Player> combobox1Property() {
        return cb1;
    }

    public ObjectProperty<Player> combobox2Property() {
        return cb2;
    }

    public ObjectProperty<String> combobox3Property() {
        return cb3;
    }

    public SimpleIntegerProperty indexMatchProperty() {
        return new SimpleIntegerProperty(indexMatch.get());
    }

    public ObjectProperty<Match> matchSelectedProperty() {
        return matchSelected;
    }

    public SimpleListProperty<Player> opponentsListProperty() {
        return new SimpleListProperty<Player>(this.oppList);
    }

    public SimpleListProperty<Match> matchsProperty() {
        return new SimpleListProperty<Match>(facade.getMatchList());
    }

    public SimpleListProperty<Tournament> tournamantProperty() {
        return new SimpleListProperty<Tournament>(facade.getTournamentList());
    }

    public TournamentFacade getFacade() {
        return facade;
    }

    public ObjectProperty<Player> actualProperty() {
        return actualPlayer;
    }

    public ObservableList<Match> getAllMatch() {
        return facade.getTournament().getMatchList();
    }

    public IntegerProperty indexTournamentProperty() {
        return this.indexTournament;
    }

    public void setTournamant(int index) {
        this.indexTournamentProperty().set(index);
    }

    public Tournament getTournament() {
        return facade.getTournament();
    }

    public void launchPopUp() throws FileNotFoundException {
        new PopUpDelete(matchSelected.get(), this);
    }

    public void launchGame(Player p1, Player p2) throws Exception {

        new ViewGamePlayer1(this, p1, p2);
    }

    public void launchPlay(Player p1,Player p2) throws Exception {
         cptPointProperty().set(0);
         cptFillQuestions.set(1);
        new ViewGamePlayer2(this, selectedQuestionList,p1,p2);
        if (indexQuestion.get() <= selectedQuestionList.size()) {
            afficheQuestion();
            indexQuestion.set(indexQuestion.get() + 1);

        }

    }

    public void createMatch() {
        Match m = new Match(new Player(cb1.getValue().toString()),
                new Player(cb2.getValue().toString()),
                results(cb3.getValue().toString()));
        if (!matchsProperty().contains(m)) {
            facade.getTournament().addMatch(m);
        }
    }

    public void removeMatch() {
        if (this.indexMatch.get() == 0) {
            this.getTournament().getMatchList().remove(this.matchSelected.get());
        } else {
            this.getTournament().getMatchList().remove(this.matchSelected.get());
        }
    }

    private RESULTS results(String res) {
        if (res.equals(RESULTS.VAINQUEUR_J1.name())) {
            return RESULTS.VAINQUEUR_J1;
        }
        if (res.equals(RESULTS.VAINQUEUR_J2.name())) {
            return RESULTS.VAINQUEUR_J2;
        }
        if (res.equals(RESULTS.EX_AEQUO.name())) {
            return RESULTS.EX_AEQUO;
        }
        return null;
    }

    public ObservableList<Match> addMatchPlayed() {
        ObservableList<Match> matchPlayed = FXCollections.observableArrayList();
        for (Match m : matchsProperty()) {
            if (m.getPlayer1().getFirstName().equals(actualPlayer.get().toString())
                    || m.getPlayer2().getFirstName().equals(actualPlayer.get().toString())) {
                matchPlayed.add(m);
            }
        }
        return matchPlayed;
    }

    private boolean isTheOpponent(String p) {
        return !p.equals(actualPlayer.getValue().toString());
    }

    //ajouter les adversaire de player p dans la liste opponentsListInvalid(les joueur qui n'ont deja jouer contre player p et recois aussi la liste des matchs deja jouer par player p)
    public ObservableList<Player> addOpponentInvalidList() {
        ObservableList<Player> playerInvalid = FXCollections.observableArrayList();
        for (Match m : addMatchPlayed()) {
            if (isTheOpponent(m.getPlayer1().getFirstName())) {
                playerInvalid.add(m.getPlayer1());
            }
            if (isTheOpponent(m.getPlayer2().getFirstName())) {
                playerInvalid.add(m.getPlayer2());
            }
        }
        return playerInvalid;
    }

    public void oppValidList() {
        ObservableList<Player> playerValid = FXCollections.observableArrayList();
        ObservableList<Player> list2 = addOpponentInvalidList();
        oppList.clear();
        for (Player s : subscribesListProperty()) {
            if (!list2.contains(s) && !s.getFirstName().equals(actualPlayer.getValue().toString())) {
                this.oppList.add(s);
            }

        }
    }

    public void addPoint() {
        for (Question x : facade.getQuestion()) {
            pointTotaux.set(pointTotaux.get() + x.pointsProperty().get());
        }
    }

    public void emptyselectedList() {
        selectedQuestionList.clear();
    }

    public void afficheQuestion() {
        if (indexQuestion.get() < 0) {
            indexQuestion.set(0);
            setAttributQuetion(selectedQuestionList.get(indexQuestion.get()));
        } else if (indexQuestion.get() < selectedQuestionList.size()) {
            setAttributQuetion(selectedQuestionList.get(indexQuestion.get()));
        }

    }

    public void nextQuestion(String t) {
        if (indexQuestion.get() < selectedQuestionList.size()) {
            afficheQuestion();
            if (rightResponse(t)) {
                AjouterPointQuestion();
            }
            cptFillQuestions.set(cptFillQuestions.get()+1);
            indexQuestion.set(indexQuestion.get() + 1);
        }

    }

    public boolean rightResponse(String s) {
        int r = selectedQuestionList.get(indexQuestion.get() - 1).getNumCorrectResponse().get();
        String st = selectedQuestionList.get(indexQuestion.get() - 1).getResponses().get(r - 1);
        return st.equals(s);
    }

    public void AjouterPointQuestion() {
        Question q = selectedQuestionList.get(indexQuestion.get()-1);
        cptPointProperty().set(cptPointProperty().get() + q.getPoints());
    }
}
