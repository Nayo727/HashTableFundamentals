import java.util.*;

public class Q1 {
    private Map<String, Integer> userMap = new HashMap<>();
    private Map<String, Integer> attemptFrequency = new HashMap<>();
    public void registerUser(String username, int userId) {
        userMap.put(username, userId);
    }
    public boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
        return !userMap.containsKey(username);
    }
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        if (userMap.containsKey(username)) {
            suggestions.add(username + "1");
            suggestions.add(username + "2");
            suggestions.add(username.replace("_", "."));
        }
        return suggestions;
    }
    public String getMostAttempted() {
        String mostAttempted = null;
        int maxAttempts = 0;
        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }
        return mostAttempted + " (" + maxAttempts + " attempts)";
    }
    public static void main(String[] args) {
        Q1 checker = new Q1();
        checker.registerUser("john_doe", 1);
        checker.registerUser("jane_smith", 2);
        System.out.println("checkAvailability(\"john_doe\") → " + checker.checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") → " + checker.checkAvailability("jane_smith"));
        System.out.println("checkAvailability(\"new_user\") → " + checker.checkAvailability("new_user"));
        System.out.println("suggestAlternatives(\"john_doe\") → " + checker.suggestAlternatives("john_doe"));
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        System.out.println("getMostAttempted() → " + checker.getMostAttempted());
    }
}
