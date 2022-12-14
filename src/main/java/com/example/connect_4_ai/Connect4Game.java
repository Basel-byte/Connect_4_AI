package com.example.connect_4_ai;

import com.example.connect_4_ai.Tree.ShowTreeController;
import com.example.connect_4_ai.minimax_algorithms.MiniMax;
import com.example.connect_4_ai.minimax_algorithms.MinimaxWithPruning;
import com.example.connect_4_ai.minimax_algorithms.MinimaxWithoutPruning;
import com.example.connect_4_ai.utilities.Node;
import com.example.connect_4_ai.utilities.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.max;


public class Connect4Game {
    // player 1 red || player 2 yellow
    private int score1, score2;
    private Label score1Label, score2Label;
    private Label playerTurnLabel;
    private boolean player1Turn = true;
    private final boolean singlePlayer;
    private final Alert alert;
    private final ImageView winIcon;
    private final ImageView loseIcon;
    private final Image boardImage;
    private final Image redCircleImage;
    private final Image yellowCircleImage;
    private char[][] board;
    public final int[] lastRowIndices;

    public long maxTime=0;
    public long maxExpandedNodes=0;
    public long time =0;
    public long expandedNodes=0;
    public Node node;
    private boolean minimax = true;
    private int k = 4;

    public Connect4Game(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
        score1 = 0;
        score2 = 0;
        String boardPNG = "file:src/main/resources/images/board.png";
        String redCirclePNG = "file:src/main/resources/images/red-circle.png";
        String yellowCirclePNG = "file:src/main/resources/images/yellow-circle.png";
        String icon1 = "file:src/main/resources/images/icons8-win-48.png";
        String icon2 = "file:src/main/resources/images/icons8-loser-48.png";
        boardImage = new Image(boardPNG);
        redCircleImage = new Image(redCirclePNG);
        yellowCircleImage = new Image(yellowCirclePNG);
        this.board = new char[6][7];
        lastRowIndices = new int[7];
        alert = new Alert(Alert.AlertType.INFORMATION);
        winIcon = new ImageView(new Image(icon1));
        loseIcon = new ImageView(new Image(icon2));
    }

    public void chooseAiAgent(Stage stage) {
        Group group = new Group();
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Minimax Without Pruning",
                        "Minimax With Pruning"
                );
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setOnAction(e -> {
            String choice = comboBox.getValue();
            minimax = !choice.equals("Minimax With Pruning");
        });
        comboBox.getSelectionModel().selectFirst();
        comboBox.setLayoutX(180);
        comboBox.setLayoutY(210);
//        comboBox.setStyle("-fx-font: 15px \"Impact\";");
        TextField textField = new TextField();
        textField.setPromptText("Input k");
        textField.setLayoutX(180);
        textField.setLayoutY(250);
        textField.setPrefWidth(210);
        Button start = new Button("Start Game");
        start.setLayoutX(230);
        start.setLayoutY(300);
        Button back = new Button("Back");
        back.setLayoutX(250);
        back.setLayoutY(340);


        start.setOnAction(e -> {
            try {
                k = Integer.parseInt(textField.getText());
                System.out.println("k = " + k);
                startGame(stage);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        back.setOnAction(e -> {
            try {
                new HelloApplication().start(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ImageView imageView = new ImageView("file:src/main/resources/images/connect-4-1.jpg");
        group.getChildren().addAll(imageView, comboBox, textField, start, back);
        group.setStyle("-fx-font: 15px \"Impact\";");
        Scene scene = new Scene(group, 552, 580);
        stage.setScene(scene);
    }

    public void startGame(Stage stage) throws FileNotFoundException {
        System.out.println("Starting Game");
        player1Turn = true;
        board = new char[6][7];
        score1 = 0;
        score2 = 0;
        Arrays.fill(lastRowIndices, 6);
        drawBoard(stage);
    }

    private void drawBoard(Stage stage) {
        playerTurnLabel = createLabel("Player 1 Turn", 220);
        score1Label = createLabel("Score " + score1, 360);
        score2Label = createLabel("Score " + score2, 450);
        score1Label.setTextFill(Color.rgb(255, 215, 0));
        score2Label.setTextFill(Color.RED);
        Button restartButton = new Button("Restart");
        restartButton.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        restartButton.setLayoutX(0);
        restartButton.setLayoutY(552);
        restartButton.setOnAction(e -> {
            try {
                startGame(stage);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        backButton.setLayoutX(87);
        backButton.setLayoutY(552);
        backButton.setOnAction(e -> {
            try {
                new HelloApplication().start(stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button treeButton = new Button("Tree");
        treeButton.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        treeButton.setLayoutX(150);
        treeButton.setLayoutY(552);
        treeButton.setOnAction(e -> showTree());

        Canvas canvas = new Canvas(552, 552);
        GraphicsContext context = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            System.out.println(e.getX() + " " + e.getY());
            int colIndex = getColIndex(e.getX());
            System.out.println("Index of column : " + colIndex);
            play(colIndex);
            draw(context);

            if (singlePlayer && !player1Turn) {
                colIndex = playAI();
                play(colIndex);
                draw(context);
                System.out.println("Average Time = "+time/21);
                System.out.println("Average Expanded Nodes = "+expandedNodes/21);
                System.out.println("Max Time = "+maxTime);
                System.out.println("Max Expanded Nodes = "+maxExpandedNodes);
            }
            if (isFull()) {
                draw(canvas.getGraphicsContext2D());
                if (score2 > score1) {
                    alert.setGraphic(loseIcon);
                    alert.setTitle("You lost");
                    alert.setHeaderText("You're never a loser until you quit trying.:(");
                    alert.show();

                } else if (score2 < score1) {
                    alert.setGraphic(winIcon);
                    alert.setTitle("WIN");
                    alert.setHeaderText("Congratulations :)");
                    alert.show();
                } else {
                    alert.setGraphic(winIcon);
                    alert.setTitle("Tie");
                    alert.setHeaderText(":(");
                    alert.show();

                }
            }

        });
        context.drawImage(boardImage, 0, 0, 552, 552);
        Group root = new Group();
        root.getChildren().addAll(canvas, playerTurnLabel, score1Label, score2Label, restartButton, backButton, treeButton);

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void showTree() {
        Stage stage = new Stage();
        ScrollPane pane = new ScrollPane();
        int h = 700;
        int w = 1000;
        Group root = new Group();
        ShowTreeController c = new ShowTreeController();
        root = c.showTree(node,root);
        pane.setContent(root);
        Scene scene = new Scene(pane, w, h);
        stage.setScene(scene);
        stage.show();
    }

    public void draw(GraphicsContext context) {
        for (int i = 5; i >= 0; i--) {
            for (int j = 6; j >= 0; j--) {
                char c = board[i][j];
                if (c == 'r' || c == 'y')
                    context.drawImage(getImage(c), 22 + (j * 73), 38 + (81.2 * i), 70, 70);
            }
        }
        printBoard();
    }

    private void printBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private Image getImage(char c) {
        return switch (c) {
            case 'r' -> redCircleImage;
            case 'y' -> yellowCircleImage;
            default -> null;
        };
    }

    private Label createLabel(String Title, int x) {
        Label label = new Label(Title);
        label.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        label.setLayoutX(x);
        label.setLayoutY(555);
        return label;
    }

    private int getColIndex(double x) {
        if (x <= 16 || x >= 536)
            return -1;
        return (int) (x - 16) / 75;
    }

    private void play(int col) {
        if (isValidColumn(col)) {
            applyChoice(col);
            System.out.println("Win Situation");
            score1 += player1Turn ? getScore(col) : 0;
            score2 += !player1Turn ? getScore(col) : 0;
            score1Label.setText("Score " + score1);
            score2Label.setText("Score " + score2);
            switchTurns();
        }
    }

    private boolean isValidColumn(int col) {
        return col >= 0 && col <= 6 && lastRowIndices[col] > 0;
    }

    private void applyChoice(int col) {
        int row = --lastRowIndices[col];
        board[row][col] = getChar();
    }

    private void switchTurns() {
        player1Turn = !player1Turn;
        if (player1Turn)
            playerTurnLabel.setText("Player 1 Turn");
        else
            playerTurnLabel.setText("Player 2 Turn");
    }

    private char getChar() {
        if (player1Turn)
            return 'y';
        else
            return 'r';
    }

    private int getScore(int col) {
        int row = lastRowIndices[col];
        char color = getChar();
        int count = 0;
        int score = 0;
        // vertical check

        for (int i = row + 1; i < row + 4 && i < 6; i++) {
            if (board[i][col] != color)
                break;
            count++;
        }
//
//
        if (count == 3)
            score++;
//
        count = 0;
        // horizontal check
        for (int j = col + 1; j < col + 4 && j < 7; j++) {
            if (board[row][j] != color)
                break;
            count++;
        }

        for (int j = col - 1; j > col - 4 && j >= 0; j--) {
            if (board[row][j] != color)
                break;
            count++;
        }
        if (count >= 3)
            score += count - 2;
//
        // right diagonal check
        count = 0;

        for (int k = 1; k <= 3; k++) {
            if (row - k < 0 || col + k > 6 || board[row - k][col + k] != color)
                break;
            count++;
        }

        for (int k = 1; k <= 3; k++) {
            if (row + k > 5 || col - k < 0 || board[row + k][col - k] != color)
                break;
            count++;
        }

        if (count >= 3)
            score += count - 2;

        // left diagonal check
        count = 0;

        for (int k = 1; k <= 3; k++) {
            if (row + k > 5 || col + k > 6 || board[row + k][col + k] != color)
                break;
            count++;
        }

        for (int k = 1; k <= 3; k++) {
            if (row - k < 0 || col - k < 0 || board[row - k][col - k] != color)
                break;
            count++;
        }

        if (count >= 3)
            score += count - 2;
        return score;
    }

    private boolean isFull() {
        for (int index : lastRowIndices) {
            if (index > 0)
                return false;
        }
        return true;
    }
    //////////////////////////////

    private int playAI() {
        MiniMax max;
        if (minimax)
            max = new MinimaxWithoutPruning();
        else
            max = new MinimaxWithPruning();

        long bitsBoard = Util.char2dArrayToLong(board);
        bitsBoard = Util.setBit(bitsBoard, 63);
        int col = max.Decision(bitsBoard, k);
        node = max.root;
        time+=max.time;
        expandedNodes+=max.expandedNodes;
        maxTime=max(maxTime,max.time);
        maxExpandedNodes=max(maxExpandedNodes,max.expandedNodes);
        node.col = col;
        return col;
    }
}


//if (isValidColumn(colIndex)) {
//        applyChoice(colIndex);
//        draw(canvas.getGraphicsContext2D());
//        if (win(colIndex)) {
//        alert.setGraphic(winIcon);
//        alert.setTitle("WIN");
//        alert.setHeaderText("Congratulations :)");
//        alert.show();
//        }
//        switchTurns();
//  }