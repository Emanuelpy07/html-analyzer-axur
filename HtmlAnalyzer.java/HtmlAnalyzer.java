import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Stack;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("URL connection error");
            return;
        }

        try {
            String result = analyze(args[0]);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("URL connection error");
        }
    }

    private static String analyze(String urlAddress) throws Exception {
        URL url = new URL(urlAddress);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream()));

        Stack<String> stack = new Stack<>();

        String line;
        int depth = 0;
        int maxDepth = -1;
        String deepestText = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            // Tag de abertura
            if (line.matches("<[^/].*>")) {
                String tag = line.substring(1, line.length() - 1);
                stack.push(tag);
                depth++;

            }
            // Tag de fechamento
            else if (line.matches("</.*>")) {
                String tag = line.substring(2, line.length() - 1);

                if (stack.isEmpty() || !stack.peek().equals(tag)) {
                    reader.close();
                    return "malformed HTML";
                }

                stack.pop();
                depth--;
            }
            // Texto
            else {
                if (depth > maxDepth) {
                    maxDepth = depth;
                    deepestText = line;
                }
            }
        }

        reader.close();

        if (!stack.isEmpty()) {
            return "malformed HTML";
        }

        return deepestText != null ? deepestText : "";
    }
}