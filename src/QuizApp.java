import model.Question;
import model.Response;
import model.Topic;
import dao.DaoQuestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class QuizApp {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Database connection setup
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:quiz.db")) {
            DaoQuestion daoQuestion = new DaoQuestion(connection);

            // Sample data
            Topic topic = new Topic();
            topic.setName("Science");

            Question question1 = new Question();
            question1.setTopicId(1); // Replace with the actual topic ID
            question1.setDifficulty(1);
            question1.setContent("What is the capital of France?");

            Response response1 = new Response();
            response1.setText("Paris");
            response1.setCorrect(true);

            Response response2 = new Response();
            response2.setText("Berlin");
            response2.setCorrect(false);

            question1.getResponses().add(response1);
            question1.getResponses().add(response2);

            // Save the question
            daoQuestion.saveQuestion(question1);

            // Update the question
            question1.setContent("What is the capital of Spain?");
            daoQuestion.updateQuestion(question1);

            // Search questions by topic
            List<Question> scienceQuestions = daoQuestion.getQuestionsByTopic("Science");
            for (Question q : scienceQuestions) {
                System.out.println("Question ID: " + q.getId());
                System.out.println("Content: " + q.getContent());
                System.out.println("Topic ID: " + q.getTopicId());
                System.out.println("Difficulty: " + q.getDifficulty());

                for (Response r : q.getResponses()) {
                    System.out.println("Response: " + r.getText() + ", Correct: " + r.isCorrect());
                }
            }

            // Delete the question
            //daoQuestion.deleteQuestion(question1.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
