package gallery.decode.com.gallery;

import android.graphics.Color;

/**
 * Created by lucian.cioroga on 3/7/2018.
 */

public class Media {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private String mName;
    private int mType;
    private int mColor;

    public Media(int type, String name, String color) {
        mType = type;
        mName = name;
        mColor = Color.parseColor(color);
    }

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public int getColor() {
        return mColor;
    }

    public static Media[] getMedia(int type) {
        return type == TYPE_VIDEO ? getVideos() : getImages();
    }

    public static Media[] getImages() {
        return new Media[]{
                new Media(TYPE_IMAGE, "Image 1", "#338a3e"),
                new Media(TYPE_IMAGE, "Image 2", "#c0ca33"),
                new Media(TYPE_IMAGE, "Image 3", "#4dd0e1"),
                new Media(TYPE_IMAGE, "Image 4", "#52c7b8"),
                new Media(TYPE_IMAGE, "Image 5", "#455a64"),
                new Media(TYPE_IMAGE, "Image 6", "#c43e00"),
                new Media(TYPE_IMAGE, "Image 7", "#338a3e"),
                new Media(TYPE_IMAGE, "Image 8", "#009688"),
                new Media(TYPE_IMAGE, "Image 9", "#c8a600"),
                new Media(TYPE_IMAGE, "Image 10", "#ff6f00"),
                new Media(TYPE_IMAGE, "Image 11", "#33691e"),
                new Media(TYPE_IMAGE, "Image 12", "#000051"),
                new Media(TYPE_IMAGE, "Image 13", "#338a3e"),
                new Media(TYPE_IMAGE, "Image 14", "#c7b800"),
                new Media(TYPE_IMAGE, "Image 15", "#338a3e"),
                new Media(TYPE_IMAGE, "Image 16", "#ec407a"),
                new Media(TYPE_IMAGE, "Image 17", "#5d4037"),
                new Media(TYPE_IMAGE, "Image 18", "#c8a600"),
                new Media(TYPE_IMAGE, "Image 19", "#338a3e"),
                new Media(TYPE_IMAGE, "Image 20", "#aa00ff"),
                new Media(TYPE_IMAGE, "Image 21", "#c6ff00"),
                new Media(TYPE_IMAGE, "Image 22", "#009688"),
                new Media(TYPE_IMAGE, "Image 23", "#ff3d00"),
                new Media(TYPE_IMAGE, "Image 24", "#dce775"),
                new Media(TYPE_IMAGE, "Image 25", "#673ab7"),
                new Media(TYPE_IMAGE, "Image 26", "#00675b")
        };
    }

    public static Media[] getVideos() {
        return new Media[]{
                new Media(TYPE_VIDEO, "Video 1", "#5d4037"),
                new Media(TYPE_VIDEO, "Video 2", "#338a3e"),
                new Media(TYPE_VIDEO, "Video 3", "#ff6f00"),
                new Media(TYPE_VIDEO, "Video 4", "#007ac1"),
                new Media(TYPE_VIDEO, "Video 5", "#c8a600"),
                new Media(TYPE_VIDEO, "Video 6", "#4dd0e1"),
                new Media(TYPE_VIDEO, "Video 7", "#ec407a"),
                new Media(TYPE_VIDEO, "Video 8", "#000051"),
                new Media(TYPE_VIDEO, "Video 9", "#009688"),
                new Media(TYPE_VIDEO, "Video 10", "#338a3e"),
                new Media(TYPE_VIDEO, "Video 11", "#52c7b8"),
                new Media(TYPE_VIDEO, "Video 12", "#dce775"),
                new Media(TYPE_VIDEO, "Video 13", "#338a3e"),
                new Media(TYPE_VIDEO, "Video 14", "#c7b800"),
                new Media(TYPE_VIDEO, "Video 15", "#673ab7"),
                new Media(TYPE_VIDEO, "Video 16", "#00675b"),
                new Media(TYPE_VIDEO, "Video 17", "#c43e00"),
                new Media(TYPE_VIDEO, "Video 18", "#82f7ff"),
                new Media(TYPE_VIDEO, "Video 19", "#006978"),
                new Media(TYPE_VIDEO, "Video 20", "#9c64a6"),
                new Media(TYPE_VIDEO, "Video 21", "#f44336"),
                new Media(TYPE_VIDEO, "Video 22", "#aa00ff"),
                new Media(TYPE_VIDEO, "Video 23", "#ce93d8"),
                new Media(TYPE_VIDEO, "Video 24", "#c8a600"),
                new Media(TYPE_VIDEO, "Video 25", "#ff7543"),
                new Media(TYPE_VIDEO, "Video 26", "#455a64"),
                new Media(TYPE_VIDEO, "Video 27", "#338a3e"),
                new Media(TYPE_VIDEO, "Video 28", "#c0ca33"),
                new Media(TYPE_VIDEO, "Video 29", "#ff3d00"),
                new Media(TYPE_VIDEO, "Video 30", "#c6ff00"),
                new Media(TYPE_VIDEO, "Video 31", "#d84315"),
                new Media(TYPE_VIDEO, "Video 32", "#f44336"),
                new Media(TYPE_VIDEO, "Video 33", "#33691e"),
                new Media(TYPE_VIDEO, "Video 34", "#4c8c4a"),
                new Media(TYPE_VIDEO, "Video 35", "#003300"),
                new Media(TYPE_VIDEO, "Video 36", "#9f0000"),
        };
    }
}
