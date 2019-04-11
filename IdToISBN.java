import java.util.Arrays;
import java.util.regex.Pattern;

public class IdToISBN {
  public static void main(String[] args) {
    switch (args[0]) {
      case "run":
        run(Arrays.copyOfRange(args, 1, args.length));
        break;
      case "test":
        test();
        break;
      default:
        System.out.println("Unrecognized command. Try 'javac IdToISBN run id*' or 'javac IdToISBN test'.");
    }
  }

  private static void run(String[] maybeIds) {
    for (String maybeId : maybeIds) {
      String idRegex = "^\\d{12}$";
      if (Pattern.matches(idRegex, maybeId)) {
        System.out.println(
          String.format("id = %s, ISBN = %s", maybeId, transformIdToISBN(maybeId))
        );
      } else {
        System.out.println(
          String.format("id = %s, Unrecognized id format", maybeId)
        );
      }
    }
  }

  private static void test() {
    String[][] samples = {
      //Provided Samples
      {"978155192370", "155192370x"},
      {"978140007917", "1400079179"},
      {"978037541457", "0375414576"},
      {"978037428158", "0374281580"},
      //Personal Samples
      {"978013110362", "0131103628"}, //ISBN of C Programming Language
      {"978026251087", "0262510871"}, //ISBN of Structure and Interpretation of Computer Programs
      {"978020163361", "0201633612"}, //ISBN of Design Patterns
      {"978032172133", "0321721330"}  //ISBN of POODR
    };
    for (String[] sample : samples) {
      String id = sample[0];
      String expectedISBN = sample[1];
      String computedISBN = transformIdToISBN(id);
      if (expectedISBN.equals(computedISBN)) {
        System.out.println(
          String.format("Pass: id = %s, ISBN = %s", id, computedISBN)
        );
      } else {
        System.out.println(
          String.format("Fail: id = %s, expected ISBN = %s, computed ISBN = %s", id, expectedISBN, computedISBN)
        );
      }
    }
  }

  private static String transformIdToISBN(String id) {
    String truncatedID = id.substring(3);
    char errorControlDigit = generateErrorControlDigit(truncatedID);
    return truncatedID + errorControlDigit;
  }

  private static char generateErrorControlDigit(String truncatedID)  {
    int remainder = calculateWeightedSum(truncatedID) % 11;
    int errorControlDigit = remainder == 0 ? 0 : 11 - remainder;
    return errorControlDigit == 10 ? 'x' : Character.forDigit(errorControlDigit, 10);
  }

  private static int calculateWeightedSum(String truncatedID) {
    int weight = 10;
    int weightedSum = 0;
    for (char c : truncatedID.toCharArray()) {
      int i = Character.getNumericValue(c);
      weightedSum += i * weight;
      weight--;
    }
    return weightedSum;
  }
}
