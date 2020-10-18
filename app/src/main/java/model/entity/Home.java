package model.entity;

/**
 * @author tyhj
 * @date 2020/10/18
 * @Description: java类作用描述
 */

public class Home {

    private int type;

    private String imageUrl;

    public Home(int type, String imageUrl) {
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
