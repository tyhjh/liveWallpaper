package model.entity;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Tyhj on 2017/9/6.
 */
@Table("wallpaper_now")
public class WallPaperNow {

    @PrimaryKey(AssignType.BY_MYSELF)
    int name;

    @NotNull
    int id;

    @NotNull
    int type;

    int resource;

    String path;

    public WallPaperNow(int id, int type, int resource, String path) {
        name=1;
        this.id = id;
        this.type = type;
        this.resource = resource;
        this.path = path;
    }


    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
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

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return "WallPaperNow{" +
                "name=" + name +
                ", id=" + id +
                ", type=" + type +
                ", resource=" + resource +
                ", path='" + path + '\'' +
                '}';
    }
}
