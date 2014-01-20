package ch.hortis.mongodb.training.blog.web;


import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Path("oauth/token")
@FrameworkEndpoint
@Component
public class TokenResource  {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WebResponseExceptionTranslator providerExceptionHandler = new DefaultWebResponseExceptionTranslator();

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationRequestManager authorizationRequestManager;

    @Autowired
    private AuthorizationRequestManager defaultAuthorizationRequestManager;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Assert.state(tokenGranter != null, "TokenGranter must be provided");
        Assert.state(clientDetailsService != null, "ClientDetailsService must be provided");
        defaultAuthorizationRequestManager = new DefaultAuthorizationRequestManager(clientDetailsService);
        if (authorizationRequestManager == null) {
            authorizationRequestManager = defaultAuthorizationRequestManager;
        }
    }


    @POST
    @Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAccessToken(@Context javax.ws.rs.core.SecurityContext securityContext,@FormParam("grant_type") String grantType, @Context HttpServletRequest httpServletRequest) {
        Map<String, String> parameters = toMap(httpServletRequest.getParameterMap());


        final Principal principal = securityContext.getUserPrincipal();

        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }

        HashMap<String, String> request = new HashMap<String, String>(parameters);
        String clientId = getClientId(principal);
        if (clientId != null) {
            request.put("client_id", clientId);
        }

        if (!StringUtils.hasText(grantType)) {
            throw new InvalidRequestException("Missing grant type");
        }

        authorizationRequestManager.validateParameters(parameters,
                clientDetailsService.loadClientByClientId(clientId));

        DefaultAuthorizationRequest authorizationRequest = new DefaultAuthorizationRequest(
                authorizationRequestManager.createAuthorizationRequest(request));
        if (isAuthCodeRequest(parameters) || isRefreshTokenRequest(parameters)) {
            // The scope was requested or determined during the authorization step
            if (!authorizationRequest.getScope().isEmpty()) {
                logger.debug("Clearing scope of incoming auth code request");
                authorizationRequest.setScope(Collections.<String> emptySet());
            }
        }
        if (isRefreshTokenRequest(parameters)) {
            // A refresh token has its own default scopes, so we should ignore any added by the factory here.
            authorizationRequest.setScope(OAuth2Utils.parseParameterList(parameters.get("scope")));
        }
        OAuth2AccessToken token = tokenGranter.grant(grantType, authorizationRequest);
        if (token == null) {
            throw new UnsupportedGrantTypeException("Unsupported grant type: " + grantType);
        }

        return  Response.status(Response.Status.OK).entity(token).build();
    }

    private Map<String, String> toMap(Map<String, String[]> queryParameters) {
        Map<String, String> resultMap = new HashMap<String, String>();
        for(String key : queryParameters.keySet()) {
              resultMap.put(key, queryParameters.get(key)[0]);
        }


        return resultMap;
    }

    /**
     * @param principal the currently authentication principal
     * @return a client id if there is one in the principal
     */
    protected String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        }
        String clientId = client.getName();
        if (client instanceof OAuth2Authentication) {
            // Might be a client and user combined authentication
            clientId = ((OAuth2Authentication) client).getAuthorizationRequest().getClientId();
        }
        return clientId;
    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        return providerExceptionHandler.translate(new BadClientCredentialsException());
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        return providerExceptionHandler.translate(e);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }


}
