package view.signupsigninGUI;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AvatarComponent extends StackPane {

    public AvatarComponent(double size) {
        Circle bg = new Circle(size / 2);
        bg.setFill(Color.rgb(110, 90, 150));

        Circle head = new Circle(size / 6);
        head.setTranslateY(-size / 6);
        head.setFill(Color.rgb(30, 30, 30));

        Circle body = new Circle(size / 4);
        body.setTranslateY(size / 6);
        body.setFill(Color.rgb(30, 30, 30));

        getChildren().addAll(bg, head, body);
        setPrefSize(size, size);
    }
}


