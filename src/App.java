import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        sparqlTest();
        FOF();
    }

    static void sparqlTest() {

        String queryString = "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?penombre ?pdate ?dnom ?unom" +
                "WHERE {" +
                "?uni wdt:P31 wd:Q3918 ." +
                "?uni rdfs:label ?unom ." +
                "?uni wdt:P17 wd:Q419 ." +
                "?persona wdt:P69 ?uni." +
                "?persona wdt:P106 wd:Q2526255." +
                "?persona rdfs:label ?dnom." +
                "?pelicula wdt:P57 ?persona." +
                "?pelicula rdfs:label ?penombre." +
                "?pelicula wdt:P577 ?pdate." +
                "FILTER(lang(?dnom)=\"es\")" +
                "FILTER(lang(?unom)=\"es\")" +
                "}" +
                "ORDER BY DESC ( ?pdate)";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", queryString);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }
    }

    static void FOF() {

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