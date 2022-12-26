package Controller.Database;

import javafx.scene.image.Image;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();
    private String pseudo;
    private Image img;

    private  DataSingleton() {}

    public static DataSingleton getInstance() {
        return instance;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
