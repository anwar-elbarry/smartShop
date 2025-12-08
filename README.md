# SmartShop

Contexte

SmartShop est une API REST (back-end uniquement) de gestion commerciale développée pour MicroTech Maroc — un distributeur B2B de matériel informatique basé à Casablanca. L'application permet de gérer un portefeuille de clients, un système de fidélité à remises progressives, des commandes multi-produits et des paiements fractionnés multi-moyens par facture. L'accent est mis sur la traçabilité financière, la gestion de stock et la robustesse des règles métier.

Points importants

- API REST JSON uniquement (pas d'interface graphique).
- Tests et démonstrations via Postman ou Swagger.
- Authentification par HTTP Session (login / logout). Pas de JWT, pas de Spring Security.
- Rôles : ADMIN (employé MicroTech) et CLIENT (entreprises clientes).

Fonctionnalités clés

1) Gestion des clients
- CRUD client (création, lecture, mise à jour, suppression).
- Statistiques par client : nombre total de commandes confirmées, montant cumulé des commandes confirmées.
- Date première / dernière commande.
- Historique des commandes d'un client (id, date création, montant TTC, statut).

2) Système de fidélité automatique
- Niveaux : BASIC, SILVER, GOLD, PLATINUM.
- Calcul automatique basé sur le nombre de commandes confirmées et le montant cumulé depuis la création du client :
  - BASIC : défaut (0 commande)
  - SILVER : ≥ 3 commandes OU ≥ 1 000 DH cumulés
  - GOLD : ≥ 10 commandes OU ≥ 5 000 DH cumulés
  - PLATINUM : ≥ 20 commandes OU ≥ 15 000 DH cumulés
- Mise à jour du niveau après chaque commande confirmée.
- Remises appliquées selon le niveau et montant :
  - SILVER : 5% si sous-total HT ≥ 500
  - GOLD : 10% si sous-total HT ≥ 800
  - PLATINUM : 15% si sous-total HT ≥ 1200
- Sous-total = montant HT avant remise.

3) Gestion des produits
- CRUD produit.
- Soft delete : si un produit est présent dans des commandes historiques, il est marqué comme "deleted" et n'apparaît plus dans les listes actives, mais reste visible dans les commandes existantes.
- Liste produits avec filtres et pagination.

4) Gestion des commandes
- Création de commandes multi-produits (quantités, prix unitaires sauvegardés dans OrderItem).
- Validation stock (quantité_demandée ≤ stock_disponible).
- Application des remises cumulatives : fidélité + code promo (format strict PROMO-XXXX, possibilité de +5%).
- Calcul automatique :
  - Sous-total HT = Σ (prix HT × quantité)
  - Montant remise totale
  - Montant HT après remise = Sous-total HT - remise
  - TVA (configurable, 20% par défaut) calculée après remise
  - Total TTC = Montant HT après remise + TVA
- Mise à jour après confirmation : décrémentation des stocks, mise à jour statistiques client (totalOrders, totalSpent), recalcul du niveau fidélité.
- Statuts : PENDING, CONFIRMED, CANCELED, REJECTED (transitions contrôlées).

5) Système de paiements multi-moyens
- Moyens : ESPECES, CHÈQUE, VIREMENT.
- Règle : une commande peut être payée en plusieurs fois et avec plusieurs moyens. Elle ne peut être validée (CONFIRMED) par un ADMIN que lorsque le montant_restant = 0.
- Statuts de paiement : EN_ATTENTE, ENCAISSÉ, REJETÉ.
- Limite légale en espèces (ex. 20 000 DH) à prendre en compte côté validation métier.

Règles métier critiques & validations

- Validation commande : doit avoir un client et au moins un article.
- Une commande PENDING ne peut passer à CONFIRMED que si totalement payée.
- PENDING → REJECTED automatique si stock insuffisant lors de la création.
- Arrondis : tous les montants à 2 décimales.
- Codes promo : @Pattern(regexp = "PROMO-[A-Z0-9]{4}") — usage et règles gérées côté back.
- Messages d'erreur clairs et codes HTTP cohérents (400, 401, 403, 404, 422, 500).
- Gestion centralisée des exceptions via @ControllerAdvice (réponse JSON standardisée : timestamp, code HTTP, type, message, path).

Modèle de données (simplifié)

- User : id, username, password, role (ADMIN / CLIENT)
- Client : id, nom, email, niveau fidélité, totalOrders, totalSpent, dateCreated
- Product : id, nom, prixUnitaireHT, stockDisponible, deleted(boolean)
- Order : id, client, date, sousTotalHT, montantRemise, htApresRemise, tva, totalTTC, codePromo, statut, montant_restant
- OrderItem : id, order, product, quantite, prixUnitaireHT, totalLigne
- Payment : id, orderId, sequenceNumber, montant, typePaiement, datePaiement, dateEncaissement, status, reference, bank

Enums principaux

- UserRole { ADMIN, CLIENT }
- CustomerTier { BASIC, SILVER, GOLD, PLATINUM }
- OrderStatus { PENDING, CONFIRMED, CANCELED, REJECTED }
- PaymentStatus { EN_ATTENTE, ENCAISSE, REJETE }

Stack technique

- Java 8+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL ou MySQL (configurable)
- Lombok (Builder pattern)
- MapStruct (mapping Entity ↔ DTO)
- JUnit + Mockito (tests unitaires)
- API REST JSON
- Authentification : HTTP Session (login/logout)

Exécution & configuration (local)

1. Pré-requis : Java 8+, Maven, une base PostgreSQL/MySQL accessible.
2. Configurer les paramètres de connexion DB et la TVA dans `src/main/resources/application.yaml`.

Exemples de commandes (PowerShell)

```powershell
# Lancer l'application via Maven
mvn spring-boot:run

# Ou exécuter le jar construit
java -jar target/SmartShop-0.0.1-SNAPSHOT.jar
```

Tests & démonstration

- Utilisez Postman ou Swagger pour tester les endpoints.
- Une collection Postman (ou documentation Swagger) doit couvrir :
  - Auth (login/logout)
  - CRUD clients
  - CRUD produits
  - Création/consultation/modification commandes
  - Ajout de paiements (paiements fractionnés)
  - Endpoints admin pour valider/annuler/rejeter commandes

Exemples métier rapides

- Fidélité : après X commandes confirmées ou Y DH cumulés, le niveau du client change et les remises s'appliquent automatiquement aux commandes futures si le sous-total atteint le seuil.
- Paiement fractionné : tant que montant_restant > 0, une commande reste en PENDING. Dès que montant_restant = 0, un ADMIN peut la passer à CONFIRMED.

Livrables attendus

- Lien GitHub du code source complet
- Diagramme de classes UML (image)
- Backlog / projet JIRA
- README clair (celui-ci)

Checklist pour la soutenance

- [ ] Le projet démarre sans erreur et se connecte à la base
- [ ] Validations (stock, TVA, remises) fonctionnelles
- [ ] Erreurs renvoyées au format JSON cohérent
- [ ] Architecture Controller-Service-Repository-DTO-Mapper claire
- [ ] Tests unitaires (couverture minimale des règles métier)
- [ ] Collection Postman ou Swagger disponible

Prochaines actions proposées

- Je peux vérifier le build du projet (mvn -DskipTests package) pour reproduire l'erreur mentionnée précédemment et corriger les imports manquants ou les packages absents.
- Si vous voulez, j'inspecte maintenant l'erreur : `Payment.java:6:35 java: package org.example.smartshop.enums does not exist` et corrige le fichier source ou la structure des packages.

Contact

Pour toute question ou ajout au README, dites-moi ce que vous souhaitez que j'ajoute (diagramme, collection Postman, exemples d'API, etc.).

---

(Ce README synthétise le cahier des charges fourni et propose les instructions de base pour exécuter et tester l'API.)

## Documentation API (endpoints découverts dans le code)

> Authentification

- POST /api/auth/login
  - Description : authentifie un utilisateur, crée une HTTP Session.
  - Body (JSON) : { "username": "...", "password": "..." }
  - Réponse : 200 OK + LoginResponse (id, username, role)
  - Session : les attributs `UserId` et `role` sont définis sur la session.

- POST /api/auth/register
  - Description : enregistre un utilisateur (utilisé pour créer des ADMINs dans le code).
  - Body (JSON) : RegisterRequest DTO
  - Réponse : 201 Created

- POST /api/auth/logout
  - Description : invalide la session
  - Réponse : 200 OK


> Clients

- POST /api/clients
  - Create client (ClientRequest)
  - Réponse : 201 Created + client

- PUT /api/clients/{clientId}
  - Update client (ClientUpdate)
  - Réponse : 202 Accepted + client

- DELETE /api/clients/{clientId}
  - Supprime (soft delete selon implémentation)
  - Réponse : 200 OK

- GET /api/clients
  - Liste paginée des clients (Pageable)
  - Réponse : 200 + Page

- GET /api/clients/{clientId}
  - Détails d'un client (avec historique/statistiques selon service)
  - Réponse : 200 + client


> Produits

- POST /api/products
  - Create product (ProductRequest)
  - Réponse : 201 Created

- PUT /api/products/{productId}
  - Update product (ProductUpdate)
  - Réponse : 200 OK

- DELETE /api/products/{productId}
  - Delete product (soft delete si utilisé dans commandes)
  - Réponse : 200 OK

- GET /api/products
  - Liste paginée de produits (Pageable)
  - Réponse : 200 + Page

- GET /api/products/{productId}
  - Détails produit
  - Réponse : 200 + product


> Commandes (Orders)

- POST /api/orders
  - Crée une commande multi-produits (OrderRequest)
  - Réponse : 201 Created + order (statut initial PENDING ou REJECTED si stock insuffisant)

- GET /api/orders/{orderId}
  - Récupère une commande par id
  - Réponse : 200 + order

- GET /api/orders
  - Liste paginée des commandes
  - Réponse : 200 + Page<OrderResponse>


> Paiements

- POST /api/payments
  - Traiter un paiement (PaymentRequest) — supporte paiements fractionnés et multi-moyens
  - Réponse : 201 Created + payment

- GET /api/payments
  - Liste paginée des paiements (option : ?clientId=...)
  - Réponse : 200 + Page<PaymentResponse>

- GET /api/payments/order/{orderId}
  - Paiements d'une commande (paginated)
  - Réponse : 200 + Page<PaymentResponse>

- GET /api/payments/order/{orderId}/status
  - Vérifie si la commande est totalement payée (boolean)
  - Réponse : 200 + true|false

- PUT /api/payments/{paymentId}/status
  - Mise à jour du statut d'un paiement (UpdatePaymentStatusRequest)
  - Réponse : 200 + updated payment


## Exemple d'appels (PowerShell / curl)

1) Login (récupère la session cookie)

```powershell
curl -i -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}' -c cookies.txt
```

2) Créer un client (en utilisant le cookie de session)

```powershell
curl -X POST "http://localhost:8080/api/clients" -H "Content-Type: application/json" -d '{"name":"ACME","email":"contact@acme.test"}' -b cookies.txt
```

3) Créer un produit

```powershell
curl -X POST "http://localhost:8080/api/products" -H "Content-Type: application/json" -d '{"name":"Laptop","prixUnitaireHT":10000,"stockDisponible":50}' -b cookies.txt
```

4) Créer une commande (OrderRequest simplifié)

```powershell
curl -X POST "http://localhost:8080/api/orders" -H "Content-Type: application/json" -d '{"clientId":"<clientId>","items":[{"productId":"<productId>","quantity":2}]}' -b cookies.txt
```

Remplacez <clientId> et <productId> par des ids réels renvoyés précédemment.


## Swagger / Documentation interactive

- Le projet utilise `springdoc` + annotations OpenAPI (io.swagger.v3). Une fois l'application démarrée, ouvrez :
  - http://localhost:8080/swagger-ui.html
  - ou http://localhost:8080/swagger-ui/index.html

Cela vous donnera la documentation interactive pour chaque endpoint et les DTOs.


## Configuration & exécution

- Paramètres principaux : `src/main/resources/application.yaml` (connexion DB, port, taux TVA, autres propriétés).

Lancer en local (PowerShell):

```powershell
mvn spring-boot:run
# ou
mvn clean package -DskipTests; java -jar target/SmartShop-0.0.1-SNAPSHOT.jar
```


## Tests

- Tests unitaires : JUnit + Mockito. Pour exécuter les tests :

```powershell
mvn test
```


## Structure du projet

Voici une arborescence simplifiée (extrait) du projet et l'emplacement des fichiers/dossiers importants :

```
SmartShop/
├─ pom.xml
├─ mvnw
├─ mvnw.cmd
├─ HELP.md
├─ README.md
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ org/example/smartshop/
│  │  │     ├─ SmartShopApplication.java         # point d'entrée Spring Boot
│  │  │     ├─ config/                           # configurations (intercepteurs, beans)
│  │  │     ├─ controller/                       # API controllers (auth, client, product, order, payment)
│  │  │     │  ├─ auth/
│  │  │     │  ├─ client/
│  │  │     │  ├─ product/
│  │  │     │  ├─ order/
│  │  │     │  └─ payment/
│  │  │     ├─ dto/                              # DTOs (request/response for APIs)
│  │  │     ├─ entity/                           # Entities JPA (Client, Product, Order, Payment, User...)
│  │  │     ├─ enums/                            # Enums (OrderStatus, PaymentStatus, UserRole, CustomerTier...)
│  │  │     ├─ exception/                        # gestion centralisée des exceptions
│  │  │     ├─ mapper/                           # MapStruct mappers Entity <-> DTO
│  │  │     ├─ repository/                       # Spring Data repositories
│  │  │     └─ service/                          # Services et leurs implémentations
│  │  └─ resources/
│  │     ├─ application.yaml                     # configuration (DB, TVA, etc.)
│  │     ├─ static/
│  │     └─ templates/
│  └─ test/
│     └─ java/org/example/smartshop/             # tests unitaires (JUnit + Mockito)
└─ target/                                       # build output
```

Explication rapide des dossiers

- `src/main/java/org/example/smartshop/controller` : contient les endpoints REST. Regardez `*ControllerImpl.java` pour les implémentations et `*Controller.java` pour les interfaces.
- `src/main/java/org/example/smartshop/dto` : tous les DTOs (Request, Response, Update) utilisés par les controllers.
- `src/main/java/org/example/smartshop/entity` : entités JPA, champs mappés vers la base (Client, Product, Order, OrderItem, Payment, User...).
- `src/main/java/org/example/smartshop/enums` : énumérations métier (OrderStatus, PaymentStatus, UserRole, CustomerTier, TypePaiement, ...).
- `src/main/java/org/example/smartshop/service` : logique métier. Les services exposent des interfaces et des implémentations (ex : `OrderService`, `OrderServiceImpl`).
- `src/main/java/org/example/smartshop/repository` : interfaces Spring Data JPA pour l'accès aux données.
- `src/main/java/org/example/smartshop/mapper` : MapStruct mappers pour convertir Entity <-> DTO.
- `src/main/java/org/example/smartshop/exception` : classes pour gérer les erreurs et un `@ControllerAdvice` centralisé.
- `src/main/resources/application.yaml` : configuration (connexion DB, propriétés applicatives, taux TVA, etc.).
- `src/test/java` : tests unitaires et d'intégration (JUnit + Mockito).

Où regarder en premier (pour la soutenance)

1. `SmartShopApplication.java` : point d'entrée.
2. `application.yaml` : vérifiez la configuration de la DB et la propriété `tva` si elle est externalisée.
3. `controller/` : montrez les endpoints exposés (l'OpenAPI/Swagger est annoté dans les controllers).
4. `service/` : la logique métier (stock, remises, calcul TVA, mise à jour du niveau de fidélité).
5. `repository/` et `entity/` : structure de la BDD.
6. `mapper/` : montre comment les entités sont transformées en DTOs (MapStruct).

Bonnes pratiques pour la présentation

- Ouvrez `src/main/resources/application.yaml` et fournissez un `application-example.yaml` pour faciliter le démarrage en local (H2 si nécessaire).
- Préparez une collection Postman basée sur les endpoints exposés et incluez des exemples pour les scénarios métier (création commande + paiements fractionnés + confirmation).
- Montrez un diagramme de classes simple (entités principales et relations) pour appuyer la structure du code.

## Présentation

- **Lien vers la présentation Canva** : [Voir la présentation](https://www.canva.com/design/DAG68pxiBWw/sLmXUfXvrpQEUoTE3BhaDQ/edit?ui=e30)
