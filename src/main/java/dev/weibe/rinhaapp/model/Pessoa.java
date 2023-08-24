package dev.weibe.rinhaapp.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.weibe.rinhaapp.util.validation.ListItems;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class Pessoa {

    private UUID id;
    @NotNull @NotEmpty @Size(max = 32)
    private String apelido;
    @NotNull @NotEmpty @Size(max = 100)
    private String nome;
    @NotNull @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nascimento;
    @ListItems
    private List<Object> stack;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public List<Object> getStack() {
        return stack;
    }

    public void setStack(List<Object> stack) {
        this.stack = stack;
    }
}
