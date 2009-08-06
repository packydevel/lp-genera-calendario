package org.lp.calendar.writers;

import java.io.IOException;
import java.util.ArrayList;
import org.lp.calendar.AccoppiamentoVO;

public class Xls2 extends Xls {

    public Xls2(String nome){
        super(nome);
    }

    public Xls2(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    @Override
    public void write(){
        try {
            init(null);
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                addSheet("Giornata " + (gg+1), gg);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1)
                        addRowCells(i, getSquadre().get(singolo.getCasa()-1),
                                getSquadre().get(singolo.getOspite()-1));
                    else
                        addRowCells(i, "Riposa:",
                                getSquadre().get(singolo.getRiposa()-1));
                }
            } //end for giornate
            close();
        } catch (IOException ioe) {}
    }//end write
}