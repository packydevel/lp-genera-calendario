import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class jfHome2 extends JFrame implements WindowListener{
    
    private JPanel jpanel;
    private GridBagConstraints gbcLabel, gbcTextField, gbcComboBox, gbcButton;
    private JTextField jtfNomeCampionato;
    private JComboBox jcbNumeroSquadre, jcbTipoFile;
    private JButton jbPulisci, jbCreaCalendario;
    private int previousTeamsNumber;
    private JLabel[] jlTeams;
    private JTextField[] jtfTeams;

    /**Costruttore */
    public jfHome2(){
        super("Generatore Calendario ");
        this.setPreferredSize(new Dimension(400, 650));

        jpanel = new JPanel(new GridBagLayout());

        gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(2, 2, 2, 2);
        gbcLabel.anchor = GridBagConstraints.NORTHWEST;

        gbcTextField = new GridBagConstraints();
        gbcTextField.insets = new Insets(2, 2, 2, 2);
        gbcTextField.anchor = GridBagConstraints.NORTHWEST;

        gbcComboBox = new GridBagConstraints();
        gbcComboBox.insets = new Insets(2, 2, 2, 2);
        gbcComboBox.anchor = GridBagConstraints.NORTHWEST;

        gbcButton = new GridBagConstraints();
        gbcButton.weighty = 1;
        gbcButton.insets = new Insets(2, 2, 2, 2);
        gbcButton.anchor = GridBagConstraints.SOUTHEAST;

        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        jpanel.add(new JLabel("Nome Campionato:"), gbcLabel);

        jtfNomeCampionato = new JTextField(20);
        gbcTextField.gridx = 1;
        gbcTextField.gridy = 0;
        gbcTextField.gridwidth = 2;
        gbcTextField.fill = GridBagConstraints.HORIZONTAL;
        jpanel.add(jtfNomeCampionato, gbcTextField);
        
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 1;
        jpanel.add(new JLabel("Numero squadre:"), gbcLabel);

        Integer teamsNumber[] = {null, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        jcbNumeroSquadre = new JComboBox(teamsNumber);
        gbcComboBox.gridx = 1;
        gbcComboBox.gridy = 1;
        jpanel.add(jcbNumeroSquadre, gbcComboBox);
        jcbNumeroSquadre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaOggetti();
            }
        });        

        jbPulisci = new JButton("Pulisci Campi");
        jbPulisci.setPreferredSize(new Dimension(120, 20));
        gbcButton.gridx = 2;
        gbcButton.gridy = 22;
        jbPulisci.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pulisciCampi();
            }
        });
        jpanel.add(jbPulisci, gbcButton);

        jbCreaCalendario = new JButton("Crea Calendario");
        jbCreaCalendario.setPreferredSize(new Dimension(140, 20));
        gbcButton.gridx = 1;
        gbcButton.weightx = 1;
        jbCreaCalendario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaCalendario();
            }
        });
        jpanel.add(jbCreaCalendario, gbcButton);
        
        gbcTextField.fill = GridBagConstraints.NONE;

        this.add(jpanel);
        this.setVisible(true);
        pack();
        addWindowListener(this);
    } // end costruttore

    /**Lancia l'applicazione
     *
     * @param args argomenti
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                @SuppressWarnings("unchecked")
                jfHome2 jframe = new jfHome2();
            } //end run
        }); //end invokelater
    }//end main

    /** Crea dinamicamente gli oggetti label e textfield della squadra */
    private void creaOggetti() {
        int i;
        int y = 3;

        if (jcbNumeroSquadre.getSelectedItem() == null) {
            return;
        }

        int currentTeamsNumber = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();

        if (currentTeamsNumber != previousTeamsNumber && previousTeamsNumber != 0) {
            for (int j = 0; j < previousTeamsNumber; j++) {
                jpanel.remove(jlTeams[j]);
                jpanel.remove(jtfTeams[j]);
                validate();
            }
        }

        i = currentTeamsNumber;
        jlTeams = new JLabel[i];
        jtfTeams = new JTextField[i];

        for (int j = 0; j < i; j++) {

            jlTeams[j] = new JLabel("Squadra " + (j + 1) + ":");
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            jpanel.add(jlTeams[j], gbcLabel);
            jlTeams[j].setVisible(true);

            jtfTeams[j] = new JTextField();
            jtfTeams[j].setPreferredSize(new Dimension(250, 20));
            gbcTextField.gridx = 1;
            gbcTextField.gridy = y;
            gbcTextField.ipadx = 100;
            jpanel.add(jtfTeams[j], gbcTextField);
            jtfTeams[j].setVisible(true);

            y++;
        }
        previousTeamsNumber = currentTeamsNumber;
        validate();
    }// end creaOggetti

    /**Evento creazione Calendario*/
    private void creaCalendario(){
        long inizio = System.currentTimeMillis();
        int numeroSquadreCorrente = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();

        if (jtfNomeCampionato.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Tutti i campi devo essere valorizzati!!!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int j = 0; j < numeroSquadreCorrente; j++) {
            if (jtfTeams[j].getText().length() == 0) {
                JOptionPane.showInternalMessageDialog(this, "Tutti i campi devo essere valorizzati!!!",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ArrayList<String> alSquadre = new ArrayList<String>();
        for (int i=0; i<numeroSquadreCorrente; i++){
            alSquadre.add(jtfTeams[i].getText());
        }

        for (String s : alSquadre) {
            if (Collections.frequency(alSquadre, s) > 1) {
                JOptionPane.showInternalMessageDialog(this,
                        "Non è possibile avere più Squadre con lo stesso nome!!!",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ArrayList<ArrayList<AccoppiamentoVO>> alGiornate = Algoritmi.doBergerAlgorithm(numeroSquadreCorrente);
        Write.writeTXT(alGiornate, alSquadre);
        long tempo = System.currentTimeMillis() - inizio;
        JOptionPane.showMessageDialog(this, "Terminato in " + tempo + " ms");
    } //end creaCalendario

    private void pulisciCampi(){

    }

    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(this, "Vuoi chiudere l'applicazione?",
                "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == 0)
            dispose();
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
} //end class