# Variables per als directoris i fitxers d'entrada i sortida
CLASS_INPUT = ./main/domain/classes/*.java \
              ./main/domain/classes/algorism/*.java \
              ./main/domain/classes/types/*.java \
	      ./main/domain/classes/exceptions/*.java \
              ./main/domain/controllers/*.java \
              ./main/persistence/classes/*.java \
              ./main/persistence/controllers/*.java \
              ./main/presentation/controllers/*.java \
              ./main/presentation/functions/*.java \
              ./main/presentation/utils/*.java \
              ./main/presentation/vistas/*.java

DRIVER_INPUT = ./drivers/presentationDriver.java
OUTPUTS_DIR = ./test/outputs/
CLASS_OUTPUT = ../../EXE/
JAR_OUTPUT = ../../EXE/jars/
GSON_JAR = ../../lib/gson-2.10.1.jar

# Regla per compilar tot el codi
all: compile_classes

# Compilar el codi principal i els drivers
compile_classes:
		javac -cp $(GSON_JAR) -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(DRIVER_INPUT)

# Crear els .jar per al driver de presentació
jars: jarPresentacio

# Crear el .jar per al Driver de Presentació
jarPresentacio:
		javac -cp $(GSON_JAR) -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(DRIVER_INPUT)
		jar cmvf ./drivers/presentation.mf $(JAR_OUTPUT)presentationDriver.jar -C $(CLASS_OUTPUT) .

# Executar el Driver de Presentació
executaPresentationDriver:
		java -cp $(JAR_OUTPUT)presentationDriver.jar:$(GSON_JAR) drivers.presentationDriver


# Netejar els fitxers compilats i els .jar
clean:
		rm -r $(CLASS_OUTPUT)* $(JAR_OUTPUT)*

# Netejar completament els fitxers .class
classclean: 
		find $(CLASS_OUTPUT) -name "*.class" -type f -delete

