package org.jala.university.presentation.EntityView;

import org.jala.university.domain.entity.CreditCard;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;

public class CreditCardGeneratorView {
    private boolean isFront = true;
    private String cardType;
    private String customerName;
    private CreditCard creditCard;

    public CreditCardGeneratorView(String customerName, CreditCard creditCard) {
        this.customerName = customerName;
        this.creditCard = creditCard;
        this.cardType = creditCard.getCreditCardType().getTypeName();
    }

    public Pane generateCard() {
        Pane root = new Pane();
        Canvas canvas = new Canvas(400, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawCreditCardFront(gc, cardType);

        canvas.setOnMouseClicked((MouseEvent event) -> {
            if (isFront) {
                drawCreditCardBack(gc, cardType);
            } else {
                drawCreditCardFront(gc, cardType);
            }
            isFront = !isFront;
        });

        root.getChildren().add(canvas);
        return root; 
    }

    private void drawCreditCardFront(GraphicsContext gc, String cardType) {
        Color backgroundColor;
        Color circleColor1;
        Color circleColor2;

        switch (cardType) {
            case "Basic":
                backgroundColor = Color.LIGHTGRAY;
                circleColor1 = Color.DARKGRAY;
                circleColor2 = Color.GRAY;
                break;
            case "Gold":
                backgroundColor = Color.GOLD;
                circleColor1 = Color.ORANGE;
                circleColor2 = Color.DARKORANGE;
                break;
            case "Black":
                backgroundColor = Color.BLACK;
                circleColor1 = Color.DARKGRAY;
                circleColor2 = Color.GRAY;
                break;
            case "Platinum (Personal)":
                backgroundColor = Color.SILVER;
                circleColor1 = Color.DARKSLATEGRAY;
                circleColor2 = Color.LIGHTGRAY;
                break;
            case "Platinum (Business)":
                backgroundColor = Color.DARKSLATEBLUE;
                circleColor1 = Color.DARKBLUE;
                circleColor2 = Color.SLATEBLUE;
                break;
            default:
                backgroundColor = Color.DARKSLATEBLUE;
                circleColor1 = Color.ORANGE;
                circleColor2 = Color.RED;
        }

        gc.setFill(backgroundColor);
        gc.fillRoundRect(0, 0, 400, 200, 20, 20);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Jala University", 30, 40);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 24));
        gc.fillText(creditCard.getCardNumber(), 30, 100);

        gc.setFont(new Font("Arial", 18));
        gc.fillText(customerName, 30, 150);

        int month = creditCard.getExpirationDate().getMonthValue();
        int year = creditCard.getExpirationDate().getYear();
        String expiration = month + "/" + year;

        gc.setFont(new Font("Arial", 16));
        gc.fillText(expiration, 320, 150);

        gc.setFill(circleColor1);
        gc.fillOval(320, 30, 40, 40);
        gc.setFill(circleColor2);
        gc.fillOval(340, 30, 40, 40);
    }

    private void drawCreditCardBack(GraphicsContext gc, String cardType) {
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRoundRect(0, 0, 400, 200, 20, 20);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 40, 400, 40);

        gc.setFill(Color.WHITE); // Firma
        gc.fillRect(20, 110, 250, 30);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(20, 110, 250, 30);

        String cvv = "CVV: " + creditCard.getSecurityCode();

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 18));
        gc.fillText(cvv, 290, 130);

        gc.setFont(new Font("Arial", 10));
        gc.fillText("Not valid without signature", 20, 155);
    }
}
