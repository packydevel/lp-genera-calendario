package org.lp.calendar.writers;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

/**Effettua la scrittura del calendario su più tipi di file
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class All extends Write{

    private Txt txt;
    private Html html;
    private Pdf pdf;
    private Xls xls;

    public All(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    /**scrive su tutti i formati implementati */
    @Override
    public void write() {
        txt = new Txt(getNomefile());
        pdf = new Pdf(getNomefile());
        html = new Html(getNomefile());
        try {
            txt.init(null);
            html.init("Calendario "+ getNomefile());
            pdf.init(null);
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                String giornata = "Giornata " + (gg+1);
                txt.addRow(giornata);
                html.initTableHTML(giornata);
                pdf.addParagraphPdf(giornata, true);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    txt.addRow(partita);
                    html.initTableRowHTML(partita);
                    pdf.addParagraphPdf(partita, false);
                }
                txt.addRow("");
                html.closeTableHTML();
                pdf.addParagraphPdf("", false);
            }
            txt.close();
            html.close();
            pdf.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }
        /*
        String crlf = "\r\n";
        try {
            bwTXT = creaFile(nomefile + ".txt");
            bwHTML = creaFile(nomefile + ".html");
            initPDF();
            initHtml("Calendario "+ nomefile);
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                String giornata = "Giornata " + (gg+1);
                bwTXT.write(giornata + crlf);
                initTableHTML(giornata);
                addParagraphPdf(giornata, true);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    bwTXT.write(partita+crlf);
                    initTableRowHTML(partita);
                    addParagraphPdf(partita, false);
                }
                bwTXT.write(crlf);
                closeTableHTML();
                addParagraphPdf("", false);
            }
            bwTXT.close();
            closeHTML();
            bwHTML.close();
            pdf.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }
*/              
}