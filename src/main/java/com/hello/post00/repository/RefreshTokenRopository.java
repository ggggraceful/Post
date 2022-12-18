package com.hello.post00.repository;

import com.hello.post00.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRopository extends JpaRepository<RefreshToken, Long> {

}
