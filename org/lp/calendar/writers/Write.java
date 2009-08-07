package org.lp.calendar.writers;

import com.lowagie.text.DocumentException;

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
    private final String pdf = ".pdf";
    private final String xls = ".xls";

    /**Costruttore
     * 
     */
    public Write(String nome) {
        nomefile = nome;
    }

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

    protected ArrayList<ArrayList<AccoppiamentoVO>> getGiornate() {
        return alGiornate;
    }

    protected ArrayList<String> getSquadre() {
        return alSquadre;
    }

    protected BufferedWriter getBuffered() {
        return bw;
    }

    protected void setBuffered(BufferedWriter bw) {
        this.bw = bw;
    }

    protected String getNomefile() {
        return nomefile;
    }

    protected void closeBuffered() throws IOException{
        bw.close();
    }

    protected void writeBuffered(String text) throws IOException {
        bw.write(text);
    }

    protected String getExt(WritersMode wm){
        if (wm == WritersMode.TXT)
            return txt;
        else if (wm == WritersMode.HTML)
            return html;
        else if (wm == WritersMode.PDF)
            return pdf;
        else if (wm == WritersMode.XLS)
            return xls;
        return null;
    }

    protected void init(String title) throws IOException, DocumentException{}
    public void write() {}
    protected void close() throws IOException {}
}