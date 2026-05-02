# 🎓 GestMaquette

> **Plateforme de gestion intelligente des maquettes pédagogiques universitaires.**

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17+-red?logo=angular)](https://angular.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)](https://www.docker.com/)

Ce projet, réalisé dans le cadre de la **L3 MIAGE à l'Université d'Orléans (2025-2026)**, permet de piloter les maquettes de formation, de gérer les charges d'enseignement et de suivre les unités d'enseignement (UE) via un système de tags dynamiques.

---

## 🚀 Lancement Rapide

L'application est entièrement conteneurisée pour garantir une **portabilité totale**. Aucune installation locale de Java, Node ou PostgreSQL n'est requise.

### 1. Prérequis
*   [Docker Desktop](https://www.docker.com/products/docker-desktop/) installé et lancé.

### 2. Installation
```bash
# Cloner le projet
git clone https://github.com/Hickstead00/projet_sysinfo_l3
cd projet_sysinfo_l3

# Lancer toute la stack (Frontend, API, BDD)
docker compose up --build
```

### 3. Accès aux services
| Service | URL | Description |
| :--- | :--- | :--- |
| **Frontend** | [http://localhost:4200](http://localhost:4200) | Interface utilisateur Angular |
| **Backend API** | [http://localhost:8080](http://localhost:8080) | Point d'entrée de l'API REST |
| **Swagger UI** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | Documentation interactive de l'API |

---

## 🏗️ Architecture du Système

Le projet suit un pattern **MVC distribué** pour une séparation stricte des responsabilités.

### 🌐 Frontend (Vue)
Développé avec **Angular & Angular Material**. L'architecture est modulaire et fortement typée.
*   **Gestion des Modèles :** Utilisation de DTOs TypeScript (interfaces avec et sans ID) pour sécuriser les flux de création/modification.
*   **Arborescence :**
```text
src/app
├── component/    ← Composants UI (Enseignants, UE, Tags...)
├── model/        ← Interfaces et DTOs (Données)
├── service/      ← Communication asynchrone avec l'API
├── guard/        ← Sécurisation des accès aux routes
└── interceptor/  ← Gestion globale des headers et erreurs
```

### ⚙️ Backend (Contrôleur)
Développé avec **Java 21** et **Spring Boot 4**. Il assure la logique métier et la sécurité.
*   **Arborescence :**
```text
com.amgboddel.backend
├── config/       ← Sécurité et configuration CORS
├── controller/   ← Exposition des Endpoints REST
├── service/      ← Logique métier (calculs d'heures, validations)
├── repository/   ← Couche d'accès aux données (Spring Data JPA)
├── entity/       ← Modèles persistants en base de données
└── dto/          ← Objets de transfert pour limiter l'exposition des entités
└── exception/    ← Controleur d'exception global, pour rediriger correctement ces dernières.
```

### 🗄️ Base de données (Modèle)
Utilisation de **PostgreSQL 16**. Le schéma comporte 13 tables gérant l'intégrité référentielle des maquettes.

---

## 🛠️ Portabilité & Docker
L'utilisation de `docker-compose.yml` permet de virtualiser l'environnement Linux nécessaire au projet :
*   **Isolation :** Les conflits de versions locales sont impossibles.
*   **Persistence :** Les données de la base PostgreSQL sont conservées via des volumes Docker.
*   **Réseau :** Le backend et la base de données communiquent sur un réseau privé isolé.

---

## 📈 Suivi des Versions (Tags)

| Tag | État | Fonctionnalités clés |
| :--- | :--- | :--- |
| **v0.1** | ✅ | Init de la stack + Entités JPA + Config Docker |
| **v0.2** | ✅ | CRUD Tags & Swagger Integration |
| **v0.3** | ✅ | Gestion des Professeurs & Logique Service |
| **v0.4** | ✅ | Gestion des UE & Validations métier |
| **v0.5** | 🚀 | Maquettes, Semestres et calculs automatiques d'ECTS |

---

## 👥 L'Équipe
*   **AMGHAR Gassien**
*   **BODIN Virgile**
*   **DELAHAYE Antoine**

---
*Projet réalisé dans un but pédagogique - MIAGE Orléans.*