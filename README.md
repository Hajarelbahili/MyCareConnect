# 🏥 MyCareConnect

Application médicale développée avec **Spring Boot** permettant la **gestion des utilisateurs** et la **réservation des rendez-vous médicaux** selon les rôles (Admin, Docteur, Personnel, Patient).

---

## 🚀 Fonctionnalités principales

### 👤 Gestion des utilisateurs et des rôles
- **Admin de l’hôpital :**
  - Crée les comptes des **docteurs** et du **personnel**
  - Gère les rôles, départements et spécialités médicales
  - Supervise l’ensemble des rendez-vous et des utilisateurs

- **Docteur :**
  - Ne crée pas de compte (créé par l’admin)
  - Peut **modifier ses informations personnelles**
  - Peut **consulter, annuler ou gérer ses rendez-vous**
  - Peut **ajouter ses disponibilités** (par jour ou par mois)
  - Calendrier flexible : horaires différents selon les jours
  - Classé par **spécialité** et **département**

- **Personnel médical :**
  - Ne crée pas de compte (créé par l’admin)
  - Peut **créer et gérer les comptes des patients**
  - Peut **modifier les informations des patients**
  - Peut **ajouter des documents médicaux** liés aux patients (état de santé, ordonnances, résultats, etc.)

- **Patient :**
  - Peut **créer son propre compte**
  - Peut **modifier ou supprimer** son compte
  - Peut **réserver, modifier ou annuler** un rendez-vous
  - Peut **télécharger les documents médicaux** liés à ses consultations
  - Peut **consulter la disponibilité des médecins**

---

### 📅 Gestion des rendez-vous et disponibilités
- Réservation et annulation de rendez-vous selon la disponibilité du médecin  
- Gestion des créneaux par jour, semaine ou mois  
- Affichage dynamique du calendrier pour chaque médecin  
- ✅ **Contrôle intelligent des chevauchements de rendez-vous :**
  - Un patient ne peut pas réserver deux rendez-vous qui se chevauchent dans le temps.  
  - Un patient ne peut pas réserver plusieurs rendez-vous avec le même médecin pour la même journée.

---

### 🔐 Sécurité et authentification
- Authentification et autorisation via **Spring Security**
- Accès restreint selon les rôles : Admin, Docteur, Personnel, Patient
- Gestion sécurisée des sessions et des accès aux ressources
- Validation des rôles pour chaque opération sensible

---

## 🛠️ Technologies utilisées
- **Backend :** Spring Boot, Spring Data JPA, Spring Security  
- **Base de données :** MySQL  
- **Gestionnaire de dépendances :** Maven  
- **IDE :** IntelliJ IDEA  
- **Langage :** Java  

---

## ⚙️ Lancer le projet
```bash
mvn spring-boot:run
