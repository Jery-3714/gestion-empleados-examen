package edu.umg.programacion2.gestionempleados.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.gestionempleados.dao.EmpleadoDAO;
import edu.umg.programacion2.gestionempleados.modelo.Empleado;

public class VentanaEmpleados extends JFrame {

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextField txtBonoCompetencia;
    private JTextField txtFecha;
    private JCheckBox chkActivo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private EmpleadoDAO dao = new EmpleadoDAO();

    public VentanaEmpleados() {
        setTitle("Sistema de Gestion de Empleados");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(7, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombre = new JTextField();
        txtDepartamento = new JTextField();
        txtSalario = new JTextField();
        txtBonoCompetencia = new JTextField();
        txtFecha = new JTextField();
        chkActivo = new JCheckBox("Empleado activo", true);

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Nombre completo:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Departamento:"));
        formulario.add(txtDepartamento);

        formulario.add(new JLabel("Salario:"));
        formulario.add(txtSalario);

        formulario.add(new JLabel("Bono de competencia:"));
        formulario.add(txtBonoCompetencia);

        formulario.add(new JLabel("Fecha de contratacion (AAAA-MM-DD):"));
        formulario.add(txtFecha);

        formulario.add(new JLabel("Estado:"));
        formulario.add(chkActivo);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Departamento", "Salario",
                    "Bono de competencia", "Total",
                    "Fecha de contratacion", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tabla.getSelectedRow();

                if (fila >= 0) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtDepartamento.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtSalario.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtBonoCompetencia.setText(
                            modeloTabla.getValueAt(fila, 4).toString());
                    txtFecha.setText(modeloTabla.getValueAt(fila, 6).toString());
                    chkActivo.setSelected(
                            modeloTabla.getValueAt(fila, 7).toString().equals("Sí"));
                }
            }
        });

        JButton btnCrear = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnRecargar = new JButton("Recargar");

        btnCrear.addActionListener(e -> crearEmpleado());
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRecargar.addActionListener(e -> cargarEmpleados());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnRecargar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarEmpleados();
    }

    private void cargarEmpleados() {
        try {
            modeloTabla.setRowCount(0);

            List<Empleado> empleados = dao.listarTodos();

            for (Empleado empleado : empleados) {

                double total = empleado.getSalario()
                        + empleado.getBonoCompetencia();

                modeloTabla.addRow(new Object[]{
                    empleado.getId(),
                    empleado.getNombreCompleto(),
                    empleado.getDepartamento(),
                    empleado.getSalario(),
                    empleado.getBonoCompetencia(),
                    total,
                    empleado.getFechaContratacion(),
                    empleado.isActivo() ? "Sí" : "No"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar empleados: " + e.getMessage());
        }
    }

    private boolean validarFormulario() {
        try {
            if (txtNombre.getText().trim().isEmpty()
                    || txtDepartamento.getText().trim().isEmpty()
                    || txtSalario.getText().trim().isEmpty()
                    || txtBonoCompetencia.getText().trim().isEmpty()
                    || txtFecha.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Completa todos los campos.");
                return false;
            }

            double salario = Double.parseDouble(txtSalario.getText().trim());

            if (!Double.isFinite(salario) || salario <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El salario debe ser mayor que cero.");
                return false;
            }

            double bonoCompetencia = Double.parseDouble(
                    txtBonoCompetencia.getText().trim());

            if (!Double.isFinite(bonoCompetencia) || bonoCompetencia < 0) {
                JOptionPane.showMessageDialog(this,
                        "El bono de competencia no puede ser negativo.");
                return false;
            }

            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());

            if (fecha.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de contratacion no puede ser futura.");
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El salario y el bono deben ser numeros validos.");
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Usa el formato de fecha AAAA-MM-DD.");
        }

        return false;
    }

    private Empleado obtenerEmpleadoFormulario() {
        return new Empleado(
                txtNombre.getText().trim(),
                txtDepartamento.getText().trim(),
                Double.parseDouble(txtSalario.getText().trim()),
                Double.parseDouble(txtBonoCompetencia.getText().trim()),
                LocalDate.parse(txtFecha.getText().trim()),
                chkActivo.isSelected()
        );
    }

    private void crearEmpleado() {
        if (!validarFormulario()) {
            return;
        }

        try {
            if (dao.crear(obtenerEmpleadoFormulario())) {
                JOptionPane.showMessageDialog(this,
                        "Empleado registrado correctamente.");
                cargarEmpleados();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar: " + e.getMessage());
        }
    }

    private void actualizarEmpleado() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un empleado de la tabla.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Empleado empleado = obtenerEmpleadoFormulario();
            empleado.setId(Integer.parseInt(txtId.getText()));

            if (dao.actualizar(empleado)) {
                JOptionPane.showMessageDialog(this,
                        "Empleado actualizado correctamente.");
                cargarEmpleados();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un empleado de la tabla.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar este empleado?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());

            if (dao.eliminar(id)) {
                JOptionPane.showMessageDialog(this,
                        "Empleado eliminado correctamente.");
                cargarEmpleados();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtBonoCompetencia.setText("");
        txtFecha.setText("");
        chkActivo.setSelected(true);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaEmpleados().setVisible(true);
        });
    }
}