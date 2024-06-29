package roomescape.domain.member.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.Role;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT * 
            FROM member m
            WHERE
                m.email = ? AND 
                m.password = ?;
            """;
    private static final String FIND_BY_ID_SQL = "SELECT * FROM member m WHERE m.id = ?;";
    private static final String FIND_ALL_SQL = "SELECT * FROM member;";
    private static final String UPDATE_ADMIN_ROLE_SQL = "UPDATE member SET role = ? WHERE id = ?;";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String MEMBER = "MEMBER";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(MEMBER)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        Member member = jdbcTemplate.queryForObject(FIND_BY_EMAIL_AND_PASSWORD_SQL, memberRowMapper(), email, password);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Member member = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, memberRowMapper(), memberId);
        return Optional.of(member);
    }

    @Override
    public Long save(Member member) {
        Map<String, String> dataSource = setMemberDataSource(member);
        Number number = jdbcInsert.executeAndReturnKey(dataSource);
        return number.longValue();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, memberRowMapper());
    }

    @Override
    public Long updateAdminRole(Long id) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    UPDATE_ADMIN_ROLE_SQL,
                    new String[]{ID}
            );
            preparedStatement.setString(1, Role.ADMIN.getRole());
            preparedStatement.setLong(2, id);
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private Map<String, String> setMemberDataSource(Member member) {
        Map<String, String> dataSource = new HashMap<>();
        dataSource.put(NAME, member.getName());
        dataSource.put(EMAIL, member.getEmail());
        dataSource.put(PASSWORD, member.getPassword());
        dataSource.put(ROLE, member.getRole());
        return dataSource;
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) ->
                new Member(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(EMAIL),
                        rs.getString(PASSWORD),
                        rs.getString(ROLE)
                );
    }
}
