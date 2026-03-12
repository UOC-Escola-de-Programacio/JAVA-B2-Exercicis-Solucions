# 🎓 JAVA B2 — Repositori d'Exercicis / Repositorio de Ejercicios

> 🇪🇸 [Versión en castellano más abajo](#-ejercicios-java-b2---guía-del-alumno)
> 🇨🇦 [Versió en català a continuació](#-exercicis-java-b2---guia-de-lalumne)

---

## 🇨🇦 Exercicis Java B2 — Guia de l'Alumne

### 🚀 Com Començar

**1. Clona el repositori**
```bash
git clone https://github.com/TU_USUARI/JAVA-B2-Exercicis.git
cd JAVA-B2-Exercicis
```

**2. Obre a IntelliJ IDEA**
- `File → Open` → selecciona la carpeta arrel (`JAVA-B2-Exercicis`)
- Accepta l'import com a projecte Maven
- Espera que IntelliJ descarregui les dependències (primera vegada pot trigar 2-3 min)

**3. Com implementar un exercici**

Cada exercici té **dos fitxers**:
- **Esquelet** (`src/main/java/.../ExNom.java`) — **aquí has d'escriure el teu codi**
- **Tests** (`src/test/java/.../ExNomTest.java`) — NO els modifiquis!

Busca els comentaris `// TODO` i implementa el codi allà.

**4. Executa els tests localment**
```bash
# Tots els tests
mvn test

# Un mòdul específic
mvn test -pl tema1-poo

# Un test específic
mvn test -pl tema1-poo -Dtest=Exercici01TemperaturaTest
```

**5. Fes commit i push**
```bash
git add .
git commit -m "tema1/ex01: implementa Temperatura"
git push
```

El pipeline de GitHub Actions s'executarà automàticament i podrás veure el resultat a la pestanya **Actions** del teu repositori.

### 📚 Estructura del Projecte

```
JAVA-B2-Exercicis/
├── tema1-poo/           ← POO Avançada, Lambdas, Streams, Optional, Observer
│   └── src/
│       ├── main/java/edu/uoc/b2/tema1/
│       │   ├── ex01/  Temperatura.java           ⭐ Fàcil
│       │   ├── ex02/  FiltroTextos.java          ⭐⭐ Mitjà
│       │   ├── ex03/  ProcesadorProductos.java   ⭐⭐ Mitjà
│       │   ├── ex04/  GestorUsuarios.java        ⭐⭐⭐ Difícil
│       │   └── ex05/  SistemaNotificaciones.java ⭐⭐⭐⭐ Expert
│       └── test/...   (Tests JUnit 5 — NO modifiques!)
│
├── tema3-sql/           ← SQL (MySQL local)
│   └── src/
│       ├── main/java/edu/uoc/b2/tema3/
│       │   └── ConsultasSQL.java                 ⭐→⭐⭐⭐⭐
│       └── test/resources/schema.sql             (BD de proves)
│
├── tema4-jdbc/          ← JDBC, PreparedStatement, DAO, Transaccions
│   └── src/main/java/edu/uoc/b2/tema4/
│       ├── ex01/  ProductoDAO.java               ⭐⭐ Mitjà
│       ├── ex02/  ConsultasSQL.java              ⭐⭐ Mitjà
│       └── ex03/  TraspasoBancario.java          ⭐⭐⭐ Difícil
│
├── tema5-mongodb/       ← MongoDB, NoSQL, Drivers
│   └── src/main/java/edu/uoc/b2/tema5/
│       ├── ex01/  ProductoRepositorioMongo.java  ⭐⭐ Mitjà
│       └── ex02/  MigradorSQLMongo.java          ⭐⭐⭐ Difícil
│
└── tema6-excepcions/    ← Excepcions, Jerarquies, Wrapping
    └── src/main/java/edu/uoc/b2/tema6/
        ├── ex01/  CalculadoraSegura.java         ⭐ Fàcil
        └── ex02/  ValidadorUsuario.java          ⭐⭐ Mitjà
```

### 🏅 Llegenda de Dificultat

| Nivell | Descripció |
|--------|------------|
| ⭐ | **Fàcil** — Conceptes bàsics, poc codi |
| ⭐⭐ | **Mitjà** — Requereix entendre el concepte |
| ⭐⭐⭐ | **Difícil** — Lògica complexa, múltiples casos |
| ⭐⭐⭐⭐ | **Expert** — Combinació avançada de conceptes |

### ❓ Problemes Freqüents

**"Package does not match"** a IntelliJ → Tanca i torna a obrir el projecte com Maven.

**Tests fallen tots** → Assegura't que has implementat tots els `TODO` (els `throw new UnsupportedOperationException` hi causen problemes).

**No em carrega Maven** → Executa `mvn dependency:resolve` des del terminal.

---

## 🇪🇸 Ejercicios Java B2 — Guía del Alumno

### 🚀 Cómo Empezar

**1. Clona el repositorio**
```bash
git clone https://github.com/TU_USUARIO/JAVA-B2-Exercicis.git
cd JAVA-B2-Exercicis
```

**2. Abre en IntelliJ IDEA**
- `File → Open` → selecciona la carpeta raíz (`JAVA-B2-Exercicis`)
- Acepta el import como proyecto Maven
- Espera que IntelliJ descargue las dependencias (la primera vez puede tardar 2-3 min)

**3. Cómo implementar un ejercicio**

Cada ejercicio tiene **dos archivos**:
- **Esqueleto** (`src/main/java/.../ExNombre.java`) — **aquí debes escribir tu código**
- **Tests** (`src/test/java/.../ExNombreTest.java`) — ¡NO los modifiques!

Busca los comentarios `// TODO` e implementa el código ahí.

**4. Ejecuta los tests localmente**
```bash
# Todos los tests
mvn test

# Un módulo específico
mvn test -pl tema1-poo

# Un test específico
mvn test -pl tema1-poo -Dtest=Exercici01TemperaturaTest
```

**5. Haz commit y push**
```bash
git add .
git commit -m "tema1/ex01: implementa Temperatura"
git push
```

El pipeline de GitHub Actions se ejecutará automáticamente y podrás ver el resultado en la pestaña **Actions** de tu repositorio.

### 📚 Estructura del Proyecto

```
JAVA-B2-Exercicis/
├── tema1-poo/           ← POO Avanzada, Lambdas, Streams, Optional, Observer
│   └── src/
│       ├── main/java/edu/uoc/b2/tema1/
│       │   ├── ex01/  Temperatura.java           ⭐ Fácil
│       │   ├── ex02/  FiltroTextos.java          ⭐⭐ Medio
│       │   ├── ex03/  ProcesadorProductos.java   ⭐⭐ Medio
│       │   ├── ex04/  GestorUsuarios.java        ⭐⭐⭐ Difícil
│       │   └── ex05/  SistemaNotificaciones.java ⭐⭐⭐⭐ Experto
│       └── test/...   (Tests JUnit 5 — ¡NO modifiques!)
│
├── tema3-sql/           ← SQL (MySQL local)
│   └── src/
│       ├── main/java/edu/uoc/b2/tema3/
│       │   └── ConsultasSQL.java                 ⭐→⭐⭐⭐⭐
│       └── test/resources/schema.sql             (BD de prueba)
│
├── tema4-jdbc/          ← JDBC, PreparedStatement, DAO, Transacciones
│   └── src/main/java/edu/uoc/b2/tema4/
│       ├── ex01/  ProductoDAO.java               ⭐⭐ Medio
│       ├── ex02/  ConsultasSQL.java              ⭐⭐ Medio
│       └── ex03/  TraspasoBancario.java          ⭐⭐⭐ Difícil
│
├── tema5-mongodb/       ← MongoDB, NoSQL, Drivers
│   └── src/main/java/edu/uoc/b2/tema5/
│       ├── ex01/  ProductoRepositorioMongo.java  ⭐⭐ Medio
│       └── ex02/  MigradorSQLMongo.java          ⭐⭐⭐ Difícil
│
└── tema6-excepcions/    ← Excepciones, Jerarquías, Wrapping
    └── src/main/java/edu/uoc/b2/tema6/
        ├── ex01/  CalculadoraSegura.java         ⭐ Fácil
        └── ex02/  ValidadorUsuario.java          ⭐⭐ Medio
```

### 🏅 Leyenda de Dificultad

| Nivel | Descripción |
|-------|-------------|
| ⭐ | **Fácil** — Conceptos básicos, poco código |
| ⭐⭐ | **Medio** — Requiere entender el concepto |
| ⭐⭐⭐ | **Difícil** — Lógica compleja, múltiples casos |
| ⭐⭐⭐⭐ | **Experto** — Combinación avanzada de conceptos |

### ❓ Problemas Frecuentes

**"Package does not match"** en IntelliJ → Cierra y vuelve a abrir el proyecto como Maven.

**Los tests fallan todos** → Asegúrate de haber implementado todos los `TODO` (los `throw new UnsupportedOperationException` causan fallos automáticos).

**Maven no carga** → Ejecuta `mvn dependency:resolve` desde el terminal.

---

## 🔄 CI/CD — Evaluación Automática / Avaluació Automàtica

Cada `git push` lanza el pipeline de **GitHub Actions** que:

1. 🏗️ Compila todo el proyecto (`mvn compile`)
2. 🧪 Ejecuta / Executa todos los tests JUnit 5 (`mvn test`)
3. 📊 Publica / Publica los resultados de los tests
4. 📦 Crea / Crea los artefactos con los informes

Puedes ver / Pots veure el resultado en la pestaña / pestanya **Actions** de GitHub.

---

*Apuntes del curso / Apunts del curs:* [Java B2 — Temari](https://TU_USUARIO.github.io/JAVA-B2-Temari/)
