package com.hello.post00.exception.entityException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/* 400 Client Error */
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request", "잘못된 요청입니다."),
	INVALID_VALUE(HttpStatus.BAD_REQUEST, "Invalid value in the request", "요청에 잘못된 값이 있습니다."),
	BAD_PASSWORD(HttpStatus.BAD_REQUEST, "Password incorrect", "비밀번호를 확인하세요"),
	LIKE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Like is already exist", "좋아요를 이미 눌렀습니다."),

	/* 401 Client Error */
	AUTHORIZATION_IS_EMPTY(HttpStatus.UNAUTHORIZED, "Authorization is empty", "Authorization 요청에 헤더가 없습니다."),
	REFRESHTOKEN_IS_EMPTY(HttpStatus.UNAUTHORIZED, "RefreshToken is empty", "RefreshToken 이 없습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid", "토큰값이 유효하지 않아 인증에 실패했습니다."),

	/* 404 Client Error */
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not exist", "존재하지 않는 게시글 입니다."),
	POST_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "PostId not exist", "존재하지 않는 게시글 id 입니다."),
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found", "존재하지 않는 댓글 입니다."),
	COMMENT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "CommentId not exist", "존재하지 않는 댓글 id 입니다."),
	TITLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Title is empty", "제목을 작성해 주세요."),
	CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Content is empty", "내용을 입력해 주세요."),

	/* 409 Client Error */
	DUPLICATE_MEMBER_ID(HttpStatus.CONFLICT, "Member is duplicated", "중복된 사용자 ID가 존재합니다."),
	BAD_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "Password and PasswordConfirm don't match", "비밀번호와 비밀번호 확인이 다릅니다."),


	/* 500 Server Error */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "서버 오류"),
	DB_EMPTY_DATA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Empty data from Database", "DB에 데이터가 존재하지 않습니다."),
	MEMBER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Member not found", "멤버를 찾을 수 없습니다.");


	private final HttpStatus code;
	private final String message;
	private final String detail;


}