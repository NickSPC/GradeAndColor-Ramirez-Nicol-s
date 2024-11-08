package Community_interaction_project_Controller;

public class ColorModel {
    private int idColor;
    private String colorName;

    // Constructor
    public ColorModel(int idColor, String colorName) {
        this.idColor = idColor;
        this.colorName = colorName;
    }

    // Getter And Setters
    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
