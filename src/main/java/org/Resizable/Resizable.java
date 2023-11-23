package main.java.org.Resizable;

/**
 * Ein Interface, das bei der Einstellung der Fenstergröße hilft
 */
public interface Resizable {

    /**
     * Diese Funktion kann aufgerufen werden, falls die Fenstergröße verändert wird
     * @param width die neue Breite des Fensters
     * @param height die neue Höhe des Fensters
     */
    void onResize(int width, int height);
}