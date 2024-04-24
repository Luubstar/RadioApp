package net.radioapp.commandController;

import java.util.List;

public class StringUtils {

    public static Command findClosestCommand(List<Command> comandos, String input){
        double max = 0;
        Command res = comandos.getFirst();
        for(Command c: comandos){
            for(String name : c.getAliasesAndName()) {
                double tmp = similarity(name, input);
                if (tmp > max) {
                    max = tmp;
                    res = c;
                }
            }
        }
        if (max >= 0.7){return res;} //TODO: Debería preguntar si es el que quieres usar
        return null;
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0;}
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static String[] splitCommands(String res){
        if(res.contains("&&")){
            return res.split("&&");
        }
        else {return new String[]{res};}
    }

}
