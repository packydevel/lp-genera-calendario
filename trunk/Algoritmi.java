import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Luca     
 */
@SuppressWarnings("serial")
public class Algoritmi {

    private static boolean ghostTeam; //squadra fantasma per giornata riposo
    private static int numberTeam; // numero squadre

    /**Algoritmo di Berger funzionante
     * Si prepara un arraylist di numeri interi corrispondenti alle squadre, lo si shuffla
     * si crea la matrice di berger, 2righe e squadre/2 colonne e lo si popola con i numeri dell'arraylist
     *
     *
     * @param squadre numero di squadre
     */
    public static ArrayList<ArrayList<AccoppiamentoVO>> doBergerAlgorithm(int squadre){
        long inizio = System.currentTimeMillis();
        controlloDispari(squadre);

        ArrayList<Integer> alNumbers = new ArrayList<Integer>();

        for (int i = 1; i <= numberTeam; i++)
            alNumbers.add(i);

        int mid = numberTeam/2;
        int giornate = (numberTeam - 1) * 2;
        int verso = -1;
        if (isDispari(random(numberTeam)))
            verso = 1;
        int[][] matriceBerger = new int[2][mid];

        //esegue un rimescolamento dei numeri
        Collections.shuffle(alNumbers);
        //estrae l'elemento fisso e lo rimuove dall'arraylist e lo posiziona in 0,0 della matrice
        if (ghostTeam)
            matriceBerger[0][0] = numberTeam;
        else
            matriceBerger[0][0] = alNumbers.get(random(numberTeam));

        alNumbers.remove(alNumbers.indexOf(matriceBerger[0][0]));

        for (int i=0; i<mid-1; i++)
            matriceBerger[0][i+1] = alNumbers.get(i);
        for (int i=mid-1; i<alNumbers.size(); i++)
            matriceBerger[1][i+1-mid] = alNumbers.get(i);

        ArrayList<ArrayList<AccoppiamentoVO>> alGiornate = new ArrayList<ArrayList<AccoppiamentoVO>>();
        // Ad ogni ciclo/giornata, ruota nel senso del verso gli elementi dell'arraylist numeri
        // tenendo fisso un elemento
        for (int gg = 0; gg < giornate; gg++) {
            ArrayList<AccoppiamentoVO> alAccoppiamenti= new ArrayList<AccoppiamentoVO>();
            for (int col = 0; col < mid; col++){
                AccoppiamentoVO objAccopp = null;
                if (ghostTeam) {
                    if (col < mid - 1){
                        int k = col + 1;
                        if (isDispari(gg))
                            objAccopp = new AccoppiamentoVO(matriceBerger[0][k],matriceBerger[1][k]);
                        else
                            objAccopp = new AccoppiamentoVO(matriceBerger[1][k],matriceBerger[0][k]);
                    } else if (col == mid - 1)
                        objAccopp = new AccoppiamentoVO(matriceBerger[1][0]);
                } else {
                    if (isDispari(gg))
                        objAccopp = new AccoppiamentoVO(matriceBerger[0][col],matriceBerger[1][col]);
                    else
                        objAccopp = new AccoppiamentoVO(matriceBerger[1][col],matriceBerger[0][col]);
                }
                alAccoppiamenti.add(objAccopp);
            }
            alGiornate.add(alAccoppiamenti);
            matriceBerger = rotateMatriceBerger(matriceBerger, verso, mid);
        }
        long tempo = System.currentTimeMillis() - inizio;
        System.out.println("tempo algoritmo berger = " + tempo + " ms");
        return alGiornate;
    } // end berger

    /**Algoritmo totalmente random, con uso dei nomi delle squadre e stop/ritorno indietro
     * di giornata se si blocca???
     *
     * @param alLista
     */
    public void randomStringBackwardAlgorithm(ArrayList<String> alLista){
        long inizio = System.currentTimeMillis();
        int numero = alLista.size();

        boolean[][] accoppiamenti = new boolean[numero][numero];
        boolean[] giornata = new boolean[numero];

        for (int i = 0; i < numero; i++) {
            for (int j = 0; j < numero; j++) {
                if (i == j)
                    accoppiamenti[i][j] = true;
                else
                    accoppiamenti[i][j] = false;
            }
        }

        for (int i = 1; i < numero; i++) {
            System.out.println("Giornata " + i);

            @SuppressWarnings("unchecked")
            ArrayList<String> alCopia = (ArrayList<String>) alLista.clone();
            ArrayList<String> alRimozione = new ArrayList<String>();

            for (int j = 0; j < numero; j++)
                giornata[j] = false;

            int contatore = numero;
            long tentativi = 0;

            while (alCopia.size() != 0) {
                int a = random(contatore);
                int b = random(contatore);

                while (b == a)
                    b = random(contatore);

                String x = alCopia.get(a);
                String y = alCopia.get(b);

                int m = confronta(alLista, x);
                int n = confronta(alLista, y);

                if ((giornata[m] == false) && (giornata[n] == false)) {
                    if ((accoppiamenti[m][n] == false) && (accoppiamenti[m][n] == false)) {
                        giornata[m] = true;
                        giornata[n] = true;
                        accoppiamenti[m][n] = true;
                        accoppiamenti[n][m] = true;
                        alRimozione.add(x);
                        alRimozione.add(y);
                        tentativi = 0;
                        System.out.println(x + "-" + y);

                        if (a > b) {
                            alCopia.remove(a);
                            alCopia.remove(b);
                        } else {
                            alCopia.remove(b);
                            alCopia.remove(a);
                        }

                        contatore -= 2;
                    } else
                        tentativi++;
                } //endif giornata

                if (tentativi == (long) (Math.pow( (double) alLista.size(), 2.0) * i)) {
                    i--;

                    for (int conta = 0; conta < alRimozione.size(); conta += 2) {
                        String squadra = alRimozione.get(conta);
                        int appo1 = confronta(alLista, squadra);
                        squadra = alRimozione.get(conta + 1);
                        int appo2 = confronta(alLista, squadra);
                        accoppiamenti[appo1][appo2] = false;
                        accoppiamenti[appo2][appo1] = false;
                    } //end for conta

                    break;
                } //endif tentativi
            } //end while alcopia
        }
        long tempo = System.currentTimeMillis() - inizio;
        System.out.println("tempo = " + tempo);
    } // end crea calendario

    public void crea1(ArrayList<String> al_lista){
        controlloDispari(al_lista.size());

        int[][] accoppiamenti = new int[numberTeam][numberTeam];
        boolean[] giornata = new boolean[numberTeam-1];

        /*inizializza accoppiamenti con valori adatti allo scopo
         0 sono le caselle che nn possono essere occupate xkè significherebbe giocare con se stesso,
         -1 sono invece le caselle che possono essere sostituite col numero di giornata*/
        for (int i = 0; i < numberTeam; i++) {
            for (int j = 0; j < numberTeam; j++) {
                if (i == j)
                    accoppiamenti[i][j] = 0;
                else
                    accoppiamenti[i][j] = -1;
            }
        }

        
        

    }

    /**Genera la prima riga in modo casuale, le rimanenti seguendo la diagonale inversa
     * partendo dalla stessa prima riga
     *
     * @param al_lista lista squadre
     */
    public void firstRandom_AllIndirectAlgorithm(int squadre){
        long inizio = System.currentTimeMillis();        
        controlloDispari(squadre);

        int[][] accoppiamenti = new int[numberTeam][numberTeam];
        boolean[] giornata = new boolean[numberTeam-1];

        /*inizializza accoppiamenti con valori adatti allo scopo
         0 sono le caselle che nn possono essere occupate xkè significherebbe giocare con se stesso*/
        for (int i = 0; i < numberTeam; i++) {
            for (int j = 0; j < numberTeam; j++) {
                if (i == j)
                    accoppiamenti[i][j] = 0;                
            }
        }

        //creo un arraylist di numeri da 1 a numero - 1
        ArrayList<Integer> al_numeri = new ArrayList<Integer>();
        for (int i=1;i<numberTeam; i++)
            al_numeri.add(i);

        //genero la prima riga e prima colonna
        for (int i=1; i < numberTeam; i++){
            int numero_random = random(al_numeri.size());
            accoppiamenti[i][0] = accoppiamenti[0][i]= al_numeri.get(numero_random);
            System.out.print(accoppiamenti[0][i] + " ");
            al_numeri.remove(numero_random);
        }
        System.out.println("");

        for (int i=1; i<numberTeam; i++){
            int riga = i-1;
            for (int j=i+1; j<numberTeam; j++){
                int colonna = j+1;
                if (colonna==numberTeam)
                    colonna = i+1;
                accoppiamenti[i][j] = accoppiamenti[riga][colonna];
                System.out.print(accoppiamenti[i][j] + " ");
            }
            System.out.println("");
        }

        long tempo = System.currentTimeMillis() - inizio;
        System.out.println("tempo = " + tempo);
    }//end


    public void generateArrayListsRandom(int size){
        ArrayList<ArrayList<Integer>> al_lista;
        ArrayList<Integer> al_riga;

        //creo un arraylist di numeri da 1 a numero - 1
        ArrayList<Integer> al_numeri = new ArrayList<Integer>();
        for (int i=1;i<size; i++)
            al_numeri.add(i);

        //genero la prima riga e prima colonna

        al_riga = new ArrayList<Integer>();
        for (int i=1; i < size; i++){
            int numero_random = random(al_numeri.size());
            al_riga.add(al_numeri.get(numero_random));
            al_numeri.remove(numero_random);
        }

    }

    /**Algoritmo by packy
     *
     */
    public void packyAlgorithm(){
        ArrayList<ArrayList<Integer>> calendar = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> row = new ArrayList<Integer>();

        for (int i = 0; i <= 9; i++) {
            if (i == 9) {
                row.add(null);
            } else {
                row.add(i);
            }
        }

        calendar.add(new ArrayList<Integer>(row));

        Collections.shuffle(calendar.get(0));

        int j = (int) (Math.floor(Math.random() * 9) + 1);

        for (int i = 1; i <= 9; i++) {
            calendar.add(new ArrayList<Integer>(calendar.get(i - 1)));
            Collections.rotate(calendar.get(i), 1);
        }

        Collections.shuffle(calendar);

        for (ArrayList<Integer> a : calendar) {
            for (Integer i : a) {
                System.out.print(i == null ? " " : i);
            }
            System.out.println();
        }
    }        

    public void blueBonesAlgorithm(int squadre){
        controlloDispari(squadre);

        // Generate the fixtures using the cyclic algorithm.
        int totalRounds = numberTeam - 1;
        int matchesPerRound = numberTeam / 2;
        String[][] rounds = new String[totalRounds][matchesPerRound];

        for (int round = 0; round < totalRounds; round++) {
            for (int match = 0; match < matchesPerRound; match++) {
                int home = (round + match) % (totalRounds);
                int away = (totalRounds - match + round) % (totalRounds);
                // Last team stays in the same place while the others
                // rotate around it.
                if (match == 0) {
                    away = totalRounds;
                }
                // Add one so teams are number 1 to teams not 0 to teams - 1
                // upon display.
                rounds[round][match] = (home + 1) + " v " + (away + 1);
            }
        }

        // Interleave so that home and away games are fairly evenly dispersed.
        String[][] interleaved = new String[totalRounds][matchesPerRound];

        int evn = 0;
        int odd = (numberTeam / 2);
        for (int i = 0; i < rounds.length; i++) {
            if (i % 2 == 0) {
                interleaved[i] = rounds[evn++];
            } else {
                interleaved[i] = rounds[odd++];
            }
        }

        rounds = interleaved;

        // Last team can't be away for every game so flip them
        // to home on odd rounds.
        for (int round = 0; round < rounds.length; round++) {
            if (round % 2 == 1) {
                rounds[round][0] = flip(rounds[round][0]);
            }
        }

        // Display the fixtures
        for (int i = 0; i < rounds.length; i++) {
            System.out.println("Round " + (i + 1));
            System.out.println(Arrays.asList(rounds[i]));
            System.out.println();
        }

        System.out.println();

        if (ghostTeam) {
            System.out.println("Matches against team " + numberTeam + " are byes.");
        }

        System.out.println("Use mirror image of these rounds for "
            + "return fixtures.");
    }

    private String flip(String match) {
        String[] components = match.split(" v ");
        return components[1] + " v " + components[0];
    }


    private static int random(int numero) {
        return (int)(Math.random()*numero);
    }

    private int confronta(ArrayList<String> alTemp, String stringa) {
        int pos=0;
        while (!(alTemp.get(pos).equals(stringa)))
            pos++;
        return pos;
    }

    /**controlla se il numero è dispari e imposta il ghostTeam e il numberTeam
     *
     * @param numero numero da controllare
     */
    private static void controlloDispari(int numero){
        ghostTeam = false;
        numberTeam = numero;
        
        if (isDispari(numero)){
            ghostTeam = true;
            numberTeam++;
        }
    } //end controlloDispari

    /**controlla se il numero è dispari
     *
     * @param numero numero da controllare
     * @return booleano true = dispari, false = pari
     */
    private static boolean isDispari(int numero){
        boolean dispari = false;
        if (numero % 2 == 1)
            dispari = true;
        return dispari;
    }

    private static int[][] rotateMatriceBerger(int[][] matrice, int verso, int mid){
        int pop, row, col;
        int scambiToDo = mid*2-2;
        if (verso == -1){
            row = 0;
            col = 1;
            pop = matrice[row][col];
            while (scambiToDo>0){
                if ((row == 0) && ((col+1)<mid))
                    matrice[row][col++] = matrice[row][col];                    
                else if ((row == 0) && ((col+1) == mid))                
                    matrice[row++][col] = matrice[row][col];
                else if (row==1)
                    matrice[row][col--] = matrice[row][col];                                    
                scambiToDo--;
            }
            matrice[row][col] = pop;
        } else if (verso == 1){
            row = 1;
            col = 0;
            pop = matrice[row][col];
            while (scambiToDo>0){
                if ((row == 1) && ((col+1)<mid))
                    matrice[row][col++] = matrice[row][col];
                else if ((row == 1) && ((col+1) == mid))
                    matrice[row--][col] = matrice[row][col];
                else if (row==0)
                    matrice[row][col--] = matrice[row][col];
                scambiToDo--;
            }
            matrice[row][col] = pop;
        }
        return matrice;
    }
}