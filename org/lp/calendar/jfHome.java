package org.lp.calendar;

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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.lp.calendar.writers.*;

/**Gui principale
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class jfHome extends JFrame implements WindowListener{

    private JPanel jpGenera;
    private GridBagConstraints gbcLabel, gbcTextField, gbcComboBox, gbcButton, gbcRadioB;
    private JTextField jtfNomeCampionato;
    private JComboBox jcbNumeroSquadre;
    private JButton jbPulisci, jbCreaCalendario;
    private int previousTeamsNumber, currentTeamsNumber;
    private JLabel[] jlTeams;
    private JTextField[] jtfTeams;
    private JMenuBar jmbMenu;
    private ArrayList<ArrayList<AccoppiamentoVO>> alGiornate;
    private ArrayList<String> alSquadre;
    private JRadioButton[] jrbOption;

    /**Costruttore */
    public jfHome(){
        super("Generatore Calendario ");
        this.setPreferredSize(new Dimension(400, 650));

        jpGenera = new JPanel(new GridBagLayout());

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

        gbcRadioB = new GridBagConstraints();
        gbcRadioB.anchor = GridBagConstraints.NORTHWEST;

        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        jpGenera.add(new JLabel("Nome Campionato:"), gbcLabel);

        jtfNomeCampionato = new JTextField(20);
        gbcTextField.gridx = 1;
        gbcTextField.gridy = 0;
        gbcTextField.gridwidth = 2;
        gbcTextField.fill = GridBagConstraints.HORIZONTAL;
        jpGenera.add(jtfNomeCampionato, gbcTextField);
        
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 2;
        jpGenera.add(new JLabel("Numero squadre:"), gbcLabel);

        Integer teamsNumber[] = {null, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        jcbNumeroSquadre = new JComboBox(teamsNumber);
        gbcComboBox.gridx = 1;
        gbcComboBox.gridy = 2;
        jpGenera.add(jcbNumeroSquadre, gbcComboBox);
        jcbNumeroSquadre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaOggetti();
            }
        });

        gbcRadioB.gridx = 0;
        gbcRadioB.gridy = 1;
        jpGenera.add(new JLabel("Tipo Calendario:"), gbcRadioB);

        jrbOption = new JRadioButton[2];
        jrbOption[0] = new JRadioButton("Solo Andata");
        jrbOption[1] = new JRadioButton("Andata/Ritorno");        
        //Group the radio buttons.
        ButtonGroup groupJRB = new ButtonGroup();
        groupJRB.add(jrbOption[0]);
        groupJRB.add(jrbOption[1]);
        gbcRadioB.gridx = 1;
        jpGenera.add(jrbOption[0],gbcRadioB);
        gbcRadioB.gridx = 2;
        jpGenera.add(jrbOption[1],gbcRadioB);

        jbPulisci = new JButton("Pulisci Campi");
        jbPulisci.setPreferredSize(new Dimension(120, 20));
        gbcButton.gridx = 2;
        gbcButton.gridy = 22;
        jbPulisci.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pulisciCampi();
            }
        });
        jpGenera.add(jbPulisci, gbcButton);

        jbCreaCalendario = new JButton("Crea Calendario");
        jbCreaCalendario.setPreferredSize(new Dimension(140, 20));
        gbcButton.gridx = 1;
        gbcButton.weightx = 1;
        jbCreaCalendario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                creaCalendario();
            }
        });
        jpGenera.add(jbCreaCalendario, gbcButton);
        
        gbcTextField.fill = GridBagConstraints.NONE;

        this.add(jpGenera);
        initMenuBar();
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
                jfHome jframe = new jfHome();
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

        currentTeamsNumber = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();

        if (currentTeamsNumber != previousTeamsNumber && previousTeamsNumber != 0)
            removeLabel_TextField();

        i = currentTeamsNumber;
        jlTeams = new JLabel[i];
        jtfTeams = new JTextField[i];
        gbcLabel.gridx = 0;
        gbcTextField.gridx = 1;
        gbcTextField.ipadx = 10;
        gbcTextField.gridwidth = 3;

        for (int j = 0; j < i; j++) {

            jlTeams[j] = new JLabel("Squadra " + (j + 1) + ":");
            
            gbcLabel.gridy = y;
            jpGenera.add(jlTeams[j], gbcLabel);
            jlTeams[j].setVisible(true);

            jtfTeams[j] = new JTextField();
            jtfTeams[j].setPreferredSize(new Dimension(250, 20));
            gbcTextField.gridy = y;            
            jpGenera.add(jtfTeams[j], gbcTextField);
            jtfTeams[j].setVisible(true);

            y++;
        }
        previousTeamsNumber = currentTeamsNumber;

        validate();
    }// end creaOggetti

    /**Evento creazione Calendario*/
    private void creaCalendario(){
        if (jcbNumeroSquadre.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this, "Tutti i campi devo essere valorizzati!!!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numeroSquadreCorrente = ((Integer) jcbNumeroSquadre.getSelectedItem()).intValue();

        if (jtfNomeCampionato.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Tutti i campi devono essere valorizzati!!!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int j = 0; j < numeroSquadreCorrente; j++) {
            if (jtfTeams[j].getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Tutti i campi devono essere valorizzati!!!",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!jrbOption[0].isSelected() && !jrbOption[1].isSelected()){
            JOptionPane.showMessageDialog(this, "Deve essere scelto il Tipo di Calendario da generare!!!",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        alSquadre = new ArrayList<String>();
        for (int i=0; i<numeroSquadreCorrente; i++){
            alSquadre.add(jtfTeams[i].getText());
        }

        for (String s : alSquadre) {
            if (Collections.frequency(alSquadre, s) > 1) {
                JOptionPane.showMessageDialog(this,
                        "Non è possibile avere più Squadre con lo stesso nome!!!",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        alGiornate = Algoritmi.doBergerAlgorithm(numeroSquadreCorrente, jrbOption[0].isSelected());
        JOptionPane.showMessageDialog(this, "Calendario generato, puoi scegliere la modalità di stampa dal menù alla voce stampa:)");
    } //end creaCalendario

    /**pulisce i campi textfield e combobox*/
    private void pulisciCampi(){
        jtfNomeCampionato.setText(null);
        jcbNumeroSquadre.setSelectedItem(null);
        for (int i=0; i<currentTeamsNumber; i++)
            jtfTeams[i].setText(null);
    }

    /**Inizializza la menubar*/
    private void initMenuBar(){
        jmbMenu = new JMenuBar();

        JMenu jmCalendar = new JMenu("Calendario");
        jmbMenu.add(jmCalendar);
        JMenuItem jmiCreateCalendar = new JMenuItem("Crea nuovo calendario");
        jmiCreateCalendar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        jmCalendar.add(jmiCreateCalendar);

        JMenu jmPrint = new JMenu("Stampa");
        jmbMenu.add(jmPrint);
        JMenuItem jmiTxt = new JMenuItem("File TXT");
        jmiTxt.setName(WritersMode.TXT.name());
        jmiTxt.addActionListener(actionListener());
        JMenuItem jmiHtml = new JMenuItem("File HTML");
        jmiHtml.setName(WritersMode.HTML.name());
        jmiHtml.addActionListener(actionListener());
        JMenuItem jmiPdf = new JMenuItem("File PDF");
        jmiPdf.setName(WritersMode.PDF.name());
        jmiPdf.addActionListener(actionListener());
        JMenuItem jmiXLS1 = new JMenuItem("File XLS v.1");
        jmiXLS1.setName(WritersMode.XLS1.name());
        jmiXLS1.addActionListener(actionListener());
        JMenuItem jmiXLS2 = new JMenuItem("File XLS v.2");
        jmiXLS2.setName(WritersMode.XLS2.name());
        jmiXLS2.addActionListener(actionListener());
        JMenuItem jmiAll = new JMenuItem("Tutti i file");
        jmiAll.setName(WritersMode.ALL.name());
        jmiAll.addActionListener(actionListener());
        jmPrint.add(jmiTxt);
        jmPrint.add(jmiHtml);
        jmPrint.add(jmiPdf);
        jmPrint.add(jmiXLS1);
        jmPrint.add(jmiXLS2);
        jmPrint.add(jmiAll);

        this.setJMenuBar(jmbMenu);
    }

    private ActionListener actionListener(){
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Write scrittura = null;
                String nome = jtfNomeCampionato.getText();
                String menuItemSource = ((JMenuItem)evt.getSource()).getName();
                if (menuItemSource.equalsIgnoreCase(WritersMode.TXT.name()))
                    scrittura = new Txt(nome, alGiornate, alSquadre);
                else if (menuItemSource.equalsIgnoreCase(WritersMode.HTML.name()))
                    scrittura = new Html(nome, alGiornate, alSquadre);
                else if (menuItemSource.equalsIgnoreCase(WritersMode.PDF.name()))
                    scrittura = new Pdf(nome, alGiornate, alSquadre);
                else if (menuItemSource.equalsIgnoreCase(WritersMode.XLS1.name()))
                    scrittura = new Xls(nome+"_v1", alGiornate, alSquadre);
                else if (menuItemSource.equalsIgnoreCase(WritersMode.XLS2.name()))
                    scrittura = new Xls2(nome+"_v2", alGiornate, alSquadre);
                else if (menuItemSource.equalsIgnoreCase(WritersMode.ALL.name()))
                    scrittura = new All(nome, alGiornate, alSquadre);
                scrittura.write();
                JOptionPane.showMessageDialog(getContentPane(),
                                            "file/s salvati in "+Common.getCurDir());
            }
        };
    }   

    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(this, "Vuoi chiudere l'applicazione?",
                "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == 0)
            dispose();
    }

    private void clear() {
        removeLabel_TextField();
        jtfNomeCampionato.setText(null);
        jcbNumeroSquadre.setSelectedItem(null);
        previousTeamsNumber = currentTeamsNumber = 0;
        jlTeams = null;
        jtfTeams = null;
        if (alGiornate!=null)
            alGiornate.clear();
        if (alSquadre!=null)
            alSquadre.clear();
        jrbOption[0].setSelected(false);
        jrbOption[1].setSelected(false);
        validate();
    }

    private void removeLabel_TextField(){
        for (int j = 0; j < previousTeamsNumber; j++) {
            jpGenera.remove(jlTeams[j]);
            jpGenera.remove(jtfTeams[j]);
            validate();
        }
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
} //end class