
# Directori src

**Path absolut**: `/FONTS/src`

## Descripció del directori
Aquest directori conté tot el codi del projecte organitzat per packages.

## Elements del directori

- **Directori drivers**: Conté els drivers utilitzats per a testejar el sistema complet.
- **Directori main**: Inclou els codis de les classes del model, organitzats per capes seguint l'arquitectura en tres capes. Actualment, només el directori domini conté contingut.
- **Directori test**: Inclou els tests unitaris implementats amb JUnit, classificats segons les classes que implementen funcionalitats i les que defineixen tipus de dades.
- **Makefile**: Permet executar el sistema i testejar les classes a través dels drivers, amb diverses opcions disponibles.

## Descripció Makefile

- `make all`

      Compila totes les classes del sistema. És la regla principal que invoca `compile_classes`.

- `make compile_classes`

      Compila el codi principal i els drivers. Els fitxers `.class` es guarden al directori definit per la variable `CLASS_OUTPUT`.

- `make jars`

      Crea tots els fitxers `.jar` necessaris, incloent-hi el Driver de Presentació.

- `make jarPresentacio`

      Compila el codi del sistema i el Driver de Presentació, i crea un fitxer `.jar` executable al directori `JAR_OUTPUT`.

- `make executaPresentationDriver`

      Executa el fitxer `.jar` del Driver de Presentació, incloent la dependència de la llibreria GSON.

- `make clean`

      Elimina tots els fitxers compilats i els `.jar` generats. Esborra el contingut dels directoris `CLASS_OUTPUT` i `JAR_OUTPUT`.

- `make classclean`

      Neteja completament els fitxers `.class` dins del directori `CLASS_OUTPUT` utilitzant el comando `find`.

## Test inputs
Dins el directori test/inputs es troben els fitxers d'input amb productes per introduir al sistema;

- test1.txt: 5 productes
- test2.txt: 16 productes
- test3.txt: 32 productes
- test4.txt: 100 productes


