package edu.cmu.cs.mvelezce.analysis.option.json;

import java.util.List;

//@JsonDeserialize(using = SourceToSinkPath.SourceToSinkDeserializer.class)
public class SourceToSinkPath {

    private List<PathElement> path;

    private SourceToSinkPath() {
        ;
    }

    public SourceToSinkPath(List<PathElement> path) {
        this.path = path;
    }

    public List<PathElement> getPath() {
        return path;
    }

    public void setPath(List<PathElement> path) {
        this.path = path;
    }

    public static class PathElement {
        private String className;
        private int javaLineNumber;

        private PathElement() {
            ;
        }

        public PathElement(String className, int javaLineNumber) {
            this.className = className;
            this.javaLineNumber = javaLineNumber;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getJavaLineNumber() {
            return javaLineNumber;
        }

        public void setJavaLineNumber(int javaLineNumber) {
            this.javaLineNumber = javaLineNumber;
        }
    }


//    public static class SourceToSinkDeserializer extends JsonDeserializer<SourceToSinkPath> {
//
//        @Override
//        public SourceToSinkPath deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//            ObjectCodec oc = jsonParser.getCodec();
//            JsonNode node = oc.readTree(jsonParser);
//
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectReader reader = mapper.readerFor(new TypeReference<List<PathElement>>() {
//            });
//            List<PathElement> path = reader.readValue(node.get("configuration"));
//
//            return null;
//        }
//    }
}
