import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class Write {

    private static String curDir = System.getProperty("user.dir") + File.separator;
    private static String crlf = "\r\n";

    public static void writeTXT(ArrayList<ArrayList<AccoppiamentoVO>> alGiornate, ArrayList<String> alSquadre) {

        String nomefile = curDir + "prova.txt";
        File file = new File(nomefile);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                String giornata = "Giornata " + (gg+1) + crlf;
                out.write(giornata);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    String partita = null;
                    if ((i==size-1) && (singolo.getRiposa()!=0))
                        partita = "Riposa: " + alSquadre.get(singolo.getRiposa()-1) + crlf;
                    else
                        partita = alSquadre.get(singolo.getCasa()-1) + " - " +
                            alSquadre.get(singolo.getOspite()-1) + crlf;
                    out.write(partita);
                }
                out.write(crlf);
            }
            out.close();
        } catch (IOException ex) {}
    }

}
