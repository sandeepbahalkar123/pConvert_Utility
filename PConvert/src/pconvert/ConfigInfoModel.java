
package pconvert;

/**
 *
 * @author riteshpandhurkar
 */
public class ConfigInfoModel {

    private String ooLibPath = "";
    private String pathOfDestinationFolder = "";
    private String nameOfDestinationFile = "";
    private String pathOfSourceFolder = "";
    private String nameOfSourceFile = "";
    private String tempFolder = "";
    private String conversionType = "";

    /**
     * Gets the folder containing the Open Office libraries
     *
     * @return The folder containing the Open Office libraries
     */
    public String getOOLibPath() {
        return ooLibPath;
    }

    /**
     * Sets the folder containing the Open Office libraries
     *
     * @param val The folder containing the Open Office libraries
     */
    public void setOOLibPath(String val) {
        ooLibPath = val;
    }

    public String getPathOfDestinationFolder() {
        return pathOfDestinationFolder;
    }

    public void setPathOfDestinationFolder(String pathOfDestinationFolder) {
        this.pathOfDestinationFolder = pathOfDestinationFolder;
    }

    public String getNameOfDestinationFile() {
        return nameOfDestinationFile;
    }

    public void setNameOfDestinationFile(String nameOfDestinationFile) {
        this.nameOfDestinationFile = nameOfDestinationFile;
    }

    public String getPathOfSourceFolder() {
        return pathOfSourceFolder;
    }

    public void setPathOfSourceFolder(String pathOfSourceFolder) {
        this.pathOfSourceFolder = pathOfSourceFolder;
    }

    public String getNameOfSourceFile() {
        return nameOfSourceFile;
    }

    public void setNameOfSourceFile(String nameOfSourceFile) {
        this.nameOfSourceFile = nameOfSourceFile;
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }

    public String getConversionType() {
        return conversionType;
    }

    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    @Override
    public String toString() {
        return "ConfigInfoModel{" + "ooLibPath=" + ooLibPath + ", pathOfDestinationFolder=" + pathOfDestinationFolder + ", nameOfDestinationFile=" + nameOfDestinationFile + ", pathOfSourceFolder=" + pathOfSourceFolder + ", nameOfSourceFile=" + nameOfSourceFile + ", tempFolder=" + tempFolder + ", conversionType=" + conversionType + '}';
    }

}
