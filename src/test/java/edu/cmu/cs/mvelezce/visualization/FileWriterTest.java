package edu.cmu.cs.mvelezce.visualization;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FileWriterTest {

    @Test
    public void readFile() throws IOException {
        FileWriter fw = new FileWriter();
        fw.readFile("kanzi");
    }

}