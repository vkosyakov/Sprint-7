import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultCreateCourierJSON {
    private int code;
    private String message;
    private boolean ok;
}
