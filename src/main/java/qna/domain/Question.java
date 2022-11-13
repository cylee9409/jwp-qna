package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Answers getAnswers() {
        return answers;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.addAnswer(answer);
    }

    public DeleteHistories delete(User loginUser, Long questionId) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();

        checkDeleteAuthorization(loginUser);

        if (answers.isEmpty()) {
            this.deleted = true;
            deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, writer, LocalDateTime.now()));
            return deleteHistories;
        }

        if (answers.checkAnswerWriter(loginUser)) {
            this.deleted = true;
            deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, writer, LocalDateTime.now()));
            deleteHistories = answers.delete(deleteHistories);
        }

        return deleteHistories;
    }

    public void checkDeleteAuthorization(User loginUser) throws CannotDeleteException {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }
}