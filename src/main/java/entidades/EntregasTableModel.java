package entidades;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class EntregasTableModel extends AbstractTableModel {

    private List<Entrega> entregas;
    private String[] columnNames = {"Cliente", "Toneladas", "Remito"};

    public EntregasTableModel() {
        entregas = new ArrayList<>();

    }

    public void agregarEntregas(Entrega entrega) {
        entregas.add(entrega);
        fireTableRowsInserted(entregas.size() - 1, entregas.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return entregas.size();
    }

    public Object getValueAt(int row, int col) {
        Entrega entrega = entregas.get(row);
        switch (col) {
            case 0:
                return entrega.getCliente().getNombre();
            case 1:
                return entrega.getTeorico();
            case 2:
                return entrega.getRemito();
            default:
                return null;
        }
    }

    public Entrega getEntregaAt(int row) {
        return entregas.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(List<Entrega> entregas) {
        this.entregas = entregas;
        fireTableDataChanged();
    }

    public void actualizarDatosEnFila(int fila) {
        // Actualiza los datos en la fila correspondiente (fila en base a tu lógica)
        // Notifica a la tabla que la fila ha sido actualizada
        fireTableRowsUpdated(fila, fila);
    }
}
