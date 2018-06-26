package uk.co.ribot.androidboilerplate.data.model.net.response;

import java.io.Serializable;

/**
 * Created by mike on 2017/9/29.
 */

public class ImageResponse implements Serializable {
    /**
     * imageMedium : /gongfu/image/product/8/image_medium/
     * image : /gongfu/image/product/8/image/
     * imageSmall : /gongfu/image/product/8/image_small/
     * imageID : 8
     */
    private int id;
    private String imageMedium;
    private String image;
    private String imageSmall;
    private int imageID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
