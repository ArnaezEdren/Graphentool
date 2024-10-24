package at.spengergasse.model;

import java.io.*;
import java.util.*;

public class Graph {
    private HashMap<Integer, int[][]> meineArrays;
    private HashMap<Integer, Integer> radiusdurchmesser;
    private HashMap<Integer, String> stringHash;
    private int[][] graph;
    private String text;
    private char infinitySymbol;
    public void einlesenCSV(String dateiName) throws GraphException {

        meineArrays = new HashMap<>();
        if (dateiName != null) {
            File file = new File(dateiName);
            if (file.exists() && file.canRead() && file.isFile()) {
                String sep = ";";
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String zeile = br.readLine();
                    int count = zeile.replace(";", "").length();
                    graph = new int[count][count];
                    int i = 0;
                    while (zeile != null) {
                        String[] zeilenTeile = zeile.split(sep);
                        for (int s = 0; s < count; s++) {
                            graph[i][s] = Integer.parseInt(zeilenTeile[s]);
                        }
                        i++;
                        zeile = br.readLine();
                    }
                    meineArrays.put(0, graph);
                } catch (IOException e) {
                    throw new GraphException("Fehler beim Import: Eingabe/Ausgabe-Fehler!" + e.getMessage());
                }
            } else {
                throw new GraphException("Fehler beim Import: Keine Datei ausgewählt!");
            }
        }
    }
    public void berechnungDistanzMatrix() {
        radiusdurchmesser = new HashMap<>();
        stringHash = new HashMap<>();
        int key = 0;
        int[][] graphMatrix = new int[graph.length][graph.length];
        for (int i = 0; i < graph.length; i++) {
            graphMatrix[i] = graph[i].clone();
        }
        int[][] array1 = new int[graph.length][graph.length];
        int[][] array2;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i == j) {
                    graphMatrix[i][j] = 0;
                } else {
                    if (graphMatrix[i][j] == 0) {
                        graphMatrix[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        String arrayAsString = Arrays.deepToString(graphMatrix);
        String gesuchtWert = String.valueOf(Integer.MAX_VALUE);
        int anzahl = 1;
        array2 = meineArrays.get(0);
        while ((arrayAsString.contains(gesuchtWert) && anzahl <= graph.length - 1)) {
            for (int k = 0; k < graph.length; k++) {
                for (int l = 0; l < graph.length; l++) {
                    for (int m = 0; m < graph.length; m++) {
                        array1[k][l] += array2[k][m] * graph[m][l];
                    }
                }
            }
            for (int k = 0; k < graph.length; k++) {
                for (int l = 0; l < graph.length; l++) {
                    if (graphMatrix[k][l] == Integer.MAX_VALUE && array1[k][l] != 0) {
                        graphMatrix[k][l] = anzahl + 1;
                    }
                }
            }
            arrayAsString = Arrays.deepToString(graphMatrix);
            meineArrays.put(anzahl, array1);
            array1 = new int[graph.length][graph.length];
            array2 = meineArrays.get(anzahl);
            anzahl += 1;
        }
        for (int y = 0; y < graph.length; y++) {
            int exzentritaet = 0;
            for (int z = 0; z < graph.length; z++) {
                if (graphMatrix[y][z] > exzentritaet) {
                    exzentritaet = graphMatrix[y][z];
                }
            }
            if (exzentritaet == Integer.MAX_VALUE) {
                infinitySymbol = '\u221E';
                text = "Exzentriät für " + (y + 1) + ": " + infinitySymbol;
                stringHash.put((key), text);
                text = "Exzentriät für " + (y + 1) + ": " + exzentritaet;
                radiusdurchmesser.put(y, exzentritaet);
            } else {
                text = "Exzentriät für " + (y + 1) + ": " + exzentritaet;
                stringHash.put((key), text);
                radiusdurchmesser.put(y, exzentritaet);
            }
            key = key + 1;
        }
    }
    public void herauslesenradiusdurchmesser() {
        if (radiusdurchmesser.containsValue(Integer.MAX_VALUE)) {
            text = ("Radius: " + infinitySymbol);
            stringHash.put(stringHash.size(), text);
            text = ("Durchmesser: " + infinitySymbol);
            stringHash.put(stringHash.size(), text);
            text = ("Zentrum ist nicht vorhanden");
            stringHash.put(stringHash.size(), text);
        } else {
            int durchmessser = Collections.max(radiusdurchmesser.values());
            int radius = Collections.min(radiusdurchmesser.values());
            text = ("Radius: " + radius);
            stringHash.put(stringHash.size(), text);
            text = ("Durchmesser: " + durchmessser);
            stringHash.put(stringHash.size(), text);
            for (Map.Entry<Integer, Integer> entry : radiusdurchmesser.entrySet()) {
                int key = 1 + entry.getKey();
                Integer value = entry.getValue();
                if (value == radius) {
                    text = ("Zentrum ist: " + key);
                    stringHash.put(stringHash.size(), text);
                }
            }
        }
    }
    public void kompWegMatrix() {
        text = ("Anzahl der Komponente: " + findeKomponenten(graph));
        stringHash.put(stringHash.size(), text);
    }
    public void findeBruecken() {
        HashMap<Integer, String> bruecken = new HashMap<>();
        int anzahlKomponentenAlt = findeKomponenten(graph);
        int brueckekey = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = i+1 ; j < graph.length; j++) {
                if (graph[i][j] != 0) {
                    int temp = graph[i][j];
                    graph[i][j] = graph[j][i] = 0;
                    int anzahlKomponenten = findeKomponenten(graph);
                    String BrueckenAnKnoten = " " + (i + 1) + " + " + (j + 1);
                    if (!bruecken.containsKey(brueckekey) && anzahlKomponenten > anzahlKomponentenAlt) {
                        bruecken.put(brueckekey, BrueckenAnKnoten);
                        brueckekey = brueckekey + 1;
                    }
                    graph[i][j] = graph[j][i] = temp;
                }
            }
        }
        for (int i = 0; i < bruecken.size(); i++) {
            text = ("Eine Brücke ist zwischen " + bruecken.get(i));
            stringHash.put(stringHash.size(), text);
        }
    }
    public int findeKomponenten(int[][] graph) {
        int n = graph.length;
        boolean[] besucht = new boolean[n];
        int komponentenZaehler = 0;
        for (int i = 0; i < n; i++) {
            if (!besucht[i]) {
                if (!istIsolierterKnoten(i, graph)) {
                    tiefensuche(i, graph, besucht);
                }
                komponentenZaehler++;
            }
        }
        return komponentenZaehler;
    }
    private boolean istIsolierterKnoten(int knoten, int[][] graph) {
        for (int nachbar = 0; nachbar < graph.length; nachbar++) {
            if (graph[knoten][nachbar] != 0 || graph[nachbar][knoten] != 0) {
                return false;
            }
        }
        return true;
    }
    private void tiefensuche(int knoten, int[][] graph, boolean[] besucht) {
        besucht[knoten] = true;
        for (int nachbar = 0; nachbar < graph.length; nachbar++) {
            if (graph[knoten][nachbar] != 0 && !besucht[nachbar]) {
                tiefensuche(nachbar, graph, besucht);
            }
        }
    }
    public void findeArtikulation() {
        for (int knotenEntfernen = 0; knotenEntfernen < graph.length; knotenEntfernen++) {
            int[][] graphOhneKnoten = entferneKnoten(graph, knotenEntfernen);
            int anzahlKomponenten = findeKomponenten(graphOhneKnoten);
            int anzahlKomponentenAlt = findeKomponenten(graph);
            if (anzahlKomponenten > anzahlKomponentenAlt) {
                text = ("Der Knoten " + (knotenEntfernen + 1) + " ist eine Artikulation");
                stringHash.put(stringHash.size(), text);
            }
        }
    }
    private int[][] entferneKnoten(int[][] graph, int knoten) {
        int[][] graphOhneKnoten = new int[graph.length - 1][graph.length - 1];
        int neueZeile = 0;
        int neueSpalte;
        for (int i = 0; i < graph.length; i++) {
            if (i != knoten) {
                neueSpalte = 0;
                for (int j = 0; j < graph.length; j++) {
                    if (j != knoten) {
                        graphOhneKnoten[neueZeile][neueSpalte] = graph[i][j];
                        neueSpalte++;
                    }
                }
                neueZeile++;
            }
        }
        return graphOhneKnoten;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                sb.append(graph[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public String toStringBerechnung() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringHash.size(); i++) {
            sb.append(stringHash.get(i)).append("\n");
        }
        return sb.toString();
    }
}






