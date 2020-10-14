import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 * @see http://stackoverflow.com/questions/7137786
 * @see http://stackoverflow.com/questions/7092219
 * @see http://stackoverflow.com/questions/7093213
 */
public class SelectAllHeaderTest {

    private static final int BOOLEAN_COL = 2;
    private static final Object colNames[] = {"Column 1", "Column 2", ""};
    private DefaultTableModel model = new DefaultTableModel(null, colNames) {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == BOOLEAN_COL) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }
    };
    private JTable table = new JTable(model);

    public void create() {
        for (int x = 1; x < 6; x++) {
            model.addRow(new Object[]{
                    "Row " + x + ", Col 1", "Row " + x + ", Col 2", false
                });
        }
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(320, 160));
        TableColumn tc = table.getColumnModel().getColumn(BOOLEAN_COL);
        tc.setHeaderRenderer(new SelectAllHeader(table, BOOLEAN_COL));
        JFrame f = new JFrame();
        f.add(new JScrollPane(table));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SelectAllHeaderTest().create();
            }
        });
    }
}

/**
 * A TableCellRenderer that selects all or none of a Boolean column.
 * 
 * @param targetColumn the Boolean column to manage
 */
class SelectAllHeader extends JToggleButton implements TableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ALL = "✓ Select all";
    private static final String NONE = "✓ Select none";
    private JTable table;
    private TableModel tableModel;
    private JTableHeader header;
    private TableColumnModel tcm;
    private int targetColumn;
    private int viewColumn;

    public SelectAllHeader(JTable table, int targetColumn) {
        super(ALL);
        this.table = table;
        this.tableModel = table.getModel();
        if (tableModel.getColumnClass(targetColumn) != Boolean.class) {
            throw new IllegalArgumentException("Boolean column required.");
        }
        this.targetColumn = targetColumn;
        this.header = table.getTableHeader();
        this.tcm = table.getColumnModel();
        this.applyUI();
        this.addItemListener(new ItemHandler());
        header.addMouseListener(new MouseHandler());
        tableModel.addTableModelListener(new ModelHandler());
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
        return this;
    }


    @Override
    public void updateUI() {
        super.updateUI();
        applyUI();
    }

    private void applyUI() {
        this.setFont(UIManager.getFont("TableHeader.font"));
        this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        this.setBackground(UIManager.getColor("TableHeader.background"));
        this.setForeground(UIManager.getColor("TableHeader.foreground"));
    }


    // Return true if this toggle needs to match the model.
    private boolean needsToggle() {
        boolean allTrue = true;
        boolean allFalse = true;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            boolean b = (Boolean) tableModel.getValueAt(r, targetColumn);
            allTrue &= b;
            allFalse &= !b;
        }
        return allTrue && !isSelected() || allFalse && isSelected();
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            viewColumn = header.columnAtPoint(e.getPoint());
            int modelColumn = tcm.getColumn(viewColumn).getModelIndex();
            if (modelColumn == targetColumn) {
                doClick();
            }
        }
    }

    private class ItemHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean state = e.getStateChange() == ItemEvent.SELECTED;
            setText((state) ? NONE : ALL);
            for (int r = 0; r < table.getRowCount(); r++) {
                table.setValueAt(state, r, viewColumn);
            }
        }
    }

    private class ModelHandler implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (needsToggle()) {
                doClick();
                header.repaint();
            }
        }
    }
}