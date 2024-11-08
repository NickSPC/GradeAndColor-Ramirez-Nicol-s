package Community_interaction_project_Controller;

import Community_interaction_project_Model.ConnectionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColorGradeService {

    private ConnectionBD connectionBD;  // Objeto de la clase de conexión

    // Constructor que inicializa la conexión
    public ColorGradeService() {
        connectionBD = new ConnectionBD();
    }

    // Método para obtener la lista de colores
    public List<ColorModel> getColors() throws SQLException {
        List<ColorModel> colors = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionBD.conectar();
            String query = "SELECT * FROM color";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                colors.add(new ColorModel(resultSet.getInt("id_color"), resultSet.getString("color_name")));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener los colores de la base de datos", e);
        }
        return colors;
    }

    // Método para obtener el nombre del color asociado a un número de calificación
    public String getColorByGrade(int gradeNumber) throws SQLException {
        Connection connection = null;
        String colorName = null;

        try {
            connection = connectionBD.conectar();
            String query = "SELECT c.color_name FROM grade g JOIN color c ON g.id_color = c.id_color WHERE g.grade_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, gradeNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                colorName = resultSet.getString("color_name");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener el color por calificación", e);
        }
        return colorName;
    }

    // Método para obtener todas las calificaciones y colores en un formato adecuado para la tabla del CRUD
    public List<String[]> getAllGradesAndColors() throws SQLException {
        List<String[]> data = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionBD.conectar();
            String query = "SELECT g.grade_number, c.color_name FROM grade g JOIN color c ON g.id_color = c.id_color";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String grade = resultSet.getString("grade_number");
                String color = resultSet.getString("color_name");
                data.add(new String[]{grade, color});
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener las calificaciones y colores de la base de datos", e);
        }
        return data;
    }

    // Método para insertar una nueva calificación y color
    public void insertGradeAndColor(int gradeNumber, String colorName) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionBD.conectar();

            // Verificar si el color ya existe en la tabla 'color'
            String checkColorQuery = "SELECT id_color FROM color WHERE color_name = ?";
            PreparedStatement checkColorStmt = connection.prepareStatement(checkColorQuery);
            checkColorStmt.setString(1, colorName);
            ResultSet resultSet = checkColorStmt.executeQuery();
            int colorId;

            if (resultSet.next()) {
                colorId = resultSet.getInt("id_color");  // Obtener el ID del color si ya existe
            } else {
                // Si el color no existe, insertarlo y obtener su ID
                String insertColorQuery = "INSERT INTO color (color_name) VALUES (?)";
                PreparedStatement insertColorStmt = connection.prepareStatement(insertColorQuery, Statement.RETURN_GENERATED_KEYS);
                insertColorStmt.setString(1, colorName);
                insertColorStmt.executeUpdate();

                ResultSet generatedKeys = insertColorStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    colorId = generatedKeys.getInt(1);  // Obtener el ID generado
                } else {
                    throw new SQLException("Error al obtener el ID del nuevo color insertado.");
                }
            }

            // Insertar la nueva calificación con el ID del color y el ID de usuario
            String insertGradeQuery = "INSERT INTO grade (grade_number, id_color, id_user_create, id_user_update) VALUES (?, ?, ?, ?)";
            PreparedStatement insertGradeStmt = connection.prepareStatement(insertGradeQuery);
            insertGradeStmt.setInt(1, gradeNumber);
            insertGradeStmt.setInt(2, colorId);
            insertGradeStmt.setInt(3, 29); // Aquí insertamos el ID de usuario 29
            insertGradeStmt.setInt(4, 29);
            insertGradeStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al insertar la nueva calificación y color", e);
        }
    }

    // Método para actualizar una calificación y su color asociado
    public void updateGradeAndColor(int gradeNumber, int newGradeNumber, String newColorName) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionBD.conectar();

            // Obtener el ID del color actual asociado a la calificación
            String getColorIdQuery = "SELECT id_color FROM grade WHERE grade_number = ?";
            PreparedStatement getColorIdStmt = connection.prepareStatement(getColorIdQuery);
            getColorIdStmt.setInt(1, gradeNumber);
            ResultSet resultSet = getColorIdStmt.executeQuery();
            int colorId;

            if (resultSet.next()) {
                colorId = resultSet.getInt("id_color");
            } else {
                throw new SQLException("La calificación especificada no existe.");
            }

            // Actualizar el nombre del color en la tabla 'color'
            String updateColorQuery = "UPDATE color SET color_name = ? WHERE id_color = ?";
            PreparedStatement updateColorStmt = connection.prepareStatement(updateColorQuery);
            updateColorStmt.setString(1, newColorName);
            updateColorStmt.setInt(2, colorId);
            updateColorStmt.executeUpdate();

            // Actualizar la calificación con el nuevo número de calificación
            String updateGradeQuery = "UPDATE grade SET grade_number = ?, id_user_update = ? WHERE grade_number = ?";
            PreparedStatement updateGradeStmt = connection.prepareStatement(updateGradeQuery);
            updateGradeStmt.setInt(1, newGradeNumber);
            updateGradeStmt.setInt(2, 29); // Aquí insertamos el ID de usuario que actualiza, por ejemplo, 29
            updateGradeStmt.setInt(3, gradeNumber);
            updateGradeStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al actualizar la calificación y su color asociado", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    // Método para eliminar una calificación y su color asociado
    public void deleteGrade(int gradeNumber) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionBD.conectar();

            // Obtener el ID del color asociado a la calificación
            String getColorIdQuery = "SELECT id_color FROM grade WHERE grade_number = ?";
            PreparedStatement getColorIdStmt = connection.prepareStatement(getColorIdQuery);
            getColorIdStmt.setInt(1, gradeNumber);
            ResultSet resultSet = getColorIdStmt.executeQuery();
            int colorId = -1;

            if (resultSet.next()) {
                colorId = resultSet.getInt("id_color");
            }

            // Eliminar la calificación
            String deleteGradeQuery = "DELETE FROM grade WHERE grade_number = ?";
            PreparedStatement deleteGradeStmt = connection.prepareStatement(deleteGradeQuery);
            deleteGradeStmt.setInt(1, gradeNumber);
            deleteGradeStmt.executeUpdate();

            // Eliminar el color si no está asociado a ninguna otra calificación
            if (colorId != -1) {
                String checkColorUsageQuery = "SELECT COUNT(*) AS count FROM grade WHERE id_color = ?";
                PreparedStatement checkColorUsageStmt = connection.prepareStatement(checkColorUsageQuery);
                checkColorUsageStmt.setInt(1, colorId);
                ResultSet countResultSet = checkColorUsageStmt.executeQuery();
                if (countResultSet.next() && countResultSet.getInt("count") == 0) {
                    // Si no hay más calificaciones que usen este color, eliminarlo
                    String deleteColorQuery = "DELETE FROM color WHERE id_color = ?";
                    PreparedStatement deleteColorStmt = connection.prepareStatement(deleteColorQuery);
                    deleteColorStmt.setInt(1, colorId);
                    deleteColorStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al eliminar la calificación y su color asociado", e);
        }
    }
}
