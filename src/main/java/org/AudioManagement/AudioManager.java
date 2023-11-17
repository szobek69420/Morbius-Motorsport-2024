package main.java.org.AudioManagement;

import main.java.org.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public final class AudioManager {
    public static enum SOUNDS{
        DEATH,
        JUMP,
        SPAWN,
        BLOCK_BREAKS,
        MUSIC
    };

    private static ArrayList<Sound> activeSounds=new ArrayList<>();

    private static synchronized void addSound(Sound sound){
        activeSounds.add(sound);
    }
    private static synchronized void closeSound(Sound sound){
        if(!activeSounds.contains(sound))
            return;

        sound.clip.stop();
        sound.clip.close();
        activeSounds.remove(sound);
    }

    public static synchronized void closeAll(){
        for (Sound s : activeSounds){
            s.clip.stop();
            s.clip.close();
        }
    }

    public static void playSound(SOUNDS sound){
        switch (sound){
            case DEATH -> addSound(new Sound(SOUNDS.DEATH, false));
            case SPAWN -> addSound(new Sound(SOUNDS.SPAWN, false));
            case JUMP -> addSound(new Sound(SOUNDS.JUMP, false));
            case BLOCK_BREAKS -> addSound(new Sound(SOUNDS.BLOCK_BREAKS, false));
            case MUSIC -> addSound(new Sound(SOUNDS.MUSIC,true));
        }
    }

    private static class Sound {
        public Clip clip;

        public Sound(AudioManager.SOUNDS sound, boolean loopEternally){
            try{
                File audioFile=new File(Main.assetsDirectory,"audio");

                switch (sound){
                    case DEATH -> audioFile=new File(audioFile,"death.wav");
                    case SPAWN -> audioFile=new File(audioFile,"spawn.wav");
                    case JUMP -> audioFile=new File(audioFile,"jump.wav");
                    case BLOCK_BREAKS -> audioFile=new File(audioFile,"block_breaks.wav");
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
