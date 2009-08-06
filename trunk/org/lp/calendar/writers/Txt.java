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

    public Txt(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    /**Scrive su file di testo */
    public void write() {
        try {
            setBuffered(Common.creaFile(getNomefile() + getExt(WritersMode.TXT)));
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = super.getGiornate().get(gg);
                writeBuffered("Giornata " + (gg+1) + crlf);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    writeBuffered(partita+crlf);
                }
                writeBuffered(crlf);
            }
            closeBuffered();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } //end txt
}