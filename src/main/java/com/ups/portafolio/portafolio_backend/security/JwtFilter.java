package com.ups.portafolio.portafolio_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verificamos si hay cabecera
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(" FILTRO: No hay token o no empieza por Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            userEmail = jwtUtil.extractUsername(jwt);
            System.out.println(" FILTRO: Usuario extraído del token: " + userEmail);
        } catch (Exception e) {
            System.out.println(" FILTRO: Error al extraer usuario: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 2. Imprimimos los roles que vienen de la Base de Datos
            System.out.println(" FILTRO: Roles en Base de Datos para " + userEmail + ": " + userDetails.getAuthorities());

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println(" FILTRO: Autenticación exitosa. Usuario puesto en contexto.");
            } else {
                System.out.println(" FILTRO: validateToken devolvió FALSE (Token inválido o expirado)");
            }
        }
        filterChain.doFilter(request, response);
    }

    
}