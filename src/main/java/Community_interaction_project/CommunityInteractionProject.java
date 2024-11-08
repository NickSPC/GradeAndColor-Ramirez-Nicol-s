package Community_interaction_project;

import javax.swing.SwingUtilities;
import Community_interaction_project_View.GradeColorChanger;

public class CommunityInteractionProject {

   public static void main(String[] args) {
        // Ejecuta la interfaz gráfica
        SwingUtilities.invokeLater(() -> new GradeColorChanger());
    }
}
