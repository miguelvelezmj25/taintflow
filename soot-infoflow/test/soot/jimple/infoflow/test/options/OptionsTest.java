package soot.jimple.infoflow.test.options;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OptionsTest {

    protected static String appPath;
    protected static String libPath;
    protected static List<String> sources;
    protected static List<String> sinks;

    @BeforeClass
    public static void setUp() throws IOException {
        File f = new File("./soot-infoflow/");
        File testSrc1 = new File(f, "bin");

        if(!testSrc1.exists()) {
            fail("Test aborted - none of the test OptionsTest.sources are available");
        }

        OptionsTest.appPath = testSrc1.getCanonicalPath();
        OptionsTest.libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar";
    }

    @Before
    public void beforeTest() {
        OptionsTest.sources = new ArrayList<>();
        OptionsTest.sources.add("<soot.jimple.infoflow.test.options.Options: boolean getOption()>");

        OptionsTest.sinks = new ArrayList<>();
        OptionsTest.sinks.add("<soot.jimple.infoflow.test.options.Options: boolean getDecision(boolean)>");
    }

    protected void checkInfoflow(Infoflow infoflow, int resultCount){
        if(infoflow.isResultAvailable()){
            InfoflowResults map = infoflow.getResults();
            boolean containsSink = false;
            List<String> actualSinkStrings = new LinkedList<String>();
            assertEquals(resultCount, map.size());
            for(String sink : OptionsTest.sinks){
                if(map.containsSinkMethod(sink)){
                    containsSink = true;
                    actualSinkStrings.add(sink);
                }
            }

            assertTrue(containsSink);
            boolean onePathFound = false;
            for(String sink : actualSinkStrings){
                boolean hasPath = false;
                for(String source : OptionsTest.sources){
                    if(map.isPathBetweenMethods(sink, source)){
                        hasPath = true;
                        break;
                    }
                }
                if(hasPath){
                    onePathFound = true;
                }
            }
            assertTrue(onePathFound);

        }else{
            fail("result is not available");
        }
    }

    protected void negativeCheckInfoflow(Infoflow infoflow){
        if(infoflow.isResultAvailable()){
            InfoflowResults map = infoflow.getResults();
            for(String sink : OptionsTest.sinks){
                if(map.containsSinkMethod(sink)){
                    fail("sink is reached: " +sink);
                }
            }
            assertEquals(0, map.size());
        }else{
            fail("result is not available");
        }
    }

    @Test
    public void sleep0Test1() {
        Infoflow infoflow = new Infoflow();
        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<soot.jimple.infoflow.test.options.code.Sleep0: void main(java.lang.String[])>");
        infoflow.computeInfoflow(OptionsTest.appPath, OptionsTest.libPath, entryPoints, OptionsTest.sources, OptionsTest.sinks);
        checkInfoflow(infoflow, 1);
    }

}
