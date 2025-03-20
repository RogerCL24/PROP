
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
### Comandes principals:

- `make all`: Compila totes les classes del sistema, incloent-hi les del directori drivers.
- `make compile_classes`: Compila les classes principals del projecte i els drivers, generant els fitxers .class al directori EXE.
- `make compile_tests`: Compila tots els tests unitaris amb JUnit i les seves dependències.
- `make jars`: Genera els executables dels drivers en format .jar al directori EXE. Si no existeix el directori `outputs`, es crea automàticament.
- `make fulltest`: Executa tots els tests o suites de tests de totes les classes.

## Comandes per executar drivers i tests:

- `make executaDomainDriver`: Executa el driver del domini (domainDriver) utilitzant el fitxer .jar corresponent
- `make executaDomainDriverTest`: Executa el driver del domini (domainDriver) amb entrada des d'un fitxer de proves. Es pot especificar el fitxer amb la variable TEST_FILE.

### Comandes per executar tests individuals:

- `make TestCjtDistribuciones`: Executa els tests de la classe CjtDistribucionesTest.
- `make TestCjtLlistesProductes`: Executa els tests de la classe CjtLlistesProductesTest.
- `make TestCjtUsers`: Executa els tests de la classe CjtUsersTest.
- `make TestDistribution`: Executa els tests de la classe DistributionTest.
- `make TestLlistaProductes`: Executa els tests de la classe LlistaProductesTest.
- `make TestMensaje`: Executa els tests de la classe MensajeTest.
- `make TestPair`: Executa els tests de la classe PairTest.
- `make TestAproximationKruskalILS`: Executa els tests de l'algorisme AproximationKruskalILS.
- `make TestVoraz`: Executa els tests de l'algorisme Voraz.

### Comandes de neteja:

- `make clean`: Elimina els fitxers .class i .jar generats en el directori EXE.
- `make classclean`: Neteja completament els fitxers .class dels directoris de sortida.

### Tests disponibles:

- `test1`: 5 productes
- `test2`: 16 productes
- `test3`: 32 productes
- `test4`: 100 productes

- `make all`: Compila totes les classes del sistema.

