package com.rein.interface_web;

import com.rein.instance.Altruiste;
import com.rein.instance.Instance;
import com.rein.instance.Noeud;
import com.rein.instance.Paire;
import com.rein.io.InstanceReader;
import com.rein.io.exception.ReaderException;
import com.rein.solution.Chaine;
import com.rein.solution.Solution;
import com.rein.transplantation.Cycle;
import com.rein.transplantation.Sequence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class InterfaceWeb {

    private final Solution solution;
    private ArrayList<Integer> pairesNonUtilisees;
    private ArrayList<Integer> altruistesNonUtilises;
    private Integer nbNoeudsNonUtilises;
    private String beneficeChaqueSequence;
    private String html;
    private String INSTANCES_PATHNAME = "./instances";

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

    public String getHtmlBody() {
        return  "    <div style='margin: 0; width: 100%; border-top: 1px solid black; border-bottom: 1px solid black; display: grid; grid-template-columns: repeat(3, 1fr); grid-gap: 10px; background-color: #bbb'>" +
                "       <div>" +
                "           <p>Taille max chaînes : <b>" + this.solution.getInstance().getTailleMaxChaines() + "</b></p>" +
                "        </div>" +
                "       <div style='text-align: center;'>" +
                "           <p>Bénéfice médical total : <b>" + this.solution.getBenefMedicalTotal() + "</b></p>\n" +
                "       </div>" +
                "       <div style='text-align: right;'>" +
                "           <p>Taille max cycles : <b>" + this.solution.getInstance().getTailleMaxCycles() + "</b></p>" +
                "       </div>" +
                "    </div>" +
                "    <div id=\"mynetwork\"></div>\n" +
                "    <script type=\"text/javascript\">";
    }

    private ArrayList<String> getFilesNames() {
        ArrayList<String> res = new ArrayList<>();
        File folder = new File(INSTANCES_PATHNAME);
        if(folder.listFiles() != null)
            for (File file : folder.listFiles())
                if (!file.isDirectory())
                    res.add(file.getName());
        return res;
    }

    public String getHeadersOfHtml() {
        this.setAltruistesNonUtilises();
        this.setBeneficeChaqueSequence();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<meta charset='utf-8'>\n" +
                "  <head>\n" +
                "    <script src=\"https://visjs.github.io/vis-network/standalone/umd/vis-network.min.js\"></script>\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "     <h2 id='instanceName'>" + this.solution.getInstance().getNom() + "</h2>" +
                this.createDropdownInstances();
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
        String idsAltruistes = "", idsPaires = "";
        for(Integer id : this.altruistesNonUtilises)
            idsAltruistes += id + " ";
        this.setPairesNonUtilisees();
        for(Integer id : this.pairesNonUtilisees)
            idsPaires += id + " ";
        this.setNbNoeudsNonUtilises();
        float proportionPaireNonSollicitee = ((float) this.pairesNonUtilisees.size() / (float) this.nbNoeudsNonUtilises) * 100;
        float proportionDonneurNonSollicitee = ((float) this.altruistesNonUtilises.size() / (float) this.nbNoeudsNonUtilises) * 100;
        int pourcentageNoeudNonUtilise = (int) (((float) this.nbNoeudsNonUtilises/ (float) this.solution.getInstance().getTabNoeud().length) * 100);
        String options = "width:'100%', height:'500px',";
        options += "interaction:{navigationButtons:true, hover:true, hoverConnectedEdges:true}";
        return  "      var container = document.getElementById(\"mynetwork\");\n" +
                "      var data = {\n" +
                "        nodes: nodes,\n" +
                "        edges: edges,\n" +
                "      };\n" +
                "      var options = {" + options + "};\n" +
                "      var network = new vis.Network(container, data, options);\n" +
                "      network.setOptions(options);" +
                "</script>"+
                "    <hr>" +
                "    <div style='float: left; width: 50%;'>" +
                "       <p>Proportion de paire(s) et altruiste(s) non-sollicité(s) : <b>" + pourcentageNoeudNonUtilise + "%</b></p>\n" +
                "       <canvas id='chartNoeuds' style='width: 10vh'></canvas>\n " +
                "    </div>" +
                "    <div style='float: right; width: 50%; text-align: right;'>" +
                "       <p>Bénéfice de chaque séquence : </p>" + this.beneficeChaqueSequence +
                "   </div>" +
                "   <script>" +
                "       const labels = [\n" +
                "           'Type de cas'\n" +
                "       ];\n" +
                "       const dataNoeuds = {\n" +
                "           labels: labels,\n" +
                "           datasets: [{\n" +
                "               label: 'Pourcentage altruistes non-sollicités',\n" +
                "               backgroundColor: 'rgb(241, 177, 18)',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               data: ["+proportionDonneurNonSollicitee+"]\n" +
                "           }, {" +
                "               label: 'Pourcentage paires non-sollicitées',\n" +
                "               backgroundColor: 'rgb(18, 214, 241)',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               data: ["+proportionPaireNonSollicitee+"]\n" +
                "           }]\n" +
                "       };\n" +
                "       const config = {\n" +
                "           type: 'bar',\n" +
                "           data: dataNoeuds,\n" +
                "           options: {}\n" +
                "       };" +
                "       var ctx = document.getElementById('chartNoeuds').getContext('2d');" +
                "       const chartNoeuds = new Chart(ctx, config);" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";
    }

    public void setHtmlCode() {
        this.html = this.getHeadersOfHtml() + this.getHtmlBody() + this.getNodes() + this.getEdges() + this.getEndOfHtml();
    }

    public String createDropdownInstances() {
        String options = "<select id='select' onchange='location = this.value;'>";
        options += "<option>Nom de l'instance</option>";
        for(String path : getFilesNames())
            options += "<option value='" + path + ".html'>" + path + "</option>";
        options += "</select>";
        return options;
    }

    public void createHtmlFile() throws IOException {
        this.setHtmlCode();
        String pathname = "./results/" + this.solution.getInstance().getNom() + ".html";
        File result = new File(pathname);
        result.createNewFile();
        FileWriter myWriter = new FileWriter(pathname);
        myWriter.write(this.html);
        myWriter.close();
    }
}
