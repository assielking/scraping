# Scraper d'informations sur les entreprises de restauration en Scala

Ce projet Scala a été développé pour extraire des informations à partir des 10 premières entreprises de restauration de Go Africa Online. Le scraper collecte des données telles que les coordonnées, les services, les informations et les horaires des entreprises à partir d'une source en ligne (précisez la source ici, par exemple, un site web ou une API).

## Configuration requise

Avant d'exécuter le scraper, assurez-vous d'avoir installé les éléments suivants :

- Scala (version x.x.x)
- SBT (Scala Build Tool) (version 3.3.1)
- Autres dépendances (le cas échéant)

## Compilez le projet à l'aide de SBT

- sbt compile

## Exécutez le scraper en utilisant la commande suivante 

- sbt run

Le scraper collectera automatiquement les informations des 10 premières entreprises de restauration à partir de la source configurée.

Les données extraites seront stockées dans un fichier de sortie ( restaurants.csv).