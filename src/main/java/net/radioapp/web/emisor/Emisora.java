package net.radioapp.web.emisor;

import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.Main;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.json.EmisorJSON;
import net.radioapp.web.json.EmisorJSONObject;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Emisora {
    private final String name;
    private final Path path;
    private double frequency;
    private final List<Audio> files = new ArrayList<>();
    private Audio actualTrack;
    private int filesPosition = 0;
    private int currentChunk = 0;

    public Emisora(String name, Path p) {
        this.path = p;
        this.name = name;
        try{
            readFiles();
            changeSong();
            actualTrack.load();
        }
        catch (IOException e){
            ActionHandler.handleException(e, "Fallo al leer los ficheros en la creación de las emisoras");
        }
    }

    private void readFiles() throws IOException {
        try(Stream<Path> files = Files.list(path)){
            files.filter(Files::isRegularFile).filter((e) -> !e.getFileName().toString().contains(".json")).forEach((e) ->
            {
                try {
                    Audio a = Audio.newAudio(e.toFile());
                    this.files.add(a);
                }
                catch (UnsupportedAudioFileException | IOException ex) {
                    ActionHandler.handleException(ex, "Archivo de audio " + e.getFileName() + " no soportado");}
            });
        }
    }

    private void changeSong(){
        Audio oldTrack = actualTrack;
        currentChunk = 0;

        if(filesPosition >= files.size()){filesPosition = 0;}
        actualTrack = files.get(filesPosition);
        if(oldTrack != actualTrack){
            if(oldTrack!=null) {oldTrack.unload();}
            actualTrack.load();
        }
        filesPosition++;
    }

    public byte[] getNextChunk(){
        byte[] v = actualTrack.getChunk(currentChunk);
        currentChunk++;
        if (v == null){
            changeSong();
            v = getNextChunk();
        }
        return v;
    }

    private boolean hasConfigFile(){
        return new File(path.toString() + "/config.json").exists();
    }

    private void createConfigFile() throws IOException {
        EmisorJSON.create(new File(path.toString() + "/config.json").toPath(), new EmisorJSONObject());
    }

    public void readConfigFile() throws IOException{
        if (!hasConfigFile()){createConfigFile();}
        EmisorJSONObject obj = EmisorJSON.read(new File(path + "/config.json"));
        if (obj.getVersion() < EmisorJSON.EMISORVERSION) {
            Main.filterAction(new Action("Inicialización emisoras",
                    "La versión de los JSON de "  + getName() + " es inferior a la esperada (" + EmisorJSON.EMISORVERSION +"), puede causar errores\n",
                    ActionType.ERROR));
        }
        setFrequency(obj.getFrecuency());
    }

    public String getName() {
        return name;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public Audio getActualTrack() {
        return actualTrack;
    }
}
