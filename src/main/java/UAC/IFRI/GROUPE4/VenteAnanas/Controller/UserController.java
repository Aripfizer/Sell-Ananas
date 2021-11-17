package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.AppException;
import UAC.IFRI.GROUPE4.VenteAnanas.Exception.BadRequestException;
import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest.PutPasswordField;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.UserResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Role;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.RoleName;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.User;
import UAC.IFRI.GROUPE4.VenteAnanas.Payload.ApiResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Payload.SignUpRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.RoleRepository;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.UserRepository;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.CurrentUser;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('CLIENT')")
    public UserResponse getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserResponse userSummary = new UserResponse(currentUser.getId(),currentUser.getUsername(), currentUser.getLastName(), currentUser.getFirstName(), currentUser.getEmail(),currentUser.getPhoneNumber());
        return userSummary;
    }


    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserResponse userProfile = new UserResponse(user.getId(),user.getUsername(), user.getLastName(), user.getFirstName(), user.getEmail(),user.getPhoneNumber());

        return userProfile;
    }

    @PutMapping("/user/me")
    @PreAuthorize("hasRole('CLIENT')")
    public UserResponse PutCurrentUser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody SignUpRequest user)
    {
        if(passwordEncoder.matches(user.getPassword(), currentUser.getPassword()))
        {
           User userSummary =  userRepository.findByEmail(currentUser.getEmail()).get();

           userSummary.setEmail(user.getEmail());
           userSummary.setFirstName(user.getFirstName());
           userSummary.setLastName(user.getLastName());
           userSummary.setPhoneNumber(user.getPhoneNumber());

            userSummary = userRepository.save(userSummary);

            UserResponse userResponse = new UserResponse();

            userResponse.setFirtName(userSummary.getFirstName());
            userResponse.setEmail(userSummary.getEmail());
            userResponse.setLastName(userSummary.getLastName());
            userResponse.setPhoneNumber(userSummary.getPhoneNumber());
            userResponse.setUsername(userSummary.getUsername());

            return  userResponse;
        }else
        {
            throw new BadRequestException("Password Incorrect");
        }

    }


    @PutMapping("/user/me/password")
    @PreAuthorize("hasRole('CLIENT')")
    public UserResponse PutCurrentUserPassword(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody PutPasswordField putPasswordField)
    {

        if(passwordEncoder.matches(putPasswordField.getCurrentPassword(), currentUser.getPassword()))
        {
            User userSummary =  userRepository.findByEmail(currentUser.getEmail()).get();

            userSummary.setPassword(passwordEncoder.encode(putPasswordField.getNewPassword()));

            userSummary = userRepository.save(userSummary);

            UserResponse userResponse = new UserResponse();

            userResponse.setFirtName(userSummary.getFirstName());
            userResponse.setEmail(userSummary.getEmail());
            userResponse.setLastName(userSummary.getLastName());
            userResponse.setPhoneNumber(userSummary.getPhoneNumber());
            userResponse.setUsername(userSummary.getUsername());

            return  userResponse;

        }else
        {
            throw new BadRequestException("Password Incorrect");
        }
    }

    @PutMapping("/user/{email}/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> AddRole(@CurrentUser UserPrincipal currentUser, @PathVariable String email, @PathVariable RoleName role)
    {
        Optional<User> userA = userRepository.findByEmail(email);
        if(userA.isPresent())
        {
            User user = userA.get();
            boolean trouver  = false;
            for(Role currentRole : user.getRoles())
            {
                if(currentRole.getName().equals(role))
                {
                    trouver = true;
                }
            }
            if(trouver)
            {
                throw new BadRequestException("No Match Role Or The User have : " + role);
            }else
            {
                Role userRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new AppException("User Role not set."));

                user.getRoles().add(userRole);
                User result = userRepository.save(user);
                return new ResponseEntity(new ApiResponse(true, "The : " + role + " is add"), HttpStatus.OK);
            }
        }else
        {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

}
