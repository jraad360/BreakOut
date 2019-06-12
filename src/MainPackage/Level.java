package MainPackage;


import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Level {
    private ArrayList<Node> pauseMenuImages = setupPauseMenu();

    public static final int HEIGHT = 800;
    public static final int WIDTH = 400;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_SPEED = 30;
    public static final int ITEM_SPEED = 200;
    public static final int BOUNCER_SIZE = 20;
    public static final int BLOCK_WIDTH = WIDTH/8;
    public static final int BLOCK_HEIGHT = BLOCK_WIDTH/2;
    public static final int STATUSBAR_HEIGHT = HEIGHT/20;
    public static final int MULT_FACTOR = 4;
    public static final double FREEZE_FACTOR = 0.2;

    public static final int SPEED1 = 300;
    public static final int SPEED2 = 350;
    public static final int SPEED3 = 450;

    public static final String LEVEL1 = "resources/1.txt";
    public static final String LEVEL2 = "resources/2.txt";
    public static final String LEVEL3 = "resources/3.txt";


    public static final String PADDLE_IMAGE = "red_paddle.gif";
    //public static final String BOUNCER_IMAGE = "shiny_blue.gif";

    public static final String FONT_STYLE = "Copperplate";
    public static final String PAUSE_ICON= "pause.gif";

    public static final String PADDLE = "red_paddle.gif";
    public static final String BRICK1 = "brick1.gif";
    public static final String BRICK2 = "brick2.gif";
    public static final String BRICK3 = "brick3.gif";
    public static final String MULTIPLIER = "multiplier.gif";
    public static final String POISON = "poison.gif";
    public static final String FREEZE = "freeze.gif";
    public static final String ENLARGER = "enlarger.gif";
    public static final String POISON_BOUNCER = "poison_bouncer.gif";

    // power-up probabilities in percentages, poison depends on level
    private int poisonProbablity = 0;
    private int poisonModeLength = 0;
    private long poisonTime = 0;
    private int powerUpProbablity = 5;
    private long freezeTime = 0;

    private String myBouncerImage = "shiny_blue.gif";
    private boolean TOGGLE_FLOOR = false;
    private boolean RUN_STATE = false;
    private int myScore = 0;
    private String myFileName = "";
    private Group myRoot;
    private ArrayList<Block> myBlocks;
    private ArrayList<Bouncer> myBouncers;
    private ArrayList<Item> myItems;
    private Paddle myPaddle = new Paddle(PADDLE, PADDLE_WIDTH, WIDTH/2 - PADDLE_WIDTH/2, HEIGHT - 2*PADDLE_HEIGHT, PADDLE_SPEED);
    public double myBouncerSpeed;
    private boolean poisonTag = false; //whether or not you have been killed by poison mode

    /**
     * Creates an empty Level.
     */
    Level(){
    }

    /**
     * Creates a Level.
     * @param myFileName - String containing the name of the file the level should be read in from
     */
    Level(String myFileName){
        this.myFileName = myFileName;
        this.myRoot = new Group();
        readInLevel(this.myFileName, this.myRoot);
        this.myBouncerSpeed = myBouncerSpeed;
        if(this.myFileName == LEVEL1){
            this.myBouncerSpeed = SPEED1;
            poisonProbablity = 0;
            poisonModeLength = 10000;

        }
        else if(this.myFileName == LEVEL2){
            this.myBouncerSpeed = SPEED2;
            poisonProbablity = 5;
            poisonModeLength = 10000;
        }
        else if(this.myFileName == LEVEL3){
            this.myBouncerSpeed = SPEED3;
            poisonProbablity = 10;
            poisonModeLength = 20000;
        }
        double[] vel = findComponents(myBouncerSpeed, 90);
        placeCenterBall(vel[0], vel[1]);
    }

    /**
     * Updates the position of every object within the Level and returns an int that indicates what
     * event occured during the update.
     * @param elapsedTime - amount of time elapsed since last time display was updated
     * @param score - player's current score
     *
     * @return -2 if the player should lose a life due to poison mode
     *         -1 if the player should lose a life due to dropping bouncer
     *         player's updated score if life should not be lost
     */
    public int stepThrough(double elapsedTime, int score) {
        myScore = score;
        if(!RUN_STATE){
            return myScore;
        }

        if(myBouncers.size() == 0){
            return -1;
        }

        for (int k = 0; k < myBouncers.size(); k++) {
            Bouncer bouncer = myBouncers.get(k);
            bouncer.setX(bouncer.getX() + bouncer.getXVelocity() * elapsedTime);
            bouncer.setY(bouncer.getY() - bouncer.getYVelocity() * elapsedTime);
            executeWallCollision(bouncer);
            executePaddleCollision(bouncer);
            executeBlockCollision(bouncer);
        }
        if(System.currentTimeMillis() - poisonTime >= poisonModeLength && poisonTime > 1){
            poisonTime = 0; // end poison mode after certain time
            TOGGLE_FLOOR = false;
            for(Bouncer bouncer : myBouncers){
                bouncer.setImage(myBouncerImage);
            }
        }
        if(System.currentTimeMillis() - freezeTime >= 10000 && freezeTime > 1){
           freezeTime = 0; // end freeze mode after 10 sec
            for(Bouncer bouncer : myBouncers){
                bouncer.setXVelocity(bouncer.getXVelocity() / FREEZE_FACTOR);
                bouncer.setYVelocity(bouncer.getYVelocity() / FREEZE_FACTOR);
            }
        }
        if(poisonTag){
            poisonTag = false;
            return -2; // life lost due to poison
        }
        for(int k = 0; k < myItems.size(); k++){
            Item item = myItems.get(k);
            item.setY(item.getY() + ITEM_SPEED*elapsedTime);
            if (myPaddle.intersects(item)){
                activateItem(item);
            }
            if(item.getY()>HEIGHT){
                myItems.remove(item);
                myRoot.getChildren().remove(item);
            }
        }
        return myScore;
    }

    private void readInLevel(String fileName, Group root){
        myBlocks = new ArrayList<>();
        myBouncers = new ArrayList<>();
        myItems = new ArrayList<>();
        File file = new File(fileName);
        myRoot.getChildren().add(myPaddle.getImageView());
        Rectangle statusBar = new Rectangle(0, 0, Color.DEEPPINK);
        statusBar.setHeight(STATUSBAR_HEIGHT);
        statusBar.setWidth(WIDTH);
        Text levelText = placeText("Lv. " + fileName.charAt(10), "Copperplate", STATUSBAR_HEIGHT, Color.BLACK, TextAlignment.LEFT, WIDTH);
        levelText.setX(0);
        levelText.setY(STATUSBAR_HEIGHT*3/4);
        myRoot.getChildren().addAll(statusBar, levelText);

        try {
            int lineNumber = 0;
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String[] line = sc.next().split("");
                for(int k = 0; k < line.length; k++){
                    Block block;
                    if(line[k].equals("1")){ //regular block
                        block = new Block(BRICK1, BLOCK_WIDTH, k*BLOCK_WIDTH, STATUSBAR_HEIGHT+lineNumber*BLOCK_HEIGHT);
                        root.getChildren().add(block.getImageView());
                        myBlocks.add(block);
                    }
                    else if(line[k].equals("2")){ // medium block
                        block = new Block(BRICK2, BLOCK_WIDTH, k*BLOCK_WIDTH, STATUSBAR_HEIGHT+lineNumber*BLOCK_HEIGHT);
                        root.getChildren().add(block.getImageView());
                        myBlocks.add(block);
                    }
                    else if(line[k].equals("3")){ //hard block
                        block = new Block(BRICK3, BLOCK_WIDTH, k*BLOCK_WIDTH, STATUSBAR_HEIGHT+lineNumber*BLOCK_HEIGHT);
                        root.getChildren().add(block.getImageView());
                        myBlocks.add(block);
                    }
                }
                lineNumber++;
            }
            sc.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();;
        }
    }

    /**
     * Places a bouncer in the middle, towards the bottom of the screen.
     * @param xVel - x Velocity at which the bouncer should be initialized with.
     * @param yVel - y Velocity at which the bouncer should be initialized with.
     */
    public void placeCenterBall(double xVel, double yVel){
        Bouncer initialBouncer = new Bouncer(myBouncerImage, BOUNCER_SIZE, WIDTH/2 - BOUNCER_SIZE/2, myPaddle.getY() - BOUNCER_SIZE);
        initialBouncer.setXVelocity(xVel);
        initialBouncer.setYVelocity(yVel);
        myBouncers.add(initialBouncer);
        myRoot.getChildren().add(initialBouncer.getImageView());
    }

    /**
     * Decides whether a bouncer has hit the paddle.
     * In poison mode:  Pauses game and flips the poisonTag to indicate that the user should lose a life.
     * Else: Updates the bouncer's velocities after collision.
     * @param bouncer
     */
    public void executePaddleCollision(Bouncer bouncer) {
        double x = bouncer.getX() + bouncer.getSize()/2;
        double y = bouncer.getY() + bouncer.getSize();
        //glitch causing poison mode to continuously cause the player to lose a life is because I accidentally put this
        // bit of code OUTSIDE of the if statement that checks whether the bouncer has touched the paddle
        if (myPaddle.contains(x,y)) {
            if(poisonTime != 0){ // poison timer running + paddle collision = lost life
                poisonTag = true;
                RUN_STATE = !RUN_STATE;
            }
            //bouncer.setY(myPaddle.getY() - bouncer.getSize() - 10);
            x = bouncer.getX() - myPaddle.getX()+myPaddle.getWidth()/2;
            double[] vel = findComponents(myBouncerSpeed, Math.toRadians(-((double)45*8/myPaddle.getWidth()*myPaddle.getWidth()*myPaddle.getWidth())*x*x*x+90));
            bouncer.setXVelocity(vel[0]);
            bouncer.setYVelocity(-vel[1]);
        }
    }

    private void executeWallCollision(Bouncer bouncer){
        if (bouncer.getX() <= 0 || bouncer.getX() >= (WIDTH - bouncer.getSize())) {
            bouncer.setXVelocity(-1*bouncer.getXVelocity());
        }
        else if (bouncer.getY() <= STATUSBAR_HEIGHT) {
            bouncer.setYVelocity(-1*bouncer.getYVelocity());
        }
        else if(bouncer.getY() >= HEIGHT - BOUNCER_SIZE && TOGGLE_FLOOR){
            bouncer.setYVelocity(-1*bouncer.getYVelocity());
        }
        else if(bouncer.getY() > HEIGHT){
            myBouncers.remove(bouncer);
            myRoot.getChildren().remove(bouncer);
        }
    }

    private void executeBlockCollision(Bouncer bouncer) {
        for(int k = 0; k < myBlocks.size(); k++){
            Block block = myBlocks.get(k);
            if(block.contains(bouncer.getX() + bouncer.getSize()/2 - 2, bouncer.getY())){ // TOP OF BALL
                bouncer.setYVelocity(-1*bouncer.getYVelocity());
                updateBlock(block);
            }
            else if(block.contains(bouncer.getX() + bouncer.getSize()/2, bouncer.getY() + bouncer.getSize())){ // BOTTOM OF BALL
                bouncer.setYVelocity(-1*bouncer.getYVelocity());
                updateBlock(block);
            }
            else if(block.contains(bouncer.getX(), bouncer.getY()+bouncer.getSize()/2)){ // RIGHT SIDE OF BALL
                bouncer.setXVelocity(-1*bouncer.getXVelocity());
                updateBlock(block);
            }
            else if(block.contains(bouncer.getX()+bouncer.getSize(), bouncer.getY()+bouncer.getSize()/2)){ // LEFT SIDE OF BALL
                bouncer.setXVelocity(-1*bouncer.getXVelocity());
                updateBlock(block);
            }
        }
    }

    private void updateBlock(Block block) {
        if(block.getFileName().equals(BRICK1)){
            myScore += 10;
            myBlocks.remove(block);
            myRoot.getChildren().remove(block.getImageView());
            Double random = Math.random()*100;
            int counter = 0; // make sure no more than one item is dropped from a block
            if(random <= poisonProbablity){
                Item item = new Item(POISON, BOUNCER_SIZE,block.getX()+ block.getWidth()/2, block.getY()+ block.getHeight()/2);
                myItems.add(item);
                myRoot.getChildren().add(item.getImageView());
                counter++;
            }
            random = Math.random()*100;
            if(random <= powerUpProbablity && counter==0){
                Item item = new Item(FREEZE, BOUNCER_SIZE,block.getX()+ block.getWidth()/2, block.getY()+ block.getHeight()/2);
                myItems.add(item);
                myRoot.getChildren().add(item.getImageView());
                counter++;
            }
            random = Math.random()*100;
            if(random <= powerUpProbablity && counter==0){
                Item item = new Item(MULTIPLIER, BOUNCER_SIZE,block.getX()+ block.getWidth()/2, block.getY()+ block.getHeight()/2);
                myItems.add(item);
                myRoot.getChildren().add(item.getImageView());
                counter++;
            }
            random = Math.random()*100;
            if(random <= powerUpProbablity && counter==0) {
                Item item = new Item(ENLARGER, BOUNCER_SIZE, block.getX() + block.getWidth() / 2, block.getY() + block.getHeight() / 2);
                myItems.add(item);
                myRoot.getChildren().add(item.getImageView());
                counter++;
            }

        }
        else if(block.getFileName().equals(BRICK2)){
            myScore += 20;
            block.setImage(BRICK1);
        }
        else if(block.getFileName().equals(BRICK3)){
            myScore += 30;
            block.setImage(BRICK2);
        }
    }

    /**
     * "Activates" item by checking which kind of item it is, updating the score, and then implementing the item's
     * effects within the level.
     * @param item - item desired to be activated
     */
    private void activateItem(Item item){
        if(item.getFileName() == MULTIPLIER){
            activateMultiplier();
            setBouncer(myBouncerImage);
            myScore += 50;
        }
        if(item.getFileName() == ENLARGER){
            myPaddle.setWidth(myPaddle.getWidth()*1.1);
            myScore += 50;
        }
        if(item.getFileName() == FREEZE){
            myScore += 50;
            freezeTime = System.currentTimeMillis();
            for(Bouncer bouncer : myBouncers){
                bouncer.setXVelocity(bouncer.getXVelocity() * FREEZE_FACTOR);
                bouncer.setYVelocity(bouncer.getYVelocity() * FREEZE_FACTOR);
            }
        }
        if(item.getFileName() == POISON){
            myScore -= 200;
            poisonTime = System.currentTimeMillis();
            for(Bouncer bouncer : myBouncers){
                bouncer.setImage((POISON_BOUNCER)); //accidentally put the wrong file name here, should've switched the bouncers to the poison bouncers
            }
            TOGGLE_FLOOR = true; //the floor being present is a critical part of poison mode that I forgot
        }

        myItems.remove(item);
        myRoot.getChildren().remove(item.getImageView());
    }

    private void activateMultiplier(){
        for(int k = 0; k < MULT_FACTOR; k++){
            Bouncer bouncer = new Bouncer(myBouncerImage, BOUNCER_SIZE, myPaddle.getX()+PADDLE_WIDTH/2, myPaddle.getY()-BOUNCER_SIZE);
            bouncer.setX(myPaddle.getX()+PADDLE_WIDTH/2);
            bouncer.setY(myPaddle.getY()-BOUNCER_SIZE);
            double[] vel = findComponents(myBouncerSpeed, 45+ k*90/MULT_FACTOR);
            bouncer.setXVelocity(vel[0]);
            bouncer.setYVelocity(vel[1]);
            myBouncers.add(bouncer);
            myRoot.getChildren().add(bouncer.getImageView());
        }
    }

    private ArrayList<Node> setupPauseMenu(){
        ImageView pauseIcon = placeImage(PAUSE_ICON, WIDTH/3);
        Text pauseText = placeText("Press SPACEBAR to continue", FONT_STYLE, 40, Color.ORANGERED, TextAlignment.CENTER, WIDTH - 50);
        pauseText.setY(pauseIcon.getY()+pauseIcon.getBoundsInLocal().getHeight() + 100);
        Text pauseTitle = placeText("PAUSED", FONT_STYLE, 75, Color.ORANGERED, TextAlignment.CENTER, WIDTH);
        pauseTitle.setY(pauseIcon.getY() - pauseTitle.getBoundsInLocal().getHeight());
        ArrayList<Node> list = new ArrayList<>();
        list.add(pauseIcon);
        list.add(pauseText);
        list.add(pauseTitle);
        return list;
    }

    private ImageView placeImage(String name, int width){
        ImageView IV = new ImageView();
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(name));
        IV.setImage(image);
        IV.setPreserveRatio(true);
        IV.setFitWidth(width);
        IV.setX(WIDTH/2 - IV.getBoundsInLocal().getWidth()/2);
        IV.setY(HEIGHT/2 - IV.getBoundsInLocal().getHeight()/2);
        return IV;
    }

    /**
     * @param content - String that contains text to be displayed
     * @param font - String indicating what font style the text is to be written in
     * @param fontSize - size of the text
     * @param color - color of the text
     * @param alignment - alignment of the text
     * @param wrappingWidth - width at which text goes into next line
     *
     * @return Text that has been created
     */
    public Text placeText(String content, String font, int fontSize, Color color, TextAlignment alignment, int wrappingWidth){
        Text text = new Text(content);
        text.setTextAlignment(alignment);
        text.setWrappingWidth(wrappingWidth);
        text.setX(WIDTH/2 - text.getBoundsInLocal().getWidth()/2);
        text.setY(HEIGHT/2 - text.getBoundsInLocal().getHeight()/2);
        text.setFont(Font.font(font, fontSize));
        text.setFill(color);
        return text;
    }

    /**
     * Gives the root of the level so that the scene may be updated within RunBreakOut
     * @return Root of the level
     */
    public Group getRoot(){
        return myRoot;
    }

    /**
     * Returns Paddle that is being used in the level so that RunBreakOut class can change its location in response to
     * keyboard input
     * @return the Level's Paddle
     */
    public Paddle getPaddle(){
        return myPaddle;
    }

    /**
     * Switches boolean indicating whether there is a "floor" present that prevents the bouncer from falling off. Each level always begins
     * with no floor present
     */
    public void toggleFloor(){
        TOGGLE_FLOOR = !TOGGLE_FLOOR;
    }

    /**
     * Switches boolean indicating whether the Level will update the objects' positions within the stepThrough method.
     *
     */
    public void toggleRunState(){
        RUN_STATE = !RUN_STATE;
    }

    /**
     * Switches boolean indicating whether the Level will update the objects' positions within the stepThrough method.
     * Unlike togglePause(), also adds or removes Pause Menu from screen.
     */
    public void togglePause(){
        if(RUN_STATE){
            for(Node node : pauseMenuImages){
                myRoot.getChildren().add(node);
            }
        }
        else{
            for(Node node : pauseMenuImages){
                myRoot.getChildren().remove(node);
            }
        }
        RUN_STATE = !RUN_STATE;
    }

    /**
     * @return String representing the name of the file from which the level was loaded.
     */
    public String getFileName(){
        return myFileName;
    }

    /**
     * @return an int representing the number of blocks left within the level. This will be used by the main RunBreakOut
     * class to decide whether the next level should be loaded.
     */
    public int getNumberOfBlocks(){
        return this.myBlocks.size();
    }

    private double[] findComponents(double Magnitude, double angle){
        return new double[] {Magnitude*Math.cos(Math.toRadians(angle)), Magnitude*Math.sin(Math.toRadians(angle))};
    }

    /**
     * Switches the image of the bouncers being used within the level.
     * @param name - String representing the file from which the bouncer's new image should come from
     */
    public void setBouncer(String name){
        for(Bouncer bouncer : myBouncers){
            bouncer.setImage(name);
        }
    }

    /**
     * Sets the image of the "main bouncer". This should be expected to be called by the main class when initializing
     * every level. This will only be used to set the bouncers' images to that chosen by the player in the start menu.
     * @param fileName
     */
    public void setMainBouncer(String fileName){
        myBouncerImage = fileName;
        setBouncer(myBouncerImage);
    }

}
