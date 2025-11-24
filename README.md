# Sales Revenue per Product – Hadoop MapReduce Project

## Description

Ce projet implémente un job **Hadoop MapReduce en Java** pour calculer le chiffre d'affaires total par produit à partir d'un fichier CSV de ventes.
Le projet inclut les étapes suivantes :

* Chargement des fichiers dans **HDFS** (/input/ventes)
* Traitement avec **MapReduce** :

  * **Mapper** : calcule le revenu par ligne (`price * quantity`)
  * **Reducer** : somme le revenu par produit
* Export des résultats en CSV pour visualisation

## Technologies

* Java 8+
* Hadoop 3.x
* HDFS
* MapReduce

## Structure du projet

```
Hadoop-java/
├── WordCount_Basique/         # Exemple WordCount
├── sales-revenue.jar          # Jar compilé du projet
├── SalesRevenuePerProduct.java # Code source Java
├── sales.csv                  # Fichier de données de ventes
└── README.md                  # Documentation du projet
```

## Instructions pour exécuter

### 1. Copier le fichier CSV dans HDFS

```bash
hdfs dfs -mkdir -p /input/ventes
hdfs dfs -put sales.csv /input/ventes/
```

### 2. Compiler le code Java

```bash
javac -cp $(hadoop classpath) SalesRevenuePerProduct.java
```

### 3. Créer le JAR

```bash
jar -cvf sales-revenue.jar SalesRevenuePerProduct*.class
```

### 4. Lancer le job Hadoop

```bash
hadoop jar sales-revenue.jar SalesRevenuePerProduct /input/ventes/sales.csv /output/revenue
```

### 5. Récupérer les résultats

```bash
hdfs dfs -cat /output/revenue/part-r-00000
```

### 6. Copier les résultats sur ton ordinateur local (optionnel)

```bash
docker cp namenode:/tmp/revenue.csv ./revenue.csv
```

## Résultats attendus

```
P100    899.97
P200    249.95
P300    49.95
```

## Visualisation

Les résultats peuvent être importés dans **Excel, Python (pandas/matplotlib) ou Power BI** pour créer des graphiques.

## Remarques

* Les lignes d'en-tête du CSV sont ignorées dans le mapper.
* Les valeurs `price` et `quantity` sont converties en `double` et `int` respectivement.
* Le projet peut être étendu pour inclure d'autres métriques comme chiffre d'affaires par catégorie ou par date.

