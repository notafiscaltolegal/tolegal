package gov.goias.dtos;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diogo-rs on 8/11/2014.
 * <LoteNFe id=''>
 *     <NFes>
 *         <NFe>
 *             <id></id>
 *             <cpf_dest></cpf_dest>
 *             <valor_total></valor_total>
 *         </NFe>
 *     </NFes>
 * </lote>
 */
@XStreamAlias("LoteNFe")
public class DTOLoteDeNotas implements Serializable{

    @XStreamAlias("id")
    @XStreamAsAttribute
    private Long idLote;


    @XStreamAlias("NFes")
    private List<DTONFEIdentificacao> notas = new ArrayList<DTONFEIdentificacao>();

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public List<DTONFEIdentificacao> getNotas() {
        return notas;
    }

    public void setNotas(List<DTONFEIdentificacao> notas) {
        this.notas = notas;
    }
}
