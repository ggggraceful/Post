package com.hello.post00.exception;

import com.hello.post00.exception.entityException.ErrorCode;
import com.hello.post00.exception.entityException.ErrorResponse;
import com.hello.post00.exception.entityException.commentException.CommentIdNotFoundException;
import com.hello.post00.exception.entityException.likeException.LikeIsAlreadyExistException;
import com.hello.post00.exception.entityException.memberException.BadPasswordConfirmException;
import com.hello.post00.exception.entityException.memberException.BadPasswordException;
import com.hello.post00.exception.entityException.memberException.DuplicateMemberException;
import com.hello.post00.exception.entityException.memberException.MemberNotFoundException;
import com.hello.post00.exception.entityException.postException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// 중복아이디 - CONFLICT
	@ExceptionHandler(DuplicateMemberException.class)
	public ResponseEntity<ErrorResponse> handlerDuplicateMemberException(Exception e){ // e: 입력한 메세지(String)
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_MEMBER_ID.getMessage(), ErrorCode.DUPLICATE_MEMBER_ID.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	// 비밀번호 비밀번호 확인 일치여부 - BAD_REQUEST
	@ExceptionHandler(BadPasswordConfirmException.class)
	public ResponseEntity<ErrorResponse> handlerBadPasswordConfirmException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.BAD_PASSWORD_CONFIRM.getMessage(), ErrorCode.BAD_PASSWORD_CONFIRM.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// 존재하는 회원이 없을 때 - INTERNAL_SERVER_ERROR
	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerMemberNotFoundException(Exception e) {
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 일치하는 비밀번호인지 확인 - UNAUTHORIZED
	 @ExceptionHandler(BadPasswordException.class)
	 public ResponseEntity<ErrorResponse> handlerBadPasswordException(Exception e){
		 ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.BAD_PASSWORD.getMessage(), ErrorCode.BAD_PASSWORD.getDetail());
		 log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		 return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	// Access 토큰값이 존재하는지 - UNAUTHORIZED
	@ExceptionHandler(AccessTokenNotExistException.class)
	public ResponseEntity<ErrorResponse> handlerAccessTokenNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHORIZATION_IS_EMPTY.getMessage(), ErrorCode.AUTHORIZATION_IS_EMPTY.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	// Access 토큰값이 존재하는지 - UNAUTHORIZED
	@ExceptionHandler(RefreshTokenNotExistException.class)
	public ResponseEntity<ErrorResponse> handlerRefreshTokenNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.REFRESHTOKEN_IS_EMPTY.getMessage(), ErrorCode.REFRESHTOKEN_IS_EMPTY.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}


	// 토큰값이 일치하는지 - UNAUTHORIZED
	@ExceptionHandler(TokenInvalidException.class)
	public ResponseEntity<ErrorResponse> handlerTokenInvalidException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.INVALID_TOKEN.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	// 제목 확인(제목을 입력해주세요) - NOT_FOUND
	@ExceptionHandler(TitleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerTitleNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.TITLE_NOT_FOUND.getMessage(), ErrorCode.TITLE_NOT_FOUND.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	// 내용 확인(내용을 입력해주세요) - NOT_FOUND
	@ExceptionHandler(ContentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerContentNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.CONTENT_NOT_FOUND.getMessage(), ErrorCode.CONTENT_NOT_FOUND.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

	}

	// 게시글 id 확인(존재하지 않는 게시글 id 입니다.) - NOT_FOUND
	@ExceptionHandler(PostIdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerPostIdNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.POST_ID_NOT_FOUND.getMessage(), ErrorCode.POST_ID_NOT_FOUND.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	// 댓글 id 확인(존재하지 않는 게시글 id 입니다.) - NOT_FOUND
	@ExceptionHandler(CommentIdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerCommentIdNotFoundException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.COMMENT_ID_NOT_FOUND.getMessage(), ErrorCode.COMMENT_ID_NOT_FOUND.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	//좋아요가 있을시
	@ExceptionHandler(LikeIsAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handlerLikeIsAlreadyExistException(Exception e){
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.LIKE_ALREADY_EXIST.getMessage(), ErrorCode.LIKE_ALREADY_EXIST.getDetail());
		log.error("error:{}, stacktrace:{}", errorResponse, e.getStackTrace());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}


}
