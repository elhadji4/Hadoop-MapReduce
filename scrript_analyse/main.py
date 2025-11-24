import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Lire le CSV exporté
df = pd.read_csv(r"C:\Users\TUBA\Desktop\Workspace\Hadoop-java\revenue.csv", sep="\t", header=None)
df.columns = ["Product", "Revenue"]

# Afficher le tableau
print(df)

# Graphique du revenu par produit
plt.figure(figsize=(8,5))
sns.barplot(x="Product", y="Revenue", data=df, palette="viridis")
plt.title("Revenu total par produit")
plt.ylabel("Revenu (€)")
plt.xlabel("Produit")
plt.show()
