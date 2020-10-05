package main.java;


import main.gui.GUI;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.util.List;

/**
 * Main Klasse um die Anwendung zu starten
 *
 * @author Mwangi Eric Zwyssig(s0568127) und Philipp Ferdinand Koch(s0565190)
 * @version 3.0
 * @since JavaSE-11
 */
public class Main {

    /**
     * Main Methode
     *
     * @throws XPathExpressionException - Fehler in xPath Expression
     */
    public static void main(String[] args) throws XPathExpressionException {

        /**
         * Einer Liste eine XML-Datei übergeben
         */
        List<?> list = new XMLReader().getList("src/main/resources/Muster-Ausschreibungs-LV-ErdMauerBetonarbeiten-xml32.x83");

        /**
         * GUI aufrufen
         */
        JFrame window = new GUI(list);

        /**
         * GUI sichtbar machen
         */
        window.setVisible(true);
    }
}

