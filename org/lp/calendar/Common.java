package org.lp.calendar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Common {

    private static final String curDir = System.getProperty("user.dir") + File.separator;

    /**Restituisce la directory corrente
     *
     * @return directory
     */
    public static String getCurDir(){
        return curDir;
    }

    /**crea il file, se esiste lo cancella, e inizializza il bufferedwriter
     *
     * @param nome nome del file
     * @return nuovo oggetto BufferedWriter
     * @throws IOException
     */
    public static BufferedWriter creaFile(String nome) throws IOException{
        File file = controllaFile(nome);
        file.createNewFile();
        return new BufferedWriter(new FileWriter(file));
    }

    public static File controllaFile(String nome){
        File file = new File(curDir + nome);
        if (file.exists())
            file.delete();
        return file;
    }

    public static String accoppiamenti(AccoppiamentoVO singolo, ArrayList<String> alSquadre,
                                        int i, int size){
        String partita = null;
        if ((i==size-1) && (singolo.getRiposa()!=-1))
            partita = "Riposa: " + alSquadre.get(singolo.getRiposa()-1);
        else
            partita = alSquadre.get(singolo.getCasa()-1) + " - " +
                alSquadre.get(singolo.getOspite()-1);
        return partita;
    }
}