# Projet-Informatique: Images & métadonnées avec Java


## Description brève

Ce projet met en scène l'utilisation de Java pour manipuler des images et leurs métadonnées. L'objectif principal est de développer une application capable de lire, d'analyser et de modifier les métadonnées des fichiers image, tout en offrant une interface utilisateur intuitive pour faciliter ces opérations.

## Compilation et execution

1) Prérequis :

Assurez-vous d'avoir Java JRE 21 installé sur votre machine.

2) Cloner le dépôt (si ce n'est pas déjà fait) :
```
git clone https://github.com/votre-utilisateur/ProjetMetadonneeL2.git
cd ProjetMetadonneeL2
```
3) Exécution de l'application :

Ouvrez un terminal ou une invite de commande.
Naviguez jusqu'au répertoire contenant le fichier .jar.
Exécutez la commande suivante :

```
java -jar ProjetMetadonneeL2.jar
```

## Structre de projet
```
/ProjetMetadonneeL2-master (3)
│
├── .idea/                  # Configuration de l'IDE
├── src/                    # Code source
│   ├── main/               # Classe principale et interface utilisateur
│   ├── core/               # Logique métier
│   │   ├── cli/            # Commandes en ligne
│   │   └── gui/            # Interface graphique
│   └── exception/          # Gestion des exceptions personnalisées
│
├── .lib/                   # Bibliothèques externes
│   ├── gson-2.11.0.jar
│   ├── xmpcore-6.1.11.jar
│   └── metadata-extractor-2.19.0.jar
│
├── allclasses-index.html   # Documentation générée
├── allpackages-index.html   # Documentation générée
└── README.md               # Ce fichier
```
## Documentation

La documentation complète du projet est générée à l'aide de Javadoc et se trouve dans les fichiers allclasses-index.html et allpackages-index.html. 
Vous pouvez ouvrir ces fichiers dans un navigateur pour explorer les classes et les méthodes disponibles.

## Contributions tiers et License

Ce projet utilise plusieurs bibliothèques tierces, notamment :
Gson : pour la manipulation de JSON.
XMPCore : pour la gestion des métadonnées.
Metadata Extractor : pour l'extraction des métadonnées des fichiers image.

Le projet est sous la licence MIT. Vous êtes libre de l'utiliser, de le modifier et de le distribuer, tant que vous mentionnez les auteurs originaux.

## Auteurs et remerciments

Nous avons donc été deux à travailler sur ce projet:
- Dylan MANSERI
- Alexis BERTRAND

Nous tenions tous de même à remercier nos professeur d'informatique, qui sans eux, nous n'aurions pas pus arriver oµ nous en sommes.

## Statut du projet

Notre équipe part du principe qu'un projet n'est jamais complètement fini, en effet, nous pensons qu'on peut toujours tout améliorer. Malgré tout, nous pensons tous deux aujourd'hui qu'il est préférable pour nous d'avancer et de partir sur un autre projet, qui pourrait nous permettre d'avancer encore plus dans le monde de la programmation.
