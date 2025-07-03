package jdbc;

import java.util.List;
import java.util.Random;

public class TestApplication {
    public static void main(String[] args) {
        SimpleJDBCRepository repository = new SimpleJDBCRepository();

        // Generate and insert random users
        System.out.println("Creating Random Users...");
        for (int i = 0; i < 5; i++) {
            User user = generateRandomUser();
            Long id = repository.createUser(user);
            System.out.println("Created User ID: " + id);
        }

        // Find All Users
        System.out.println("\nFetching All Users:");
        List<User> allUsers = repository.findAllUser();
        allUsers.stream().map(User::getLastName).forEach(System.out::println);

        // Find User by ID (First User)
        if (!allUsers.isEmpty()) {
            Long firstUserId = allUsers.get(0).getId();
            User foundById = repository.findUserById(firstUserId);
            System.out.println("\nFound by ID (" + firstUserId + "): " + foundById.getLastName());
        }

        // Find User by First Name (Using First User)
        if (!allUsers.isEmpty()) {
            String name = allUsers.get(0).getFirstName();
            User foundByName = repository.findUserByName(name);
            System.out.println("\nFound by First Name (" + name + "): " + foundByName.getLastName());
        }

        // Update a User (First User)
        if (!allUsers.isEmpty()) {
            User userToUpdate = allUsers.get(0);
            userToUpdate.setFirstName("UpdatedName");
            userToUpdate.setLastName("UpdatedLast");
            userToUpdate.setAge(99);

            repository.updateUser(userToUpdate);
            User updated = repository.findUserById(userToUpdate.getId());
            System.out.println("\nUpdated User: " + updated.getLastName());
        }

        //⃣ Delete a User (Last User)
        if (!allUsers.isEmpty()) {
            Long lastUserId = allUsers.get(allUsers.size() - 1).getId();
            repository.deleteUser(lastUserId);
            System.out.println("\nDeleted User with ID: " + lastUserId);
        }

        // Final check - all users after deletion
        System.out.println("\nRemaining Users:");
        repository.findAllUser().stream().map(User::getLastName).forEach(System.out::println);
    }

    private static User generateRandomUser() {
        Random random = new Random();
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Eve"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones"};

        User user = new User();
        user.setFirstName(firstNames[random.nextInt(firstNames.length)]);
        user.setLastName(lastNames[random.nextInt(lastNames.length)]);
        user.setAge(18 + random.nextInt(50)); // Age between 18 and 68

        return user;
    }
}
