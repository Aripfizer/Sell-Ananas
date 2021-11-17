package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse
{
    private Long id;
    private String username;
    private String lastName;
    private String firtName;
    private String email;
    private String phoneNumber;

    public UserResponse(String username, String lastName, String firtName, String email, String phoneNumber) {
        this.username = username;
        this.lastName = lastName;
        this.firtName = firtName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
