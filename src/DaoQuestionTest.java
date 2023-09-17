import model.Question;
import org.junit.Test;

import dao.DaoQuestion;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DaoQuestionTest {

    @Test
    public void testSaveQuestion() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {

            DaoQuestion daoQuestion = new DaoQuestion(connection);
            // Create a sample question
            Question question = new Question();
            question.setTopicId(1); // Replace with the actual topic ID
            question.setDifficulty(1);
            question.setContent("What is the capital of France?");

            // Save the question
            daoQuestion.saveQuestion(question);

            // Verify that the question has been saved with an ID
            assertTrue(question.getId() > 0);

            // Clean up: Delete the created question
            daoQuestion.deleteQuestion(question.getId());

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetQuestionsByTopic() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {

            DaoQuestion daoQuestion = new DaoQuestion(connection);
            // Create a sample question and save it
            Question question = new Question();
            question.setTopicId(1); // Replace with the actual topic ID
            question.setDifficulty(1);
            question.setContent("What is the capital of Germany?");
            daoQuestion.saveQuestion(question);

            // Retrieve questions by topic
            List<Question> questions = daoQuestion.getQuestionsByTopic("Geography");

            // Verify that the list is not empty
            assertFalse(questions.isEmpty());

            // Clean up: Delete the created question
            daoQuestion.deleteQuestion(question.getId());

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}