package dev.weibe.rinhaapp.controller;


import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dev.weibe.rinhaapp.model.Pessoa;
import dev.weibe.rinhaapp.repository.PessoaRepository;
import jakarta.validation.Valid;

@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping("/pessoas")
    ResponseEntity<Pessoa> create(@Valid @RequestBody Pessoa pessoa) {
        pessoaRepository.findByApelido(pessoa.getApelido())
                .ifPresent(p -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "");
                });

        pessoa = pessoaRepository.create(pessoa);
        return ResponseEntity.created(URI.create("/pessoas/"+pessoa.getId())).build();
    }

    @GetMapping("/pessoas/{id}")
    ResponseEntity<Pessoa> findById(@PathVariable UUID id) {
        var pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ""));

        return ResponseEntity.ok(pessoa);
    }

    @GetMapping("/pessoas")
    ResponseEntity<List<Pessoa>> findByTerm(@RequestParam("t") String term) {
        var pessoas = pessoaRepository.findByTerm(term);

        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/contagem-pessoas")
    ResponseEntity<Long> count() {
        return ResponseEntity.ok(pessoaRepository.count());
    }
}
