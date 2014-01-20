package ch.hortis.mongodb.training.blog.web;

import ch.hortis.mongodb.training.blog.model.AuthorizationResponse;
import ch.hortis.mongodb.training.blog.oauth.SparklrUserApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.DefaultAuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.LinkedHashMap;
import java.util.Map;



//@Component
public class AccessConfirmationResource {

    @Autowired
	private ClientDetailsService clientDetailsService;

    @Autowired
	private SparklrUserApprovalHandler approvalStore;


    @Path("confirm_access")
    @GET
	public AuthorizationResponse getAccessConfirmation(@Context HttpServletRequest req, @Context javax.ws.rs.core.SecurityContext contextl) throws Exception {
        HttpSession session= req.getSession(true);
        AuthorizationRequest clientAuth = (AuthorizationRequest) session.getAttribute("authorizationRequest");
		ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
		Map<String, String> scopes = new LinkedHashMap<String, String>();
		/*
        for (String scope : clientAuth.getScope()) {
			scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
		}
		for (Approval approval : approvalStore.getApprovals(principal.getName(), client.getClientId())) {
			if (clientAuth.getScope().contains(approval.getScope())) {
				scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(),
						approval.getStatus() == ApprovalStatus.APPROVED ? "true" : "false");
			}
		}
		model.put("scopes", scopes);
		*/
		return new AuthorizationResponse(client, (DefaultAuthorizationRequest) clientAuth, "");
	}

	/*@Path("error")
    @GET
	public String handleError() throws Exception {
		// We can add more stuff to the model here for JSP rendering. If the client was a machine then
		// the JSON will already have been rendered.
		model.put("message", "There was a problem with the OAuth2 protocol");
		return "oauth_error";
	} */


}
