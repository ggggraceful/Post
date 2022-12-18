package com.hello.post00.service;

import com.hello.post00.dto.response.ResponseDto;
import com.hello.post00.entity.Member;
import com.hello.post00.entity.Post;
import com.hello.post00.entity.PostLike;
import com.hello.post00.exception.entityException.likeException.LikeIsAlreadyExistException;
import com.hello.post00.exception.entityException.postException.AccessTokenNotExistException;
import com.hello.post00.exception.entityException.postException.PostIdNotFoundException;
import com.hello.post00.exception.entityException.postException.RefreshTokenNotExistException;
import com.hello.post00.exception.entityException.postException.TokenInvalidException;
import com.hello.post00.jwt.provider.JwtProvider;
import com.hello.post00.repository.LikeRepository;
import com.hello.post00.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
	private final JwtProvider jwtProvider;
	private final PostRepository postRepository;

	@Transactional
	public Member validateMember(HttpServletRequest request) {
		if (!jwtProvider.validateToken(request.getHeader("Refresh-Token"))) {
			return null;
		}
		return jwtProvider.getMemberFromAuthentication();
	}

	@Transactional(readOnly = true)
	public Post isPresentPost(Long id) {
		Optional<Post> optionalPost = postRepository.findById(id);
		return optionalPost.orElse(null);
	}

	@Transactional
	public ResponseEntity<?> pushPostLike(Long postId, HttpServletRequest request) {
		if(null == request.getHeader("Authorization")){
			throw new RefreshTokenNotExistException();
		}
		if(null == request.getHeader("Refresh-Token")){
			throw new AccessTokenNotExistException();
		}
		Member member = validateMember(request);

		if(null == member){
			throw new TokenInvalidException();
		}
		Post post = isPresentPost(postId);
		if(post == null ){
			throw new PostIdNotFoundException();
		}
		Optional<PostLike> findPostLike = likeRepository.findByPostAndMember(post, member);
		findPostLike.ifPresentOrElse(
// 좋아요 있을경우 삭제
				postLike -> {
					likeRepository.delete(postLike);
					post.discountLike(postLike);
					throw new LikeIsAlreadyExistException();
				},
//좋아요 없을 경우 좋아요 추가
				() -> {
					PostLike postLike = PostLike.builder()
							.post(post)
							.member(member)
							.build();
					likeRepository.save(postLike);
				});
		return ResponseEntity.ok(ResponseDto.success(true));
	}
}