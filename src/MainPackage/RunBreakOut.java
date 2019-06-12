package MainPackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;

/**
 * @author Jorge Alejandro Raad
 */

public class RunBreakOut extends  Application{
    public static final String TITLE = "Super Breakout";
    public static final int HEIGHT = 800;
    public static final int WIDTH = 400;
    public static final Paint BACKGROUND = Color.BLACK;
    public static final int FRAMES_PER_SECOND = 180;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String LEVEL1 = "resources/1.txt";
    public static final String LEVEL2 = "resources/2.txt";
    public static final String LEVEL3 = "resources/3.txt";
    public static final String HEART = "heart.gif";
    public static final int STATUSBAR_HEIGHT = HEIGHT/20;

    public static final String SHINY_BLUE = "shiny_blue.gif";
    public static final String HAPPYFACE = "happyface.gif";
    public static final String COLOR_BALL = "color_ball.gif";
    public static final double HOME_BOUNCER_SIZE = 50;


    private String myBouncerImage = SHINY_BLUE;
    private ImageView shinyBlue = placeImage(SHINY_BLUE, HOME_BOUNCER_SIZE);
    private ImageView happyface = placeImage(HAPPYFACE, HOME_BOUNCER_SIZE);
    private ImageView ball = placeImage(COLOR_BALL, HOME_BOUNCER_SIZE);
    private Circle selectionCircle;
    private ImageView titleArt;
    private Text startText;

    private int LIVES = 3;
    private int myScore = 0;
    private Text myScoreText = placeText(Integer.toString(myScore), "Copperplate", STATUSBAR_HEIGHT, Color.WHITE, TextAlignment.CENTER, WIDTH);;
    private Text myLives = placeText("x" + LIVES, "Copperplate", STATUSBAR_HEIGHT, Color.BLACK, TextAlignment.RIGHT, WIDTH);;

    private Scene myScene;

    private Group homeRoot = setupHome();
    private Level currentLevel = new Level();

    /**
     * Overrides javafx's Application Class and tells it how to initiate the application. It is only called once.
     * @param stage - stage that is created by launchargs()
     */
    @Override
    public void start(Stage stage){
        myScene = new Scene(homeRoot, WIDTH, HEIGHT, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        stage.show();
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step(double timeElapsed){
        myScoreText.setText(Integer.toString(myScore));
        myLives.setText("x" + LIVES);
        if(myScene.getRoot() == homeRoot){
            oscillateSize(titleArt, titleArt.getX() + titleArt.getBoundsInLocal().getWidth()/2, titleArt.getY()+titleArt.getBoundsInLocal().getHeight()/2, WIDTH, 0.005*System.currentTimeMillis());
            oscillateSize(titleArt, titleArt.getX() + titleArt.getBoundsInLocal().getWidth()/2, titleArt.getY()+titleArt.getBoundsInLocal().getHeight()/2, WIDTH, 0.005*System.currentTimeMillis());
            myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        }
        else {
            if (LIVES <= 0) {
                currentLevel.getRoot().getChildren().add(new Group(placeImage("game_over.gif", WIDTH * 4 / 5)));
            }
            if (currentLevel.getFileName() == LEVEL1 && currentLevel.getNumberOfBlocks() == 0) {
                currentLevel = new Level(LEVEL2);
                myScene.setRoot(currentLevel.getRoot());
                displayScore();
                currentLevel.setMainBouncer(myBouncerImage);
            } else if (currentLevel.getFileName() == LEVEL2 && currentLevel.getNumberOfBlocks() == 0) {
                currentLevel = new Level(LEVEL3);
                myScene.setRoot(currentLevel.getRoot());
                displayScore();
                currentLevel.setMainBouncer(myBouncerImage);
            } else if (currentLevel.getFileName() == LEVEL3 && currentLevel.getNumberOfBlocks() == 0) {
                // YOU WON
            }
            int ans = currentLevel.stepThrough(timeElapsed, myScore);
            if (ans == -1) { //no more bouncers
                LIVES--;
                currentLevel.toggleRunState();
                currentLevel.placeCenterBall(0, currentLevel.myBouncerSpeed);
            }
            else if(ans == -2) {
                LIVES--;
                //currentLevel.toggleRunState();
            }
            else {
                myScore = ans;
            }
            myScene.setRoot(currentLevel.getRoot());
        }
    }

    private void handleKeyInput(KeyCode code){
        if (code == KeyCode.RIGHT) {
            currentLevel.getPaddle().setX(currentLevel.getPaddle().getX() + currentLevel.getPaddle().getSpeed());
        }
        else if (code == KeyCode.LEFT) {
            currentLevel.getPaddle().setX(currentLevel.getPaddle().getX() - currentLevel.getPaddle().getSpeed());
        }
        else if (code == KeyCode.A) {
            LIVES++;
        }
        else if (code == KeyCode.DIGIT1) {
            currentLevel = new Level(LEVEL1);
            myScene.setRoot(currentLevel.getRoot());
            displayScore();
            currentLevel.setMainBouncer(myBouncerImage);
        }
        else if (code == KeyCode.DIGIT2) {
            currentLevel = new Level(LEVEL2);
            myScene.setRoot(currentLevel.getRoot());
            displayScore();
            currentLevel.setMainBouncer(myBouncerImage);
        }
        else if (code == KeyCode.DIGIT3) {
            currentLevel = new Level(LEVEL3);
            myScene.setRoot(currentLevel.getRoot());
            displayScore();
            currentLevel.setMainBouncer(myBouncerImage);
        }
        else if (code == KeyCode.S) {
            currentLevel.toggleFloor();
        }
        else if (code == KeyCode.SPACE) {
            currentLevel.togglePause();
        }
    }

    private void displayScore(){
        myScoreText.setY(STATUSBAR_HEIGHT*3/4);
        myLives.setY(STATUSBAR_HEIGHT*3/4);
        myLives.setX(WIDTH - myLives.getBoundsInLocal().getWidth());
        ImageView heart = new ImageView(HEART);
        heart.setPreserveRatio(true);
        heart.setFitWidth(STATUSBAR_HEIGHT*3/4);
        heart.setX(WIDTH- WIDTH/5);
        heart.setY(6);
        currentLevel.getRoot().getChildren().addAll(myLives, myScoreText, heart);
    }

    private ImageView placeImage(String name, double width){
        ImageView IV = new ImageView(name);
        IV.setPreserveRatio(true);
        IV.setFitWidth(width);
        IV.setX(WIDTH/2 - IV.getBoundsInLocal().getWidth()/2);
        IV.setY(HEIGHT/2 - IV.getBoundsInLocal().getHeight()/2);
        return IV;
    }

    private Text placeText(String content, String font, int fontSize, Color color, TextAlignment alignment, int wrappingWidth){
        Text text = new Text(content);
        text.setTextAlignment(alignment);
        text.setWrappingWidth(wrappingWidth);
        text.setX(WIDTH/2 - text.getBoundsInLocal().getWidth()/2);
        text.setY(HEIGHT/2 - text.getBoundsInLocal().getHeight()/2);
        text.setFont(Font.font(font, fontSize));
        text.setFill(color);
        return text;
    }

    private void oscillateSize(ImageView image, double centerX, double centerY, double width, double time){
        image.setFitWidth(width + 0.1*width*Math.sin(time));
        image.setX(centerX - image.getBoundsInLocal().getWidth()/2);
        image.setY(centerY - image.getBoundsInLocal().getHeight()/2);
    }

    private void handleMouseInput (double x, double y) {
        if (shinyBlue.contains(x, y)) {
            myBouncerImage = SHINY_BLUE;
            selectionCircle.setCenterX(shinyBlue.getX()+HOME_BOUNCER_SIZE/2);
            selectionCircle.setCenterY(shinyBlue.getY()+HOME_BOUNCER_SIZE/2);
        }
        else if (happyface.contains(x, y)) {
            myBouncerImage = HAPPYFACE;
            selectionCircle.setCenterX(happyface.getX()+HOME_BOUNCER_SIZE/2);
            selectionCircle.setCenterY(happyface.getY()+HOME_BOUNCER_SIZE/2);
        }
        else if (ball.contains(x, y)) {
            myBouncerImage = COLOR_BALL;
            selectionCircle.setCenterX(ball.getX()+HOME_BOUNCER_SIZE/2);
            selectionCircle.setCenterY(ball.getY()+HOME_BOUNCER_SIZE/2);
        }
        else if (startText.contains(x, y)) {
            currentLevel = new Level(LEVEL1);
            myScene.setRoot(currentLevel.getRoot());
            displayScore();
            currentLevel.setMainBouncer(myBouncerImage);
        }
    }

    private Group setupHome(){
        titleArt = placeImage("title_art.gif", WIDTH);
        titleArt.setY(WIDTH/4);
        Text selectionPrompt = placeText("CHOOSE YOUR FIGHTER", "Copperplate", 25, Color.ORANGERED, TextAlignment.CENTER, WIDTH - 50);
        selectionPrompt.setY(titleArt.getY()+titleArt.getBoundsInLocal().getHeight() + 100);
        startText = placeText("PLAYER START", "Copperplate", 40, Color.ORANGERED, TextAlignment.CENTER, WIDTH - 50);
        startText.setY(HEIGHT - 200);

        happyface.setY(selectionPrompt.getY() + selectionPrompt.getBoundsInLocal().getHeight() + 25);
        shinyBlue.setX(happyface.getX() - 50 - shinyBlue.getBoundsInLocal().getWidth());
        shinyBlue.setY(selectionPrompt.getY() + selectionPrompt.getBoundsInLocal().getHeight() + 25);
        ball.setX(happyface.getX() + happyface.getBoundsInLocal().getWidth() + 50);
        ball.setY(selectionPrompt.getY() + selectionPrompt.getBoundsInLocal().getHeight() + 25);
        selectionCircle = new Circle(shinyBlue.getX()+HOME_BOUNCER_SIZE/2, shinyBlue.getY()+HOME_BOUNCER_SIZE/2, HOME_BOUNCER_SIZE/2*5/4, Color.GREEN);
        ImageView graphicBox = placeImage("graphic_box.gif", WIDTH*4/5);
        graphicBox.setY(happyface.getY()+HOME_BOUNCER_SIZE/2-graphicBox.getBoundsInLocal().getHeight()/2);
        var root = new Group(graphicBox, selectionCircle, happyface, shinyBlue, ball, startText, titleArt, selectionPrompt);
        return root;
    }

    public static void main (String[] args) {
        launch(args);
    }
}

