# Rein_POO_LE4
## Projet optimisation échange de reins LE4 IG2i 

Les fichiers instances doivent être placés dans le dossier instancesInitiales à la racinne.

Pour créer le  fichier de  solution associé à une instance, il faut taper la commande :
    java -jar Projet_Fremaux_Robert_Shahpazian_Tryla.jar -inst {{NOM INSTANCE}}  -dSol {{NOM DOSSIER SOLUTIONS}}

Pour tester la validité de tous les fichiers de solution présents dans le répertoire, il faut taper la commande :
    `java -jar CheckerKEP.jar -all`


Trouble shooting :
    Projet has been compiled by a more recent version of the Java Runtime (class file version X.X), this version of the Java Runtime only recognizes class file versions up to X.X
        Mettre à jour la variable d'environnement PATH et y ajouter le chemin vers le JDK souhaité (ici le JDK 14 est utilisé) 