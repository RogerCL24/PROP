# Visió general del projecte

Aquest projecte consisteix a optimitzar la distribució de productes en un supermercat circular per tal de maximitzar les compres dels clients. S'assumeix que la probabilitat de compra augmenta si els productes relacionats estan a prop entre ells (per exemple, cervesa i patates fregides). Cada parell de productes té un grau de similitud que ajuda a determinar-ne la proximitat. El sistema ha de gestionar productes i les seves relacions, calcular distribucions òptimes usant almenys dos algorismes (un de força bruta o voraç i un altre d'aproximació), permetre ajustaments posteriors i permetre l'entrada de dades via programa o fitxer de text. L'eficiència i flexibilitat són prioritàries, i es valoren funcionalitats opcionals, com ara restriccions addicionals o més algoritmes. Permet:

- Carregant documents de diferents formats.
- Gestió de tots els elements enumerats al primer paràgraf:
    - Afegir productes en una llista, modificar-los i eliminar-los.
    - Crear distribucions de llistes de productes.
    - Afeguir productes a una distribució existent, modificar-la i eliminar-la.
    - Gestió d'usuaris amb els seus respectius conjunts de distribucions.
    - Crear distribucions amb diferents criteris d'algorismes.

### Funcions de primer lliurament
S'ha implementat un conjunt de controladors de domini per habilitar totes les operacions necessàries, excepte la càrrega i desar documents.

---

## Estructura de directoris

- **DOCS**: Conté la documentació completa del projecte, inclosos diagrames de casos d'ús amb descripcions, un diagrama estàtic complet del model de dades conceptual en versió de disseny, amb descripcions breus dels atributs i mètodes de classe, implementacions de classe per membre de l'equip i descripcions de les estructures de dades i algorismes utilitzats per implementar les funcionalitats d'aquesta versió.

- **EXE**: Fitxers executables (.class) per a totes les classes que permeten provar les principals funcionalitats implementades. Els subdirectoris s'organitzen per tipus de classe:  *test, exceptions, functions, types*, seguint l'estructura del paquet.

- **FONTS**: Conté el codi font de les classes de domini associades a les principals funcionalitats implementades fins ara. També inclou proves JUnit en subdirectoris organitzats segons l'estructura del paquet, permetent la recompilació directa. A més, conté el Makefile.

- **lib**: Conté biblioteques externes necessàries per a les proves JUnit.

---

## Execució del Programa

Per executar el programa, navegueu al directori `/FONTS/src`, on es troba el Makefile del programa. Creeu el sistema introduint "make" a la consola, permetent provar cada classe individualment mitjançant controladors.

---

## Makefile Commands

- `make jars`: Crea tots els executables del controlador (format .jar) al fitxer `EXE` directori.
- `make executaDriver<DriverName>`: Intents d'executar el jar per al controlador corresponent.

    - Exemple: `make executaDriverDomini` -> executa el `Domini` Driver jar.

- `make executaDomainDriverTest TEST_FILE=ruta/del/fichero.txt`: Executa el test en especific.

- `make fulltest`: Executa tots els conjunts de proves per a totes les classes.
- `make jar<DriverName>`: Compila el codi del sistema i el controlador corresponent, creant un executable `.jar`.

    - Exemple: `make jarDomini` -> crea i executa el `Domini` jar.

- `make Test<SuiteName>`: Executa el conjunt de proves/proves especificat.

- `make Test<TestName>`: Executa el test en especific.      

- `make all`: Compila totes les classes del sistema.

---

## Descripció del projecte

Aquest projecte implica construir un entorn de full de càlcul senzill que admeti la manipulació de dades en format de taula i diverses operacions mitjançant funcions definides. Amb l'objectiu de millorar l'eficiència, la flexibilitat i la usabilitat, el projecte inclou:

- Funcionalitats bàsiques per a la gestió de documents, fulls, files, columnes i cel·les.
- Capacitats de manipulació de dades amb suport de funcions predefinides.
- Implementació que prioritza l'eficiència i la modularitat.

**NOTE**: Les versions futures poden incloure una interfície d'usuari gràfica i funcions ampliades.

---

## Enunciat del projecte

**Distribució de productes a un supermercat**

Tenim un supermercat, i volem trobar la distribució òptima dels productes que s'ofereixen perquè
els clients comprin més. Per fer-ho, suposarem que la probabilitat que un client compri un
producte s'incrementa si aquest producte està al costat d'un producte relacionat amb ell (per
exemple, si va a comprar cervesa i veu les patates fregides al costat, segurament es recordi de
la necessitat de comprar-les). Suposarem que tot parell de productes tenen un grau de similitud
o relació entre si, que l'usuari coneix i pot proporcionar.

Per simplificar, suposarem que només hi ha una prestatgeria circular al supermercat, i que només
té un prestatge per posar els productes (o diversos prestatges dins d’aquesta prestatgeria). A
partir del grau de similitud entre els productes que l'usuari vol oferir, el sistema haurà de trobar
la distribució òptima dels productes que ofereix per maximitzar la probabilitat de que els seus
clients comprin més.

Per garantir que el projecte està aprovat, com a mínim s'han de oferir les següents funcionalitats:

- Gestió de productes
-  Gestió de similituds entre productes 
- Càlcul de distribucions. El programa haurà de proporcionar almenys dos algorismes per
a trobar aquesta distribució òptima: una solució bàsica (de “força bruta” o algorisme
voraç) i un algorisme d’aproximació (es proporcionarà més informació al respecte). Els
paràmetres d’aquests algorismes, si n’hi ha, han de ser configurables interactivament des
de l’aplicació. Es valorarà tota optimització addicional d’ambdues solucions
- El sistema haurà de permetre la modificació posterior de la solució proposada 
-  Les dades s’han de poder definir via el programa o importar des d’un fitxer de text 

A criteri de cada equip, el sistema es pot estendre amb funcionalitats opcionals, com a l’ús de
restriccions en la distribució del productes, l’implementació d’algorismes addicionals, etc. 

A més dels altres factors de qualitat de qualsevol programa (disseny, codificació, reusabilitat,
modificabilitat, usabilitat, documentació,...), es valorarà en particular l'eficiència i flexibilitat
d’aquest. 

**Funcionalitats principals a entregar al primer lliurament:**

- Implementació dels dos algorismes per a trobar la distribució òptima

**Reducció de funcionalitat als equips de 3 persones:**

- No s’ha de tractar l’opció de diversos prestatges dins la prestatgeria 
- No s’han d’optimitzar les solucions

**Dates dels lliuraments:** 

- Primer: dilluns 18 de novembre
- Segon: dilluns 16 de desembre
- Tercer: dilluns 23 de desembre (lliuraments interactius: a partir del 7 de gener)




