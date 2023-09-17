package dao;

import model.Question;
import model.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoQuestion {
    private Connection connection;

    public DaoQuestion(Connection connection) {
        this.connection = connection;
    }

    // Save a new question into the database
    public void saveQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO Questions (topic_id, difficulty, content) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, question.getTopicId());
            statement.setInt(2, question.getDifficulty());
            statement.setString(3, question.getContent());
            statement.executeUpdate();

            // Retrieve the generated question ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int questionId = generatedKeys.getInt(1);
                question.setId(questionId);

                // Save responses for the question
                saveResponses(question);
            }
        }
    }

    // Update an existing question in the database
    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE Questions SET topic_id=?, difficulty=?, content=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, question.getTopicId());
            statement.setInt(2, question.getDifficulty());
            statement.setString(3, question.getContent());
            statement.setInt(4, question.getId());
            statement.executeUpdate();

            // Update responses for the question
            updateResponses(question);
        }
    }

    // Delete a question from the database
    public void deleteQuestion(int questionId) throws SQLException {
        // Delete responses for the question first
        deleteResponses(questionId);

        String sql = "DELETE FROM Questions WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            statement.executeUpdate();
        }
    }

    // Search questions by topic
    public List<Question> getQuestionsByTopic(String topic) throws SQLException {
        String sql = "SELECT * FROM Questions WHERE topic_id IN (SELECT id FROM Topics WHERE name=?)";
        List<Question> questions = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, topic);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getInt("id"));
                question.setTopicId(resultSet.getInt("topic_id"));
                question.setDifficulty(resultSet.getInt("difficulty"));
                question.setContent(resultSet.getString("content"));
                // Load responses for the question here if needed
                question.setResponses(getResponsesForQuestion(question.getId()));
                questions.add(question);
            }
        }
        return questions;
    }

    // Save responses for a question
    private void saveResponses(Question question) throws SQLException {
        String sql = "INSERT INTO Responses (question_id, text, is_correct) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Response response : question.getResponses()) {
                statement.setInt(1, question.getId());
                statement.setString(2, response.getText());
                statement.setBoolean(3, response.isCorrect());
                statement.executeUpdate();
            }
        }
    }

    // Update responses for a question
    private void updateResponses(Question question) throws SQLException {
        // First, delete existing responses for the question
        deleteResponses(question.getId());
        
        // Then, save the updated responses
        saveResponses(question);
    }

    // Delete responses for a question
    private void deleteResponses(int questionId) throws SQLException {
        String sql = "DELETE FROM Responses WHERE question_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            statement.executeUpdate();
        }
    }

    // Get responses for a question
    private List<Response> getResponsesForQuestion(int questionId) throws SQLException {
        String sql = "SELECT * FROM Responses WHERE question_id=?";
        List<Response> responses = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Response response = new Response();
                response.setId(resultSet.getInt("id"));
                response.setQuestionId(resultSet.getInt("question_id"));
                response.setText(resultSet.getString("text"));
                response.setCorrect(resultSet.getBoolean("is_correct"));
                responses.add(response);
            }
        }
        return responses;
    }
}