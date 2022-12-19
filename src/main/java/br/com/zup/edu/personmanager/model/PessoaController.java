package br.com.zup.edu.personmanager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping
    public ResponseEntity<?> inserir(@Valid @RequestBody PessoaRequest request){


        logger.info("Inserido com sucesso!");
        logger.warn("Inserido");
        logger.error("Falha! Cadastrado não realizado");

        var novaPessoa = request.toModel();

        if(pessoaRepository.findByCpf(novaPessoa.getCpf()).isPresent()){
            return ResponseEntity.badRequest().body("Já existe uma pessoa cadastrada com esse cpf");
        }else{
            pessoaRepository.save(novaPessoa);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(PessoaResponse.from(novaPessoa));
        }
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){

        var pessoa = pessoaRepository.findById(id)
                .orElseThrow(PessoaInexistenteException::new);

        pessoaRepository.delete(pessoa);
    }
}
