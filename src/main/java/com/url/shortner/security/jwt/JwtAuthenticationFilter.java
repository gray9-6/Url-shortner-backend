package com.url.shortner.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            /*  To authenticate any request,there are series of steps which we need to follow
                    1. Get JWT From Header (because with the jwt token we can know, user is valid user or not)
                    2. Validate Token
                    3. If Token is Valid :- Get UserDetails
                       3.1 get the username  -> load user -> set the auth context
             */

            String jwt = jwtUtils.getJwtFromHeader(request);

            if(jwt!= null && jwtUtils.validateToken(jwt)){
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(userDetails != null){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request,response);
    }
}

/*  Note :-
         1. Every request have to pass through this filter once.
         2. This filter is responsible to validating the jwt token from the request and to load the user details.
         3. But spring security doesn't know when to execute this filter in the filter chain
         4. we will define this in spring security config.
         5. By default, spring security doesn't automatically include your custom filters (i.e JwtAuthenticationFilter)
            in the filter chain  unless you explicitly add it.
         6.


 */
