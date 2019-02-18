package Twitter.Util;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;


@Singleton
//Singleton class that will manage users message displaying with a built fade in functionality
public class MessageHandler {
    public static MessageHandler messageHandler;
    protected static FadeTransition fadeIn;
    protected static FadeTransition fadeOut;

    //Lazy init for messageHandler
    public static  MessageHandler getMessageHandler() {
        if(messageHandler == null) {
            messageHandler = new MessageHandler();
            initTransitions();
        }

        return messageHandler;

    }

    @Methods
    //Method that will init the transition element to recreate a fade in/out look
    private static void initTransitions() {
        fadeIn = new FadeTransition(Duration.seconds(2));
        fadeOut = new FadeTransition(Duration.seconds(2));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
    }

    //Method that display a message with the given color at the given label
    public void displayMessage(String message, Color color, Label communicationLabel) {
        communicationLabel.setText(message);
        communicationLabel.setTextFill(color);
        fadeIn.setNode(communicationLabel);
        fadeOut.setNode(communicationLabel);
        fadeIn.play();
        fadeOut.play();
    }

}
