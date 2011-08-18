
public enum Shader {
    SIMPLE("simple.vs", "simple.fs");

    private final String vertShaderFileName;
    private final String fragShaderFileName;
    
    private Shader(String vertShaderFileName, String fragShaderFileName) {
        this.vertShaderFileName = vertShaderFileName;
        this.fragShaderFileName = fragShaderFileName;
    }

    public String getVertShaderFileName() {
        return vertShaderFileName;
    }

    public String getFragShaderFileName() {
        return fragShaderFileName;
    }
    
        
}
