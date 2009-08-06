package org.lp.calendar.writers;

import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

public class Html extends Write{

    public Html(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    public void write(){
        try {
            setBuffered(Common.creaFile(getNomefile() + getExt(WritersMode.HTML)));
            initHtml("Calendario "+ getNomefile());
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                initTableHTML("Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    initTableRowHTML(partita);
                }
                closeTableHTML();
            }
            closeHTML();
            closeBuffered();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } // end html

    /**inizializza l'html e scrive il codice iniziale
     *
     * @param titolo titolo dell'html
     * @throws IOException
     */
    private void initHtml(String titolo) throws IOException{
        writeBuffered("<html>\n<head>");
        if (titolo!=null)
            writeBuffered("\n<title>"+ titolo + "</title>\n");
        writeBuffered("</head>\n<body>\n");
    }

    private void initTableHTML(String testo) throws IOException{
        writeBuffered("<table>\n<tr><td><b>" + testo + "</b></td></tr>\n");
    }

    private void initTableRowHTML(String testo) throws IOException{
        writeBuffered("<tr><td>" + testo + "</td></tr>\n");
    }

    /**Scrive il tag di chiusura della tabella
     *
     * @throws IOException
     */
    private void closeTableHTML() throws IOException{
        writeBuffered("</table>\n");
    }

    private void closeHTML() throws IOException{
        writeBuffered("</body></html>");
    }

}