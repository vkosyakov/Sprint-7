import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

    private String login;
    private String password;
    private String firstname;

   public Courier(String login, String password){
        this.login = login;
        this.password = password;
    }


}
