package net.radioapp.web.emisor;

import net.radioapp.commandController.Colors;
import net.radioapp.commandController.actions.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.Network.NetHandler;
import net.radioapp.web.json.GrupoJSON;
import net.radioapp.web.json.GrupoJSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Grupo {
    private final Path path;
    private final String name;
    private double maxFrequency;
    private double minFrequency;
    private double defaultFrequency;
    public final List<Emisora> emisoras;

    public Grupo(String n, Path p,List<Emisora> emisoras){
        this.name = n;
        this.path = p;
        this.emisoras = emisoras;
    }

    @Override
    public String toString() {
        return Colors.Green.colorize(getName()) + Colors.Blue.colorize(" (" + minFrequency + " - " + maxFrequency + " MHz)\n");
    }

    private boolean hasConfigFile(){
        return new File(path.toString() + "/config.json").exists();
    }

    private void createConfigFile() throws IOException {
        GrupoJSON.create(new File(path.toString() + "/config.json").toPath(), new GrupoJSONObject());
    }

    public void readConfigFile() throws IOException{
        if(!hasConfigFile()){createConfigFile();}

        GrupoJSONObject obj = GrupoJSON.read(new File(path + "/config.json"));
        if (obj.getVersion() < GrupoJSON.GRUPOVERSION) {
            ActionHandler.filterAction(new Action("Inicialización grupos",
                    "La versión de los JSON de " + getName() + " es inferior a la esperada (" + GrupoJSON.GRUPOVERSION +"), puede causar errores\n",
                    ActionType.ERROR));
        }

        setMaxFrequency(obj.getMaxFrequency());
        setMinFrequency(obj.getMinFrequency());
        setDefaultFrequency(obj.getDefaultFrequency());

        for(Emisora e : getEmisoras()){
            if (e.getFrequency() > getMaxFrequency() || e.getFrequency() < getMinFrequency()){
                ActionHandler.filterAction(new Action("Inicialización grupos",
                        "La emisora " + e.getName() + " está fuera del rango de la frecuencia del grupo " + getName(),
                        ActionType.QUIT));
            }
        }
    }

    public void reset(){for(Emisora a : emisoras){a.reset();}}

    public String getName() {
        return name;
    }

    public double getMaxFrequency() {
        return maxFrequency;
    }

    public double getDefaultFrequency() { return defaultFrequency;}

    public void setMaxFrequency(double maxFrequency){
        this.maxFrequency = maxFrequency;
    }

    public double getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(double minFrequency) {
        this.minFrequency = minFrequency;
    }

    public void setDefaultFrequency(double defaultFrequency) {
        this.defaultFrequency = defaultFrequency;
    }

    public List<Emisora> getEmisoras() {
        return emisoras;
    }
}
