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
    private String userName;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    public UserResponse(String username, String lastName, String firtName, String email, String phoneNumber) {
        this.userName = username;
        this.lastName = lastName;
        this.firstName = firtName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
