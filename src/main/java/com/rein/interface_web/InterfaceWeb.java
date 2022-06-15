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
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mathis Tryla
 * @author Martin Fremaux
 */
public class InterfaceWeb {

    private final Solution solution;
    private final ArrayList<Integer> pairesNonUtilisees;
    private final ArrayList<Integer> altruistesNonUtilises;
    private Integer nbNoeudsNonUtilises;
    private String beneficeChaqueSequence;
    private String html;

    /**
     * Constructeur par données d'une solution
     * @param solution dont on veut récupérer les données
     */
    public InterfaceWeb(Solution solution) {
        this.solution = solution;
        this.pairesNonUtilisees = new ArrayList<>();
        this.altruistesNonUtilises = new ArrayList<>();
        this.nbNoeudsNonUtilises = 0;
        this.beneficeChaqueSequence = "";
        this.html = "";
    }

    /**
     * Définit tous les altruistes non-sollicités
     * dans l'attribut altruistesNonUtilises
     */
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

    /**
     * Définit toutes les paires non-sollicitées
     * dans l'attribut pairesNonUtilisees
     */
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

    /**
     * Définit le nombre total d'altruistes et de paires non-sollicités
     * dans l'attribut nbNoeudsNonUtilises
     */
    public void setNbNoeudsNonUtilises() {
        this.nbNoeudsNonUtilises = this.altruistesNonUtilises.size() + this.pairesNonUtilisees.size();
    }

    /**
     * Définit le bénéfice de chaque séquence de la solution
     * dans une liste non-ordonnée HTML
     */
    public void setBeneficeChaqueSequence() {
        String res = "<ul style=\"list-style-type:none;\">";
        for(Sequence sequence : this.solution.getListeSequences()){
            res += "<li class=\"sequence\" data-toggle=\"tooltip\" data-html=\"true\" title=\"Bénefice médical associé : &#10;" + sequence.getBenefMedicalSequence() + "\">";
            for(Noeud n : sequence.getListeNoeuds()){
                res += n.getId() + "->";
            }
            res = res.substring(0, res.length()-2);
        }
        res += "</ul>";
        this.beneficeChaqueSequence = res;
    }

    /**
     * Enregistre les informations principales de la solution
     * dans une variable sous format HTML
     * @return une chaîne de caractères comportant le code HTML
     */
    public String getHtmlBody() {
        return  "    <div class='btn btn-dark w-100 border mt-2' style='display: grid; grid-template-columns: repeat(3, 1fr);'>" +
                "           <div>Taille max chaînes : <b>" + this.solution.getInstance().getTailleMaxChaines() + "</b></div>" +
                "           <div>Bénéfice médical total : <b>" + this.solution.getBenefMedicalTotal() + "</b></div>\n" +
                "           <div>Taille max cycles : <b>" + this.solution.getInstance().getTailleMaxCycles() + "</b></div>" +
                "    </div>" +
                "    <div id=\"mynetwork\"></div>\n" +
                "    <div class=\"row\">" +
                "       <div class=\"vis\" style=\"width: 80%\">";
    }

    /**
     * Récupére le nom de tous les fichiers d'instance
     * @return une liste comportant les noms des instances
     */
    private ArrayList<String> getFilesNames() {
        ArrayList<String> res = new ArrayList<>();
        String INSTANCES_PATHNAME = "./instances";
        File folder = new File(INSTANCES_PATHNAME);
        if(folder.listFiles() != null)
            for (File file : Objects.requireNonNull(folder.listFiles()))
                if (!file.isDirectory())
                    res.add(file.getName());
        return res;
    }

    /**
     * Définit les headers HTML avec
     * les librairies JS de graphiques et vue des séquences.
     * Aussi, initialise le sélecteur d'instances dont
     * il faut afficher la vue.
     * @return une chaîne de caractères comportant le code HTML
     */
    public String getHeadersOfHtml() {
        this.setAltruistesNonUtilises();
        this.setBeneficeChaqueSequence();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<meta charset='utf-8'>\n" +
                "   <head>\n" +
                "       <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css\">\n" +
                "       <script src=\"https://visjs.github.io/vis-network/standalone/umd/vis-network.min.js\"></script>\n" +
                "       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>" +
                "       <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2\" crossorigin=\"anonymous\"></script>\n" +
                "       <script>\n" +
                "           $(document).ready(function(){\n" +
                "               $('[data-toggle=\"tooltip\"]').tooltip();   \n" +
                "        });" +
                "       </script>\n" +
                "       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n" +
                "       <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "       <style>\n" +
                "           .tooltip.show{\n" +
                "               opacity: 1;\n" +
                "               white-space: pre-line;\n" +
                "           }\n" +
                "           .sequence{\n" +
                "               width: fit-content;\n" +
                "               margin-left: auto;\n" +
                "               margin-right: 0;\n" +
                "           }\n" +
                "       </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "     <h2 id='instanceName'>" + this.solution.getInstance().getNom() + "</h2>" +
                this.createDropdownInstances();
    }

    /**
     * Récupére l'ensemble des noeuds de la solution
     * à afficher
     * @param type 0 si chaine, 1 si cycle
     * @return une chaîne de caractères comportant le code JS
     */
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
        nodes += "]);";

        return nodes;
    }

    /**
     * Récupére l'ensemble des échanges de la solution
     * à afficher
     * @return une chaîne de caractères comportant le code JS
     */
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
                        for (Map.Entry<Noeud, Integer> entry : sequence.getListeNoeuds().get(i).getListeEchanges().entrySet())
                            if (entry.getKey().getId() == receveur.getId())
                                benefMedical = entry.getValue();
                        edges += "{ from: " + donneur.getId() + ", to: " + receveur.getId() + ", label: \"" + benefMedical + "\", color: { color: \"black\" }, font: { align: \"top\" }, arrows: \"to\" },";
                    }
                    // CYCLE
                    if (sequence instanceof Cycle) {
                        for (Map.Entry<Noeud, Integer> entry : donneur.getListeEchanges().entrySet())
                            if (entry.getKey().getId() == receveur.getId())
                                benefMedical = entry.getValue();
                        edges += "{ from: " + donneur.getId() + ", to: " + receveur.getId() + ", label: \"" + benefMedical + "\", color: { color: \"black\" }, font: { align: \"top\" }, arrows: \"to\" },";
                    }
                } else {
                    // CYCLE
                    if (sequence instanceof Cycle) {
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

    /**
     * Récupére l'affichage des grpahiques et des vues
     * @param type chaine ou cycle
     * @return chaîne comportant le code JS
     */
    public String getEndOfJs(String type){
        String options;
        if (type == "Chaines") {
            options = "width:'100%', height:'200px',";
        } else {
            options = "width:'100%', height:'500px',";
        }
        options += "interaction:{navigationButtons:true, hover:true, hoverConnectedEdges:true}, " +
                "groups: { altruiste: {color:{background:'#97C2FC'}, shape: 'box'}, paire: {color:{background:'orange'}, shape : 'circle'}},";
        if (type == "Chaines")
            options += "layout: { hierarchical: { direction: 'UD', levelSeparation: 45, nodeSpacing: 10, treeSpacing: 45 }}" +
                    ", physics : {enabled: true, repulsion: { centralGravity: 0.2, nodeDistance: 4 }, hierarchicalRepulsion: { centralGravity: 0.0, nodeDistance: 4, avoidOverlap: 0 }}";
        else if (type == "Cycles")
            options += " physics: {\n" +
                    "       enabled: true, " +
                    "       repulsion: { centralGravity: 0.2, nodeDistance: 4 }," +
                    "       hierarchicalRepulsion: { centralGravity: 0, nodeDistance: 4, avoidOverlap: 0 }," +
                    "       barnesHut: { \"gravitationalConstant\": -80, \"centralGravity\": 0 }," +
                    "        minVelocity: 1" +
                    "    }";

        return  "      var container = document.getElementById(\"" + type + "\");\n" +
                "      var data = {\n" +
                "        nodes: nodes,\n" +
                "        edges: edges,\n" +
                "      };\n" +
                "      var options = {" + options + "};\n" +
                "      var network = new vis.Network(container, data, options);\n" +
                "      network.setOptions(options);" +
                "</script>" +
                "    <hr>";
    }

    /**
     * Récupére les statistiques de la solution
     * à afficher sous forme de graphique
     * @return une chaîne de caractères comportant le code JS
     */
    public String getEndOfHtml()
    {
        String idsAltruistes = "", idsPaires = "";
        for(Integer id : this.altruistesNonUtilises)
            idsAltruistes += id + " ";
        this.setPairesNonUtilisees();
        for(Integer id : this.pairesNonUtilisees)
            idsPaires += id + " ";
        this.setNbNoeudsNonUtilises();
        float proportionPaireNonSollicitee = ((float) this.pairesNonUtilisees.size() / (float) this.solution.getInstance().getNbPaires()) * 100;
        float proportionDonneurNonSollicitee = ((float) this.altruistesNonUtilises.size() / (float) this.solution.getInstance().getNbAltruistes()) * 100;
        int pourcentageNoeudNonUtilise = (int) (((float) this.nbNoeudsNonUtilises/ (float) this.solution.getInstance().getTabNoeud().length) * 100);
        proportionPaireNonSollicitee = Math.round(proportionPaireNonSollicitee);
        proportionDonneurNonSollicitee = Math.round(proportionDonneurNonSollicitee);
        pourcentageNoeudNonUtilise = Math.round(pourcentageNoeudNonUtilise);
        return "</div> " +
                "    <div class=\"sticky-top btn btn-dark mt-1 mb-auto mx-auto\" style='float: right; width: fit-content; text-align: right; margin-right: 2em; top: 1rem;'>" +
                "       <p>Bénéfice de chaque séquence : </p>" + this.beneficeChaqueSequence +
                "   </div></div>" +
                "   <hr>" +
                "   <p>Proportion de paire(s) et altruiste(s) non-sollicité(s) : <b>" + pourcentageNoeudNonUtilise + "%</b></p>\n" +
                "   <div class=\"w-100 mx-auto row\">" +
                "       <div class=\"col-6 my-auto\">" +
                "           <canvas class='my-auto' id='chartBar'></canvas>\n" +
                "       </div>" +
                "       <div class=\"col-3\">" +
                "           <canvas id='chartPie1'></canvas>\n " +
                "       </div>" +
                "       <div class=\"col-3\">" +
                "           <canvas id='chartPie2'></canvas>\n " +
                "       </div>" +
                "   </div>" +
                "   <script>" +
                "       const labels = [\n" +
                "           'Type de cas'\n" +
                "       ];\n" +
                "       const dataNoeudsBar = {\n" +
                "           labels: labels,\n" +
                "           datasets: [{\n" +
                "               label: 'Altruistes sollicitées',\n" +
                "               backgroundColor: 'rgb(18, 214, 241)',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               yAxisID: 'A',\n" +
                "               data: ["+ (this.solution.getInstance().getNbAltruistes() - this.altruistesNonUtilises.size()) +"]\n" +
                "           }, {" +
                "               label: 'Altruistes non-sollicités',\n" +
                "               backgroundColor: '#0bacc1',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               yAxisID: 'A',\n" +
                "               data: ["+this.altruistesNonUtilises.size()+"]\n" +
                "           }, {" +
                "               label: 'Paires sollicitées',\n" +
                "               backgroundColor: 'orange',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               yAxisID: 'B',\n" +
                "               data: ["+ (this.solution.getInstance().getNbPaires() - this.pairesNonUtilisees.size()) +"]\n" +
                "           }, {" +
                "               label: 'Paires non-sollicitées',\n" +
                "               backgroundColor: '#cc8500',\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               yAxisID: 'B',\n" +
                "               data: ["+this.pairesNonUtilisees.size()+"]\n" +
                "           }]\n" +
                "       };\n" +
                "       const dataPie1 = {\n" +
                "           labels: ['Altruistes sollicitées', 'Altruistes non-sollicités'],\n" +
                "           datasets: [{\n" +
                "               backgroundColor: ['#12d6f1', '#0bacc1'],\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               data: ["+ (100 - proportionDonneurNonSollicitee) +", " + proportionDonneurNonSollicitee + "]\n" +
                "           }]\n" +
                "       };\n" +
                "       const dataPie2 = {\n" +
                "           labels: ['Paires sollicitées', 'Paires non-sollicités'],\n" +
                "           datasets: [{\n" +
                "               backgroundColor: ['orange', '#cc8500'],\n" +
                "               borderColor: 'rgb(100, 100, 100)',\n" +
                "               data: ["+ (100 - proportionPaireNonSollicitee) +", " + proportionPaireNonSollicitee + "]\n" +
                "           }]\n" +
                "       };\n" +
                "       const configBar = {\n" +
                "           type: 'bar',\n" +
                "           data: dataNoeudsBar,\n" +
                "           options: {" +
                "               scales: {\n" +
                "                   A: {\n" +
                "                       type: 'linear',\n" +
                "                       position: 'left',\n" +
                "                   },\n" +
                "                    B: {\n" +
                "                       type: 'linear',\n" +
                "                       position: 'right',\n" +
                "                   }\n" +
                "               }" +
                "           }\n" +
                "       };" +
                "       const configPie1 = {\n" +
                "           type: 'pie',\n" +
                "           data: dataPie1,\n" +
                "       };" +
                "       const configPie2 = {\n" +
                "           type: 'pie',\n" +
                "           data: dataPie2,\n" +
                "       };" +
                "       var ctx = document.getElementById('chartBar').getContext('2d');" +
                "       const chartBar = new Chart(ctx, configBar);" +
                "       var ctx = document.getElementById('chartPie1').getContext('2d');" +
                "       const chartPie1 = new Chart(ctx, configPie1);" +
                "       var ctx = document.getElementById('chartPie2').getContext('2d');" +
                "       const chartPie2 = new Chart(ctx, configPie2);" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";
    }

    /**
     * Définit l'ensemble du code HTML de l'interface graphique
     */
    public void setHtmlCode(){
        this.html = this.getHeadersOfHtml() + this.getHtmlBody();
        //Affichage des chaines si il y en a
        if (this.solution.hasSequenceOfClass(Chaine.class)){
            this.html += this.getBeginningOfJs("Chaines") + this.getNodes(0) + this.getEdges() + this.getEndOfJs("Chaines");
        }
        //Affichage des cycles si il y en a
        if (this.solution.hasSequenceOfClass(Cycle.class)) {
            this.html += this.getBeginningOfJs("Cycles") + this.getNodes(1) + this.getEdges() + this.getEndOfJs("Cycles");
        }
        this.html += this.getEndOfHtml();
    }

    /**
     * Récupére les div cycles et chaînes à afficher
     * @param type chaîne ou cycle
     * @return la chaîne comportant le code HTML
     */
    private String getBeginningOfJs(String type) {
        return "    <div style=\"width: 100%\" id=\"" + type +"\"></div>" +
               "    <script type=\"text/javascript\">\n";
    }

    /**
     * Crée le sélecteur d'instances dont
     * on souhaite afficher les informations
     * @return une chaîne de caractères comportant le code HTML
     */
    public String createDropdownInstances() {
        String options = "<select id='select' onchange='location = this.value;'>";
        options += "<option>Nom de l'instance</option>";
        for(String path : getFilesNames())
            options += "<option value='" + path + ".html'>" + path + "</option>";
        options += "</select>";
        return options;
    }

    /**
     * Crée le fichier HTML de la solution à
     * partir de l'attribut html
     * @throws IOException
     */
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
