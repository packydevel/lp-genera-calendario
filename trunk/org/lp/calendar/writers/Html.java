package org.lp.calendar.writers;

import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

public class Html extends Write{

    public Html(String nome){
        super(nome);
    }

    public Html(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }


    @Override
    public void write(){
        try {            
            init("Calendario "+ getNomefile());
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
            close();            
        } catch (IOException ioe) {ioe.printStackTrace();}
    } // end html

    /**inizializza l'html e scrive il codice iniziale
     *
     * @param titolo titolo dell'html
     * @throws IOException
     */
    @Override
    protected void init(String titolo) throws IOException{
        setBuffered(Common.creaFile(getNomefile() + getExt(WritersMode.HTML)));
        writeBuffered("<html>\n<head>");
        if (titolo!=null)
            writeBuffered("\n<title>"+ titolo + "</title>\n");
        writeBuffered("</head>\n<body>\n");
    }

    protected void initTableHTML(String testo) throws IOException{
        writeBuffered("<table>\n<tr><td><b>" + testo + "</b></td></tr>\n");
    }

    protected void initTableRowHTML(String testo) throws IOException{
        writeBuffered("<tr><td>" + testo + "</td></tr>\n");
    }

    /**Scrive il tag di chiusura della tabella
     *
     * @throws IOException
     */
    protected  void closeTableHTML() throws IOException{
        writeBuffered("</table>\n");
    }

    @Override
    protected void close() throws IOException{
        writeBuffered("</body></html>");
        closeBuffered();
    }
}