package az.jwtapp.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        try {


            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);

            if (token != null && jwtTokenProvider.validateToken(token)) {

                Authentication auth = jwtTokenProvider.getAuthentication(token);


                if (auth != null) {

                    SecurityContextHolder.getContext().setAuthentication(auth);


                }
            }

        } catch (ExpiredJwtException ex) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) req;

            String isRefreshToken = httpServletRequest.getHeader("isRefreshToken");
            String requestURL = httpServletRequest.getRequestURL().toString();
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, httpServletRequest);

            } else {
                req.setAttribute("exception", ex);
            }



        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println(exception.getStackTrace());

            req.setAttribute("exception", exception);
        }

        filterChain.doFilter(req, res);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        request.setAttribute("claims", ex.getClaims());

    }


}
