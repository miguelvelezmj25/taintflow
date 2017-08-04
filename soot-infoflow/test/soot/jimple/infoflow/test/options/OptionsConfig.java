package soot.jimple.infoflow.test.options;

import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;

public class OptionsConfig implements IInfoflowConfig {

    @Override
    public void setSootOptions(Options options) {
        // explicitly include packages for shorter runtime:
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        options.set_output_format(Options.output_format_none);
        Options.v().setPhaseOption("jb", "use-original-names:true");
//        Options.v().set_keep_line_number(true);
        Options.v().set_keep_offset(true);
        Options.v().set_coffi(true);
//        Options.v().set_ignore_classpath_errors(true);
    }
}
