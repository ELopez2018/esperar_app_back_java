package com.example.esperar_app.controller.user;

import com.example.esperar_app.persistence.dto.user.CreateLegalPersonDto;
import com.example.esperar_app.persistence.dto.user.CreateNaturalPersonDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.user.UpdateUserDto;
import com.example.esperar_app.persistence.utils.ImageType;
import com.example.esperar_app.service.user.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.HEAD,
        RequestMethod.TRACE,
        RequestMethod.PATCH
})
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new natural person user
     * @param createNaturalPersonDto the user to be created
     * @return the created user
     */
    @PostMapping("sign-up/natural-person")
    public ResponseEntity<RegisteredUser> signUp(@RequestBody @Valid CreateNaturalPersonDto createNaturalPersonDto) {
        logger.info("Sign up natural person request received.");
        RegisteredUser newUser = userService.create(createNaturalPersonDto);
        return ResponseEntity.ok(newUser);
    }

    /**
     * Create a new legal person user
     * @param createLegalPersonDto the user to be created
     * @return the created user
     */
    @PostMapping("sign-up/legal-person")
    public ResponseEntity<RegisteredUser> createLegalPerson(
            @RequestBody @Valid CreateLegalPersonDto createLegalPersonDto) {
        logger.info("Sign up legal person request received.");
        RegisteredUser newUser = userService.createLegalPerson(createLegalPersonDto);
        return ResponseEntity.ok(newUser);
    }

    /**
     * Find all users paginated
     * @param pageable the pagination parameters
     * @return a page of users
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Page<GetUserDto>> findAll(Pageable pageable) {
        logger.info("Find all users request received.");
        Page<GetUserDto> usersPage = userService.findAll(pageable);
        return ResponseEntity.ok(usersPage != null ? usersPage : Page.empty());
    }

    /**
     * Find a user by its identifier
     * @param id the user identifier
     * @return the user
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetUserDto> findById(@PathVariable Long id) {
        logger.info("Find user with id: [" + id + "] request received.");
        GetUserDto getUserDto = userService.findById(id);
        return ResponseEntity.ok(getUserDto);
    }

    /**
     * Update a user by its identifier
     * @param id the user identifier
     * @param updateUserDto the user to be updated
     * @return the updated user
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetUserDto> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDto updateUserDto) {
        logger.info("Update user with id: [" + id + "] request received.");
        GetUserDto user = userService.update(id, updateUserDto);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete a user by its identifier
     * @param id the user identifier
     * @return no content
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete user with id: [" + id + "] request received.");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find all connected users
     * @return a list of connected users
     */
    @GetMapping("connectedUsers")
    public ResponseEntity<List<GetUserDto>> findConnectedUsers() {
        logger.info("Find all connected users request received.");
        List<GetUserDto> connectedUsers = userService.findConnectedUsers();
        return ResponseEntity.ok(connectedUsers != null ? connectedUsers : List.of());
    }

    @GetMapping("license-soon-to-expire")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Page<GetUserDto>> findDriversWithLicenseSoonToExpire(Pageable pageable) {
        logger.info("Find drivers with license soon to expire request received.");
        Page<GetUserDto> drivers = userService.findDriversWithLicenseSoonToExpire(pageable);
        return ResponseEntity.ok(drivers != null ? drivers : Page.empty());
    }

    @PostMapping("/single/upload/document")
    public ResponseEntity<String> uploadUserDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("imageType") ImageType imageType) {
        logger.info("File uploading request received.");
        logger.info("The file original name is: " + file.getOriginalFilename());
        logger.info("User id is: " + userId);

        boolean response = userService.uploadUserDocument(file, userId, imageType);

        if(!response) return ResponseEntity
                .badRequest()
                .body("An error occurred while uploading the file. Please try again.");

        return ResponseEntity.ok("Document successfully uploaded");
    }
}
