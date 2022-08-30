package typingTutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * BackgroundSound plays background sound while the GUI is stil on or not exited from
 * 
 * @author Shaun 
 * Date created 23/08/22
 * @version 1
 */


public class BackgroundSound extends Thread implements LineListener{


    String audioFilePath; // audio file path
    File audioFile; // audio file
    AtomicBoolean pause;
    long clipTimePosition = 0;

    /**
     * BackgroundSound constructor with a single parameter
     * @param audio the path of the audio file
     */
    public BackgroundSound(String audio){
        audioFilePath = audio;
        audioFile = new File(audioFilePath); // create the audio path connection
    }


    public void run(){
     
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // audi input stream
 
            AudioFormat format = audioStream.getFormat(); // get the format of the audio
 
            DataLine.Info info = new DataLine.Info(Clip.class, format); 
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this); // add an action listener to play the audio
 
            audioClip.open(audioStream); // open the audio clip
             
            audioClip.loop(Clip.LOOP_CONTINUOUSLY); // run the audio repeatedly
           
        
        } catch(FileNotFoundException e){ // error handler to inform the user which folder to use to run the app
            System.out.println("To hear the background sound, run the code inside the folder with the Makefile :)");
        } catch (UnsupportedAudioFileException ex) { // error handle for unsupported audio file
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) { // error handle when the audio play back line is unavailable
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) { // error while playing the audio file
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
         
    }
    /**
     * update method to control the audio
     */
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
        }
    }
}
