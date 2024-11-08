package Community_interaction_project_View;

import Community_interaction_project_Controller.ColorGradeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GradeColorChanger extends JFrame {

    private JComboBox<Integer> gradeComboBox;  // Desplegable para seleccionar la calificación
    private JButton colorButton;  // Botón que cambiará de color
    private ColorGradeService colorGradeService;  // Servicio para acceder a la BD

    public GradeColorChanger() {
        colorGradeService = new ColorGradeService();  // Inicializamos el servicio de la BD

        // Configuración básica de la ventana
        setTitle("Calificación y Color");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar ventana
        setLayout(new BorderLayout());

        // Establecer fondo gris para todo el JFrame
        getContentPane().setBackground(Color.DARK_GRAY);

        // Crear el panel superior (donde va la cabecera)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(60, 63, 65));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel headerLabel = new JLabel("Sistema de Calificaciones y Colores");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 26));

        headerPanel.add(headerLabel);

        // Panel central (donde va el JComboBox y el botón)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("Selecciona una calificación del 1 al 5:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(CENTER_ALIGNMENT);

        gradeComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        gradeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        gradeComboBox.setMaximumSize(new Dimension(150, 30));
        gradeComboBox.setAlignmentX(CENTER_ALIGNMENT);

        // Crear el botón que cambiará de color
        colorButton = new JButton("Cambiar Color");
        colorButton.setPreferredSize(new Dimension(300, 150));  // Tamaño del botón
        colorButton.setFont(new Font("Arial", Font.BOLD, 20));  // Cambiar la fuente del botón
        colorButton.setBackground(Color.LIGHT_GRAY);  // Color de fondo inicial del botón
        colorButton.setForeground(Color.BLACK);  // Color de texto del botón
        colorButton.setFocusPainted(false);  // Eliminar borde de selección del botón
        colorButton.setAlignmentX(CENTER_ALIGNMENT);
        colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));  // Bordes redondeados

        // Añadir un efecto hover para el botón
        colorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorButton.setBackground(Color.LIGHT_GRAY);
            }
        });

        // Añadir los componentes al panel central
        centerPanel.add(label);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // Espacio entre componentes
        centerPanel.add(gradeComboBox);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 100)));  // Espacio entre componentes
        centerPanel.add(colorButton);

        // Listener para el JComboBox que cambia el color del botón según la calificación seleccionada
        gradeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedGrade = (int) gradeComboBox.getSelectedItem();  // Obtener la calificación seleccionada
                try {
                    // Obtener el color correspondiente desde la BD según la calificación
                    String colorName = colorGradeService.getColorByGrade(selectedGrade);
                    if (colorName != null) {
                        // Cambiar el color del botón según el nombre del color recuperado
                        colorButton.setBackground(getColorFromName(colorName));
                    } else {
                        // Si no se encuentra el color, el botón se vuelve gris claro
                        colorButton.setBackground(Color.LIGHT_GRAY);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();  // Mostrar la excepción si hay un error
                    JOptionPane.showMessageDialog(null, "Error al obtener el color desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Crear el footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(60, 63, 65));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Sistema de Calificaciones - Nicolás Ramirez.");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        footerPanel.add(footerLabel);

        // Añadir paneles al JFrame
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // ***Botón CRUD***

        JButton crudButton = new JButton("Administrar Colores y Calificaciones");
        crudButton.setPreferredSize(new Dimension(300, 50));  // Tamaño del botón CRUD
        crudButton.setFont(new Font("Arial", Font.BOLD, 18));  // Fuente del botón CRUD
        crudButton.setBackground(new Color(102, 205, 170));  // Color de fondo del botón
        crudButton.setForeground(Color.BLACK);  // Color de texto del botón
        crudButton.setFocusPainted(false);
        crudButton.setBorder(BorderFactory.createEmptyBorder());
        crudButton.setAlignmentX(CENTER_ALIGNMENT);

        // Añadimos un espacio adicional y el botón CRUD al panel central
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));  // Espacio entre el botón principal y el nuevo botón
        centerPanel.add(crudButton);
        
        // Acción para abrir la ventana CRUD
        crudButton.addActionListener(e -> {
            new CrudFrame();  // Abre la ventana CRUD al hacer clic
        });

        // Hacer visible la View
        setVisible(true);
    }

    // Método para convertir el nombre del color en un objeto Color
    private Color getColorFromName(String colorName) {
        // Mapa de los nombres de colores que se utilizarán
        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("rojo", Color.RED);
        colorMap.put("naranja", Color.ORANGE);
        colorMap.put("amarillo", Color.YELLOW);
        colorMap.put("verde claro", new Color(144, 238, 144)); 
        colorMap.put("verde oscuro", new Color(0, 100, 0));

        return colorMap.getOrDefault(colorName.toLowerCase(), Color.LIGHT_GRAY);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        // Ejecuta la interfaz gráfica en el main principal de la interfaz
        SwingUtilities.invokeLater(() -> new GradeColorChanger());
    }
}
