package main.java.org.Updateable;

/**
 * Interface, das von Spielobjekten implementiert werden kann
 * (tudom, hogy szarul van leírva, de most már így hagyom)
 */
public interface Updateable {
    /**
     * Diese Funktion wird für solche Objekte gerufen, die in einem UpdateableManager registriert sind
     * @param deltaTime die Zeit, die nach dem letzten UpdateableManager-Anruf verging
     */
    void update(double deltaTime);
}
