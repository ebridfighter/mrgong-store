package uk.co.ribot.androidboilerplate.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dong on 2017/11/10.
 */

public class Image implements Parcelable{
    private String imageMedium;
    private String image;
    private String imageSmall;

    public String getImageMedium() {
        return imageMedium;
    }

    public String getImage() {
        return image;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageMedium);
        dest.writeString(this.image);
        dest.writeString(this.imageSmall);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.imageMedium = in.readString();
        this.image = in.readString();
        this.imageSmall = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
