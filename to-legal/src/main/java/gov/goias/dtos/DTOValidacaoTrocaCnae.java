package gov.goias.dtos;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @author diogo-rs
 * @since 28/07/2014
 */
@XStreamAlias("validacao")
public class DTOValidacaoTrocaCnae implements Serializable{

    private static final long serialVersionUID = 9196489902560653578L;

    private Boolean usuarioParticipante = false;
    private Boolean cnaeParticipante = false;
    private String mensagem="";

    public DTOValidacaoTrocaCnae(Boolean usuarioParticipante, Boolean cnaeParticipante) {
        this.usuarioParticipante = usuarioParticipante;
        this.cnaeParticipante = cnaeParticipante;
        definaMensagem();
    }

    private void definaMensagem() {
        if(usuarioParticipante && !cnaeParticipante) mensagem = "CNAE n&#225;o autorizado. O contribuinte será descadastrado do NFG";
        if(!usuarioParticipante && cnaeParticipante) mensagem = "Contribuinte n&#225;o cadastrado no NFG e CNAE participante do NFG.";
    }

    public DTOValidacaoTrocaCnae(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getUsuarioParticipante() {
        return usuarioParticipante;
    }

    public void setUsuarioParticipante(Boolean usuarioParticipante) {
        this.usuarioParticipante = usuarioParticipante;
    }

    public Boolean getCnaeParticipante() {
        return cnaeParticipante;
    }

    public void setCnaeParticipante(Boolean cnaeParticipante) {
        this.cnaeParticipante = cnaeParticipante;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public static void main(String[] args){
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        System.out.println(xStream.toXML(new DTOValidacaoTrocaCnae(true, false)));
    }
}
