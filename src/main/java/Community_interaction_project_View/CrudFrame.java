package Community_interaction_project_View;

import Community_interaction_project_Controller.ColorGradeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class CrudFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField gradeField;
    private JTextField colorField;
    private ColorGradeService colorGradeService;  // Servicio para manejar la conexión a la BD

    public CrudFrame() {
        colorGradeService = new ColorGradeService();  // Inicializar servicio de conexión a la BD

        // Configuración básica de la ventana CRUD
        setTitle("Administrar Colores y Calificaciones");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Estilo general
        Color backgroundColor = new Color(45, 52, 54);
        Color formBackgroundColor = new Color(99, 110, 114);
        Color buttonColor = new Color(39, 174, 96);
        Color buttonHoverColor = new Color(46, 204, 113);
        Color textColor = Color.WHITE;

        // Establecer el fondo del JFrame
        getContentPane().setBackground(backgroundColor);

        // Panel superior (Formulario para ingresar datos)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(formBackgroundColor);

        JLabel gradeLabel = new JLabel("Calificación:");
        gradeLabel.setForeground(textColor);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gradeField = new JTextField();
        gradeField.setFont(new Font("Arial", Font.PLAIN, 14));
        gradeField.setBackground(Color.WHITE);
        gradeField.setForeground(Color.BLACK);

        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setForeground(textColor);
        colorLabel.setFont(new Font("Arial", Font.BOLD, 16));

        colorField = new JTextField();
        colorField.setFont(new Font("Arial", Font.PLAIN, 14));
        colorField.setBackground(Color.WHITE);
        colorField.setForeground(Color.BLACK);

        formPanel.add(gradeLabel);
        formPanel.add(gradeField);
        formPanel.add(colorLabel);
        formPanel.add(colorField);

        // Panel inferior (Botones de acción)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(backgroundColor);

        JButton addButton = new JButton("Agregar");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");

        // Estilo de los botones
        styleButton(addButton, buttonColor, buttonHoverColor, textColor);
        styleButton(updateButton, buttonColor, buttonHoverColor, textColor);
        styleButton(deleteButton, buttonColor, buttonHoverColor, textColor);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Panel central (Tabla de colores y calificaciones)
        String[] columnNames = {"Calificación", "Color"};
        tableModel = new DefaultTableModel(columnNames, 0);  // Modelo de la tabla
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Añadir los paneles a la ventana
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cargar datos desde la base de datos cuando se abra la ventana
        loadTableData();

        // Acción del botón "Agregar"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String grade = gradeField.getText();
                String color = colorField.getText();

                if (!grade.isEmpty() && !color.isEmpty()) {
                    try {
                        // Insertar en la base de datos
                        colorGradeService.insertGradeAndColor(Integer.parseInt(grade), color);
                        // Agregar fila a la tabla
                        tableModel.addRow(new Object[]{grade, color});
                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al agregar la calificación y el color.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, complete ambos campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /* Acción del botón "Actualizar"
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    String grade = gradeField.getText();
                    String color = colorField.getText();

                    if (!grade.isEmpty() && !color.isEmpty()) {
                        try {
                            // Actualizar en la base de datos
                            colorGradeService.updateGradeAndColor(Integer.parseInt(grade), color);
                            // Actualizar fila seleccionada
                            tableModel.setValueAt(grade, selectedRow, 0);
                            tableModel.setValueAt(color, selectedRow, 1);
                            clearFields();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al actualizar la calificación y el color.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, complete ambos campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
         */
        
        // Acción del botón "Actualizar"
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    String gradeText = gradeField.getText();
                    String color = colorField.getText();

                    Integer newGradeNumber = null;
                    if (!gradeText.isEmpty()) {
                        newGradeNumber = Integer.parseInt(gradeText);
                    }

                    try {
                        // Obtener la calificación existente
                        Integer existingGradeNumber = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

                        // Actualizar en la base de datos
                        colorGradeService.updateGradeAndColor(
                                existingGradeNumber, // Calificación existente
                                newGradeNumber, // Nueva calificación (puede ser null)
                                color.isEmpty() ? null : color // Nuevo color (puede ser null)
                        );

                        // Actualizar fila seleccionada
                        if (newGradeNumber != null) {
                            tableModel.setValueAt(newGradeNumber, selectedRow, 0);
                        }
                        if (!color.isEmpty()) {
                            tableModel.setValueAt(color, selectedRow, 1);
                        }

                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al actualizar la calificación y el color.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Acción del botón "Eliminar"
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    try {
                        // Obtener la calificación de la fila seleccionada
                        int grade = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                        // Eliminar de la base de datos
                        colorGradeService.deleteGrade(grade);
                        // Eliminar fila seleccionada de la tabla
                        tableModel.removeRow(selectedRow);
                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar la calificación.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Acción al seleccionar una fila de la tabla
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    gradeField.setText(table.getValueAt(selectedRow, 0).toString());
                    colorField.setText(table.getValueAt(selectedRow, 1).toString());
                }
            }
        });

        // Hacer visible la ventana CRUD
        setVisible(true);
    }

    // Método para cargar los datos desde la base de datos y mostrarlos en la tabla
    private void loadTableData() {
        try {
            List<String[]> data = colorGradeService.getAllGradesAndColors(); // Obtén los datos desde el servicio
            for (String[] row : data) {
                tableModel.addRow(row);  // Añadir cada fila a la tabla
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar los campos del formulario
    private void clearFields() {
        gradeField.setText("");
        colorField.setText("");
    }

    // Método para estilizar botones
    private void styleButton(JButton button, Color backgroundColor, Color hoverColor, Color textColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2, true));
        button.setPreferredSize(new Dimension(120, 40));

        // Añadir efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }
}
