package com.dataware.repository.impl;

import com.dataware.database.DatabaseConnection;
import com.dataware.model.Member;
import com.dataware.model.enums.MemberRole;
import com.dataware.repository.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryImpl implements MemberRepository {

    private final Connection connection;

    public MemberRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    

    public MemberRepositoryImpl(Connection connection) {
		this.connection = connection;
	}



	@Override
    public void addMember(Member member) {
        String sql = "INSERT INTO Member (first_name, last_name, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getRole().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Member> getMemberById(int id) {
        String sql = "SELECT * FROM Member WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	 String roleStr = resultSet.getString("role"); 
                 MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                return Optional.of(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Member> getAllMembers(int page, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member LIMIT ? OFFSET ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, (page - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String roleStr = resultSet.getString("role"); 
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    
    
    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String roleStr = resultSet.getString("role"); 
                MemberRole role = MemberRole.fromString(roleStr);

                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public void updateMember(Member member) {
        String sql = "UPDATE Member SET first_name = ?, last_name = ?, email = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getRole().toString());
            statement.setInt(5, member.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMember(int id) {
        String sql = "DELETE FROM Member WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM Member";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public List<Member> findByEmail(String email) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Member WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
            	 String roleStr = resultSet.getString("role"); 
                 MemberRole role = MemberRole.fromString(roleStr);
                Member member = new Member(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        role
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

	  public int getLastInsertedId() {
	        String query = "SELECT LAST_INSERT_ID()";
	        try (PreparedStatement pstmt = connection.prepareStatement(query);
	             ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return -1; // Retourne -1 si aucun ID n'a été trouvé ou en cas d'erreur
	    }
}
