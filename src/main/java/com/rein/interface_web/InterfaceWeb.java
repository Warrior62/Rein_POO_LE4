package com.rein.interface_web;

import com.rein.instance.Altruiste;
import com.rein.instance.Noeud;
import com.rein.solution.Chaine;
import com.rein.solution.Solution;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class InterfaceWeb {

    private final Solution solution;
    private ArrayList<Integer> pairesNonUtilisees;
    private ArrayList<Integer> altruistesNonUtilises;
    private String html;

    public InterfaceWeb(Solution solution) {
        this.solution = solution;
        this.pairesNonUtilisees = new ArrayList<>();
        this.altruistesNonUtilises = new ArrayList<>();
        this.html = "";
    }

    public void setAltruistesNonUtilises() {
        ArrayList<Integer> allAltruistes = new ArrayList<>();
        ArrayList<Integer> solutionAltruistes = new ArrayList<>();
        // Itère sur les noeuds de l'instance
        for(Noeud noeud : this.solution.getInstance().getTabNoeud())
            if(noeud instanceof Altruiste)
                allAltruistes.add(noeud.getId());
        // Itère sur les noeuds de la solution
        for(Sequence sequence : this.solution.getListeSequences())
            for(Noeud n : sequence.getListeNoeuds())
                if(n instanceof Altruiste)
                    solutionAltruistes.add(n.getId());
        // si noeud de allAltruistes n'est pas dans solutionAltruistes
        for(Integer id : allAltruistes)
            if(!solutionAltruistes.contains(id))
                this.altruistesNonUtilises.add(id);
    }

    public String getBeginningOfHtml() {
        String idsAltruistes = "";
        this.setAltruistesNonUtilises();
        for(Integer id : this.altruistesNonUtilises)
            idsAltruistes += id + " ";
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <script src=\"https://d3js.org/d3.v6.min.js\"></script>\n" +
                "    <script src=\"https://visjs.github.io/vis-network/standalone/umd/vis-network.min.js\"></script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h2>" + this.solution.getInstance().getNom() + "</h2>\n" +
                "    <p>Bénéfice médical total : " + this.solution.getBenefMedicalTotal() + "</p>\n" +
                "    <hr>" +
                "    <p>Paire(s) non-utilisée(s) : </p>\n" +
                "    <p>Altruiste(s) non-utilisé(s) : [ " + idsAltruistes + "]</p>\n" +
                "    <p>Bénéfice de chaque séquence : </p>\n" +
                "    <hr>" +
                "    <div id=\"mynetwork\"></div>\n" +
                "    <script type=\"text/javascript\">";
    }

    public String getNodes() {
        String nodes = "var nodes = new vis.DataSet([";
        for (Sequence sequence : this.solution.getListeSequences()) {
            boolean isAltruiste = false;
            for (Noeud noeud : sequence.getListeNoeuds()) {
                int idNoeud = noeud.getId();
                String shape = "circle";
                String color = "orange";
                if (sequence instanceof Chaine && !isAltruiste) {
                    shape = "box";
                    color = "#97C2FC";
                    isAltruiste = true;
                }
                nodes += "{ id: " + idNoeud + ", label: \"" + idNoeud + "\", shape: \"" + shape + "\", color: \"" + color + "\" },";
            }
        }
        nodes = nodes.substring(0, nodes.length() - 1);
        nodes += "]);";

        return nodes;
    }

    public String getEdges() {
        String edges = "var edges = [";
        for (Sequence sequence : this.solution.getListeSequences()) {
            for (int i = 0; i < sequence.getListeNoeuds().size(); i++) {
                // if it's not the last node of the sequence
                if (i + 1 < sequence.getListeNoeuds().size()) {
                    Noeud donneur = sequence.getListeNoeuds().get(i);
                    Noeud receveur = sequence.getListeNoeuds().get(i + 1);
                    int benefMedical = -1;
                    // CHAINE
                    if (sequence instanceof Chaine) {
                        System.out.println("chaine");
                        for (Map.Entry<Noeud, Integer> entry : sequence.getListeNoeuds().get(i).getListeEchanges().entrySet())
                            if (entry.getKey().getId() == receveur.getId())
                                benefMedical = entry.getValue();
                        edges += "{ from: " + donneur.getId() + ", to: " + receveur.getId() + ", label: \"" + benefMedical + "\", color: { color: \"black\" }, font: { align: \"top\" }, arrows: \"to\" },";
                    }
                    // CYCLE
                    if (sequence instanceof Cycle) {
                        System.out.println("cycle");
                        for (Map.Entry<Noeud, Integer> entry : donneur.getListeEchanges().entrySet())
                            if (entry.getKey().getId() == receveur.getId())
                                benefMedical = entry.getValue();
                        edges += "{ from: " + donneur.getId() + ", to: " + receveur.getId() + ", label: \"" + benefMedical + "\", color: { color: \"black\" }, font: { align: \"top\" }, arrows: \"to\" },";
                    }
                } else {
                    // CYCLE
                    if (sequence instanceof Cycle) {
                        System.out.println("cycle");
                        int benefMedical = -1;
                        Noeud donneur = sequence.getListeNoeuds().get(i);
                        Noeud receveur = sequence.getListeNoeuds().get(0);
                        for (Map.Entry<Noeud, Integer> entry : donneur.getListeEchanges().entrySet()) {
                            if (entry.getKey().getId() == receveur.getId()) {
                                benefMedical = entry.getValue();
                                edges += "{ from: " + donneur.getId() + ", to: " + receveur.getId() + ", label: \"" + benefMedical + "\", color: { color: \"black\" }, font: { align: \"top\" }, arrows: \"to\" },";
                            }
                        }
                    }
                }
            }
        }
        edges = edges.substring(0, edges.length() - 1);
        edges += "];";

        return edges;
    }

    public String getEndOfHtml() {
        String options = "width:'1200px', height:'500px',";
        options += "interaction:{navigationButtons:true, hover:true, hoverConnectedEdges:true},";
        return "var container = document.getElementById(\"mynetwork\");\n" +
                "      var data = {\n" +
                "        nodes: nodes,\n" +
                "        edges: edges,\n" +
                "      };\n" +
                "      var options = {"+options+"};\n" +
                "      var network = new vis.Network(container, data, options);\n" +
                "      network.setOptions(options);" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";
    }

    public void setHtmlCode() {
        this.html = this.getBeginningOfHtml() + this.getNodes() + this.getEdges() + this.getEndOfHtml();
    }

    public void createHtmlFile() throws IOException {
        this.setHtmlCode();
        String pathname = "./results.html";
        File result = new File(pathname);
        result.createNewFile();
        FileWriter myWriter = new FileWriter(pathname);
        myWriter.write(this.html);
        myWriter.close();
    }
}
