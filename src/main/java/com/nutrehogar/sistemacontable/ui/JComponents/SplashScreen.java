package com.nutrehogar.sistemacontable.ui.JComponents;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SplashScreen extends JWindow {
    private float opacity = 0f;
    private final String[] SPLASH_SCREEN_TITLE = {
            "birch-tree-5571242.svg",
            "bird-7937942.svg",
            "frog-7001629.svg",
            "geometric-7719159.svg",
            "girl-8435339.svg",
            "homes-8194751.svg",
            "house-7497002.svg",
            "hut-6968718.svg",
            "leaves-6975462.svg",
            "mosque-8008801.svg",
            "plant-7413415.svg",
            "shooting-star-2024127.svg",
            "sign-post-5655110.svg",
            "squirrel-6664212.svg"
    };

    public String getRandomSplashScreenTitle() {
        Random random = new Random();
        int randomIndex = random.nextInt(SPLASH_SCREEN_TITLE.length); // Genera un índice aleatorio dentro del rango del array
        return SPLASH_SCREEN_TITLE[randomIndex]; // Devuelve el valor aleatorio seleccionado
    }

    public SplashScreen() {
        String img = "svgs/splashScreen/"+getRandomSplashScreenTitle();
        FlatSVGIcon banner = new FlatSVGIcon(img); // Carga la imagen
        System.out.println(img);
        System.out.println(banner.getName());
        JLabel label = new JLabel(banner);
        label.setBackground(Color.WHITE);
        getContentPane().add(label, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
        setSize(banner.getIconWidth(), banner.getIconHeight());
        setLocationRelativeTo(null);
    }
}