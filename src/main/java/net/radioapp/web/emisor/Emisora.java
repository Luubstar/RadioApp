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
    private double frecuency;
    private final List<Audio> ficheros = new ArrayList<>();
    private Audio actualTrack;
    private float time;

    public Emisora(String name, Path p) {
        this.path = p;
        this.name = name;
        try{
            readFiles();
            actualTrack = ficheros.getFirst();
        }
        catch (IOException e){
            ActionHandler.handleException(e, "Fallo al leer los ficheros en la creación de las emisoras");
        }
    }

    private void readFiles() throws IOException {
        Stream<Path> files = Files.list(path);
        files.filter(Files::isRegularFile).filter((e) -> !e.getFileName().toString().contains(".json")).forEach((e) ->
        {
            try {
                Audio a = Audio.newAudio(e.toFile());
                ficheros.add(a);
            }
            catch (UnsupportedAudioFileException | IOException ex) {
                ActionHandler.handleException(ex, "Archivo de audio " + e.getFileName() + " no soportado");
            }
        });
    }

    public void addSeconds(float s){
        time += s;
        int songduration = (int) actualTrack.getArchivo().length() / actualTrack.getBytespersecond();
        if(time >= songduration){
            time -= songduration;
            changeSong();
            addSeconds(0);
        }
    }

    private void changeSong(){
        //TODO: Cambiar de pista
        actualTrack = ficheros.getFirst();
    }

    public byte[] getSecondsFromAudio(int seconds) throws IOException {
        byte[] buffer = actualTrack.getSeconds((int) time, seconds);
        if (buffer.length < actualTrack.getBytespersecond() * seconds){
            changeSong();
            byte[] aux = getSecondsFromAudio(seconds);
            byte[] res = new byte[aux.length + buffer.length];
            System.arraycopy(buffer,0,buffer,0,buffer.length);
            System.arraycopy(aux,0, res, buffer.length,res.length);
            return res;
        }
        return buffer;
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
        setFrecuency(obj.getFrecuency());
    }

    public String getName() {
        return name;
    }

    public double getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(double frecuency) {
        this.frecuency = frecuency;
    }

}
