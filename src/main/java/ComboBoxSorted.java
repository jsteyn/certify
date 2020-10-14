import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ComboBoxSorted extends JFrame {

    private static final long serialVersionUID = 1L;
    private JComboBox comboBox;
    private JTextField textField;

    public ComboBoxSorted() {
        textField = new JTextField(15);
        textField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                comboBox.addItem(textField.getText());
                textField.setText("");
                comboBox.showPopup();
            }
        });

        String[] items = {"one", "two", "three", "four", "five"};
        SortedComboBoxModel model = new SortedComboBoxModel(items);
        comboBox = new JComboBox(model);
        comboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        JFrame frame = new JFrame("Add Item on Runtime");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.add(comboBox, BorderLayout.SOUTH);
        frame.add(textField, BorderLayout.WEST);
        frame.add(new JLabel("Enter to add Item  "), BorderLayout.EAST);
        frame.pack();
        frame.setLocation(150, 150);
        frame.setVisible(true);
    }

    private class SortedComboBoxModel extends DefaultComboBoxModel {

        private static final long serialVersionUID = 1L;

        public SortedComboBoxModel() {
            super();
        }

        public SortedComboBoxModel(Object[] items) {
            Arrays.sort(items);
            int size = items.length;
            for (int i = 0; i < size; i++) {
                super.addElement(items[i]);
            }
            setSelectedItem(items[0]);
        }

        public SortedComboBoxModel(Vector items) {
            Collections.sort(items);
            int size = items.size();
            for (int i = 0; i < size; i++) {
                super.addElement(items.elementAt(i));
            }
            setSelectedItem(items.elementAt(0));
        }

        @Override
        public void addElement(Object element) {
            insertElementAt(element, 0);
        }

        @Override
        public void insertElementAt(Object element, int index) {
            int size = getSize();
            //  Determine where to insert element to keep model in sorted order            
            for (index = 0; index < size; index++) {
                Comparable c = (Comparable) getElementAt(index);
                if (c.compareTo(element) > 0) {
                    break;
                }
            }
            super.insertElementAt(element, index);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ComboBoxSorted comboBoxSorted = new ComboBoxSorted();
            }
        });
    }
}
