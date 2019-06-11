package edu.wpi.cs3733d19.teamg;

import edu.wpi.cs3733d19.teamg.models.Node;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SnakeController extends Controller {

    private static final int FPS = 7;
    private static final int MOVE_DISTANCE = 10;

    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView background;
    @FXML
    private Label gameOverLabel;
    @FXML
    private Label scoreLabel;

    private ScheduledExecutorService movement;
    private int direction = 0;
    private List<Rectangle> snake;
    private List<Integer> tailDirections;
    private List<Circle> foodLeft;
    private Shape shown;
    private boolean playing;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private int nextLevel;

    @FXML
    private void initialize() {
        KioskApplication.getPrimaryScene().addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);
        start();
    }

    private void start() {
        gameOverLabel.setText("");
        nextLevel = 0;
        foodLeft = new ArrayList<>();
        snake = new ArrayList<>();
        tailDirections = new ArrayList<>();
        background.setPreserveRatio(false);
        Executors.newSingleThreadScheduledExecutor().schedule(() ->
                Platform.runLater(() -> {
                    playing = true;
                    snake.add(new Rectangle(pane.getWidth() / 2, pane.getHeight() / 2, 10, 10));
                    snake.get(0).setFill(Color.color(.004, .161, .325));
                    pane.getChildren().add(snake.get(0));
                    tailDirections.add(0);
                    startMovement();
                    background.fitWidthProperty().bind(pane.widthProperty());
                    background.fitHeightProperty().bind(pane.heightProperty());
                    loadLevel(nextLevel++);
                    updateScoreLabel();
                }), 5, TimeUnit.MILLISECONDS);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Eaten: " + (snake.size() - 1)
                + "\nRemaining on this floor: " + (foodLeft.size() + 1));

    }

    private void loadLevel(int level) {
        clearEdges();
        switch (level) {
            case 0:
                background.setImage(KioskApplication.getLowerLevelTwoImage());
                loadNodes("L2");
                initEdges();
                showFood();
                break;
            case 1:
                background.setImage(KioskApplication.getLowerLevelOneImage());
                loadNodes("L1");
                initEdges();
                showFood();
                break;
            case 2:
                background.setImage(KioskApplication.getGroundFloorImage());
                loadNodes("G");
                initEdges();
                showFood();
                break;
            case 3:
                background.setImage(KioskApplication.getFirstFloorImage());
                loadNodes("1");
                initEdges();
                showFood();
                break;
            case 4:
                background.setImage(KioskApplication.getSecondFloorImage());
                loadNodes("2");
                initEdges();
                showFood();
                break;
            case 5:
                background.setImage(KioskApplication.getThirdFloorImage());
                loadNodes("3");
                initEdges();
                showFood();
                break;
            case 6:
                background.setImage(KioskApplication.getFourthFloorImage());
                loadNodes("4");
                initEdges();
                showFood();
                break;
            default:
                break;

        }
    }

    private void loadNodes(String floor) {
        double heightRatio = (pane.getHeight() / 3400);
        double widthRatio = (pane.getWidth() / 5000);
        Node.getAll(KioskApplication.getEntityManager()).stream().filter(node -> node.getFloor()
                .equals(floor)).forEach(node -> foodLeft.add(new Circle(node.getXCoord()
                * widthRatio, node.getYCoord() * heightRatio, 5, Color.RED)));
    }

    private void initEdges() {
        maxX = 0;
        maxY = 0;
        minX = (int) pane.getWidth();
        minY = (int) pane.getHeight();
        for (Circle c : foodLeft) {
            int xx = (int) c.getCenterX();
            int yy = (int) c.getCenterY();
            if (xx > maxX) {
                maxX = xx;
            }
            if (xx < minX) {
                minX = xx;
            }
            if (yy > maxY) {
                maxY = yy;
            }
            if (yy < minY) {
                minY = yy;
            }
        }
        minY = (minY / 10 - 1) * 10;
        minX = (minX / 10 - 1) * 10;
        maxY = (maxY / 10 + 1) * 10;
        maxX = (maxX / 10 + 1) * 10;
        if ((maxY - minY) * (maxX - minX) < (snake.size() + foodLeft.size() * 100)) {
            int diff = (snake.size() + foodLeft.size() * 100) - (maxY - minY) * (maxX - minX);
            while (diff > 0) {
                maxX += 10;
                maxY += 10;
                if (minX >= 10) {
                    minX -= 10;
                }
                if (minY >= 10) {
                    minY -= 10;
                }
                diff = (snake.size() + foodLeft.size() * 100) - (maxY - minY) * (maxX - minX);
            }
        }
        Line l1 = new Line(minX, minY, minX, maxY);
        l1.setStrokeWidth(3);
        Line l2 = new Line(minX, minY, maxX, minY);
        l2.setStrokeWidth(3);
        Line l3 = new Line(maxX, maxY, minX, maxY);
        l3.setStrokeWidth(3);
        Line l4 = new Line(maxX, maxY, maxX, minY);
        l4.setStrokeWidth(3);
        pane.getChildren().addAll(l1, l2, l3, l4);
        int startX = (minX + maxX) / 2;
        if (startX % 10 != 0) {
            startX += 5;
        }
        int startY = (minY + maxY) / 2;
        if (startY % 10 != 0) {
            startY += 5;
        }
        for (Rectangle r : snake) {
            r.setX(startX);
            r.setY(startY);
        }
    }

    private void startMovement() {
        movement = Executors.newSingleThreadScheduledExecutor();
        movement.schedule(() -> {
            Platform.runLater(() -> move());
            startMovement();
        }, 1000 / FPS, TimeUnit.MILLISECONDS);
    }

    private void moveSection(Rectangle section, int direction) {
        switch (direction) {
            case 1:
                section.setX(section.getX() + MOVE_DISTANCE);
                break;
            case 2:
                section.setY(section.getY() + MOVE_DISTANCE);
                break;
            case 3:
                section.setX(section.getX() - MOVE_DISTANCE);
                break;
            case 4:
                section.setY(section.getY() - MOVE_DISTANCE);
                break;
            default:
                break;
        }
    }

    private void move() {
        if (!playing) {
            return;
        }
        tailDirections.add(0, direction);
        tailDirections.remove(tailDirections.size() - 1);
        for (int i = 0; i < snake.size(); i++) {
            moveSection(snake.get(i), tailDirections.get(i));
        }
        if (snake.get(0).getX() < minX || snake.get(0).getX() + 10 > maxX || snake.get(0).getY()
                < minY || snake.get(0).getY() + 10 > maxY) {
            pane.getChildren().remove(snake.get(0));
            gameOver();
        }
        if (direction > 0) {
            for (int i = 1; i < snake.size(); i++) {
                if (Shape.intersect(snake.get(0), snake.get(i))
                        .getBoundsInLocal().getWidth() > 0) {
                    pane.getChildren().remove(snake.get(0));
                    gameOver();
                }
            }
        }
        if (Shape.intersect(snake.get(0), shown).getBoundsInLocal().getWidth() > 0) {
            nom();
        }
    }

    private void gameOver() {
        playing = false;
        gameOverLabel.setText("Game Over.\n You ate " + (snake.size() - 1) + " nodes.");
    }

    private void nom() {
        pane.getChildren().remove(shown);
        Rectangle newSection = new Rectangle(snake.get(snake.size() - 1).getX(),
                snake.get(snake.size() - 1).getY(), 10, 10);
        newSection.setFill(Color.color(.004, .161, .325));
        snake.add(newSection);
        pane.getChildren().add(newSection);
        tailDirections.add(tailDirections.size() - 1, 0);
        if (foodLeft.size() > 0) {
            showFood();
        } else {
            for (int i = 0; i < tailDirections.size(); i++) {
                tailDirections.set(i, 0);
            }
            direction = 0;
            if (nextLevel > 6) {
                gameOverLabel.setText("You win.");
                playing = false;
            } else {
                loadLevel(nextLevel++);
            }
        }
        updateScoreLabel();
    }

    private void showFood() {
        Circle next = foodLeft.remove((int) (Math.random() * foodLeft.size()));
        pane.getChildren().add(1, next);
        shown = next;
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        if ((keyEvent.getCode().equals(KeyCode.RIGHT)
                || keyEvent.getCode().equals(KeyCode.D)) && !(tailDirections.get(0) == 3)) {
            direction = 1;
        } else if ((keyEvent.getCode().equals(KeyCode.DOWN)
                || keyEvent.getCode().equals(KeyCode.S)) && !(tailDirections.get(0) == 4)) {
            direction = 2;
        } else if ((keyEvent.getCode().equals(KeyCode.LEFT)
                || keyEvent.getCode().equals(KeyCode.A)) && !(tailDirections.get(0) == 1)) {
            direction = 3;
        } else if ((keyEvent.getCode().equals(KeyCode.UP)
                || keyEvent.getCode().equals(KeyCode.W)) && !(tailDirections.get(0) == 2)) {
            direction = 4;
        } else if ((keyEvent.getCode().equals(KeyCode.R))) {
            restart();
        }
    }

    private void restart() {
        gameOverLabel.setText("");
        movement.shutdownNow();
        for (Rectangle rect : snake) {
            pane.getChildren().remove(rect);
        }
        pane.getChildren().remove(shown);
        clearEdges();
        direction = 0;
        start();
    }

    private void clearEdges() {
        for (int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Line) {
                pane.getChildren().remove(i--);
            }
        }
    }
}
