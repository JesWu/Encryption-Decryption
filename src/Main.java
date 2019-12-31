import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String operation = "enc";
        String text = "";
        int key = 0;
        File inFile = null;
        File outFile = null;
        BufferedReader fReader = null;
        BufferedWriter fWriter = null;
        Algorithm a = new Shift();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    operation = args[i++ + 1];
                    break;
                case "-data":
                    text = args[i++ + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i++ + 1]);
                    break;
                case "-in":
                    inFile = new File(args[i++ + 1]);
                    try {
                        fReader = new BufferedReader(new FileReader(inFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "-out":
                    outFile = new File(args[i++ + 1]);
                    try {
                        fWriter = new BufferedWriter(new FileWriter(outFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "-alg":
                    if (args[i++ + 1].equals("unicode")) a = new Unicode();
                    break;
                default:
                    break;
            }
        }

        //if there is text use it
        //else use freader if not null
        //else use empty string;
        if (text != "") {
            if (fWriter != null) {
                fWriter.write(a.encodeDecode(text, operation, key) + "\n");
                fWriter.close();
            } else {
                char[] charArr = text.toCharArray();
                System.out.println(a.encodeDecode(text, operation, key));
            }
        } else if (fReader != null) {
            if (fWriter != null) {
                while ((text = fReader.readLine()) != null) {
                    fWriter.write(a.encodeDecode(text, operation, key) + "\n");
                }
                fReader.close();
                fWriter.close();
            } else {
                while ((text = fReader.readLine()) != null) {
                    System.out.println(a.encodeDecode(text, operation, key));
                }
                fReader.close();
            }
        }
    }

    abstract static class Algorithm {
        public abstract String encodeDecode(String text, String operation, int key);
    }

    static class Shift extends Algorithm {
        @Override
        public String encodeDecode(String text, String operation, int key) {
            char[] charArr = text.toCharArray();
            switch (operation) {
                case "enc":
                    for (int i = 0; i < charArr.length; i++) {
                        if (charArr[i] >= 'a' && charArr[i] <= 'z') {
                            char temp = (char) (charArr[i] + key);
                            if (temp > 'z') temp = (char) (temp - 'z' + 'a' - 1);
                            charArr[i] = temp;
                        } else if (charArr[i] >= 'A' && charArr[i] <= 'Z') {
                            char temp = (char) (charArr[i] + key);
                            if (temp > 'Z') temp = (char) (temp - 'Z' + 'A' - 1);
                            charArr[i] = temp;
                        }
                    }
                    break;
                case "dec":
                    for (int i = 0; i < charArr.length; i++) {
                        if (charArr[i] >= 'a' && charArr[i] <= 'z') {
                            char temp = (char) (charArr[i] - key);
                            if (temp < 'a') temp = (char) ('z' - ('a' - temp) + 1);
                            charArr[i] = temp;
                        } else if (charArr[i] >= 'A' && charArr[i] <= 'Z') {
                            char temp = (char) (charArr[i] - key);
                            if (temp < 'A') temp = (char) ('Z' - ('A' - temp) + 1);
                            charArr[i] = temp;
                        }
                    }
                    break;
            }
            return new String(charArr);

        }
    }

    static class Unicode extends Algorithm {

        @Override
        public String encodeDecode(String text, String operation, int key) {
            char[] charArr = text.toCharArray();
            switch (operation) {
                case "enc":
                    for (int i = 0; i < charArr.length; i++) {
                        char temp = (char) (charArr[i] + key);
                        charArr[i] = temp;
                    }
                    break;
                case "dec":
                    for (int i = 0; i < charArr.length; i++) {
                        char temp = (char) (charArr[i] - key);
                        charArr[i] = temp;
                    }
                    break;
            }
            return new String(charArr);
        }
    }
}
