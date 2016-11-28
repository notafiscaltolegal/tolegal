package gov.goias.entidades;

import gov.goias.entidades.enums.TipoMotivoReclamacao;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

/**
 * Created by lucas-mp.
 */
@Entity
@Table(name = "NFG_MOTIVO_RECLAMACAO_DOC_FISC")
@Repository
public class MotivoReclamacao extends GenericRepository {

    @Id
    @Column(name = "CODG_MOTIVO_RECLAMACAO")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer codigo;

    @Column(name = "DESC_MOTIVO_RECLAMACAO")
    private String descricao;

    @Transient
    private TipoMotivoReclamacao tipoMotivo;

    public TipoMotivoReclamacao getTipoMotivo() {
        Integer codigo = getCodigo();
        return codigo !=null? TipoMotivoReclamacao.get(codigo) : null;

    }

    public String getTipoMotivoDescricao() {
        Integer codigo = getCodigo();
        return codigo !=null?(TipoMotivoReclamacao.get(getCodigo())).getDescricao(): null;
    }

    public void setTipoMotivo(TipoMotivoReclamacao tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
        this.codigo = tipoMotivo.getValue();
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

