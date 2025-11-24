# Hadoop MapReduce - Sales Revenue Calculator

Développement d'un job Hadoop MapReduce en Java pour calculer le revenu total par produit à partir d'un fichier de ventes CSV. Implémentation des étapes : ingestion des données dans HDFS, MapReduce (mapper + reducer), génération des résultats et export CSV pour visualisation.

## Description

Ce projet implémente un job Hadoop MapReduce qui calcule le revenu total par produit à partir d'un fichier CSV de ventes. Le job traite les données de ventes, agrège les revenus par produit, et génère un rapport CSV pour la visualisation.

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   sales.csv     │────▶│   SalesMapper   │────▶│  SalesReducer   │
│   (Input)       │     │ (product,revenue)│     │ (product,total) │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                         │
                                                         ▼
                                              ┌─────────────────┐
                                              │  Output CSV     │
                                              │  (product,total)│
                                              └─────────────────┘
```

## Structure du Projet

```
├── src/
│   ├── main/java/com/sales/
│   │   ├── SalesMapper.java        # Mapper class
│   │   ├── SalesReducer.java       # Reducer class
│   │   └── SalesRevenueDriver.java # Driver class
│   └── test/java/com/sales/
│       └── SalesMapReduceTest.java # Unit tests
├── data/
│   └── sales.csv                   # Sample sales data
├── config/
│   ├── core-site.xml               # Hadoop core configuration
│   ├── hdfs-site.xml               # HDFS configuration
│   ├── mapred-site.xml             # MapReduce configuration
│   └── yarn-site.xml               # YARN configuration
├── scripts/
│   ├── start-hadoop.sh             # Script to start Hadoop
│   └── run-job.sh                  # Script to run the MapReduce job
├── Dockerfile                       # Docker image definition
├── docker-compose.yml              # Docker Compose configuration
└── pom.xml                         # Maven build configuration
```

## Prérequis

- Docker et Docker Compose
- Maven 3.x (pour la compilation)
- Java 8+

## Format des Données d'Entrée

Le fichier CSV d'entrée doit avoir le format suivant:

```csv
transaction_id,product_name,quantity,unit_price,date
1,Laptop,2,999.99,2024-01-15
2,Mouse,5,29.99,2024-01-15
```

## Compilation

```bash
# Compiler le projet avec Maven
mvn clean package
```

## Exécution avec Docker

### 1. Construire et démarrer le conteneur Hadoop

```bash
# Construire l'image et démarrer le conteneur
docker-compose up -d --build
```

### 2. Exécuter le job MapReduce

```bash
# Se connecter au conteneur
docker exec -it hadoop-sales-revenue bash

# Exécuter le job
/app/run-job.sh
```

### 3. Récupérer les résultats

Les résultats sont exportés dans le répertoire `./output/revenue_by_product.csv`.

## Exécution Manuelle (sans Docker)

Si vous avez un cluster Hadoop configuré:

```bash
# 1. Copier les données dans HDFS
hdfs dfs -mkdir -p /input
hdfs dfs -put data/sales.csv /input/

# 2. Exécuter le job MapReduce
hadoop jar target/sales-revenue-mapreduce-1.0.0.jar com.sales.SalesRevenueDriver /input/sales.csv /output/sales-revenue

# 3. Voir les résultats
hdfs dfs -cat /output/sales-revenue/part-r-00000
```

## Résultats Attendus

Pour les données d'exemple fournies, le résultat sera:

```csv
product_name,total_revenue
Headphones,1799.88
Keyboard,1119.86
Laptop,6999.93
Monitor,1399.96
Mouse,779.74
USB Cable,349.65
```

## Tests

```bash
# Exécuter les tests unitaires
mvn test
```

## Interfaces Web

Une fois le conteneur démarré, accédez aux interfaces web:

- **HDFS NameNode**: http://localhost:9870
- **YARN ResourceManager**: http://localhost:8088
- **YARN NodeManager**: http://localhost:8042

## Composants MapReduce

### SalesMapper
- **Input**: Lignes CSV (transaction_id, product_name, quantity, unit_price, date)
- **Output**: (product_name, revenue) où revenue = quantity × unit_price

### SalesReducer
- **Input**: (product_name, [revenue1, revenue2, ...])
- **Output**: (product_name, total_revenue) où total_revenue = Σ(revenues)

## Technologies Utilisées

- **Java 8**: Langage de programmation
- **Apache Hadoop 3.3.6**: Framework de traitement distribué
- **Maven**: Gestion des dépendances et build
- **Docker**: Conteneurisation
- **MRUnit**: Tests unitaires MapReduce

## License

MIT License - voir le fichier [LICENSE](LICENSE) pour plus de détails.
