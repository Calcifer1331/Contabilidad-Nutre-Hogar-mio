/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nutrehogar.sistemacontable.ui.JComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class PanelDegradado extends JPanel {

    public int getOpacidadBorder() {
        return opacidadBorder;
    }

    public void setOpacidadBorder(int opacidadBorder) {
        this.opacidadBorder = opacidadBorder;
    }

    public int getOpacidadInical() {
        return opacidadInical;
    }

    public void setOpacidadInical(int opacidadInical) {
        this.opacidadInical = opacidadInical;
    }

    public int getOpacidadFinal() {
        return opacidadFinal;
    }

    public void setOpacidadFinal(int opacidadFinal) {
        this.opacidadFinal = opacidadFinal;
    }

    private int opacidadInical;
    private int opacidadFinal;
    private int borderRedondeado;
    private int grosorBorde;
    private int opacidadBorder;

    public int getBorderRedondeado() {
        return borderRedondeado;
    }

    public void setBorderRedondeado(int borderRedondeado) {
        this.borderRedondeado = borderRedondeado;
    }

    public int getGrosorBorde() {
        return grosorBorde;
    }

    public void setGrosorBorde(int grosorBorde) {
        this.grosorBorde = grosorBorde;
    }

    public PanelDegradado() {
        setOpaque(false);
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        setOpacidadBorder(25);
        setOpacidadInical(60);
        setOpacidadFinal(60);
        setBorderRedondeado(30);
        setGrosorBorde(1);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        Point2D pt1 = new Point2D.Double(((double) getWidth() / 2), 0);
        Point2D pt2 = new Point2D.Double(((double) getWidth() / 2), height);
        // Dibuja el fondo con opacidad
        GradientPaint gradien = new GradientPaint(pt1, new Color(0, 0, 0, getOpacidadInical()), pt2, new Color(0, 0, 0, getOpacidadFinal()));
        g2.setPaint(gradien);
        g2.fillRoundRect(0, 0, width, height, getBorderRedondeado(), getBorderRedondeado());

        // Dibuja el borde blanco
        if (getGrosorBorde() > 0) {
            g2.setColor(new Color(250, 250, 250, getOpacidadBorder()));
            g2.setStroke(new java.awt.BasicStroke(getGrosorBorde()));
            g2.draw(new RoundRectangle2D.Double(getGrosorBorde() / 2.0, getGrosorBorde() / 2.0, width - getGrosorBorde(), height - getGrosorBorde(), getBorderRedondeado(), getBorderRedondeado()));
        }
        super.paintComponent(grphcs);
    }
}
