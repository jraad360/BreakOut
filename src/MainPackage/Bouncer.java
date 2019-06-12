package MainPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bouncer {
    private String myFileName;
    private ImageView myImage;
    public double mySpeed;
    public double myXVelocity;
    public double myYVelocity;

    Bouncer(String myImageName, int mySize){
        this.myFileName = myImageName;
        this.myImage = new ImageView(myImageName);
        this.myImage.setPreserveRatio(true);
        this.myImage.setFitWidth(mySize);
    }
    Bouncer(String myImageName, int mySize, double myX, double myY){
        this.myFileName = myImageName;
        this.myImage = new ImageView(new Image(myImageName));
        myImage.setPreserveRatio(true);
        myImage.setFitWidth(mySize);
        myImage.setX(myX);
        myImage.setY(myY);
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
     * Return's the current x position.
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
     * Returns the x component of the bouncer's velocity
     * @return double representing x component of bouncer's velocity
     */
    public double getXVelocity(){
        return myXVelocity;
    }

    /**
     * Returns the y component of the bouncer's velocity
     * @return double representing y component of bouncer's velocity
     */
    public double getYVelocity(){
        return myYVelocity;
    }

    /**
     * Sets the x component of the velocity to the given value
     * @param myXVelocity - new x component of velocity
     */
    public void setXVelocity(double myXVelocity){
        this.myXVelocity = myXVelocity;
    }

    /**
     * Sets the y component of the velocity to the given value
     * @param myYVelocity - new y component of velocity
     */
    public void setYVelocity(double myYVelocity){
        this.myYVelocity = myYVelocity;
    }

    /**
     * Sets the Item's image to the image specified.
     * @param name - String containing name of file of picture to be used.
     */
    public void setImage(String name){
        myImage.setImage(new Image(name));
    }

    /**
     * Returns ImageView of Bouncer
     * @return ImageView of Bouncer
     */
    public ImageView getImageView(){
        return myImage;
    }

    /**
     * Returns double representing the bouncer's diameter
     * @return bouncer's size
     */
    public double getSize(){
        return myImage.getBoundsInLocal().getWidth();
    }
}
