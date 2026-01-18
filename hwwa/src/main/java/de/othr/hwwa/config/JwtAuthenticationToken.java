package de.othr.hwwa.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long companyId;

    public JwtAuthenticationToken(String email, Long companyId) {
        super(email, null, List.of());
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}