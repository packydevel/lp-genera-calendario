package org.lp.calendar.writers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;


public class Write {

    private String nomefile;
    private ArrayList <ArrayList<AccoppiamentoVO>> alGiornate;
    private ArrayList<String> alSquadre;
    private BufferedWriter bw;
    private final String txt = ".txt";
    private final String html = ".html";

    /**Costruttore
     *
     * @param nome nome del campionato
     * @param giornate lista risultato algoritmo
     * @param squadre lista squadre partecipanti
     */
    public Write (String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate,
            ArrayList<String> squadre) {
        nomefile = nome;
        alGiornate = giornate;
        alSquadre = squadre;
    }

    public ArrayList<ArrayList<AccoppiamentoVO>> getGiornate() {
        return alGiornate;
    }

    public ArrayList<String> getSquadre() {
        return alSquadre;
    }

    public BufferedWriter getBuffered() {
        return bw;
    }

    public void setBuffered(BufferedWriter bw) {
        this.bw = bw;
    }

    public String getNomefile() {
        return nomefile;
    }

    public void closeBuffered() throws IOException{
        bw.close();
    }

    public void writeBuffered(String text) throws IOException {
        bw.write(text);
    }

    protected String getExt(WritersMode wm){
        if (wm == WritersMode.TXT)
            return txt;
        else if (wm == WritersMode.HTML)
            return html;
        else if (wm == WritersMode.TXT)
            return txt;

        return null;
    }
}