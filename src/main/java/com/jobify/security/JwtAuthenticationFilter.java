package com.jobify.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class.getName());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenHelper jwtTokenHelper;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        // 1. get token
        String requestToken = request.getHeader("Authorization");

        String username = null;

        String token = null;

        if (!isEmpty(requestToken) && requestToken.startsWith("Bearer"))
        {
            token = requestToken.substring(7);

            try
            {
                username = this.jwtTokenHelper.extractUsername(token);

            } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e)
            {
                LOGGER.error("{}", e.getLocalizedMessage());

            }

        } else
        {
            LOGGER.warn("{}", "JWT token doesn't starts with Bearer");
        }

        // once we got the token , now validate
        if (!isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.jwtTokenHelper.validateToken(token, userDetails))
            {
                // Now do the authentication
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } else
            {
                LOGGER.error("{}", "Invalid jwt token");
            }
        }
        filterChain.doFilter(request, response);
    }
}

