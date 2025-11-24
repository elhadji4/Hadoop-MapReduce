# Hadoop-MapReduce

Développement d'un job Hadoop MapReduce en Java pour calculer le revenu total par produit à partir d'un fichier de ventes CSV. Implémentation des étapes : ingestion des données dans HDFS, MapReduce (mapper + reducer), génération des résultats et export CSV pour visualisation.

## Description

Ce projet implémente un job Hadoop MapReduce en Java qui :
1. Lit un fichier CSV de données de ventes
2. Calcule le revenu total pour chaque produit (quantité × prix unitaire)
3. Agrège les revenus par produit
4. Exporte les résultats en format CSV pour la visualisation

## Prérequis

- Java JDK 8 ou supérieur
- Apache Maven 3.x
- Apache Hadoop 3.x (configuré et en cours d'exécution)
- HDFS configuré et accessible

## Structure du Projet

```
Hadoop-MapReduce/
├── pom.xml                           # Configuration Maven
├── data/
│   └── sales.csv                     # Fichier de données exemple
├── scripts/
│   ├── ingest_data.sh                # Script d'ingestion des données dans HDFS
│   ├── run_job.sh                    # Script d'exécution du job MapReduce
│   └── export_results.sh             # Script d'export des résultats en CSV
└── src/main/java/com/sales/mapreduce/
    ├── SalesMapper.java              # Classe Mapper
    ├── SalesReducer.java             # Classe Reducer
    └── SalesDriver.java              # Classe Driver (point d'entrée)
```

## Format du Fichier CSV d'Entrée

Le fichier CSV doit avoir le format suivant :
```
product_id,product_name,quantity,unit_price
1,Laptop,5,999.99
2,Mouse,50,25.50
```

- `product_id` : Identifiant du produit
- `product_name` : Nom du produit
- `quantity` : Quantité vendue
- `unit_price` : Prix unitaire

## Installation et Compilation

```bash
# Cloner le dépôt
git clone https://github.com/elhadji4/Hadoop-MapReduce.git
cd Hadoop-MapReduce

# Compiler le projet
mvn clean package
```

## Utilisation

### Étape 1 : Ingestion des Données dans HDFS

```bash
# Utiliser le script fourni
./scripts/ingest_data.sh data/sales.csv /user/hadoop/sales/input

# Ou manuellement
hdfs dfs -mkdir -p /user/hadoop/sales/input
hdfs dfs -put data/sales.csv /user/hadoop/sales/input/
```

### Étape 2 : Exécution du Job MapReduce

```bash
# Utiliser le script fourni
./scripts/run_job.sh /user/hadoop/sales/input /user/hadoop/sales/output

# Ou manuellement
hadoop jar target/sales-mapreduce-1.0-SNAPSHOT.jar \
    com.sales.mapreduce.SalesDriver \
    /user/hadoop/sales/input \
    /user/hadoop/sales/output
```

### Étape 3 : Export des Résultats en CSV

```bash
# Utiliser le script fourni
./scripts/export_results.sh /user/hadoop/sales/output output/revenue_by_product.csv

# Ou manuellement
hdfs dfs -cat /user/hadoop/sales/output/part-r-* | sed 's/\t/,/g' > revenue_by_product.csv
```

## Exemple de Résultats

Fichier de sortie `revenue_by_product.csv` :
```csv
product_name,total_revenue
External HDD,1199.85
Headphones,5849.35
Keyboard,3375.0
Laptop,9999.9
Monitor,4499.85
Mouse,1912.5
Printer,2399.88
Scanner,2249.91
USB Cable,1948.5
Webcam,4499.7
```

## Architecture MapReduce

### SalesMapper
- **Entrée** : Lignes du fichier CSV (format texte)
- **Sortie** : Paires (produit, revenu)
- **Logique** : Parse chaque ligne CSV, calcule le revenu (quantité × prix), émet une paire clé-valeur

### SalesReducer
- **Entrée** : Paires (produit, [revenus...])
- **Sortie** : Paires (produit, revenu_total)
- **Logique** : Somme tous les revenus pour chaque produit

### SalesDriver
- Configure et lance le job MapReduce
- Définit les classes Mapper, Reducer, et Combiner
- Configure les formats d'entrée/sortie

## Licence

MIT License
