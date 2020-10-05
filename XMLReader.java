package main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse um die Einzelnen Attribute auf unterschiedlichen Ebenen der XML-Datei einer Liste vom Typ Listeneintrag zu übergeben
 *
 * @author Mwangi Eric Zwyssig(s0568127) und Philipp Ferdinand Koch(s0565190)
 * @version 3.0
 * @since JavaSE-11
 */
public class XMLReader {

    /**
     * Dokument
     */
    private static Document doc;

    /**
     * XPath Factory
     */
    private static XPathFactory xFac = XPathFactory.newInstance();

    /**
     * XPath
     */
    private static XPath xPath = xFac.newXPath();

    /**
     * Liste mit Listeneinträge
     */
    private static List<ListEntry> list = new ArrayList<>();

    /**
     * ID
     */
    private static final String ID = "RNoPart";

    /**
     * Erstes Level der XML-Datei
     */
    private static final String LVL1 = "GAEB/Award/BoQ/BoQBody/BoQCtgy";

    /**
     * Zweites Level der XML-Datei
     */
    private static final String LVL2 = "GAEB/Award/BoQ/BoQBody/BoQCtgy[@" + ID + "='%s']/BoQBody/BoQCtgy";

    /**
     * Erstes und zweites level Überkategorie Kurztrext
     */
    private static final String LVL1A2_Description = "LblTx";

    /**
     * Drittes Level der XML-Datei
     */
    private static final String LVL3 = "GAEB/Award/BoQ/BoQBody/BoQCtgy[@" + ID + "='%s']/BoQBody/BoQCtgy[@" + ID + "='%s']/BoQBody/Itemlist/Item";

    /**
     * Kurztext des Inhaltes der dritten Ebene
     */
    private static final String LVL3_Description = "Description/CompleteText/OutlineText/OutlTxt/TextOutlTxt";

    /**
     * Einheit des Inhaltes der dritten Ebene
     */
    private static final String LVL3_Type = "QU";

    /**
     * Menge des Inhaltes der dritten Ebene
     */
    private static final String LVL3_Amount = "Qty";


    /**
     * Methode um Attribute der XML-Datei einer Liste zu übergeben
     *
     * @param path - Der Pfad für die Datei
     * @return list - Die Liste der x83 Datei
     * @throws XPathExpressionException - Fehler der Xpath expression
     */
    public List<ListEntry> getList(String path) throws XPathExpressionException {
        doc = getDocument(path);
        parseLvl1();
        return list;
    }

    /**
     * Methode um die Ordinalzahl und den Kurztext der Dateien auf der ersten Ebene einer Liste zu übergeben
     *
     * @throws XPathExpressionException - Fehler der Xpath expression
     */
    private static void parseLvl1() throws XPathExpressionException {
        NodeList lvl1 = (NodeList) xPath.compile(LVL1).evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lvl1.getLength(); i++) {
            String id = lvl1.item(i).getAttributes().getNamedItem(ID).getNodeValue();
            String desc = ((Node) xPath.compile(LVL1A2_Description).evaluate(lvl1.item(i), XPathConstants.NODE)).getTextContent();
            list.add(new ListEntry(id, desc));
            parseLvl2(id);
        }
    }

    /**
     * Methode um die Ordinalzahl und den Kurztext der Dateien auf der zweiten Ebene einer Liste zu übergeben
     *
     * @param lvl1 - Ordinalzahl der ersten Ebene
     * @throws XPathExpressionException - Fehler der Xpath expression
     */
    private static void parseLvl2(String lvl1) throws XPathExpressionException {
        NodeList lvl2 = (NodeList) xPath.compile(String.format(LVL2, lvl1)).evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lvl2.getLength(); i++) {
            String id = lvl2.item(i).getAttributes().getNamedItem(ID).getNodeValue();
            String desc = ((Node) xPath.compile(LVL1A2_Description).evaluate(lvl2.item(i), XPathConstants.NODE)).getTextContent();
            list.add(new ListEntry(lvl1 + "." + id, desc));
            parseLvl3(lvl1, id);
        }
    }

    /**
     * Methode um die Ordinalzahl, Kurztext, Einheit und Menge  der Dateien auf der dritten Ebene einer Liste zu übergeben
     *
     * @param lvl1 - Ordinalzahl der ersten Ebene
     * @param lvl2 - Ordinalzahl der zweiten Ebene
     * @throws XPathExpressionException - Fehler der Xpath expression
     */
    private static void parseLvl3(String lvl1, String lvl2) throws XPathExpressionException {
        NodeList lvl3 = (NodeList) xPath.compile(String.format(LVL3, lvl1, lvl2)).evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lvl3.getLength(); i++) {
            String id = lvl3.item(i).getAttributes().getNamedItem(ID).getNodeValue();
            String desc = ((Node) xPath.compile(LVL3_Description).evaluate(lvl3.item(i), XPathConstants.NODE)).getTextContent();
            String type = ((Node) xPath.compile(LVL3_Type).evaluate(lvl3.item(i), XPathConstants.NODE)).getTextContent();
            String amount = ((Node) xPath.compile(LVL3_Amount).evaluate(lvl3.item(i), XPathConstants.NODE)).getTextContent();
            list.add(new ListEntry(lvl1 + "." + lvl2 + "." + id, desc, type, amount));
        }
    }

    /**
     * Documenentbuilderfactory und Exception handling
     *
     * @param path - Vorher ermittelter Pfad
     * @return docBuild - Erstelltes Dokument
     * @throws RuntimeException - Fehler beim parsen der XML-Datei
     *
     */
    private static Document getDocument(String path) {
        DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuild = docFac.newDocumentBuilder();
            return docBuild.parse(new File(path));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Fehler beim parsen der XML-Datei.");
    }
}


