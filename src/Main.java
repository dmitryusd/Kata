import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static boolean isRoman = false;

    public static TreeMap<Integer, String> mapArabToRoman = new TreeMap<Integer, String>();
    static {
        mapArabToRoman.put(100, "C");
        mapArabToRoman.put(90, "XC");
        mapArabToRoman.put(50, "L");
        mapArabToRoman.put(40, "XL");
        mapArabToRoman.put(10, "X");
        mapArabToRoman.put(9, "IX");
        mapArabToRoman.put(5, "V");
        mapArabToRoman.put(4, "IV");
        mapArabToRoman.put(1, "I");
    }

//    public static void main(String[] args) {
//        String calcInput = new Scanner(System.in).nextLine();
//        try {
//            System.out.println(calc(calcInput));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String calc(String input) throws Exception{
        Operation operation = getOperation(input).orElseThrow(IllegalArgumentException::new);
        int result = operation.performOperation();
        if (result <= 0 && isRoman) {
            throw new Exception("Result can't be negative in Romanian");
        }
        if (isRoman) {
            return arabicToRoman(result);
        }
        return String.valueOf(result);
    }

    //if any trouble - null, else - new Operation
    public static Optional<Operation> getOperation(String input) {
        String[] parts = input.split(" ");
        Operator operator;
        if (parts.length != 3) {
            return Optional.empty();
        }

        if (!isArabic(parts[0], parts[2]) && !isRoman(parts[0], parts[2])) {
            return Optional.empty();
        }

        operator = switch (parts[1]) {
            case "+" -> Operator.PLUS;
            case "-" -> Operator.MINUS;
            case "*" -> Operator.MULTIPLICATION;
            case "/" -> Operator.DIVISION;
            default -> Operator.UNDEFINED;
        };

        if (operator == Operator.UNDEFINED) {
            return Optional.empty();
        } else {
            if (isRoman(parts[0], parts[2])) {
                isRoman = true;
                return Optional.of(new Operation(romeToArabic(parts[0]), romeToArabic(parts[2]), operator));
            } else {
                return Optional.of(new Operation(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]), operator));
            }
        }
    }

    public static boolean isArabic(String operand1, String operand2) {
        Pattern pattern = Pattern.compile("\\d+");
        if (pattern.matcher(operand1).matches() && pattern.matcher(operand2).matches()) {
            if (Integer.parseInt(operand1) <= 10 && Integer.parseInt(operand2) <= 10
                    && Integer.parseInt(operand1) > 0 && Integer.parseInt(operand2) > 0) {
                return true;
            }
        }
        return false;
    }

    public static String arabicToRoman(int num) {
        int pos =  mapArabToRoman.floorKey(num);
        if (num == pos) {
            return mapArabToRoman.get(num);
        }
        return mapArabToRoman.get(pos) + arabicToRoman(num - pos);
    }

    public static int romeToArabic(String string) {
        List<Integer> list = new ArrayList<>();
        for (Character ch : string.toCharArray()) {
            switch (ch) {
                case 'I':
                    list.add(1);
                    break;
                case 'V':
                    list.add(5);
                    break;
                case 'X':
                    list.add(10);
                    break;
            }
        }
        if (list.size() >= 2) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i) < list.get(i + 1)) {
                    list.set(i + 1, list.get(i + 1) - list.get(i));
                    list.set(i, 0);
                }
            }
        }
        return list.stream().reduce(0, (l1, l2) -> l1 + l2);
    }

    public static boolean isRoman(String operand1, String operand2) {
        Pattern pattern = Pattern.compile("[^IVX]+");
        if (pattern.matcher(operand1).matches() || pattern.matcher(operand2).matches()) {
            return false;
        }
        if (romeToArabic(operand1) > 10 || romeToArabic(operand2) > 10) {
            return false;
        }
        return true;
    }

    static class Operation {
        int operand1;
        int operand2;
        Operator operator;

        public Operation(int operand1, int operand2, Operator operator) {
            this.operand1 = operand1;
            this.operand2 = operand2;
            this.operator = operator;
        }

        public int performOperation() {
            switch (operator) {
                case PLUS:
                    return operand1 + operand2;
                case MINUS:
                    return operand1 - operand2;
                case MULTIPLICATION:
                    return operand1 * operand2;
                //doesn't make sense 0 checking
                default:
                    return operand1 / operand2;
            }
        }
    }

    enum Operator {
        PLUS,
        MINUS,
        MULTIPLICATION,
        DIVISION,
        UNDEFINED
    }


}
