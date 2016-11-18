package gov.goias.dtos;

/**
 * Representa os dados usados no relatório de totais de participantes cadastrados na To Legal.
 */
public class DTOTotaisCadastrados {

    private Long totalGeral;
    private Long totalParticipamSorteio;
    private Long totalNaoParticipamSorteio;
    private Long totalExcluidos;

    public Long getTotalGeral() {
        return totalGeral;
    }

    public void setTotalGeral(Long totalGeral) {
        this.totalGeral = totalGeral;
    }

    public Long getTotalParticipamSorteio() {
        return totalParticipamSorteio;
    }

    public void setTotalParticipamSorteio(Long totalParticipamSorteio) {
        this.totalParticipamSorteio = totalParticipamSorteio;
    }

    public Long getTotalNaoParticipamSorteio() {
        return totalNaoParticipamSorteio;
    }

    public void setTotalNaoParticipamSorteio(Long totalNaoParticipamSorteio) {
        this.totalNaoParticipamSorteio = totalNaoParticipamSorteio;
    }

    public Long getTotalExcluidos() {
        return totalExcluidos;
    }

    public void setTotalExcluidos(Long totalExcluidos) {
        this.totalExcluidos = totalExcluidos;
    }

}
