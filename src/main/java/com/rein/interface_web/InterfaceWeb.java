package com.rein.interface_web;

import com.rein.instance.Altruiste;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;
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
    private Integer nbNoeudsNonUtilises;
    private String beneficeChaqueSequence;
    private String html;

    public InterfaceWeb(Solution solution) {
        this.solution = solution;
        this.pairesNonUtilisees = new ArrayList<>();
        this.altruistesNonUtilises = new ArrayList<>();
        this.nbNoeudsNonUtilises = 0;
        this.beneficeChaqueSequence = "";
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

    public void setPairesNonUtilisees() {
        ArrayList<Integer> allPaires = new ArrayList<>();
        ArrayList<Integer> solutionPaires = new ArrayList<>();
        // Itère sur les noeuds de l'instance
        for(Noeud noeud : this.solution.getInstance().getTabNoeud())
            if(noeud instanceof Paire)
                allPaires.add(noeud.getId());
        // Itère sur les noeuds de la solution
        for(Sequence sequence : this.solution.getListeSequences())
            for(Noeud n : sequence.getListeNoeuds())
                if(n instanceof Paire)
                    solutionPaires.add(n.getId());
        // si noeud de allPaires n'est pas dans solutionPaires
        for(Integer id : allPaires)
            if(!solutionPaires.contains(id))
                this.pairesNonUtilisees.add(id);
    }

    public void setNbNoeudsNonUtilises() {
        this.nbNoeudsNonUtilises = this.altruistesNonUtilises.size() + this.pairesNonUtilisees.size();
    }

    public void setBeneficeChaqueSequence() {
        String res = "<ul style=\"list-style-type:none;\">";
        for(Sequence sequence : this.solution.getListeSequences()){
            res += "<li>";
            for(Noeud n : sequence.getListeNoeuds()){
                res += n.getId() + "->";
            }
            res = res.substring(0, res.length()-2);
            res += " : <b>" + sequence.getBenefMedicalSequence() + "</b></li>";
        }
        res += "</ul>";
        this.beneficeChaqueSequence = res;
    }

    public String getBeginningOfHtml() {
        this.setAltruistesNonUtilises();
        this.setBeneficeChaqueSequence();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<meta charset='utf-8'>\n" +
                "  <head>\n" +
                "    <script src=\"https://d3js.org/d3.v6.min.js\"></script>\n" +
                "    <script src=\"https://visjs.github.io/vis-network/standalone/umd/vis-network.min.js\"></script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h2>" + this.solution.getInstance().getNom() + "</h2>\n" +
                "    <div style='margin: 0; width: 100%; border-top: 1px solid black; border-bottom: 1px solid black; display: grid; grid-template-columns: repeat(3, 1fr); grid-gap: 10px; background-color: #bbb'>" +
                "       <div>" +
                "           <p>Taille max chaînes : <b>" + this.solution.getInstance().getTailleMaxChaines() + "</b></p>" +
                "        </div>" +
                "       <div style='text-align: center;'>" +
                "           <p>Bénéfice médical total : <b>" + this.solution.getBenefMedicalTotal() + "</b></p>\n" +
                "       </div>" +
                "       <div style='text-align: right;'>" +
                "           <p>Taille max cycles : <b>" + this.solution.getInstance().getTailleMaxCycles() + "</b></p>" +
                "       </div>" +
                "    </div>";
    }

    //@type
        //0 = altruiste
        //1 = paire
    public String getNodes(int type) {
        String nodes = "var nodes = new vis.DataSet([";
        for (Sequence sequence : this.solution.getListeSequences()) {
            boolean isAltruiste = false;
            for (Noeud noeud : sequence.getListeNoeuds()) {
                int idNoeud = noeud.getId();
                String group = "paire";
                if (sequence instanceof Chaine && !isAltruiste) {
                    group = "altruiste";
                    isAltruiste = true;
                }
                if ((type==0 && sequence instanceof Chaine) || (type==1 && sequence instanceof Cycle))
                    nodes += "{ id: " + idNoeud + ", label: \"" + idNoeud + "\", group: \"" + group + "\" },";
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

    public String getEndOfJs(String type) {
        this.setNbNoeudsNonUtilises();
        String options;
        if (type == "Chaines") {
            options = "width:'100%', height:'200px',";
        }
        else {
            options = "width:'100%', height:'500px',";
        }
        options += "interaction:{navigationButtons:true, hover:true, hoverConnectedEdges:true}, physics:{" +
                "enabled: true, repulsion: { centralGravity: 0.2, nodeDistance: 4 }, hierarchicalRepulsion: { centralGravity: 0.0, nodeDistance: 4, avoidOverlap: 0 }}" +
                ", groups: { altruiste: {color:{background:'#97C2FC'}, shape: 'box'}, paire: {color:{background:'orange'}, shape : 'circle'}}";
        if (type == "Chaines")
            options += ", layout: { hierarchical: { direction: 'UD', levelSeparation: 45, nodeSpacing: 10, treeSpacing: 45 }}";

        return  "var container = document.getElementById(\"" + type + "\");\n" +
                "      var data = {\n" +
                "        nodes: nodes,\n" +
                "        edges: edges,\n" +
                "      };\n" +
                "      var options = {"+options+"};\n" +
                "      var network = new vis.Network(container, data, options);\n" +
                "      network.setOptions(options);" +
                "    </script>\n";
    }

    public String getEndOfHtml()
    {
        String idsAltruistes = "", idsPaires = "";
        for(Integer id : this.altruistesNonUtilises)
            idsAltruistes += id + " ";
        this.setPairesNonUtilisees();
        for(Integer id : this.pairesNonUtilisees)
            idsPaires += id + " ";
        return "    <hr>" +
                "    <div style='float: left; width: 50%;'>" +
                "       <p>Nombre de noeud(s) non-utilisé(s) : <b>" + this.nbNoeudsNonUtilises + "</b></p>\n" +
                "       <p>Altruiste(s) non-utilisé(s) : <b>[ " + idsAltruistes + "]</b></p>\n" +
                "       <p>Paire(s) non-utilisée(s) : <b>[ " + idsPaires + "]</b></p>\n" +
                "    </div>" +
                "    <div style='float: right; width: 50%; text-align: right;'>" +
                "       <p>Bénéfice de chaque séquence : </p>" +
                this.beneficeChaqueSequence +
                "   </div>" +
                "  </body>\n" +
                "</html>";
    }

    public void setHtmlCode() {
        this.html = this.getBeginningOfHtml() +
                this.getBeginningOfJs("Chaines") + this.getNodes(0) + this.getEdges() + this.getEndOfJs("Chaines") +
                this.getBeginningOfJs("Cycles") + this.getNodes(1) + this.getEdges() + this.getEndOfJs("Cycles") +
                this.getEndOfHtml();
    }

    private String getBeginningOfJs(String type) {
        return "    <div id=\"" + type +"\"></div>" +
                "    <script type=\"text/javascript\">\n";
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
