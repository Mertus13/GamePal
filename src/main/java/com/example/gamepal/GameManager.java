package com.example.gamepal;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Classe représentant un jeu de société
class Jeu {
    private String nom;
    private String type;
    private int nombreDeJoueurs;
    private int duree;
    private String description;

    public Jeu(String nom, String type, int nombreDeJoueurs, int duree, String description) {
        this.nom = nom;
        this.type = type;
        this.nombreDeJoueurs = nombreDeJoueurs;
        this.duree = duree;
        this.description = description;
    }

    public String getNom() { return nom; }
    public String getType() { return type; }
    public int getNombreDeJoueurs() { return nombreDeJoueurs; }
    public int getDuree() { return duree; }
    public String getDescription() { return description; }

    public void setNom(String nom) { this.nom = nom; }
    public void setType(String type) { this.type = type; }
    public void setNombreDeJoueurs(int nombreDeJoueurs) { this.nombreDeJoueurs = nombreDeJoueurs; }
    public void setDuree(int duree) { this.duree = duree; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return nom + " (" + type + ")";
    }
}

// Classe représentant un joueur
class Joueur {
    private String nom;
    private int score;

    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
    }

    public String getNom() { return nom; }
    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    @Override
    public String toString() {
        return nom + " (Score: " + score + ")";
    }
}

// Classe représentant une session de jeu
class SessionDeJeu {
    private Jeu jeu;
    private List<Joueur> joueurs;
    private LocalDate dateSession;
    private List<Integer> scores;

    public SessionDeJeu(Jeu jeu, List<Joueur> joueurs, List<Integer> scores) {
        this.jeu = jeu;
        this.joueurs = joueurs;
        this.dateSession = LocalDate.now();
        this.scores = scores;
    }

    public Jeu getJeu() { return jeu; }
    public List<Joueur> getJoueurs() { return joueurs; }
    public LocalDate getDateSession() { return dateSession; }
    public List<Integer> getScores() { return scores; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Session de Jeu: ").append(jeu.getNom()).append(" le ").append(dateSession).append("\n");
        for (int i = 0; i < joueurs.size(); i++) {
            sb.append(joueurs.get(i).getNom()).append(" - Score: ").append(scores.get(i)).append("\n");
        }
        return sb.toString();
    }
}

// Classe principale de l'application
public class GameManager extends Application {

    private ObservableList<Jeu> jeux = FXCollections.observableArrayList();
    private ObservableList<Joueur> joueurs = FXCollections.observableArrayList();
    private List<SessionDeJeu> sessionsDeJeu = new ArrayList<>();
    private ListView<Jeu> listViewJeux = new ListView<>();
    private ListView<Joueur> listViewJoueurs = new ListView<>();
    private ListView<String> listViewSessions = new ListView<>();
    private TextField rechercheField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestionnaire de Jeux de Société");

        Label label = new Label("Bienvenue dans GamePal !");
        Button buttonAjouterJeu = new Button("Ajouter un jeu");
        Button buttonModifierJeu = new Button("Modifier le jeu sélectionné");
        Button buttonSupprimerJeu = new Button("Supprimer le jeu sélectionné");
        Button buttonAjouterJoueur = new Button("Ajouter un joueur");
        Button buttonAjouterSession = new Button("Ajouter une session de jeu");
        Button buttonAfficherHistorique = new Button("Afficher l'historique des parties");
        Button buttonAfficherStatistiques = new Button("Afficher les statistiques");
        Button buttonAfficherGraphique = new Button("Afficher les performances des joueurs");

        // Ajout de jeu
        buttonAjouterJeu.setOnAction(event -> afficherDialogueAjoutJeu());

        // Modification du jeu sélectionné
        buttonModifierJeu.setOnAction(event -> {
            Jeu jeuSelectionne = listViewJeux.getSelectionModel().getSelectedItem();
            if (jeuSelectionne != null) {
                afficherDialogueModifierJeu(jeuSelectionne);
            } else {
                showAlert("Aucun jeu sélectionné", "Veuillez sélectionner un jeu à modifier.");
            }
        });

        // Suppression du jeu sélectionné
        buttonSupprimerJeu.setOnAction(event -> {
            Jeu jeuSelectionne = listViewJeux.getSelectionModel().getSelectedItem();
            if (jeuSelectionne != null) {
                jeux.remove(jeuSelectionne);
                listViewJeux.getItems().remove(jeuSelectionne);
            } else {
                showAlert("Aucun jeu sélectionné", "Veuillez sélectionner un jeu à supprimer.");
            }
        });

        // Ajout de joueur
        buttonAjouterJoueur.setOnAction(event -> afficherDialogueAjoutJoueur());

        // Ajout de session
        buttonAjouterSession.setOnAction(event -> afficherDialogueAjoutSession());

        // Affichage de l'historique
        buttonAfficherHistorique.setOnAction(event -> afficherHistoriqueParties());

        // Affichage des statistiques
        buttonAfficherStatistiques.setOnAction(event -> afficherStatistiques());

        // Affichage des performances des joueurs (graphique)
        buttonAfficherGraphique.setOnAction(event -> afficherGraphiquePerformances());

        // Recherche de jeu
        rechercheField.setPromptText("Rechercher un jeu...");
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> rechercherJeu(newValue));

        HBox hBoxRecherche = new HBox(10, new Label("Rechercher un jeu :"), rechercheField);

        VBox vbox = new VBox(10, label, hBoxRecherche, buttonAjouterJeu, buttonModifierJeu, buttonSupprimerJeu,
                buttonAjouterJoueur, buttonAjouterSession, buttonAfficherHistorique, buttonAfficherStatistiques,
                buttonAfficherGraphique, listViewJeux, listViewJoueurs, listViewSessions);
        Scene scene = new Scene(vbox, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour rechercher un jeu
    private void rechercherJeu(String texte) {
        List<Jeu> resultats = jeux.stream()
                .filter(jeu -> jeu.getNom().toLowerCase().contains(texte.toLowerCase()))
                .collect(Collectors.toList());
        listViewJeux.setItems(FXCollections.observableArrayList(resultats));
    }

    // Méthode pour ajouter un nouveau jeu
    private void afficherDialogueAjoutJeu() {
        Stage stage = new Stage();
        stage.setTitle("Ajouter un jeu");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nomField = new TextField();
        TextField typeField = new TextField();
        TextField nbJoueursField = new TextField();
        TextField dureeField = new TextField();
        TextArea descriptionArea = new TextArea();

        Button buttonAjouter = new Button("Ajouter");
        buttonAjouter.setOnAction(e -> {
            String nom = nomField.getText();
            String type = typeField.getText();
            int nbJoueurs = Integer.parseInt(nbJoueursField.getText());
            int duree = Integer.parseInt(dureeField.getText());
            String description = descriptionArea.getText();

            Jeu jeu = new Jeu(nom, type, nbJoueurs, duree, description);
            jeux.add(jeu);
            listViewJeux.getItems().add(jeu);
            stage.close();
        });

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Type :"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Nombre de joueurs :"), 0, 2);
        grid.add(nbJoueursField, 1, 2);
        grid.add(new Label("Durée (min) :"), 0, 3);
        grid.add(dureeField, 1, 3);
        grid.add(new Label("Description :"), 0, 4);
        grid.add(descriptionArea, 1, 4);
        grid.add(buttonAjouter, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Méthode pour modifier un jeu existant
    private void afficherDialogueModifierJeu(Jeu jeu) {
        Stage stage = new Stage();
        stage.setTitle("Modifier le jeu : " + jeu.getNom());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nomField = new TextField(jeu.getNom());
        TextField typeField = new TextField(jeu.getType());
        TextField nbJoueursField = new TextField(String.valueOf(jeu.getNombreDeJoueurs()));
        TextField dureeField = new TextField(String.valueOf(jeu.getDuree()));
        TextArea descriptionArea = new TextArea(jeu.getDescription());

        Button buttonModifier = new Button("Modifier");
        buttonModifier.setOnAction(e -> {
            jeu.setNom(nomField.getText());
            jeu.setType(typeField.getText());
            jeu.setNombreDeJoueurs(Integer.parseInt(nbJoueursField.getText()));
            jeu.setDuree(Integer.parseInt(dureeField.getText()));
            jeu.setDescription(descriptionArea.getText());

            listViewJeux.refresh(); // Pour mettre à jour l'affichage
            stage.close();
        });

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Type :"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Nombre de joueurs :"), 0, 2);
        grid.add(nbJoueursField, 1, 2);
        grid.add(new Label("Durée (min) :"), 0, 3);
        grid.add(dureeField, 1, 3);
        grid.add(new Label("Description :"), 0, 4);
        grid.add(descriptionArea, 1, 4);
        grid.add(buttonModifier, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour ajouter un joueur
    private void afficherDialogueAjoutJoueur() {
        Stage stage = new Stage();
        stage.setTitle("Ajouter un joueur");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nomField = new TextField();

        Button buttonAjouter = new Button("Ajouter");
        buttonAjouter.setOnAction(e -> {
            String nom = nomField.getText();
            Joueur joueur = new Joueur(nom);
            joueurs.add(joueur);
            listViewJoueurs.getItems().add(joueur);
            stage.close();
        });

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(buttonAjouter, 1, 1);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Méthode pour ajouter une session de jeu
    private void afficherDialogueAjoutSession() {
        Stage stage = new Stage();
        stage.setTitle("Ajouter une session de jeu");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        ComboBox<Jeu> jeuComboBox = new ComboBox<>(jeux);
        ListView<Joueur> joueursListView = new ListView<>(joueurs);
        joueursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TextArea scoresArea = new TextArea();
        scoresArea.setPromptText("Entrez les scores des joueurs, séparés par des virgules");

        Button buttonAjouter = new Button("Ajouter la session");
        buttonAjouter.setOnAction(e -> {
            Jeu jeu = jeuComboBox.getSelectionModel().getSelectedItem();
            List<Joueur> joueursSelectionnes = joueursListView.getSelectionModel().getSelectedItems();
            String[] scoresStr = scoresArea.getText().split(",");
            List<Integer> scores = new ArrayList<>();
            for (String scoreStr : scoresStr) {
                scores.add(Integer.parseInt(scoreStr.trim()));
            }

            SessionDeJeu sessionDeJeu = new SessionDeJeu(jeu, joueursSelectionnes, scores);
            sessionsDeJeu.add(sessionDeJeu);
            listViewSessions.getItems().add(sessionDeJeu.toString());
            stage.close();
        });

        grid.add(new Label("Jeu :"), 0, 0);
        grid.add(jeuComboBox, 1, 0);
        grid.add(new Label("Joueurs :"), 0, 1);
        grid.add(joueursListView, 1, 1);
        grid.add(new Label("Scores :"), 0, 2);
        grid.add(scoresArea, 1, 2);
        grid.add(buttonAjouter, 1, 3);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Méthode pour afficher l'historique des parties
    private void afficherHistoriqueParties() {
        StringBuilder sb = new StringBuilder();
        for (SessionDeJeu session : sessionsDeJeu) {
            sb.append(session.toString()).append("\n");
        }
        showAlert("Historique des parties", sb.toString());
    }

    // Méthode pour afficher les statistiques
    // Méthode pour afficher les statistiques des joueurs
    private void afficherStatistiques() {
        StringBuilder sb = new StringBuilder();

        // Pour chaque joueur
        for (Joueur joueur : joueurs) {
            int totalScore = 0;
            int nombreDeSessions = 0;

            // Parcourir toutes les sessions de jeu pour trouver les scores du joueur
            for (SessionDeJeu session : sessionsDeJeu) {
                if (session.getJoueurs().contains(joueur)) {
                    // Récupérer l'index du joueur dans la session
                    int index = session.getJoueurs().indexOf(joueur);

                    // Ajouter le score correspondant au joueur
                    totalScore += session.getScores().get(index);
                    nombreDeSessions++;
                }
            }

            // Si le joueur a participé à des sessions
            if (nombreDeSessions > 0) {
                double scoreMoyen = (double) totalScore / nombreDeSessions;
                sb.append(joueur.getNom())
                        .append(" - Score moyen : ")
                        .append(String.format("%.2f", scoreMoyen))
                        .append(" (")
                        .append(nombreDeSessions)
                        .append(" sessions)\n");
            } else {
                sb.append(joueur.getNom())
                        .append(" - Aucun score enregistré\n");
            }
        }

        // Affiche les résultats dans une fenêtre d'alerte
        showAlert("Statistiques des joueurs", sb.toString());
    }


    // Méthode pour afficher un graphique des performances
    private void afficherGraphiquePerformances() {
        Stage graphiqueStage = new Stage();
        graphiqueStage.setTitle("Performances des joueurs");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Session");
        yAxis.setLabel("Score");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Performances des joueurs");

        for (Joueur joueur : joueurs) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(joueur.getNom());

            int sessionCounter = 1;
            for (SessionDeJeu session : sessionsDeJeu) {
                if (session.getJoueurs().contains(joueur)) {
                    int index = session.getJoueurs().indexOf(joueur);
                    int score = session.getScores().get(index);
                    series.getData().add(new XYChart.Data<>(sessionCounter, score));
                }
                sessionCounter++;
            }
            lineChart.getData().add(series);
        }

        VBox vbox = new VBox(lineChart);
        Scene scene = new Scene(vbox, 800, 600);
        graphiqueStage.setScene(scene);
        graphiqueStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
