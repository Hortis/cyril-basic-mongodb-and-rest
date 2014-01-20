package ch.hortis.mongodb.training.blog.model;


import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.DefaultAuthorizationRequest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthorizationResponse {

    private ClientDetails client;
    private DefaultAuthorizationRequest authRequest;
    private String scope;

    public AuthorizationResponse() {
    }


    public AuthorizationResponse(ClientDetails client, DefaultAuthorizationRequest authRequest, String scope) {
        this.client = client;
        this.authRequest = authRequest;
        this.scope = scope;
    }

    public ClientDetails getClient() {
        return client;
    }

    public void setClient(ClientDetails client) {
        this.client = client;
    }

    public DefaultAuthorizationRequest getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(DefaultAuthorizationRequest authRequest) {
        this.authRequest = authRequest;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
