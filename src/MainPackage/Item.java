package MainPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {

    private String myFileName;
    private ImageView myImage;

    /**
     * @param myFileName - String containing name of file of picture to be used.
     * @param myWidth - Width the Item is to be given
     * @param myX - x Position
     * @param myY - y Position
     */
    Item(String myFileName, double myWidth, double myX, double myY){
        this.myFileName = myFileName;
        this.myImage = new ImageView(new Image(myFileName));
        this.myImage.setX(myX);
        this.myImage.setY(myY);
        this.myImage.setPreserveRatio(true);
        this.myImage.setFitWidth(myWidth);
    }

    /**
     * Sets the Item's image to the image specified.
     * @param myFileName - String containing name of file of picture to be used.
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
     * Returns the ImageView being used by the Item.
     * @return - ImageView used by Item
     */
    public ImageView getImageView(){
        return myImage;
    }

    /**
     * Returns boolean representing whether or not the point is contained within the space of the Item.
     * @param x - x coordinate of point in question
     * @param y - y coordinate of point in question
     * @return - boolean of true or false representing whether point is within space of Item
     */
    public boolean contains(double x, double y){
        return myImage.contains(x, y);
    }

    /**
     * returns the file name of the image used by the Item.
     * @return - String of the file name
     */
    public String getFileName(){
        return myFileName;
    }

}
