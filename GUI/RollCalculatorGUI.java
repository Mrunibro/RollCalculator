package GUI;

import ROTMGclasses.*;
import ROTMGRoll.AbstractRoll;
import ROTMGRoll.CompositeRoll;
import ROTMGRoll.RollCalculator;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class RollCalculatorGUI implements ItemListener {

    private NumberPresenter presenter;

    private CompositeRoll compositeRoll;

    private ArrayList<String> includedRolls;
    private ArrayList<String> chosenCharacters;

    private static JFrame frame = new JFrame("ROTMG Roll Calculator");
    private RollCalcGUIManager gui;

    private JComboBox characterLevel;
    private JButton whatAreTheOddsButton;
    private JPanel mainPanel;
    private JComboBox characterClass;
    private JComboBox characterStat;
    private JComboBox characterStatAmount;
    private JButton addRollButton;
    private JButton clearLastEntry;
    private JButton clearAllEnteredRollsButton;
    private JLabel bottomRollsLabel;

    private String bottomRollsLabelStartText = "Your rolls: ";
    private boolean settingsUseNumberPresenter = true;
    private boolean settingsClearRollsAfterShow = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame.setContentPane(new RollCalculatorGUI().mainPanel);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false); //no resizing allowed. They'd see how hacked this UI is!

            //activate the window
            frame.pack();
            frame.setVisible(true);
        });
    }

    private RollCalculatorGUI() {
        //initialize some other variables
        presenter = new NumberPresenter();
        compositeRoll = new CompositeRoll();
        includedRolls = new ArrayList<>();
        chosenCharacters = new ArrayList<>();
        bottomRollsLabel.setText(bottomRollsLabelStartText);
        //initialize gui elements
        gui = new RollCalcGUIManager();
        gui.initialize(this);
        //Calculates roll statistics with RollCalculator.
        whatAreTheOddsButton.addActionListener(e -> {
            addToRollsList();
            gui.updateBottomLabel();

            //calculateAndAdd is time consuming, multi-thread to prevent unresponsive GUI.
            SwingUtilities.invokeLater(() -> {
                calculateAndAddRoll();

                gui.updateRemoveButtons();
                gui.showRollStatistics();

                if (settingsClearRollsAfterShow){

                    if (compositeRoll.isEmpty()) return;
                    compositeRoll.clearRolls();
                    chosenCharacters.clear();
                    includedRolls.clear();

                    gui.updateBottomLabel();
                    gui.updateRemoveButtons();
                }
            });
        });
        addRollButton.addActionListener(e -> {
            addToRollsList();
            gui.updateBottomLabel();

            //calculateAndAdd is time consuming, multi-thread to prevent unresponsive GUI.
            SwingUtilities.invokeLater(() -> {
                calculateAndAddRoll();
                gui.updateRemoveButtons();
            });
        });
        //whenever a selection is made, update stat amount comboBox
        characterLevel.addActionListener(e -> gui.updateStatAmountCombobox());
        characterClass.addActionListener(e -> gui.updateStatAmountCombobox());
        characterStat.addActionListener(e -> gui.updateStatAmountCombobox());
        //clear buttons: Note that all 3 collections are coupled so their size is equal.
        clearLastEntry.addActionListener(e -> {

            if (compositeRoll.isEmpty()) return;
            compositeRoll.removeRoll();
            chosenCharacters.remove(chosenCharacters.size()-1);
            includedRolls.remove(includedRolls.size()-1);
            gui.updateBottomLabel();

            gui.updateRemoveButtons();
        });
        clearAllEnteredRollsButton.addActionListener(e -> {

            if (compositeRoll.isEmpty()) return;
            compositeRoll.clearRolls();
            chosenCharacters.clear();
            includedRolls.clear();

            gui.updateBottomLabel();
            gui.updateRemoveButtons();
        });
    }

    /**
     * Extracts data from the AbstractRoll object into a message for displaying.
     *
     * @param yourRoll the roll Object holding statistics about your roll
     * @param level the level of the rolled character
     * @return the message string holding statistics for the roll.
     */
    private String generateRollMessage(AbstractRoll yourRoll, int level){
        //todo: possibly streamline amount of 1.0 / (n) divisions here and in RollCalculator to preserve accuracy of double data type. Least significant numbers on very large values are all fricked up
        //todo: Consider conversion from double to BigDecimal with custom MathContext for arbitrary-point precision
        StringBuilder msg = new StringBuilder();
        boolean badRoll = yourRoll.isBadRoll();

        String betterPct;
        String betterOneInX;
        String worsePct;
        String worseOneInX;
        String equalOrBetterPct;
        String equalOrWorsePct;
        String separator;

        //initialize display variables
        if (settingsUseNumberPresenter) {
            betterPct = presenter.improveDoubleLooks(yourRoll.getBetterPct());
            betterOneInX = presenter.improveDoubleLooks(yourRoll.getBetterOneInX());
            worsePct = presenter.improveDoubleLooks(yourRoll.getWorsePct());
            worseOneInX = presenter.improveDoubleLooks(yourRoll.getWorseOneInX());
            equalOrBetterPct = presenter.improveDoubleLooks((1.0 / yourRoll.getBetterOneInX()) * 100);
            equalOrWorsePct = presenter.improveDoubleLooks((1.0 / yourRoll.getWorseOneInX()) * 100);
        } else {
            betterPct = Double.toString(yourRoll.getBetterPct());
            betterOneInX = Double.toString(yourRoll.getBetterOneInX());
            worsePct = Double.toString(yourRoll.getWorsePct());
            worseOneInX = Double.toString(yourRoll.getWorseOneInX());
            equalOrBetterPct = Double.toString((1.0 / yourRoll.getBetterOneInX()) * 100);
            equalOrWorsePct = Double.toString((1.0 / yourRoll.getWorseOneInX()) * 100);
        }

        System.out.println(badRoll ? "This is a bad roll" : "This is a good roll");

        msg.append("Your level ");
        msg.append(level);
        msg.append(" ");

        //show list of various characters the user selected for compositeRoll
        separator = "";
        ArrayList<String> uniqueCharacters = new ArrayList<>();
        for (String s: chosenCharacters){
            if (! uniqueCharacters.contains(s)){
                uniqueCharacters.add(s);
                msg.append(separator);
                msg.append(s);
                separator = ", ";
            }
        }
        msg.append(" ");
        msg.append("with rolls:\n");

        //list all rolls the user added. Linebreak occasionally to prevent strange window sizes
        separator = "";
        for (int i = 0; i < includedRolls.size(); i++){
            String s = includedRolls.get(i);

            if (i != 0 && i % 4 == 0){
                msg.append(separator);
                msg.append("\n");
            } else {
                msg.append(separator);
            }
            separator = ", ";
            msg.append(s);
        }

        //other roll statistics
        msg.append("\n\nIs statistically ").append(badRoll ? "worse" : "better").append(" than ");
        msg.append(badRoll ? betterPct : worsePct);
        msg.append("% of all rolls.");

        msg.append("\nAnd statistically ").append(badRoll ? "better" : "worse").append(" than ");
        msg.append(badRoll ? worsePct : betterPct);
        msg.append("% of all rolls.\n");

        msg.append("\nYour odds for getting a roll like this or ");
        msg.append(badRoll ? "worse" : "better").append(" were:\n1 in ");
        msg.append(badRoll ? worseOneInX : betterOneInX);
        msg.append(" (that's ");
        msg.append(badRoll ? equalOrWorsePct : equalOrBetterPct);
        msg.append("%)");

        msg.append("\n\nAssuming 15 minutes per roll, it will take you approximately \n");
        msg.append(badRoll ? worseOneInX : betterOneInX);
        msg.append(" rolls = ");
        msg.append(badRoll ? presenter.improveDoubleLooks(yourRoll.getWorseOneInX() * 15): presenter.improveDoubleLooks(yourRoll.getBetterOneInX() * 15));
        msg.append(" minutes \n(");
        msg.append(badRoll ? presenter.improveDoubleLooks((yourRoll.getWorseOneInX() * 15) / (3600 * 24)) : presenter.improveDoubleLooks((yourRoll.getBetterOneInX() * 15) / (3600 * 24)));
        msg.append(" days, or ");
        msg.append(badRoll ? presenter.improveDoubleLooks((yourRoll.getWorseOneInX() * 15) / 525600.0) : presenter.improveDoubleLooks((yourRoll.getBetterOneInX() * 15) / 525600.0));
        msg.append(" years)");
        msg.append("\nOf playing the game to get a roll at least as ");
        msg.append(badRoll ? "bad." : "good.");

        return msg.toString();
    }

    /**
     * Calculate the roll of the selected data,
     * Then add the resulting roll Object to compositeRoll.
     */
    private void calculateAndAddRoll(){
        //get roll
        int roll = (Integer) characterStatAmount.getSelectedItem();

        //get level
        int level = (Integer) characterLevel.getSelectedItem();
        level--; //account for offset. lvl 20 == 19 rolls.

        //get realm class object
        int characterIndex = characterClass.getSelectedIndex();
        ROTMGCharacter selectedCharacter = ROTMGCharacter.values()[characterIndex];
        ROTMGClass yourCharacter = ROTMGClass.create(selectedCharacter);

        //get realm stat object
        int statIndex = characterStat.getSelectedIndex();
        ROTMGStat yourStat = ROTMGStat.values()[statIndex];

        //create roll calculator & compute your roll statistics
        RollCalculator calculator = new RollCalculator(level, yourCharacter, yourStat);

        try {
            compositeRoll.addRoll(calculator.computeRollOdds(roll));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ex.getClass().getCanonicalName(), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Add to list of executed roll computations
     *
     * Used in messageBox and bottom display label
     */
    private void addToRollsList(){
        String stat = characterStat.getSelectedItem().toString();
        String amount = characterStatAmount.getSelectedItem().toString();
        double relativeAmount = getRelativeRoll();
        String chosenCharacter = characterClass.getSelectedItem().toString();

        includedRolls.add(stat + ": " + relativeAmount + " (" + amount + ")");
        chosenCharacters.add(chosenCharacter);
    }

    /**
     * Get roll based on relative scale of average
     * @return roll based on avg value
     */
    private double getRelativeRoll(){
        //get roll
        int roll = (Integer) characterStatAmount.getSelectedItem();
        //Convert roll to float for displaying as relative value (e.g. +0.5 VIT is possible for warriors)
        float minRoll = (float)((Integer) characterStatAmount.getItemAt(0));
        float maxRoll = (float)(Integer) characterStatAmount.getItemAt(characterStatAmount.getItemCount() - 1);
        double averageRoll = (minRoll + maxRoll) / 2.0;
        return (double) roll - averageRoll;
    }

    /**
     * One of the settings checkboxes was changed
     * @param e the change-event
     */
    public void itemStateChanged(ItemEvent e) {
        String settingName = ((JCheckBoxMenuItem) e.getItem()).getName();
        boolean toChange = ((JCheckBoxMenuItem)e.getItem()).getState();
        //could probably just negate the state. nyeh.
        switch (settingName){
            case "UseNumberPresenter":
                settingsUseNumberPresenter = toChange;
                break;
            case "ClearRollsAfterShow":
                settingsClearRollsAfterShow = toChange;
                break;
            default: break;
        }
    }

    /**
     * This inner class manages anything that will bee displayed on the GUI
     * Whenever something has to get displayed,
     * a method in this class should bee responsible for the 'displaying' aspect of the task
     */
    private class RollCalcGUIManager {
        /**
         * Initialize components
         */
        void initialize(ItemListener parent) {
            //(manually) create menubar - IntelliJ form designer doesn't support menubars?
            createMenuBar(parent);
            //place values in comboboxes
            initCharacterLevel();
            initCharacterClass();
            initCharacterStat();
            updateRemoveButtons();
            //statAmount is based on all 3 of the above and will be updated when the above change. Hence its init == update.
            updateStatAmountCombobox();
        }

        /**
         * Called on any composite roll modification
         * Dis/En-ables the compositeRoll modification buttons
         */
        void updateRemoveButtons(){
            boolean canClear = ! compositeRoll.isEmpty();
            clearAllEnteredRollsButton.setEnabled(canClear);
            clearLastEntry.setEnabled(canClear);
        }

        /**
         * Updates the label at the bottom of the GUI
         *
         * Displays all rolls currently held inside compositeRoll
         * ( = what the user will get stats of when pressing 'the' button)
         */
        void updateBottomLabel(){
            StringBuilder newText = new StringBuilder("<html>");
            newText.append(bottomRollsLabelStartText);
            String separator = "";
            for (int i = 0; i < includedRolls.size(); i++){
                if (i % 5 == 0){
                    newText.append(separator);
                    newText.append("<br/>");
                } else {
                    newText.append(separator);
                }
                separator = ", ";
                newText.append("(").append(chosenCharacters.get(i)).append(") ");
                newText.append(includedRolls.get(i));
            }
            newText.append("</html>");
            bottomRollsLabel.setText(newText.toString());

            //resize accordingly (text may or may not change amount of lines on the bottom)
            frame.pack();
        }

        /**
         * Prepare showing of a messagebox with the computed roll statistics in compositeRoll
         */
        private void showRollStatistics(){
            //get level
            int level = (Integer) characterLevel.getSelectedItem();
            //generate and show message.
            String msg = generateRollMessage(compositeRoll, level);
            JOptionPane.showMessageDialog(null, msg, "Your results are...", JOptionPane.INFORMATION_MESSAGE);
        }

        /**
         * change JComboBox "characterStatAmount" to display values
         * only applicable (possible rolls) based on:
         * level, character, and stat
         */
        private void updateStatAmountCombobox() {
            int level = (Integer) characterLevel.getSelectedItem();
            level--; //account for the fact that level 1 is not a roll. that is, lvl 20 = 19 rolls.

            int characterIndex = characterClass.getSelectedIndex();
            ROTMGCharacter character = ROTMGCharacter.values()[characterIndex];

            int statIndex = characterStat.getSelectedIndex();
            ROTMGStat stat = ROTMGStat.values()[statIndex];

            ROTMGClass selected = ROTMGClass.create(character);
            int baseValue = selected.getBaseStat(stat);
            int minValue = level * selected.getStatRange(stat).getMin() + baseValue;
            int maxValue = level * selected.getStatRange(stat).getMax() + baseValue;

            characterStatAmount.removeAllItems(); //clear the combobox before starting
            for (int i = minValue; i <= maxValue; i++){
                //noinspection unchecked
                characterStatAmount.addItem(i);
            }
            characterStatAmount.setSelectedIndex(characterStatAmount.getItemCount() / 2); //select middle item (avg)
        }

        /**
         * Initialize characterLevel combobox for levels 1 to 19
         */
        private void initCharacterLevel() {
            characterLevel.removeAllItems();
            for (int i = 1; i <= 20; i++){
                //noinspection unchecked
                characterLevel.addItem(i);
            }
            characterLevel.setSelectedIndex(19); //level 20
        }

        /**
         * Initialize characterClass comboBox for all classes in enum
         */
        private void initCharacterClass(){
            characterClass.removeAllItems();
            for (ROTMGCharacter character : ROTMGCharacter.values()){
                //character enum is in allCaps. want only the first letter capitalized.
                String c = character.toString();
                char c1 = c.charAt(0); //preserve capitalized letter
                c = c1 + c.substring(1).toLowerCase();
                //noinspection unchecked
                characterClass.addItem(c);
            }
        }

        /**
         * Initialize characterStat combobox for all stats in enum
         */
        private void initCharacterStat(){
            characterStat.removeAllItems();
            for (ROTMGStat stat : ROTMGStat.values()){
                //noinspection unchecked
                characterStat.addItem(stat);
            }
        }

        /**
         * Initialize the menu bar
         * @param parent ItemListener used by menu items to handle events
         */
        private void createMenuBar(ItemListener parent){
            JMenuBar menuBar;
            JMenu menu;
            JMenuItem menuItem;
            JCheckBoxMenuItem checkBox;

            menuBar = new JMenuBar();
            menu = new JMenu("Settings");
            menuBar.add(menu);

            checkBox = new JCheckBoxMenuItem("Empty Roll List after computing odds");
            checkBox.setState(true);
            checkBox.addItemListener(parent);
            checkBox.setName("ClearRollsAfterShow");
            menu.add(checkBox);
            checkBox = new JCheckBoxMenuItem("Post-process roll statistics for present-ability");
            checkBox.setState(true);
            checkBox.addItemListener(parent);
            checkBox.setName("UseNumberPresenter");
            menu.add(checkBox);

            menu = new JMenu("About");
            menuItem = new JMenuItem("TODO");
            menu.add(menuItem);
            menuBar.add(menu);

            frame.setJMenuBar(menuBar);
        }
    }
}
