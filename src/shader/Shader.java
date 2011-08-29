package shader;

public enum Shader {

    SIMPLE("simple.vs", "simple.fs", "vVertex", "vColor"),
    FLAT("flat.vs", "simple.fs", "vVertex", "vColor"),
    BULLET("bullet.vs", "bullet.fs", "vVertex", "vColor");
    
    private final String vertShaderFileName;
    private final String fragShaderFileName;
    private final String[] attributes;

    private Shader(String vertShaderFileName, String fragShaderFileName, String... attributes) {
        this.vertShaderFileName = vertShaderFileName;
        this.fragShaderFileName = fragShaderFileName;
        this.attributes = attributes;
    }

    public String getVertShaderFileName() {
        return vertShaderFileName;
    }

    public String getFragShaderFileName() {
        return fragShaderFileName;
    }

    public String[] getAttributes() {
        return attributes;
    }
}
