/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hortis.mongodb.training.blog.oauth;

import ch.hortis.mongodb.training.blog.model.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Collection;


@Component
@Path("oauth")
public class AdminResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private ConsumerTokenServices tokenServices;

    @Autowired
	private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;


    public AdminResource() {
        logger.info("create PostResourceImpl");
    }


	@Path("users/{user}/tokens")
    @GET
	public Collection<OAuth2AccessToken> listTokensForUser(@PathParam("user") String user, @Context javax.ws.rs.core.SecurityContext context)
			throws Exception {
		checkResourceOwner(user, context.getUserPrincipal());
		return tokenStore.findTokensByUserName(user);
	}

	@Path("users/{user}/tokens/{token}")
    @DELETE
	public Response revokeToken(@PathParam("user") String user, @PathParam("token") String token, Principal principal)
			throws Exception {
		checkResourceOwner(user, principal);
		if (tokenServices.revokeToken(token)) {
            return Response.noContent().build();
		} else {
            return Response.status(404).build();
		}
	}

    @GET
	@Path("/oauth/clients/{client}/tokens")
	public Collection<OAuth2AccessToken> listTokensForClient(@PathParam("client") String client) throws Exception {
		return tokenStore.findTokensByClientId(client);
	}

	private void checkResourceOwner(String user, Principal principal) {
		if (principal instanceof OAuth2Authentication) {
			OAuth2Authentication authentication = (OAuth2Authentication) principal;
			if (!authentication.isClientOnly() && !user.equals(principal.getName())) {
				throw new AccessDeniedException(String.format("User '%s' cannot obtain tokens for user '%s'",
						principal.getName(), user));
			}
		}
	}


    @Path("confirm_access")
    @GET
    public AuthorizationResponse getAccessConfirmation(@Context HttpServletRequest req, @Context javax.ws.rs.core.SecurityContext contextl) throws Exception {
        HttpSession session= req.getSession(true);
        AuthorizationRequest clientAuth = (AuthorizationRequest) session.getAttribute("authorizationRequest");
        ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
        return new AuthorizationResponse(client, (DefaultAuthorizationRequest) clientAuth, "");
    }


}
