package net.radioapp.web.emisor;

import net.radioapp.Main;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.json.EmisorJSON;
import net.radioapp.web.json.EmisorJSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Emisora {
    private final String name;
    private final Path path;
    private double frecuency;

    public Emisora(String name, Path p) {
        this.path = p;
        this.name = name;
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
