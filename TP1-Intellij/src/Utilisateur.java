import java.util.Objects;

public class Utilisateur {
    String username;
    String password;
    public Utilisateur(String username,String password) {
        this.username = username;
        this.password = password;
    }
    public boolean isSame(Utilisateur user){
        return Objects.equals(this.username, user.username) && Objects.equals(this.password , user.password);
    }
}
