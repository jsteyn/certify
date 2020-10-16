import javax.swing.*;
import javax.swing.table.*;

import com.jannetta.certify.view.CheckBoxHeader;

import java.awt.event.*;

public class JTableHeaderCheckBox {
	Object colNames[] = { "", "String", "String" };
	Object[][] data = {};
	DefaultTableModel dtm;
	JTable table;

	public void buildGUI() {
		dtm = new DefaultTableModel(data, colNames);
		table = new JTable(dtm);
		for (int x = 0; x < 5; x++) {
			dtm.addRow(new Object[] { false, "Row " + (x + 1) + " Col 2", "Row " + (x + 1) + " Col 3" });
		}
		JScrollPane sp = new JScrollPane(table);
		TableColumn tc = table.getColumnModel().getColumn(0);
		tc.setCellEditor(table.getDefaultEditor(Boolean.class));
		tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
		tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));
		JFrame f = new JFrame();
		f.getContentPane().add(sp);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	class MyItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getSource();
			if (source instanceof AbstractButton == false)
				return;
			boolean checked = e.getStateChange() == ItemEvent.SELECTED;
			for (int x = 0, y = table.getRowCount(); x < y; x++) {
				table.setValueAt(checked, x, 0);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JTableHeaderCheckBox().buildGUI();
			}
		});
	}
}
