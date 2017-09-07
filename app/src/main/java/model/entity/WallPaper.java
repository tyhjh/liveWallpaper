package model.entity;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by Tyhj on 2017/5/26.
 */

@Table("wall_paper")
public class WallPaper implements Serializable{



    @PrimaryKey(AssignType.BY_MYSELF)
    int id;

    @NotNull
    int type;

    @NotNull
    String name;

    String image;

    String mv;

    String imagePath;

    String dataPath;

    String preview;


    public WallPaper(int id, int type, String imageUrl, String name, String dataUrl, String imagePath, String dataPath,String previewUrl) {
        this.id = id;
        this.type = type;
        this.image = imageUrl;
        this.name = name;
        this.mv = dataUrl;
        this.imagePath = imagePath;
        this.dataPath = dataPath;
        this.preview=previewUrl;
    }


    @Override
    public boolean equals(Object obj) {
        WallPaper paper= (WallPaper) obj;
        if(paper==null)
            return false;
        if(paper.getId()==getId()){
            return true;
        }
        return false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMv() {
        return mv;
    }

    public void setMv(String mv) {
        this.mv = mv;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public String toString() {
        return "WallPaper{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", mv='" + mv + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", preview='" + preview + '\'' +
                '}';
    }
}
