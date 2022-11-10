package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @BeforeEach
    void save() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);

        questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
    }

    @Test
    @DisplayName("Answer 저장 테스트")
    void save_user_test() {

        final Answer savedAnswer = answers.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    @DisplayName("Answer 생성 및 저장 테스트")
    void create_new_user_and_save_test() {

        final Answer answer1 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "TEST");
        final Answer savedAnswer = answers.save(answer1);

        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                ()-> assertThat(savedAnswer.getContents()).isEqualTo("TEST")
        );
    }

    @Test
    @DisplayName("QuestionId로 Answer 조회 테스트")
    void find_answer_by_question_id_test() {

        final Answer answer1 = answers.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "TEST2"));
        final Answer answer3 = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q2, "TEST3"));

        final List<Answer> list = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 조회 테스트")
    void find_not_deleted_answer_test() {

        final Answer answer1 = answers.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q2, "TEST2"));
        answers.delete(answer2);

        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(answer1.getId());

        assertThat(searchResult).isPresent();
    }

    @Test
    @DisplayName("삭제된 Answer 조회 테스트")
    void find_deleted_answer_test() {

        final Answer answer1 = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q2, "TEST2"));
        answers.delete(answer2);

        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(answer2.getId());

        assertThat(searchResult).isNotPresent();
    }

    @Test
    @DisplayName("유효한 Question 조회")
    void get_valid_Question_test() {
        final Answer answer = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "TEST1"));
        final Question question = questions.findByIdAndDeletedFalse(answer.getQuestion().getId()).get();

        assertThat(question).isEqualTo(answer.getQuestion());
    }

    @Test
    @DisplayName("유효한 Writer 조회")
    void get_valid_Writer_test() {
        final Answer answer = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "TEST1"));
        final User writer = users.findByUserId(answer.getWriter().getUserId()).get();

        assertThat(writer.getId()).isEqualTo(answer.getWriter().getId());
    }
}
