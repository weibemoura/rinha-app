package dev.weibe.rinhaapp.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.weibe.rinhaapp.model.Pessoa;

class PessoaRowMapper implements RowMapper<Pessoa> {

    private final ObjectMapper objectMapper;
    private final TypeReference<List<Object>> stackRef = new TypeReference<>() {};

    public PessoaRowMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            var pessoa = new Pessoa();
            pessoa.setId(rs.getObject("id", UUID.class));
            pessoa.setApelido(rs.getString("apelido"));
            pessoa.setNome(rs.getString("nome"));
            pessoa.setNascimento(rs.getDate("nascimento"));
            pessoa.setStack(objectMapper.readValue(rs.getString("stack"), stackRef));
            return pessoa;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

@Repository
public class PessoaRepository {

    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final PessoaRowMapper pessoaRowMapper;


    public PessoaRepository(ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.pessoaRowMapper = new PessoaRowMapper(objectMapper);
    }

    @Transactional
    public Pessoa create(Pessoa pessoa) {
        var sql = "insert into pessoas (apelido, nome, nascimento, stack) values (?,?,?,?) returning id";

        var keyHolter = new GeneratedKeyHolder();
        this.jdbcTemplate.update(psc -> {
            try {
                var stmt = psc.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, pessoa.getApelido());
                stmt.setString(2, pessoa.getNome());
                stmt.setDate(3, new Date(pessoa.getNascimento().getTime()));
                stmt.setString(4, objectMapper.writeValueAsString(pessoa.getStack()));
                return stmt;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, keyHolter);

        pessoa.setId(keyHolter.getKeyAs(UUID.class));
        return pessoa;
    }

    public Optional<Pessoa> findById(UUID id) {
        var sql = "select * from pessoas where id = ?";

        var params = new Object[]{id};
        var result = this.jdbcTemplate.query(sql, this.pessoaRowMapper, params);

        if (result.isEmpty()) return Optional.empty();

        return Optional.ofNullable(result.get(0));
    }

    public Optional<Pessoa> findByApelido(String apelido) {
        var sql = "select * from pessoas where apelido = ?";

        var params = new Object[]{apelido};
        var result = this.jdbcTemplate.query(sql, this.pessoaRowMapper, params);

        if (result.isEmpty()) return Optional.empty();

        return Optional.ofNullable(result.get(0));
    }

    public List<Pessoa> findByTerm(String term) {
        var sql = "select * from pessoas where lower(apelido||' '||nome||' '||stack) similar to ? limit 50";

        var params = new Object[]{"%("+term+")%"};
        return this.jdbcTemplate.query(sql, params, this.pessoaRowMapper);
    }

    public Long count() {
        var sql = "select count(1) from pessoas";

        return this.jdbcTemplate.queryForObject(sql, Long.class);
    }
}
