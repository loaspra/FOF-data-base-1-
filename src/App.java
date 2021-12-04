import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        sparqlTest();
    }

    static void sparqlTest() {
        // FileManager.get().AddLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("archivo.rdf");
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
                "SELECT * WHERE{?person foaf:name ?x .}";
        Query query = QueryFactory.create(queryString);
        QueryExecution pexec = QueryExecutionFactory.create(query, model);
        try {
            var results = pexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Literal name = soln.getLiteral("x");
                System.out.println(name);
            }
        } finally {
            pexec.close();
        }
    }
}