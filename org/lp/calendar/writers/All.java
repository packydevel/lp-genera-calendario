package org.lp.calendar.writers;

/**Effettua la scrittura del calendario su più tipi di file
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class All {
    /**scrive su tutti i formati implementati */
/*    public void writeALL() {
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