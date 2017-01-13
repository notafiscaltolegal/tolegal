package gov.goias.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import gov.goias.dtos.DTOBilhetePessoa;
import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.entidades.RegraSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.util.UtilReflexao;

@Service
public class SorteioServiceImpl implements SorteioService {
	
	

	@Override
	public List<RegraSorteio> listRegraSorteioDivulgaResultado() {

		List<RegraSorteio> list = new ArrayList<RegraSorteio>();

		RegraSorteio rs = getRegraSorteio();
		
		RegraSorteio rs1 = new RegraSorteio();
		rs1.setDataCadastro(new Date());
		rs1.setDataExtracaoLoteria(new Date());
		rs1.setDataLimiteCadastroPessoa(new Date());
		rs1.setDataRealizacao(new Date());
		rs1.setDivulgaSorteio("S");
		rs1.setId(2);
		rs1.setInformacao("2 mock");
		rs1.setNumeroConversao(1234.5);
		rs1.setNumeroLoteria(123);
		rs1.setNumeroMaxDocFisc(1234);
		rs1.setRealizado('N');
		rs1.setRealizadoBoolean(false);
		rs1.setStatus(3);
		rs1.setTipo(2);

		list.add(rs);
		list.add(rs1);
		
		return list;
	}

	private RegraSorteio getRegraSorteio() {
		RegraSorteio rs = new RegraSorteio();
		rs.setDataCadastro(new Date());
		rs.setDataExtracaoLoteria(new Date());
		rs.setDataLimiteCadastroPessoa(new Date());
		rs.setDataRealizacao(new Date());
		rs.setDivulgaSorteio("S");
		rs.setId(1);
		rs.setInformacao("1 mock");
		rs.setNumeroConversao(1234.1);
		rs.setNumeroLoteria(123456);
		rs.setNumeroMaxDocFisc(12345);
		rs.setRealizado('N');
		rs.setRealizadoBoolean(false);
		rs.setStatus(4);
		rs.setTipo(1);
		return rs;
	}

	@Override
	public Integer pontuacaoSemSorteio(Integer idPessoaParticipante) {
		return 12345;
	}

	@Override
	public SorteioCidadaoDTO carregaDadosDoSorteioParaCidadao(Integer idSorteio, Integer id) {

		RegraSorteio sorteio = getRegraSorteio();
		int totalDocs = 3;
		int totalPontos = 10;
		Long totalBilhetes = 10L;
		Date dataFimRegra = new Date();

		SorteioCidadaoDTO sorteioCidadaoDTO = new SorteioCidadaoDTO();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		sorteioCidadaoDTO.setSorteio(sorteio);
		sorteioCidadaoDTO.setTotalDocs(totalDocs);
		sorteioCidadaoDTO.setTotalPontos(totalPontos);
		sorteioCidadaoDTO.setTotalBilhetes(totalBilhetes);
		sorteioCidadaoDTO.setDataFimRegra(dataFimRegra != null ? sdf.format(dataFimRegra) : "Em Defini&ccedil;&atilde;o");

		return sorteioCidadaoDTO;
	}

	@Override
	public RegraSorteio buscaSorteioPorId(Integer idSorteio) {
		return getRegraSorteio();
	}

	@Override
	public Integer totalDePontosPorSorteio(Integer idSorteio, String cpf) {
		return 100;
	}

	@Override
	public Long retornaTotalDeBilhetes(String cpf, Integer idSorteio) {
		return 10L;
	}

	@Override
	public List<BilhetePessoa> listBilhetes(String cpf, Integer idSorteio) {
		
		BilhetePessoa bp = new BilhetePessoa();
		
		bp.setId(1);
		bp.setIndiNumeroBilheteDefinitivo('S');
		bp.setNumeroSequencial(123);
		bp.setPessoaParticipante(MockCidadao.getPessoaParticipante(cpf));
		bp.setRegraSorteio(getRegraSorteio());
		
		List<BilhetePessoa> listBilhetePessoa = new ArrayList<>();
		listBilhetePessoa.add(bp);
		
		return listBilhetePessoa;
	}

	@Override
	public List<Map<String,DTOBilhetePessoa>> listBilhetePaginado(String cpf, Integer idSorteio, Integer max, Integer page) {
	     try{
	            List<Map<String,DTOBilhetePessoa>> listaDeMapBilhetes = new ArrayList<>();

	            int nrColunas = 3;
				List<DTOBilhetePessoa> bilhetesPremiados = listarBilhetesPremiados(max, page, nrColunas , idSorteio, cpf);

	            for (int i=0; i<bilhetesPremiados.size() ; i+=nrColunas){
	            	
	                Map<String,DTOBilhetePessoa> mapDeBilhetes = new HashMap<String, DTOBilhetePessoa>();
	                
	                if (i <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete1col",(DTOBilhetePessoa) bilhetesPremiados.get(i));

	                if (i+1 <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete2col",(DTOBilhetePessoa) bilhetesPremiados.get(i+1));

	                if (i+2 <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete3col",(DTOBilhetePessoa) bilhetesPremiados.get(i+2));

	                listaDeMapBilhetes.add(mapDeBilhetes);
	            }

	            return  listaDeMapBilhetes;
	        }catch (Exception e){
	            throw new NFGException("Algo de errado ocorreu ao tentar listar os bilhetes");
	        }
	}

	private List<DTOBilhetePessoa> listarBilhetesPremiados(Integer max, Integer page, int nrColunas, Integer idSorteio, String cpf) {
		
		DTOBilhetePessoa bilhetePessoa = new DTOBilhetePessoa();
		
		bilhetePessoa.setBilheteDefinitivo("S");
		bilhetePessoa.setIdBilhete(1);
		bilhetePessoa.setIdSorteio(12);
		bilhetePessoa.setNumero(123);
		bilhetePessoa.setPremiado("N");
		
		List<DTOBilhetePessoa> list = new ArrayList<>();
		list.add(bilhetePessoa);
		
		return list;
	}

	@Override
	public List<Map<?,?>> consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf, Integer max, Integer page) {
		
		List<DTOMinhasNotas> resultList = MockCidadao.listDtoMinhasNotas();
		
        List<Map<?,?>> listOfMapResults = new ArrayList<>();
        
        for(DTOMinhasNotas obj : resultList) {
            listOfMapResults.add(UtilReflexao.obterAtributos(obj));
        }
        
		return listOfMapResults;
	}

	@Override
	public Integer consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf) {
		return 123;
	}
	
	public static void main(String[] args) {
		
		int valorMaxSorteio = 99999;
		int valorMaxBilhetes = 100000;
		
		int valorSorteado = RandomUtils.nextInt(1, valorMaxSorteio);
		
		System.out.println("Número sortedo: "+valorSorteado);
		
		BigDecimal vlSorteio = BigDecimal.ZERO;
		BigDecimal porcentagem = new BigDecimal(100);
		BigDecimal maxSorteio = new BigDecimal(valorMaxSorteio);
		BigDecimal maxBilhete = new BigDecimal(valorMaxBilhetes);
		
		BigDecimal sorteio = new BigDecimal(valorSorteado);
		vlSorteio = sorteio.multiply(porcentagem).divide(maxSorteio, MathContext.DECIMAL32);
		
		BigDecimal vlSorteioRound = new BigDecimal(Math.round(vlSorteio.doubleValue()));
		
		BigDecimal resultado = maxBilhete.multiply(vlSorteioRound.divide(porcentagem, MathContext.DECIMAL32));
		
		int inicioBilhetePremiado = resultado.intValue();
		int fimBilhetePremiado = inicioBilhetePremiado + 1;
		
		if (valorMaxBilhetes < fimBilhetePremiado){
			
			int diferenca = fimBilhetePremiado - valorMaxBilhetes;
			
			inicioBilhetePremiado = inicioBilhetePremiado - diferenca;
			fimBilhetePremiado = inicioBilhetePremiado + 1;
		}
		
		System.out.println("Bilhetes sorteados de "+inicioBilhetePremiado+" até "+ fimBilhetePremiado);
	}
}