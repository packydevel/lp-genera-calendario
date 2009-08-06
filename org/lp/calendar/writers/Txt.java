package org.lp.calendar.writers;

import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

/**Effettua la scrittura del calendario su file txt
 *
 * @author Luca
 */
public class Txt extends Write{
    
    private final String crlf = "\r\n";

    public Txt(String nome) {
        super(nome);
    }

    public Txt(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    /**Scrive su file di testo */
    @Override
    public void write() {
        try {
            init(null);
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = super.getGiornate().get(gg);
                addRow("Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    addRow(partita);
                }
                addRow("");
            }
            close();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } //end write

    @Override
    protected  void init(String title) throws IOException {
        setBuffered(Common.creaFile(getNomefile() + getExt(WritersMode.TXT)));
    }

    protected void addRow(String text) throws IOException{
        writeBuffered(text+crlf);
    }

    @Override
    protected void close() throws IOException{
        closeBuffered();
    }
}