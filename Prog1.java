import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

class Prog1 {

    static class InputData {
        Keys keys;
        Map<String, Root> roots;

        static class Keys {
            int n;
            int k;
        }

        static class Root {
            String base;
            String value;
        }
    }

    public static void main(String[] args) {
        // Replace with your JSON file path
        String jsonFilePath = "input.json"; 
        
        // Step 1: Read the Test Case (Input)
        InputData inputData = readInput(jsonFilePath);
        if (inputData == null) return;

        // Step 2: Decode the Y Values
        List<Point> points = decodeYValues(inputData.roots);

        // Step 3: Find the Secret (C)
        if (points.size() < inputData.keys.k) {
            System.out.println("Not enough points to find the secret.");
            return;
        }
        double c = calculateConstantTerm(points);
        System.out.println("Constant term (c): " + c);
    }

    private static InputData readInput(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, InputData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Point> decodeYValues(Map<String, InputData.Root> roots) {
        List<Point> points = new ArrayList<>();
        for (Map.Entry<String, InputData.Root> entry : roots.entrySet()) {
            String xStr = entry.getKey();
            Root root = entry.getValue();
            int x = Integer.parseInt(xStr);
            int y = decodeValue(root.base, root.value);
            points.add(new Point(x, y));
        }
        return points;
    }

    private static int decodeValue(String baseStr, String value) {
        int base = Integer.parseInt(baseStr);
        return Integer.parseInt(value, base);
    }

    private static double calculateConstantTerm(List<Point> points) {
        int n = points.size();
        double sum = 0;

        for (Point point : points) {
            double x_i = point.x;
            double y_i = point.y;

            double term = y_i;
            for (Point otherPoint : points) {
                if (otherPoint.x != x_i) {
                    term *= (0 - otherPoint.x) / (x_i - otherPoint.x);
                }
            }
            sum += term;
        }
        return sum;
    }

    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

