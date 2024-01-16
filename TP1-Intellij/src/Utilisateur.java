import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class Utilisateur {
    String username;
    String password;

    public Utilisateur(String username,String password) {
        this.username = username;
        this.password = password;
    }
    public boolean isSame(Utilisateur user){
        return Objects.equals(this.username, user.username) && Objects.equals(this.password, user.password);
    }
    //returns a list of users, this over a variable cuz it can't be changed
    public static Utilisateur[] listOfUsers(){
        return new Utilisateur[]{
                new Utilisateur("latif","123"),
                new Utilisateur("user","user"),
                new Utilisateur("test","test"),
        };
    }
    public static boolean isValidUser(Utilisateur user){
        boolean temp = Arrays.stream(listOfUsers())
                .anyMatch(u -> u.isSame(user));
        System.out.println(temp);
         return temp;
    }
}
