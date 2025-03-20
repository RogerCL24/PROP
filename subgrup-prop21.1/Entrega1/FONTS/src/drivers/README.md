# Directori drivers

> Path absolut: /FONTS/src/drivers

## Descripció del directori
Aquest directori conté el codi dels Drivers dels controladors que proporcionen alguna funcionalitat principal del sistema

## Elements del directori

- **DomainDriver.java:**
Conté el codi del driver del CtrlDomini (Proporciona totes les funcionalitats actuals del sistema)

Al ser executat es mostra un menu amb les possibles opcions a executar. Es pot entrar el nombre o titol del metode a provar.

# Guia d'Ús del Programa

A continuació, es detalla l'ús del programa **Supermarket Distribution Generator** amb les opcions disponibles i el seu funcionament:

## Opcions Disponibles

### 0. Sortir del Programa
- **Descripció**: Tanca el programa.
- **Funcionament**:
  - Quan aquesta opció és seleccionada, el programa mostra un missatge de comiat i finalitza la seva execució.

### 1. Registrar-se
- **Descripció**: Permet registrar un nou usuari al sistema.
- **Funcionament**:
  - Sol·licita a l'usuari introduir informació per al registre, com un nom d'usuari i una contrasenya.
  - Si el registre és correcte, l'usuari pot accedir al sistema.

### 2. Iniciar Sessió
- **Descripció**: Permet a un usuari existent accedir al sistema.
- **Funcionament**:
  - Sol·licita l'ingrés de credencials (nom d'usuari i contrasenya).
  - Si les credencials són correctes, l'usuari accedeix al menú principal.

---

## Menú Principal (Després d'Iniciar Sessió)

### 1. Crear Nova Llista de Productes
- **Descripció**: Permet a l'usuari crear una nova llista de productes.
- **Funcionament**:
  - Crida al mètode corresponent per iniciar el procés de creació d'una llista.
  - L'usuari defineix els productes que vol incloure.

### 2. Gestionar Llistes de Productes
- **Descripció**: Mostra les llistes de productes existents i permet gestionar-les.
- **Funcionament**:
  - Crida al mètode que mostra totes les llistes disponibles.
  - Si hi ha llistes, permet a l'usuari realitzar accions com editar, eliminar o visualitzar detalls de les llistes.

### 3. Generar Distribució
- **Descripció**: Genera una distribució basada en una llista de productes seleccionada.
- **Funcionament**:
  - Sol·licita a l'usuari seleccionar una llista de productes.
  - Defineix les dimensions del prestatge.
  - L'usuari tria l'algorisme de distribució desitjat.
  - Genera la distribució i mostra els resultats.

### 4. Gestionar Distribucions
- **Descripció**: Permet gestionar distribucions prèviament creades.
- **Funcionament**:
  - Mostra les distribucions existents.
  - Si hi ha distribucions disponibles, l'usuari pot gestionar-les (modificar, eliminar o visualitzar detalls).

### 5. Missatges Entre Ususaris
- **Descripció**: Permet rebre i enviar missatges a altres usuaris del sistema.
- **Funcionament**:
  - Enviar un missatge (una distribució o una llista de productes).
  - Veure els missitages rebuts, llegir el seu contigut i guardar-lo si es vol.

---

## Execució del Programa
1. **Inicialització**: En executar el programa, aquest neteja automàticament el directori `outputs` per garantir un entorn net.
2. **Autenticació**: Els usuaris han de registrar-se o iniciar sessió per accedir al menú principal.
3. **Navegació**: Seleccioneu opcions del menú principal per realitzar diverses accions relacionades amb llistes de productes i distribucions.

---

## Notes Addicionals
- Assegureu-vos que el directori `outputs` existeixi en la ruta esperada. Si no, el programa mostrarà un missatge indicant que no s'ha trobat el directori.
- Si teniu problemes amb les credencials, podeu tornar a registrar un usuari nou.
<<<<<<< HEAD
=======

---

## Casos d'Uso que es comproven
- Tots: [IndexJocsDeProva](https://repo.fib.upc.es/grau-prop/subgrup-prop21.1/-/tree/main/Entrega1/EXE/jocsDeProva?ref_type=heads).

---

## Classes que s'utilitzen
- Totes: [Classes](https://repo.fib.upc.es/grau-prop/subgrup-prop21.1/-/tree/main/Entrega1/FONTS/src/main/domain/classes?ref_type=heads).

>>>>>>> 50401a235880bbf4b8ba0696d41a5eeb3ed0d910
