package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public boolean checkAnswerWriter(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            validateWriter(loginUser, answer);
        }
        return true;
    }

    public void validateWriter(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public DeleteHistories delete(DeleteHistories deleteHistories) {
        for (Answer answer : answers) {
            answer.delete();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }

        return deleteHistories;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isEmpty() {
        if (answers.size() == 0) {
            return true;
        }
        return false;
    }
}
