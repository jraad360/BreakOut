package MainPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
    private String myFileName;
    private ImageView myImage;

    /**
     * @param myFileName - String containing name of file of picture to be used.
     * @param myWidth - Width the Item is to be given
     * @param myX - x Position
     * @param myY - y Position
     */
    Block(String myFileName, double myWidth, double myX, double myY){
        this.myFileName = myFileName;
        this.myImage = new ImageView(myFileName);
        myImage.setPreserveRatio(true);
        myImage.setFitWidth(myWidth);
        this.setX(myX);
        this.setY(myY);
    }

    /**
     * Sets the image of the block to another specified image. Used to change blocks from a 3-hit block to a 2-hit block
     * and so on.
     * @param myFileName - String representing file from which the new image will be taken
     */
    public void setImage(String myFileName){
        myImage.setImage(new Image(myFileName));
        this.myFileName = myFileName;
    }

    /**
     * Sets the block's current x position to the specified coordinate. Used primarily by Level's stepThrough() function
     * when updating a block's position.
     * @param myX
     */
    public void setX(double myX){
        myImage.setX(myX);
    }

    /**
     * Sets the block's current y position to the specified coordinate.Used primarily by Level's stepThrough() function
     * when updating a block's position.
     * @param myY
     */
    public void setY(double myY){
        myImage.setY(myY);
    }

    /**
     * Returns the block's x coordinate.
     * @return double representing x coordinate
     */
    public double getX(){
        return myImage.getX();
    }

    /**
     * Returns the block's y coordinate.
     * @return double representing y coordinate
     */
    public double getY(){
        return myImage.getY();
    }

    /**
     * Returns ImageView of Bouncer
     * @return ImageView of Bouncer
     */
    public ImageView getImageView(){
        return myImage;
    }

    /**
     * Returns double representing width of Block.
     * @return width
     */
    public double getWidth(){
        return myImage.getBoundsInLocal().getWidth();
    }

    /**
     * Returns double representing height of Block.
     * @return height
     */
    public double getHeight(){
        return myImage.getBoundsInLocal().getHeight();
    }

    /**
     * Returns boolean representing whether or not the point is contained within the space of the Block.
     * @param x - x coordinate of point in question
     * @param y - y coordinate of point in question
     * @return - boolean of true or false representing whether point is within space of Block
     */
    public boolean contains(double x, double y){
        return myImage.contains(x, y);
    }

    /**
     *
     * @return
     */
    public String getFileName(){
        return myFileName;
    }
}
