package io.proximax.kyc.web.rest;

import com.codahale.metrics.annotation.Timed;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.repository.UserRepository;
import io.proximax.kyc.security.SecurityUtils;
// import io.proximax.kyc.service.mail.mailService;
import io.proximax.kyc.service.mail.MailService;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.dto.UserDTO;
import io.proximax.kyc.web.rest.errors.*;
import io.proximax.kyc.web.rest.vm.KeyAndPasswordVM;
import io.proximax.kyc.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import io.proximax.kyc.service.dto.PasswordChangeDTO;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;
    private final ApplicationProperties applicationProperties;
    
    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, OrganizationService organizationService,
                           ApplicationProperties applicationProperties) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
    }
    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     */
    @PostMapping("/register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword(),  false);
        mailService.sendActivationEmail(user);
    }

    @PostMapping("/api-register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> registerAccountViaAPI(@Valid @RequestBody ManagedUserVM managedUserVM, @RequestParam(value = "apiKey", required = true) String apiKey) {
        if (applicationProperties.getSecurity().getKey().equals(apiKey)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword(), true);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        } else {
            mailService.sendActivationEmailSuccess(user.get());
        }
    }

    @GetMapping("/email-exist")
    @Timed
    public boolean emailExist(@RequestParam(value = "email") String email) {
        Optional<User> user = userService.emailExist(email);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("Email does not Exist");
        }
        return true;
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws RuntimeException 500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    @Timed
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
   }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDto current and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Timed
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
   }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @throws EmailNotFoundException 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset-password/init")
    @Timed
    public void requestPasswordReset(@RequestBody String mail) {
        mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Timed
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }

        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
        // TODO: removed private key : check if working
//        else {
//            String organizationId = user.get().getOrganizationId();
//            log.debug("finishPasswordReset organizationId: {}",organizationId);
//            if ((organizationId !=null) && (!organizationId.isEmpty())) {
//                Organization organization = organizationService.findOne(user.get().getOrganizationId()).get();
//                String privateKey = CryptoUtility.encryptUsingToken(CryptoUtility.getPasswordFromToken(organization.getPrivateKey()), CryptoUtility.generatePasswordToken(keyAndPassword.getNewPassword()));
//                user.get().setPrivateKey(privateKey);
//                userRepository.save(user.get());
//            }else {
//                user.get().setPrivateKey(CryptoUtility.generatePrivateKey(keyAndPassword.getNewPassword()));
//                userRepository.save(user.get());
//            }
//        }

    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
