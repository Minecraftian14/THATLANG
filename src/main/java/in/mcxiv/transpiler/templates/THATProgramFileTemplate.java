package in.mcxiv.transpiler.templates;

public class THATProgramFileTemplate extends Template {
    public THATProgramFileTemplate() {
        super("""
                import thatlang.core.*;
                               
                public class %FILE_NAME% extends THATProgramFile {
                    
                    // Constructor definition
                    public %FILE_NAME%() {
                        // Load all THATPrograms
                        // Load all THATFunctions
                        // Import/Append/Load other files.
                    }
                                
                    // THATProgram definitions
                    %THATProgram Instance%
                   
                    // THATFunction definitions
                }
                """);
    }

    public void injectFileName(String name) {
        inject("FILE_NAME", name);
    }

    public void addProgram(String fclass) {
        add("THATProgram Instance", fclass);
    }

}
