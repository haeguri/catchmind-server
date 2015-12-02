package question.dao;

import question.model.Question;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQuestionDAO implements QuestionDAO {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public Question getQuestion(){
        String sql = "SELECT id, name FROM question ORDER BY RAND() LIMIT 1";

        Connection conn = null;
        Question question = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                question = new Question(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
            rs.close();
            ps.close();

            return question;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
