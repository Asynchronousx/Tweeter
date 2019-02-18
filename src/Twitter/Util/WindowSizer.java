package Twitter.Util;

import javafx.stage.Stage;

public interface WindowSizer {
    default void anchorSize(Stage stage, int minW, int minH, int maxW, int maxH) {
        stage.setMinWidth(minW);
        stage.setMinHeight(minH);
        stage.setMaxWidth(maxW);
        stage.setMaxHeight(maxW);
    }
}
