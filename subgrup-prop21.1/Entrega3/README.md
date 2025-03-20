## Elements del directori

### DOCS:
Conté tota la documentació referent als tests d'integració de tot el projecte, relació de les classes implementades per 
membre de l’equip i la documentació Javadoc de tot el codi.

### EXE:
Fitxers executables (*.class*) de totes les classes que permeten provar les funcionalitats principals implementades i
un fitxer *.jar* del qual es pot executar el programa sencer
Hi ha subdirectoris per cada un dels tipus de classes: test, excepcions, funcions, tipus, que segueixen l'estructura
determinada pels *packages*

### FONTS:
Codi de les classes de domini, persistència i presentació associades a les funcionalitats principals. 
Tots els fitxers estan dins dels subdirectoris que segueixen l'estructura de packages, perquè el codi sigui
recompilable directament. També inclou el fitxer *Makefile*.

### lib:
S'hi troben les llibreries externes que hem hagut d'utilitzar.

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

