package gov.goias.dtos;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by diogo-rs on 8/7/2014.
 */
@XStreamAlias("descadastramento")
public class DTODescadastramentoUsuario  implements Serializable {

    private static final long serialVersionUID = 9196489908341653578L;

    private Boolean usuarioDescadastrado = false;
    private String error="";

    public DTODescadastramentoUsuario(Boolean usuarioDescadastrado) {
        this.usuarioDescadastrado = usuarioDescadastrado;
    }

    public DTODescadastramentoUsuario(String error) {
        this.error = error;
    }

    public Boolean getUsuarioDescadastrado() {
        return usuarioDescadastrado;
    }

    public void setUsuarioDescadastrado(Boolean usuarioDescadastrado) {
        this.usuarioDescadastrado = usuarioDescadastrado;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
