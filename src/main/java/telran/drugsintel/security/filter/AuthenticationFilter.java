package telran.drugsintel.security.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.drugsintel.security.model.UserJWToken;
import telran.drugsintel.sign.dao.UserAccountRepository;
import telran.drugsintel.sign.model.UserAccount;

@Service
@AllArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	UserAccountRepository repository;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		addCorsToHeader(response);
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String token = request.getHeader("Authorization");
			String bearerToken;
			try {
				bearerToken = token.split(" ")[1];
			} catch (Exception e) {
				bearerToken = token;
			}
			UserJWToken jwt = new UserJWToken(bearerToken);
			if (!jwt.validateToken()) {
				response.sendError(401, "Token not valid");
				return;
			}
			UserAccount userAccount = repository.findById(jwt.getUserId()).orElse(null);
			if (userAccount == null) {
				response.sendError(409, "User not valid");
				return;
			}
			//Add Principal to request
			request = new WrappedRequest(request, userAccount.getId());
		}
	
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String path) {
		return "GET".equalsIgnoreCase(method) && path.matches("/dashboard/intro/?") ||
		       "GET".equalsIgnoreCase(method) && path.matches("/get/?") ||
			   "PUT".equalsIgnoreCase(method) && path.matches("/update/?") ||
			   "PUT".equalsIgnoreCase(method) && path.matches("/password/?") ||
			   "DELETE".equalsIgnoreCase(method) && path.matches("/user/\\w+/?");
	}

	private void addCorsToHeader(HttpServletResponse resp) {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Password, Content-Type, Accept, Authorization");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
	}
	
	private class WrappedRequest extends HttpServletRequestWrapper {
		String userId;

		public WrappedRequest(HttpServletRequest request, String userId) {
			super(request);
			this.userId = userId;
		}
		
		@Override
		public Principal getUserPrincipal() {
			return () -> userId;
		}
		
	}
}
