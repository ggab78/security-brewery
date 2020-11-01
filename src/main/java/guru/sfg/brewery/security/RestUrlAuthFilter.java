package guru.sfg.brewery.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RestUrlAuthFilter extends RestHeaderAuthFilter {

    public RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String userName = Optional.ofNullable(httpServletRequest.getParameter("Api-User")).orElse("");
        String password = Optional.ofNullable(httpServletRequest.getParameter("Api-Password")).orElse("");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName,password);

        if(!StringUtils.isEmpty(userName)) {
            return this.getAuthenticationManager().authenticate(token);
        }
        return null;
    }

}
