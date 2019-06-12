package MainPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle {
    private String myFileName;
    private ImageView myImage;
    public double mySpeed;

    /**
     /**
     * @param myFileName - String containing name of file of picture to be used
     * @param myWidth - Width the Item is to be given
     * @param myX - x Position
     * @param myY - y Position
     * @param mySpeed - speed at Width paddle moves when arrow get pressed
     */
    Paddle(String myFileName, double myWidth, double myX, double myY, double mySpeed){
        this.myFileName = myFileName;
        this.myImage = new ImageView(myFileName);
        myImage.setPreserveRatio(true);
        myImage.setFitWidth(myWidth);
        this.setX(myX);
        this.setY(myY);
        this.mySpeed = mySpeed;
    }

    /**
     * Sets the image of the paddle to another specified image. Could later be used implement paddle selection
     * @param myFileName - String representing file from which the new image will be taken
     */
    public void setImage(String myFileName){
        myImage.setImage(new Image(myFileName));
        this.myFileName = myFileName;
    }

    /**
     * Sets the x position.
     * @param myX - new x position
     */
    public void setX(double myX){
        myImage.setX(myX);
    }
    /**
     * Sets the y position.
     * @param myY - new y position
     */
    public void setY(double myY){
        myImage.setY(myY);
    }

    /**
     * Sets the width of the paddle to specified height
     * @param width - width of the paddle
     */
    public void setWidth(double width){
        myImage.setFitWidth(width);
    }
    /**
     * Returns the current x position.
     * @return - current x position
     */
    public double getX(){
        return myImage.getX();
    }
    /**
     * Returns the current y position.
     * @return - current y position
     */
    public double getY(){
        return myImage.getY();
    }
    /**
     * Returns the ImageView being used by the Paddle.
     * @return - ImageView used by Paddle
     */
    public ImageView getImageView(){
        return myImage;
    }

    /**
     * Returns the width of the paddle
     * @return width of paddle
     */
    public double getWidth(){
        return myImage.getBoundsInLocal().getWidth();
    }

    /**
     * Returns the height of the paddle
     * @return height of paddle
     */
    public double getHeight(){
        return myImage.getBoundsInLocal().getHeight();
    }

    /**
     * Returns doubel representing speed of paddle.
     * @return paddle speed
     */
    public double getSpeed(){
        return mySpeed;
    }

    /**
     * Returns boolean representing whether or not the point is contained within the space of the Paddle.
     * @param x - x coordinate of point in question
     * @param y - y coordinate of point in question
     * @return - boolean of true or false representing whether point is within space of Paddle
     */
    public boolean contains(double x, double y){
        return myImage.contains(x, y);
    }

    /**
     * Returns boolean representing whether or not the specified Item is touching the Paddle
     * @param item - item whose intersection you want to check
     */
    public boolean intersects(Item item){
        if(item.getX()>=this.getX() && item.getX() <= this.getX()+this.getWidth()
        && item.getY()>=this.getY() && item.getY() <= this.getY()+this.getHeight()){
            return true;
        }
        return false;
    }

    /**
     * returns the file name of the image used by the Paddle.
     * @return - String of the file name
     */
    public String getFileName(){
        return myFileName;
    }
}
