package com.project.jeu421.security;

import com.project.jeu421.dao.PlayerDAO;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    PlayerDAO playerDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PlayerEntity player = playerDAO.findByUsername(username);
        if (player == null) {
            throw new UsernameNotFoundException("Player not found");
        }
        return new CustomUserDetails(player);
    }
}
