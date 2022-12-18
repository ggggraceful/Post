package com.hello.post00.repository;

import com.hello.post00.entity.Member;
import com.hello.post00.entity.Post;
import com.hello.post00.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

	Optional<PostLike> findByPostAndMember(Post post, Member member);
}