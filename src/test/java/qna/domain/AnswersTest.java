package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswersTest {

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    @BeforeEach
    void save() {
        User writer1 = users.save(UserTest.JAVAJIGI);
        User writer2 = users.save(UserTest.SANJIGI);
    }


    @Test
    @DisplayName("Anwers가 빈 값인지 체크 테스트")
    void check_is_empty_answers_test() {
        Answers answers = new Answers();

        assertThat(answers.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Anwers가 빈 값이 아닌 경우 체크 테스트")
    void check_is_not_empty_answers_test() {
        Question question = questions.save(QuestionTest.Q1);
        question.addAnswer(AnswerTest.A1);
        question.addAnswer(AnswerTest.A2);

        Answers answers = question.getAnswers();

        assertThat(answers.isEmpty()).isFalse();
    }
}
