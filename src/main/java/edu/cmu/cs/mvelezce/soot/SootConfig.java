package edu.cmu.cs.mvelezce.soot;

import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;

import java.util.LinkedList;
import java.util.List;

public class SootConfig implements IInfoflowConfig {

    @Override
    public void setSootOptions(Options options) {
        // explicitly include packages for shorter runtime:
        List<String> includeList = new LinkedList<>();
//        includeList.add("java.lang.*");
//        includeList.add("java.util.*");
//        includeList.add("java.io.*");
//
//		includeList.add("java.security.*");
//		includeList.add("javax.crypto.*");

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);


//        includeList.add("java.util.HashMap");
//        options.set_include(includeList);

        options.set_output_format(Options.output_format_none);
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_keep_line_number(true);
        Options.v().set_keep_offset(true);
        Options.v().set_coffi(true);
        Options.v().set_ignore_classpath_errors(true);
    }

}
