package com.hello.post00.service;

import com.hello.post00.dto.request.CommentRequestDto;
import com.hello.post00.dto.response.CommentAllResponseDto;
import com.hello.post00.dto.response.CommentResponseDto;
import com.hello.post00.dto.response.ResponseDto;
import com.hello.post00.entity.Comment;
import com.hello.post00.entity.Member;
import com.hello.post00.entity.Post;
import com.hello.post00.exception.entityException.commentException.CommentIdNotFoundException;
import com.hello.post00.exception.entityException.postException.*;
import com.hello.post00.jwt.provider.JwtProvider;
import com.hello.post00.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

	private final PostService postService;
	private final CommentRepository commentRepository;
	private final JwtProvider jwtProvider;


	private Member validateMember(HttpServletRequest request) {
		if (!jwtProvider.validateToken(request.getHeader("Refresh-Token"))) {
			return null;
		}
		return jwtProvider.getMemberFromAuthentication();
	}
	//댓글이 있는지 없는지
	@Transactional(readOnly = true)
	public Comment isPresentComment(Long id){
		Optional<Comment> optionalComment = commentRepository.findById(id);
		return optionalComment.orElse(null);
	}

	public ResponseEntity<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {
		if(null == request.getHeader("Refresh-Token")){
			throw new AccessTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}
		if(null == request.getHeader("Authorization")){
			throw new RefreshTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}
		Member member = validateMember(request);
		if(null == member){
			throw new TokenInvalidException();
//			return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
		}
		if(commentRequestDto.getContent()==null){
			throw new ContentNotFoundException();
//			return ResponseDto.fail("CONTENT_EMPTY", "작성된 글이 없습니다.");
		}

		Post post = postService.isPresentPost(commentRequestDto.getPostId());
		if (null == post) {
			throw new PostIdNotFoundException();
//			return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
		}


		Comment comment = Comment.builder()
				.post(post)
				.content(commentRequestDto.getContent())
				.member(member)
				.build();
		commentRepository.save(comment);
		return ResponseEntity.ok().body(ResponseDto.success(
				CommentResponseDto.builder()
						.id(comment.getId())
						.author(comment.getMember().getUsername())
						.content(comment.getContent())
						.createdAt(comment.getCreatedAt())
						.modifiedAt(comment.getModifiedAt())
						.build()
		));
	}


	//댓글 조회
	@Transactional(readOnly = true)
	public ResponseDto<?> getAllComment() {
		List<CommentAllResponseDto> commentAllList = new ArrayList<>();
		List<Comment> commentList = commentRepository.findAllByOrderByModifiedAtDesc();
		for(Comment comment: commentList){
			commentAllList.add(
					CommentAllResponseDto.builder()
							.id(comment.getId())
							.content(comment.getContent())
							.author(comment.getMember().getUsername())
							.createdAt(comment.getCreatedAt())
							.modifiedAt(comment.getModifiedAt())
							.build()
			);
		}
		return ResponseDto.success(commentAllList);
	}

	//댓글 상세 조회
	@Transactional(readOnly = true)
	public ResponseEntity<?> getComment(Long postId) {

		Comment comment = isPresentComment(postId);

		if(null == comment){
			throw new ContentNotFoundException();
//			return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
		}

		return ResponseEntity.ok().body(ResponseDto.success(
				CommentResponseDto.builder()
						.id(comment.getId())
						.content(comment.getContent())
						.author(comment.getMember().getUsername())
						.createdAt(comment.getCreatedAt())
						.modifiedAt(comment.getModifiedAt())
						.build()
		));
	}

	//댓글 수정
	@Transactional
	public ResponseEntity<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

		if(null == request.getHeader("Authorization")){
			throw new AccessTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}

		if(null == request.getHeader("Refresh-Token")){
			throw new RefreshTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}

		Member member = validateMember(request);

		if(null == member){
			throw new TokenInvalidException();
//			return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
		}

		Post post = postService.isPresentPost(requestDto.getPostId());

		if(null == post){
			throw new PostIdNotFoundException();
//			return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다. ");
		}

		Comment comment = isPresentComment(id);
		if(null == comment){
			throw new CommentIdNotFoundException();
//			return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
		}

//		if(comment.validateMember(member)){
//			return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//		}

		comment.update(requestDto);
		commentRepository.save(comment);
		return ResponseEntity.ok().body(ResponseDto.success(
				CommentResponseDto.builder()
						.id(comment.getId())
						.author(comment.getMember().getUsername())
						.content(comment.getContent())
						.createdAt(comment.getCreatedAt())
						.modifiedAt(comment.getModifiedAt())
						.build()
		));
	}


	//댓글 삭제
	@Transactional
	public ResponseEntity<?> deleteComment(Long id, HttpServletRequest request) {

		if(null == request.getHeader("Authorization")){
			throw new AccessTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}

		if(null == request.getHeader("Refresh-Token")){
			throw new RefreshTokenNotExistException();
//			return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
		}


		Member member = validateMember(request);

		if(null == member){
			throw new TokenInvalidException();
//			return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
		}

		Comment comment = isPresentComment(id);

		if(null == comment){
			throw new CommentIdNotFoundException();
//			return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID입니다.");
		}

//		if(comment.validateMember(member)){
//			return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//		}

		commentRepository.delete(comment);
		return ResponseEntity.ok().body(ResponseDto.success("success"));
	}


}