package com.example.esperar_app.config.security.authorization;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.Operation;
import com.example.esperar_app.persistence.entity.security.Permission;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.repository.security.OperationRepository;
import com.example.esperar_app.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final OperationRepository operationRepository;
    private final UserService userService;

    @Autowired
    public CustomAuthorizationManager(
            OperationRepository operationRepository,
            UserService userService) {
        this.operationRepository = operationRepository;
        this.userService = userService;
    }

    @Override
    public AuthorizationDecision check(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext requestContext
    ) {
        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);
        System.out.println("PUBLIC ENDPOINT: " + isPublic);

        if(isPublic) return new AuthorizationDecision(true);

        boolean isGranted = isGranted(url, httpMethod, authentication.get());
        System.out.println("GRANTED: " + isGranted);

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {
        if(!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainOperations(authentication);

        return validateAccessEndpoints(url, httpMethod, operations);
    }

    private List<Operation> obtainOperations(Authentication authentication) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = (String) authToken.getPrincipal();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        return user.getRole().getPermissions().stream()
                .map(Permission::getOperation)
                .collect(Collectors.toList());
    }

    private boolean isPublic(String url, String httpMethod) {
        List<Operation> publicAccessEndpoints = operationRepository.findByIsPublic();

        return validateAccessEndpoints(url, httpMethod, publicAccessEndpoints);
    }

    private boolean validateAccessEndpoints(String url, String httpMethod, List<Operation> endpoints) {
        return endpoints.stream().anyMatch(
                operation -> {
                    String basePath = operation.getModule().getBasePath();
                    Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
                    Matcher matcher = pattern.matcher(url);

                    System.out.println("INICIO");
                    System.out.println("URL: " + url);
                    System.out.println("BASE PATH: " + basePath);
                    System.out.println("OPERATION PATH: " + operation.getPath());
                    System.out.println("MATCHER: " + matcher.matches());
                    System.out.println("HTTP METHOD: " + operation.getHttpMethod());
                    System.out.println("HTTP METHOD: " + httpMethod);
                    System.out.println("FIN");

                    return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
                }
        );
    }

    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath(); /* api/v1 */
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");
        return url;
    }
}
