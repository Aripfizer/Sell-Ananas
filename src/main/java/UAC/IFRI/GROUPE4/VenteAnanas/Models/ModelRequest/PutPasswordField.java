package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PutPasswordField
{
    @Nullable
    private String currentPassword;
    @Nullable
    private String newPassword;
}
