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
    private Xls xls1, xls2;

    public All(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    /**scrive su tutti i formati implementati */
    @Override
    public void write() {        
        try {
            init(null);
            xls1.addSheet("Calendario", 0);
            int rownum = -1;
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                String giornata = "Giornata " + (gg+1);
                txt.addRow(giornata);
                html.initTableHTML(giornata);
                pdf.addParagraphPdf(giornata, true);
                xls1.addTitleRow(++rownum, giornata);
                xls2.addSheet(giornata, gg);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    txt.addRow(partita);
                    html.initTableRowHTML(partita);
                    pdf.addParagraphPdf(partita, false);
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1){
                        xls1.addRowCells(++rownum, getSquadre().get(singolo.getCasa()-1),
                                getSquadre().get(singolo.getOspite()-1));
                        xls2.addRowCells(i, getSquadre().get(singolo.getCasa()-1),
                                getSquadre().get(singolo.getOspite()-1));
                    } else {
                        xls1.addRowCells(++rownum, "Riposa:",
                                getSquadre().get(singolo.getRiposa()-1));
                        xls2.addRowCells(i, "Riposa:",
                                getSquadre().get(singolo.getRiposa()-1));
                    }
                }
                txt.addRow("");
                html.closeTableHTML();
                pdf.addParagraphPdf("", false);
                xls1.addRow(++rownum);
            }
            close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }

    @Override
    protected void init(String text) throws IOException, DocumentException {
        txt = new Txt(getNomefile());
        pdf = new Pdf(getNomefile());
        html = new Html(getNomefile());
        xls1 = new Xls(getNomefile()+"_v1");
        xls2 = new Xls2(getNomefile()+"_v2");
        txt.init(null);
        html.init("Calendario "+ getNomefile());
        pdf.init(null);
        xls1.init(null);
        xls2.init(null);
    }

    @Override
    protected void close() throws IOException {
        txt.close();
        html.close();
        pdf.close();
        xls1.close();
        xls2.close();
    }
}