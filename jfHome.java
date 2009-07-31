import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class jfHome extends JFrame implements WindowListener {

    private JPanel jpNorth, jpCenter;
    private JComboBox jcbNumeroSquadre;
    private JTextField jtfCampionato;
    private JLabel jlSquadre[];
    private JTextField jtfSquadre[];
    private JButton jbCrea;
    private GridBagConstraints gbcLabel, gbcTextfield, gbcButton;
    private int oldNumeroSquadre = 0;

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
                jfHome jframe = new jfHome();
            } //end run
        }); //end invokelater
    }//end main

    /**Costruttore */
    public jfHome(){
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 550));

        initPaneNorth();
        this.add(jpNorth,BorderLayout.NORTH);

        jpCenter = new JPanel(new GridBagLayout());
        setDimension(jpCenter, 400, 500);
        jbCrea = new JButton("Crea calendario");
        jbCrea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaCalendario();
            }
        }); //addActionListener
        gbcButton = new GridBagConstraints();
        gbcButton.gridx = 1;
        gbcButton.gridy = 19;
        gbcButton.weightx = 0;
        gbcButton.weighty = 1;
        gbcButton.insets = new Insets(1, 1, 1, 1);
        gbcButton.anchor = GridBagConstraints.SOUTHEAST;
        jpCenter.add(jbCrea, gbcButton);
        this.add(jpCenter,BorderLayout.CENTER);

        gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.weightx = 0;
        gbcLabel.weighty = 0;
        gbcLabel.insets = new Insets(1, 1, 1, 1);
        gbcLabel.anchor = GridBagConstraints.NORTHWEST;

        gbcTextfield = new GridBagConstraints();
        gbcTextfield.gridx = 1;
        gbcTextfield.gridwidth = 2;
        gbcTextfield.weightx = 0;
        gbcTextfield.weighty = 0;
        gbcTextfield.insets = new Insets(1, 1, 1, 1);
        gbcTextfield.anchor = GridBagConstraints.NORTHWEST;
        //gbcTextfield.gridwidth = 2;
        //gbcTextfield.fill = GridBagConstraints.HORIZONTAL;

        this.setVisible(true);
        pack();
        addWindowListener(this);
    }

    /**inizializza il pannello nord */
    private void initPaneNorth(){
        Integer numeroSquadre[] = {null, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};        
        
        jpNorth = new JPanel(new FlowLayout());
        setDimension(jpNorth, 400, 30);

        jpNorth.add(new JLabel("Nome campionato"));

        jtfCampionato = new JTextField();
        jtfCampionato.setPreferredSize(new Dimension(150, 25));
        jpNorth.add(jtfCampionato);

        jpNorth.add(new JLabel("Quante squadre? "));

        jcbNumeroSquadre = new JComboBox(numeroSquadre);
        jcbNumeroSquadre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaOggetti();
            }
        }); //addActionListener
        jpNorth.add(jcbNumeroSquadre);
    }

    private void initPaneSouth(){

    }
    
    /**imposta le dimensioni del pannello
     *
     * @param pane
     * @param width
     * @param height
     */
    private void setDimension(JPanel pane, int width, int height){
        pane.setPreferredSize(new Dimension(width, height));
        pane.setMinimumSize(new Dimension(width, height));
        pane.setMaximumSize(new Dimension(width, height));
    }

    /**Crea dinamicamente gli oggetti per le squadre */
    private void creaOggetti(){                
        if (jcbNumeroSquadre.getSelectedItem() == null) {
            return;
        }

        int numeroSquadreCorrente = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();
        if (numeroSquadreCorrente != oldNumeroSquadre && oldNumeroSquadre != 0) {
            for (int j = 0; j < oldNumeroSquadre; j++) {
                jpCenter.remove(jlSquadre[j]);
                jpCenter.remove(jtfSquadre[j]);
                validate();
            }
        }

        jlSquadre = new JLabel[numeroSquadreCorrente];
        jtfSquadre = new JTextField[numeroSquadreCorrente];

        for (int j = 0; j < numeroSquadreCorrente; j++) {

            jlSquadre[j] = new JLabel("Squadra " + (j + 1) + ":");
            gbcLabel.gridy = j;
            jpCenter.add(jlSquadre[j], gbcLabel);

            jtfSquadre[j] = new JTextField();
            jtfSquadre[j].setPreferredSize(new Dimension(160, 20));
            gbcTextfield.gridy = j;
            jpCenter.add(jtfSquadre[j], gbcTextfield);
        }
        oldNumeroSquadre = numeroSquadreCorrente;
        validate();
    }

    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(this, "Vuoi chiudere l'applicazione?",
                "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == 0)
            dispose();
    }

    private void creaCalendario(){
        long inizio = System.currentTimeMillis();
        Algoritmo algor = new Algoritmo();
        ArrayList<String> alSquadre = new ArrayList<String>();
        int numeroSquadreCorrente = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();
        for (int i=0; i<numeroSquadreCorrente; i++){
            alSquadre.add(jtfSquadre[i].getText());
        }        
        ArrayList<ArrayList<Accoppiamento>> alGiornate = algor.doBergerAlgorithm(numeroSquadreCorrente);
        Write.writeTXT(alGiornate, alSquadre);
        long tempo = System.currentTimeMillis() - inizio;
        JOptionPane.showMessageDialog(this, "Terminato in " + tempo + " ms");
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }

    
}