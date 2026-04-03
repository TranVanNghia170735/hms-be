package com.hms.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private static final String SECRET = "79fc72a244c391a9ba7efa28137da35d0895fd4795982863d22a21af6099365669e0a2668840a86ca3a9f07ee6c6c92dbf2b0651e5b97b52491ffee356d4dcba";

    public TokenFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain)-> {
            String path = exchange.getRequest().getPath().toString();
            if(path.equals("/user/login") || path.equals("/user/register")){
                return chain.filter(exchange.mutate().request(r->
                        r.header("X-Secret-Key", "SECRET")).build());
            }
            HttpHeaders header = exchange.getRequest().getHeaders();
//            if(!header.containsKey(HttpHeaders.AUTHORIZATION)){
            String auth = header.getFirst(HttpHeaders.AUTHORIZATION);
            if (auth == null || auth.isEmpty()) {
                throw new RuntimeException("Authorization header is missing");
            }
            String authHeader = header.getFirst(HttpHeaders.AUTHORIZATION);
            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                throw  new RuntimeException("Authorization header is invalid");
            }
            String token = authHeader.substring(7);
            try{
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token)
                        .getBody();
                exchange = exchange.mutate().request(r ->
                        r.header("X-Secret-Key", "SECRET")).build();

            }catch (Exception e) {
                throw new RuntimeException("Token is invalid.");
            }
            return chain.filter(exchange);
        };

    }

    public static class Config {

    }
}
