package main.java;

/**
 * Klasse die genutzt wird um deren Attribute mit den der XML-Dateí zu füllen
 *
 * @author Mwangi Eric Zwyssig(s0568127) und Philipp Ferdinand Koch(s0565190)
 * @version 3.0
 * @since JavaSE-11
 */
public class ListEntry {

    /**
     * Ordinalzahl(OZ)
     */
    private String id;

    /**
     * Kurztext
     */
    private String description;

    /**
     * Einheit
     */
    private String type;

    /**
     * Menge
     */
    private String amount;

    /**
     * Konstruktor für Ebene 1 und 2 welche nur 2 Attribute haben
     *
     * @param id          - Die Ordinalzahl der x83 Datei
     * @param description - Der Kurztext der x83 Datei
     */
    public ListEntry(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Konstruktor für die Ebene 3 um alle Attribute auszugeben
     *
     * @param id          - Die Ordinalzahl
     * @param description - Kurztext
     * @param type        - Einheit
     * @param amount      - die Anzahl
     */
    public ListEntry(String id, String description, String type, String amount) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.amount = amount;
    }

    /**
     * Ordinalzahl(OZ) (Getter)
     *
     * @return id == Ordinalzahl
     */
    public String getId() {
        return id;
    }

    /**
     * Ordinalzahl(OZ) (Setter)
     *
     * @param id - Ordinalzahl
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Kurztext (Getter)
     *
     * @return description == Kurztext
     */
    public String getDescription() {
        return description;
    }

    /**
     * Kurztext (Setter)
     *
     * @param description - Kurztext
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Einheit (Getter)
     *
     * @return type == Einheit
     */
    public String getType() {
        return type;
    }

    /**
     * Einheit (Setter)
     *
     * @param type - Einheit
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Menge (Getter)
     *
     * @return amount == Anzahl
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Menge (Setter)
     *
     * @param amount - Anzahl
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
