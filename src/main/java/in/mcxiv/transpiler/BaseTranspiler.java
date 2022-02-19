package in.mcxiv.transpiler;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.transpiler.templates.THATProgramFileTemplate;
import in.mcxiv.transpiler.templates.THATProgramTemplate;
import thatlang.core.THATProgramFile;

import java.util.ArrayList;

public class BaseTranspiler {

    ArrayList<ProgramFileToken> pfts = new ArrayList<>();

    public BaseTranspiler(ProgramFileToken... tokens) {
        for (ProgramFileToken token : tokens) {
            add(token);
        }
    }

    private void add(ProgramFileToken token) {
        pfts.add(token);
    }

    public String make(int i) {
        var token = pfts.get(i);

        var template = new THATProgramFileTemplate();
        template.injectFileName(token.getProgramFileName());

        for (ProgramToken programToken : token.getProgramTokens()) {
            template.addProgram(make(programToken));
        }

        return template.toString();
    }

    private String make(ProgramToken programToken) {
        var template = new THATProgramTemplate();

        template.injectProgramName(programToken.getProgramName());

        return template.toString();
    }

}
