package com.coviscon.postservice.entity.post;

import com.coviscon.postservice.dto.request.RequestCommentEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String nickName; // member nickName

    @Column(nullable = false)
    private String tNickName; // teacher nickName

    @Setter
    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    // == 생성 메서드 ==
    public static Comment createComment(MemberResponseDto member, String content, Qna qna, Comment parent) {
        return Comment.builder()
            .nickName(member.getNickName())
            .tNickName(member.getNickName())
            .content(content)
            .qna(qna)
            .isDeleted(false)
            .parent(parent)
            .build();
    }

    /**
     * == isDelete 값 변경 ==
     *
     * false가 기본값
     * isDelete == true면, 삭제
     */
    public void changeIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // == 댓글 수정 ==
    public void updateComment(RequestCommentEdit requestPostEdit) {
        this.id = requestPostEdit.getCommentId();
        this.content = requestPostEdit.getContent();
    }
}