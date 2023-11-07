package main.java.org;

import main.java.org.InputManagement.InputManager;
import main.java.org.LinearAlgebruh.Matrix3;

import main.java.org.LinearAlgebruh.Vector3;
import main.java.org.Render.Camera.Camera;
import main.java.org.Render.Drawables.*;
import main.java.org.Screens.GameScreen;
import main.java.org.Screens.MainFrame;
import main.java.org.Updateable.Player;
import main.java.org.Updateable.Updateable;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        new MainFrame("Morbius Motorsport 2024 - GOTY Edition");
        InputManager.hideCursor(MainFrame.currentFrame);

        ((MainFrame)MainFrame.currentFrame).start();

        //InputManager.showCursor(hehe);
    }
}