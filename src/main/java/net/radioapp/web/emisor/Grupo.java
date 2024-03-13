package net.radioapp.web.emisor;

import net.radioapp.ActionHandler;
import net.radioapp.commandController.actions.Action;
import net.radioapp.commandController.actions.ActionType;
import net.radioapp.web.json.GrupoJSON;
import net.radioapp.web.json.GrupoJSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Grupo {
    private final Path path;
    private final String name;
    private double maxFrecuency;
    public final List<Emisora> emisoras;

    public Grupo(String n, Path p,List<Emisora> emisoras){
        this.name = n;
        this.path = p;
        this.emisoras = emisoras;
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder().append(name).append(":");
        for(Emisora e : emisoras){
            r.append("\n").append(e.getName()).append(" -> ").append(e.getFrecuency()).append(" MHz");
        }
        return r.toString();
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

        setFrecuency(obj.getMaxFrecuency());
        for(Emisora e : getEmisoras()){
            if (e.getFrecuency() > getMaxFrecuency()){
                ActionHandler.filterAction(new Action("Inicialización grupos",
                        "La emisora " + e.getName() + " está fuera de la frecuencia máxima del grupo " + getName(),
                        ActionType.QUIT));
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getMaxFrecuency() {
        return maxFrecuency;
    }

    public void setFrecuency(double maxFrecuency){
        this.maxFrecuency = maxFrecuency;
    }

    public List<Emisora> getEmisoras() {
        return emisoras;
    }
}
