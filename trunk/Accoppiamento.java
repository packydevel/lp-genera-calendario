/**
 *
 * @author Luca
 */

public class Accoppiamento {

    private int casa;
    private int ospite;
    private int riposa;

    public Accoppiamento(int home, int visitor) {
        //System.out.println(casa + " - " + ospite);
        this.casa = home;
        this.ospite = visitor;
    }

    public Accoppiamento(int stop) {
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