package com.tekeoglan.helper;

import java.awt.*;

public class Helper {
    public static int getScreenCenterCoordinates(char dimension, Dimension size) {
        int point;
        switch (dimension) {
            case 'x':
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case 'y':
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                System.out.println("Please give a valid dimension.");
                point = 0;
                break;
        }

        return point;
    }
}
