package com.hello.post00.repository;


import com.hello.post00.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>
{

    Optional<Member> findByUsername(String username);
}
