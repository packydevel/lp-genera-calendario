package org.lp.calendar;

/**
 *
 * @author Luca
 */

public class AccoppiamentoVO {

    private int casa;
    private int ospite;
    private int riposa;

    public AccoppiamentoVO(int home, int visitor) {
        //System.out.println(casa + " - " + ospite);
        this.casa = home;
        this.ospite = visitor;
        this.riposa = -1;
    }

    public AccoppiamentoVO(int stop) {
        this.riposa = stop;
    }

    public int getCasa() {
        return casa;
    }    

    public int getOspite() {
        return ospite;
    }

    public int getRiposa() {
        return riposa;
    }
}//end class