package main.gui;


import main.java.ListEntry;
import main.java.XMLReader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse um eine Grafische Benutzeroberfläche (GUI) aufzubauen
 *
 * @author Mwangi Eric Zwyssig(s0568127) und Philipp Ferdinand Koch(s0565190)
 * @version 3.0
 * @since JavaSE-11
 */
public class GUI extends JFrame {

    /**
     * Menubar
     */
    JMenuBar menuBar;

    /**
     * Gui
     */
    public static GUI gui;

    /**
     * Liste mit Listeneinträge
     */
    private List<ListEntry> listE;

    /**
     * Scrollbar
     */
    private JScrollPane sPanel;

    /**
     * Tabelle
     */
    private JTable table = new JTable();

    /**
     * Vordefinierte Überschrift der einzelnen Spalten
     */
    private static Object[] columnNames = {"Ordinalzahl(OZ)", "Menge", "Einheit", "Kurztext"};

    /**
     * Erzeugt GUI Main Panel
     *
     * @param list - Liste welche in Panel angezeigt wird
     */
    public GUI(List list) throws HeadlessException {
        super("Angewandte Programmierung - GAEB-Analyzer");
        listE = list;
        this.gui = this;
        setLocation(100, 100);
        setSize(1200, 800);
        initMenu();
        add(getMainPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Methode um eine Scrollbar hinzuzufügen (mit präferierter vorgegebener Größe)
     *
     * @return sPanel - Scrollbar und Größe setzen zum hoch und runter scrollen
     */
    private JScrollPane getMainPanel() {
        sPanel = new JScrollPane(fillTable(listE));
        sPanel.setPreferredSize(new Dimension(600, 700));
        return sPanel;
    }

    /**
     * Tabellen Zeilen und Spalten füllen mit Daten einer XML Datei (Spalten mit präferierter vorgegebener Größe)
     *
     * @param list - Liste mit Listeneinträge
     * @return table - Tabelle zurückgeben mit präferierter vorgegebener Größe.
     */
    private JTable fillTable(List<ListEntry> list) {
        Object[][] rowData = getData(list);
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(500);
        model.fireTableDataChanged();
        table.setFillsViewportHeight(true);
        return table;
    }

    /**
     * Methode um Daten einer Liste zu übergeben (Ohne Werte nach dem Komma)
     *
     * @param list - Liste mit Listeneinträge
     * @return data - Zweidimensionales Array um Spalten und Zeilen der Tabelle zu füllen
     */
    private Object[][] getData(List<ListEntry> list) {
        listE = list;
        Object[][] data = new Object[list.size()][4];
        int counter = 0;
        for (ListEntry eintrag : list) {
            data[counter][0] = " " + eintrag.getId();
            if (eintrag.getAmount() != null)
                data[counter][1] = " " + eintrag.getAmount().substring(0, eintrag.getAmount().length() - 4);
            if (eintrag.getType() != null)
                data[counter][2] = " " + eintrag.getType();
            data[counter][3] = " " + eintrag.getDescription();
            counter++;
        }
        return data;
    }

    /**
     * Methode um die Liste auf einer Festplatte zu speichern
     *
     * @param path - Um die Datei dort abzuspeichern
     * @throws IOException - Fehler beim in oder Output der Datei
     */
    private void writeData(String path) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (ListEntry eintrag : listE) {
            sb.append(eintrag.getId() + "," + eintrag.getAmount() + "," + eintrag.getType() + ","
                    + eintrag.getDescription() + "\n");
        }
        try (FileWriter fr = new FileWriter(new File(path))) {
            fr.write(sb.toString());
            fr.flush();
        }
    }

    /**
     * Methode um ein popup Fenster zu öffnen, worauf sich untere Jmenuitems öffnen
     */
    private void initMenu() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Datei");
        menuBar.add(menu);
        JMenuItem deleteTable = new JMenuItem("Neu");
        JMenuItem openTable = new JMenuItem("Öffnen");
        JMenuItem saveTable = new JMenuItem("Speichern");
        JMenuItem endProcess = new JMenuItem(" Beenden");

        Border empty = BorderFactory.createEmptyBorder(0, -1, -1, -1);
        Border dashed = BorderFactory.createDashedBorder(null, 1, 1);
        Border compound = new CompoundBorder(empty, dashed);
        endProcess.setBorder(compound);

        menu.add(deleteTable);
        menu.add(openTable);
        menu.add(saveTable);
        menu.add(endProcess);

        emptyTableListener(deleteTable);
        openTableListener(openTable);
        saveTableListener(saveTable);
        endProcessListener(endProcess);

        setJMenuBar(menuBar);
    }

    /**
     * Methode um die aktuell geöffnete Tabelle zu löschen
     *
     * @param deleteTable - Um Listener zu übergeben
     */
    private void emptyTableListener(JMenuItem deleteTable) {
        deleteTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTable(new ArrayList<ListEntry>());
            }
        });
    }

    /**
     * Methode um eine Datei zu öffnen und den Dateiname als Überschrift anzuzeigen
     *
     * @param openTable - Um Listener zu übergeben
     */
    private void openTableListener(JMenuItem openTable) {
        openTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path;
                    do {
                        path = JOptionPane.showInputDialog(openTable,
                                "Dateipfad von .x83 Datei eingeben: z.B \"D:\\Users\\Mwangi\\Desktop\\Projekt (1)\\asdf.x83\"", null);
                        if (GUI.checkFile(path) == false) {
                            JOptionPane.showMessageDialog(gui,
                                    "Pfad oder Datei nicht vorhanden. Oder ungültiges Dateiformat.");
                        }
                    } while (GUI.checkFile(path) == false);
                    String[] dataName = path.replaceAll("/", File.pathSeparator).split(File.pathSeparator);
                    String title = "Angewandte Programmierung - " + dataName[dataName.length - 1];
                    gui.setTitle(title);
                    fillTable(new XMLReader().getList(path));
                } catch (NullPointerException nullPointerException) {
                    JOptionPane.showMessageDialog(gui,
                            "Vorgang wurde abgebrochen.");
                } catch (XPathExpressionException fileNotFoundException) {
                    JOptionPane.showMessageDialog(gui,
                            "Datei konnte nicht geöffnet werden.");
                } catch (RuntimeException rte) {
                    JOptionPane.showMessageDialog(gui,
                            "Ungültige eingabe. Bitte erneut eingeben.");
                }
            }

        });
    }

    /**
     * Überprüft ob Path eine x83 Datei enthält
     *
     * @param path - Der zu überprüfende Dateipfad inklusive Datei
     * @return true - Wenn .x83 Datei existiert, false wenn keine x.83 Datei existiert
     */
    private static boolean checkFile(String path) {
        File tmpDir = new File(path);
        boolean exists = tmpDir.isFile();
        if (exists == true) {
            if (path.endsWith("x83") == false) {
                exists = false;
            }
        }
        return exists;

    }

    /**
     * Überprüft ob der Pfad existiert
     *
     * @param path - Der zu überprüfende Dateipfad inklusive Datei
     * @return true - Wenn Datei existiert, false - Wenn keine Datei existiert
     */
    private static boolean checkPath(String path) {
        File tmpDir = new File(path);
        boolean exists = tmpDir.exists();
        return exists;

    }


    /**
     * Methode um die aktuell geöffnete Tabelle als .csv Datei Abzuspeichern
     *
     * @param saveTable - Um Listener zu übergeben
     */
    private void saveTableListener(JMenuItem saveTable) {
        saveTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path;
                String DateiName;
                try {
                    do {
                        path = JOptionPane.showInputDialog(
                                "Pfad angeben z.b \"D:\\Users\\Mwangi\\Desktop\\Projekt\\\"", null);
                        if (GUI.checkPath(path) == false) {
                            JOptionPane.showMessageDialog(gui,
                                    "Pfad nicht vorhanden bitte korrekten Pfad eingeben");
                        }
                    } while (checkPath(path) == false);
                    DateiName = JOptionPane.showInputDialog(
                            "Dateinamen eingeben Z.b: \"CsvDoku\"", null);
                    writeData(path + "\\" + DateiName + ".csv");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(gui,
                            "Datei konnte nicht geschrieben werden." + ioException.getMessage());
                } catch (NullPointerException nullPointerException) {
                    JOptionPane.showMessageDialog(gui,
                            "Vorgang wurde abgebrochen.");
                }
            }
        });
    }

    /**
     * Methode um die Grafische Benutzeroberfläche zu schließen
     *
     * @param endProcess - Um Listener zu übergeben
     */
    private void endProcessListener(JMenuItem endProcess) {
        endProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
            }
        });
    }
}