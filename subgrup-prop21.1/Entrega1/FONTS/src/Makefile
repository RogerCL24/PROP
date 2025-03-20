# Variables para los directorios y archivos de entrada y salida
CLASS_INPUT = ./main/domain/classes/*.java \
              ./main/domain/classes/algorism/*.java \
              ./main/domain/classes/types/*.java \
              ./main/domain/controllers/*.java

DRIVER_INPUT = ./drivers/*.java
OUTPUTS_DIR = ./test/outputs/
CLASS_OUTPUT = ../../EXE/
JAR_OUTPUT = ../../EXE/jars/
JUNIT_JARS = ../../lib/junit-4.12.jar:../../lib/hamcrest-core-1.3.jar:../../lib/mockito-core-4.9.0.jar:../../lib/byte-buddy-1.12.16.jar:../../lib/byte-buddy-agent-1.12.16.jar:../../lib/objenesis-3.3.jar

# Archivos de prueba
JUNIT_TESTS = ./test/java/*.java \
              ./test/java/algorism/*.java \
              ./test/java/types/*.java

# Regla para compilar todo el código
all: compile_classes

# Compilar el código principal y los drivers
compile_classes:
	javac -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(DRIVER_INPUT)

# Compilar los tests con JUnit
compile_tests:
	javac -cp $(JUNIT_JARS):$(CLASS_OUTPUT) -d $(CLASS_OUTPUT) $(JUNIT_TESTS)

# Crear el directorio outputs si no existe
create_outputs_dir:
	mkdir -p $(OUTPUTS_DIR)

# Ejecutar todos los tests con MasterTestSuite
fulltest: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore MasterTestSuite

# Ejecutar tests individuales
TestCjtDistribuciones: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore CjtDistribucionesTest

TestCjtLlistesProductes: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore CjtLlistesProductesTest

TestCjtUsers: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore CjtUsersTest

TestDistribution: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore DistributionTest

TestLlistaProductes: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore LlistaProductesTest

TestMensaje: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore MensajeTest

TestPair: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore types.PairTest

TestAproximationKruskalILS: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore algorism.AproximationKruskalILSTest

TestVoraz: all
	java -cp $(JUNIT_JARS):$(CLASS_OUTPUT) org.junit.runner.JUnitCore algorism.VorazTest


# Crear .jar para el Driver de Dominio (sin ejecutar)
jarDomini:
	javac -d $(CLASS_OUTPUT) $(CLASS_INPUT) ./drivers/domainDriver.java
	jar cmvf ./drivers/domain.mf $(JAR_OUTPUT)domainDriver.jar -C $(CLASS_OUTPUT) .

# Crear todos los jars de los drivers sin ejecutarlos
jars: jarDomini create_outputs_dir

# Ejecutar los .jar de cada driver individualmente

executaDomainDriver:
	java -jar $(JAR_OUTPUT)domainDriver.jar

# Ejecutar juegos de prueba del domainDriver

executaDomainDriverTest:
	java -jar $(JAR_OUTPUT)domainDriver.jar < $(TEST_FILE)


# Limpiar los archivos compilados y jars
clean:
	rm -r $(CLASS_OUTPUT)* $(JAR_OUTPUT)*

# Limpiar completamente los directorios de salida
classclean: 
	find $(CLASS_OUTPUT) -name "*.class" -type f -delete
