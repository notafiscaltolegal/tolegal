package gov.goias.entidades;

import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

/**
 * Created by lucas-mp.
 */
@Entity
@Table(name = "NFG_COMPL_SITUACAO_RECLAMACAO")
@Repository
public class ComplSituacaoReclamacao {

    @Id
    @Column(name = "CODG_COMPL_SITUACAO_RECLAMACAO")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer codigo;

    @Column(name = "DESC_COMPL_SITUACAO_RECLAMACAO")
    private String descricao;


    public ComplSituacaoReclamacao( ) {

    }

    public ComplSituacaoReclamacao(int codigo) {
        this.codigo = codigo;
    }


    public ComplSituacaoReclamacao(TipoComplSituacaoReclamacao tipoComplSituacaoReclamacao) {
        this.codigo = tipoComplSituacaoReclamacao.getValue();
    }


    public static ComplSituacaoReclamacao instanciarTipo(TipoComplSituacaoReclamacao tipoCompl){
        return new ComplSituacaoReclamacao(tipoCompl.getValue());
    }

    public TipoComplSituacaoReclamacao getTipoComplSituacaoReclamacao(){
        return TipoComplSituacaoReclamacao.get(this.codigo);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipoSituacaoDescricao(){
        return TipoComplSituacaoReclamacao.get(getCodigo()).getDescricao();
    }

    public String getTipoSituacaoAcao(){
        return TipoComplSituacaoReclamacao.get(getCodigo()).getAcao();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
