package br.com.treinaweb.ediaristas.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import br.com.treinaweb.ediaristas.core.enums.TipoUsuario;
import br.com.treinaweb.ediaristas.core.exceptions.SenhasNaoConferemException;
import br.com.treinaweb.ediaristas.core.exceptions.UsuarioJaCadastradoException;
import br.com.treinaweb.ediaristas.core.exceptions.UsuarioNaoEncontradoException;
import br.com.treinaweb.ediaristas.core.models.Usuario;
import br.com.treinaweb.ediaristas.core.repositories.UsuarioRepository;
import br.com.treinaweb.ediaristas.web.dtos.UsuarioCadastroForm;
import br.com.treinaweb.ediaristas.web.dtos.UsuarioEdicaoForm;
import br.com.treinaweb.ediaristas.web.mappers.WebUsuarioMapper;

@Service
public class WebUsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private WebUsuarioMapper mapper;

    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

    public Usuario cadastrar(UsuarioCadastroForm form){
        var senha = form.getSenha();
        var confirmacaoSenha = form.getConfirmacaoSenha();
        if (!senha.equals(confirmacaoSenha)) {
            var mensagem = "As senhas não conferem";
            var fieldError = new FieldError(form.getClass().getName(), "confirmacaoSenha", form.getConfirmacaoSenha(), false, null, null, mensagem);
            throw new SenhasNaoConferemException(mensagem, fieldError);
        }

        var model = mapper.toModel(form);
        model.setTipoUsuario(TipoUsuario.ADMIN);

        validarCamposUnicos(model);
        
        return repository.save(model);
    }

    public Usuario buscarPorId(Long id) {
        var mensagem = String.format("Usuário com ID %d não encontrado", id);
        return repository.findById(id).orElseThrow(
            () -> new UsuarioNaoEncontradoException(mensagem));
    }

    public UsuarioEdicaoForm buscarFormPorId(Long id) {
        var usuario = buscarPorId(id);
        return mapper.toForm(usuario);
    }

    public Usuario editar(UsuarioEdicaoForm form, Long id){
        var usuario = buscarPorId(id);
        
        var model = mapper.toModel(form);
        model.setId(usuario.getId());
        model.setTipoUsuario(usuario.getTipoUsuario());
        model.setSenha(usuario.getSenha());
       
        validarCamposUnicos(model);
       
        return repository.save(model);
    }

    public void excluirPorId(Long id) {
        repository.deleteById(id);
    }

    private void validarCamposUnicos(Usuario usuario){
        repository.findByEmail(usuario.getEmail()).ifPresent((usuarioEncontrado)->{
            if (usuarioEncontrado.getId() != usuario.getId()) {
                var mensagem = "Já existe um usuário cadastrado com o e-mail informado";
                var fieldError = new FieldError(usuario.getClass().getName(), "email", usuario.getEmail(), false, null, null, mensagem);
                throw new UsuarioJaCadastradoException(mensagem, fieldError);
            }
        });
    }
}
