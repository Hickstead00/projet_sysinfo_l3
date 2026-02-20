# GestMaquette

Plateforme de gestion des maquettes pédagogiques universitaires.

**Projet L3 MIAGE — Université d'Orléans — 2025-2026**

## Équipe

- AMGHAR Gassien
- BODIN Virgile
- DELAHAYE Antoine

## Stack technique

| Composant        | Technologie             |
| ---------------- | ----------------------- |
| Frontend         | Angular                 |
| Backend          | Java 21 / Spring Boot 4 |
| Base de données  | PostgreSQL 16           |
| Conteneurisation | Docker Compose          |

## Prérequis

- [Docker](https://www.docker.com/) et Docker Compose installés

## Lancement

```bash
git clone
cd projet_sysinfo_l3
docker compose up --build
```

Une fois le docker lancé l'application est disponible aux URL's suivants

| Service     | URL                                   |
| ----------- | ------------------------------------- |
| Frontend    | http://localhost:4200                 |
| Backend API | http://localhost:8080                 |
| Swagger UI  | http://localhost:8080/swagger-ui.html |

## Comptes par défaut

> _À venir — les comptes de démonstration seront ajoutés dans une version ultérieure._

## Structure du projet

```
projet_sysinfo_l3/
├── backend/          ← API Spring Boot
├── front/            ← Application Angular
├── database/         ← Scripts SQL d'initialisation + triggers si necessaire
├── docker-compose.yml
└── README.md
```

## Tags de version

| Tag  | Description                                                                   |
| ---- | ----------------------------------------------------------------------------- |
| v0.1 | Structure projet + entités JPA + config CORS                                  |
| v0.2 | CRUD Tags : repository, service, controller, DTOs, exceptions, Swagger        |
| v0.3 | CRUD Professeurs : repository, service, controller, DTOs, exceptions, Swagger |

## Architecture backend

```
com.amgboddel.backend
├── config/        ← Configuration (CORS, sécurité)
├── entity/        ← Entités JPA
├── repository/    ← Interfaces Spring Data
├── service/       ← Logique métier
├── controller/    ← Endpoints REST
├── dto/           ← Objets de transfert
└── exception/     ← Exceptions levées
```

## Base de données

13 tables générées automatiquement par Hibernate :

**Tables principales :** utilisateur, tag, professeur, ue, maquette, semestre, parametres

**Tables de jointure :** tag_ue, tag_professeur, enseigne, referent, prerequis_ue, semestre_ue
