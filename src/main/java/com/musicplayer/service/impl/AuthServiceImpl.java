package com.musicplayer.service.impl;

import com.musicplayer.model.User;
import com.musicplayer.mapper.UserMapper;
import com.musicplayer.security.JwtTokenProvider;
import com.musicplayer.service.AuthService;
import com.musicplayer.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String register(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new UserException("Username already exists");
        }

        // 检查邮箱是否已存在
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new UserException("Email already exists");
        }

        // 设置默认角色
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        userMapper.insert(user);

        // 生成token
        return jwtTokenProvider.generateToken(user.getId().toString());
    }

    @Override
    public String login(String username, String password) {
        try {
            // 使用Spring Security的认证管理器进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // 设置认证信息到SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 获取用户信息
            User user = userMapper.findByUsername(username);
            if (user == null) {
                throw new UserException("User not found");
            }

            // 生成token
            return jwtTokenProvider.generateToken(user.getId().toString());
        } catch (Exception e) {
            throw new UserException("Invalid username or password");
        }
    }

    @Override
    public void logout(String token) {
        // 清除SecurityContext
        SecurityContextHolder.clearContext();
        // 这里可以添加token黑名单逻辑，如果需要的话
    }
} 