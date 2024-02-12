import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Cuerdas {
  static final BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

  public static void main(String[] args) {
    System.out.println("Creado por Samuel Marroquín (GitHub: pastelcode)\n");

    System.out.println("Instrucciones:");
    System.out.println("- Ingresa solo cuerdas formadas por letras o dígitos, ejemplos: \"abc\", \"123\", \"a1B2c3\"");
    System.out.println("GRACIAS\n");

    final String string = readValidString("Ingresa tu cuerda");
    System.out.println();

    final ProcessedString processedString = new ProcessedString(string);
    System.out.printf("%sw = %s%s\n", Colors.GREEN, string, Colors.RESET);
    System.out.printf("Prefijos = %s\n", formatArrayAsEnumeration(processedString.getPrefixes()));
    System.out.printf("Sufijos = %s\n", formatArrayAsEnumeration(processedString.getSuffixes()));
    System.out.printf("Subcuerdas = %s\n", formatArrayAsEnumeration(processedString.getSubstrings()));
    System.out.printf("Subsecuencias = %s\n", formatArrayAsEnumeration(processedString.getSubsequences()));
  }

  static String readValidString(String inputMessage) {
    final Pattern stringRegex = Pattern.compile("(\\w)+");
    while (true) {
      try {
        System.out.printf("%s: ", inputMessage);
        final String string = keyboardReader.readLine();
        if (!stringRegex.matcher(string).matches()) {
          throw new InvalidStringException();
        }
        return string;
      } catch (InvalidStringException e) {
        System.err.printf("%sIngresa una cuerda válida POR FAVOR GRACIAS%s\n\n", Colors.RED, Colors.RESET);
      } catch (Exception e) {
        System.err.printf("%sOcurrió un error al leer tu cuerda. Por favor corre de nuevo el programa.%s\n", Colors.RED, Colors.RESET);
        System.exit(1);
      }
    }
  }

  static String formatArrayAsEnumeration(String[] array) {
    String arrayRepresentation = Arrays.toString(array);
    arrayRepresentation = arrayRepresentation.replace("[", "{");
    arrayRepresentation = arrayRepresentation.replace("]", "}");
    final String string = String.format("%s, #%d", arrayRepresentation, array.length);
    return string;
  }
}

class InvalidStringException extends Exception {}

enum Colors {
  RED("\u001B[31m"),
  YELLOW("\u001B[33m"),
  GREEN("\u001B[32m"),
  RESET("\u001B[0m");

  private String representation;

  Colors(String representation) {
    this.representation = representation;
  }

  @Override
  public String toString() {
    return representation;
  }
}

class ProcessedString {
  final String string;

  ProcessedString(String string) {
    this.string = string;
  }

  private String getNullCase() {
    return null;
  }
  
  String[] getPrefixes() {
    String[] prefixes = new String[string.length() + 1];
    for (int numberOfCharactersToRemove = 0; numberOfCharactersToRemove <= string.length(); numberOfCharactersToRemove++) {
      final int index = numberOfCharactersToRemove;
      String substring = string.substring(0, string.length() - numberOfCharactersToRemove);
      if (substring.isEmpty()) {
        substring = getNullCase();
      }
      prefixes[index] = substring;
    }
    return prefixes;
  }

  String[] getSuffixes() {
    String[] suffixes = new String[string.length() + 1];
    for (int index = 0; index <= string.length(); index++) {
      String substring = string.substring(index);
      if (substring.isEmpty()) {
        substring = getNullCase();
      }
      suffixes[index] = substring;
    }
    return suffixes;
  }

  String[] getSubstrings() {
    final int substringsCount = ((string.length() * (string.length() + 1)) / 2) + 1;
    String[] substrings = new String[substringsCount];

    int indexOfSubstringToInsert = 0;
    for (int numberOfCharactersToRemove = 0; numberOfCharactersToRemove < string.length(); numberOfCharactersToRemove++) {
      final int numberOfStringsWithTheSameNumberOfCharactersRemoved = numberOfCharactersToRemove + 1;
      int numberOfCharactersToRemoveAtTheBeginning = 0;
      int numberOfCharactersToRemoveAtTheEnd = numberOfCharactersToRemove;

      for (int i = 0; i < numberOfStringsWithTheSameNumberOfCharactersRemoved; i++) {
        substrings[indexOfSubstringToInsert] = string.substring(numberOfCharactersToRemoveAtTheBeginning,
            string.length() - numberOfCharactersToRemoveAtTheEnd);
        indexOfSubstringToInsert += 1;
        numberOfCharactersToRemoveAtTheBeginning += 1;
        numberOfCharactersToRemoveAtTheEnd -= 1;
      }
    }
    return substrings;
  }

  String[] getSubsequences() {
    // This solution is based in the fact subsequences is a power set and power set has a binary solution method.
    final int subsequencesLength = (int) Math.pow(2, string.length());
    String[] subsequences = new String[subsequencesLength];
    for (int index = 0; index < subsequencesLength; index++) {
      // Pad binary number with `0` at the left.
      final String binaryNumber = String.format("%" + string.length() + "s", Integer.toBinaryString(index)).replace(' ', '0');
      String newString = "";
      for (int characterIndex = 0; characterIndex < string.length(); characterIndex++) {
        final char binaryDigit = binaryNumber.charAt(characterIndex);
        if (binaryDigit == '1') {
          newString += string.charAt(characterIndex);
        }
      }
      subsequences[index] = newString.isBlank() ? getNullCase() : newString;
    }
    return subsequences;
  }
}
