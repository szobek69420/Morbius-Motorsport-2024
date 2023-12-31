package main.java.org.AudioManagement;

import main.java.org.Main;
import main.java.org.Settings.Settings;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Lasst andere Teile des Programmes Töne spielen
 */
public final class AudioManager {
    /**
     * Die Typen der Töne
     */
    public static enum SOUNDS{
        DEATH,
        JUMP,
        SPAWN,
        BLOCK_BREAKS,
        BUTTON_CLICK,
        MUSIC
    };
    /**
     * Die Liste solcher Töne, die in diesem Zeitpunkt spielen
     */
    private static ArrayList<Sound> activeSounds=new ArrayList<>();

    /**
     * Hinzufügt einen Ton zur Liste der aktiven Töne
     * @param sound der zu hinzufügene Ton
     */
    private static synchronized void addSound(Sound sound){
        activeSounds.add(sound);
    }
    /**
     * Löscht einen Ton von der Liste der aktiven Töne
     * @param sound der zu löschene Ton
     */
    private static synchronized void closeSound(Sound sound){
        if(!activeSounds.contains(sound))
            return;

        sound.clip.stop();
        sound.clip.close();
        activeSounds.remove(sound);
    }

    /**
     * Schliesst alle aktive Töne
     */
    public static synchronized void closeAll(){
        for (Sound s : activeSounds){
            s.clip.stop();
            s.clip.close();
        }
        activeSounds=new ArrayList<>();
    }

    /**
     * Schliesst nur die Effekttöne
     */
    public static synchronized void closeAllSfx(){
        for(int i=0;i<activeSounds.size();i++){
            if(activeSounds.get(i).type!=SOUNDS.MUSIC){
                activeSounds.get(i).clip.stop();
                activeSounds.get(i).clip.close();
                activeSounds.remove(i);
                i--;
            }
        }
    }

    /**
     * Schliesst nur die Musik
     */
    public static synchronized void closeMusic(){
        for(int i=0;i<activeSounds.size();i++){
            if(activeSounds.get(i).type==SOUNDS.MUSIC){
                activeSounds.get(i).clip.stop();
                activeSounds.get(i).clip.close();
                activeSounds.remove(i);
                i--;
            }
        }
    }

    /**
     * Das Interface für das Programm, Töne zu abspielen
     * @param sound der Typ des gewünschten Tones
     */
    public static void playSound(SOUNDS sound){
        if(sound!=SOUNDS.MUSIC&& !Settings.sfxOn())
            return;

        if(sound==SOUNDS.MUSIC&&!Settings.musicOn())
            return;

        switch (sound){
            case DEATH -> addSound(new Sound(SOUNDS.DEATH, false));
            case SPAWN -> addSound(new Sound(SOUNDS.SPAWN, false));
            case JUMP -> addSound(new Sound(SOUNDS.JUMP, false));
            case BLOCK_BREAKS -> addSound(new Sound(SOUNDS.BLOCK_BREAKS, false));
            case BUTTON_CLICK -> addSound(new Sound(SOUNDS.BUTTON_CLICK,false));
            case MUSIC -> addSound(new Sound(SOUNDS.MUSIC,true));
        }
    }

    /**
     * Eine Wrapper-Klasse für Audioklippen
     */
    private static class Sound {
        /**
         * Das abgespielete Audioklip
         */
        public Clip clip;
        /**
         * Der Typ des Tones
         */
        public final SOUNDS type;

        /**
         * Erzeugt einen neuen Ton
         * @param sound der Typ des gewünschten Tones
         * @param loopEternally soll der Ton unendlich wiederholt werden?
         */
        public Sound(AudioManager.SOUNDS sound, boolean loopEternally){
            type=sound;
            try{
                File audioFile=new File(Main.assetsDirectory,"audio");

                switch (sound){
                    case DEATH -> audioFile=new File(audioFile,"death.wav");
                    case SPAWN -> audioFile=new File(audioFile,"spawn.wav");
                    case JUMP -> audioFile=new File(audioFile,"jump.wav");
                    case BLOCK_BREAKS -> audioFile=new File(audioFile,"block_breaks.wav");
                    case BUTTON_CLICK -> audioFile=new File(audioFile,"button_click.wav");
                    case MUSIC -> audioFile=new File(audioFile,"music.wav");
                }

                final Sound sond=this;

                AudioInputStream ais= AudioSystem.getAudioInputStream(audioFile);
                clip=AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        AudioManager.closeSound(sond);
                    }
                });

                if(loopEternally)
                    clip.loop(Clip.LOOP_CONTINUOUSLY);

                clip.start();
            }
            catch(Exception ex){
                System.err.println("coul'dnt play sond sry bruv");
            }
        }
    }
}
