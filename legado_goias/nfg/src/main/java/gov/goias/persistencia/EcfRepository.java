package gov.goias.persistencia;

import gov.goias.entidades.ECF;
import gov.goias.exceptions.NFGException;
import gov.goias.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Letícia Álvares on 02/06/2016.
 */
public abstract class EcfRepository extends GenericRepository<Integer, ECF> {

    @Autowired
    private static final Logger logger = Logger.getLogger(EcfRepository.class);

    @Autowired
    protected JdbcTemplate jdbcTemplateMFD;


    public boolean bTodosSemCPFCNPJ;
    private Integer inscricao;
    private boolean bTemCupom;
    private String refeArquivo;
    private String cnpjContribuinte;
    private boolean retificadora;

    private String numFabricECF;
    private String caminhoCompleto;


    /*
    Lê o arquivo completo e grava num ArrayList, que é retornado no final da mesma
     */
    public ArrayList<String> leArquivoParaArray(String arqCaminhoCompleto){

        String line;
        ArrayList<String> linhasArquivo = new ArrayList<String>();

        try {

            File file = new File(arqCaminhoCompleto);

            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            while ((line = br.readLine()) != null) {
                linhasArquivo.add(line);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            logger.error("Exception: " + e.getStackTrace());//Coloquei o stack pois podem ocorrer erros nao premeditados.
            return null;
        }

        return linhasArquivo;
    }


    /*
        Primeira validação do arquivo ECF.
        Valida apenas o layout, para ver se tem todos os registros necessários e na ordem certa.
        Rejeita os arquivos de layout errado.
        Também verifica se tem ou não cupom fiscal, mas sem rejeitar caso não tenha - apenas registra para fins de alerta.

     */
    public ArrayList<String> validarLayoutArquivoECF(ArrayList<String> linhasArquivo){

        ArrayList<String> erros =  new ArrayList<String>();
        boolean existeE01,existeE02, existeE14, existeE15;

        existeE01 = false;
        existeE02 = false;
        existeE14 = false;
        existeE15 = false;

        String linha = "";

        //O arquivo tem que ter no mínimo as linhas E01 e E02
        if (linhasArquivo.size() < 2){
            erros.add("O arquivo não tem os registros obrigatórios E01 e E02.");
            return erros;
        }


        //Verifica se o registro E01 existe e é válido
        if (linhasArquivo.get(0).substring(0, 3).equals("E01")) {
            existeE01 = true;
           Integer tamanhoLinha = linhasArquivo.get(0).length();
            if (!tamanhoLinha.equals( new Integer(166))){
                erros.add("E01 deveria conter 166 caracteres, mas contém " + tamanhoLinha.toString());
                return erros;
            }

        }
        else {
            erros.add("Cabeçalho do arquivo inválido. Não há registro E01 na linha 01 e o mesmo é obrigatório.");
            return erros;
        }

        //Verifica se o registro E02 existe e é válido
        if (linhasArquivo.get(1).substring(0, 3).equals("E02")) {
            existeE02 = true;
            Integer tamanhoLinha = linhasArquivo.get(1).length();
            if (!tamanhoLinha.equals( new Integer(272))){
                erros.add("E02 deveria conter 272 caracteres, mas contém " + tamanhoLinha.toString());
                return erros;
            }
        }
        else {
            erros.add("Cabeçalho do arquivo inválido. Não há registro E02 na linha 02 e o mesmo é obrigatório.");
            return erros;
        }


        //Verifica se o registro E01 está conectado ao E02
        if ( (! linhasArquivo.get(0).substring(3, 23).equals(linhasArquivo.get(1).substring(3, 23)))
           || (! linhasArquivo.get(0).substring(23, 23).equals(linhasArquivo.get(1).substring(23, 23)))
           || (! linhasArquivo.get(0).substring(51, 71).equals(linhasArquivo.get(1).substring(24, 44)))
           || (! linhasArquivo.get(0).substring(98, 112).equals(linhasArquivo.get(1).substring(44, 58)))){

            erros.add("Cabeçalho do arquivo inválido. Registro informado na linha E02 não associado ao registro E01 através da chave Número de Fabricação, MF Adicional, Modelo e CNPJ.");
            return erros;

        }

        if (!existeE01){
            erros.add("Conteúdo do arquivo inválido. Arquivo deve possuir ao menos um registro tipo E01.");
            return erros;
        }

        if (!existeE02){
            erros.add("Conteúdo do arquivo inválido. Arquivo deve possuir ao menos um registro tipo E02.");
            return erros;
        }


        for (Iterator iterator = linhasArquivo.iterator(); iterator.hasNext();) {

            linha = (String) iterator.next();
            if (linha.trim().equals("")) {
                continue;
            }


            //Há mais de um registro E01?
            if (!linha.equals(linhasArquivo.get(0)) && linha.substring(0,3).equals("E01")){
                erros.add("Há mais de um registro tipo E01. É permitido apenas um registro deste tipo.");
                return erros;
            }

            //Há mais de um registro E02?
            if (!linha.equals(linhasArquivo.get(1)) && linha.substring(0,3).equals("E02")){
                erros.add("Há mais de um registro tipo E02. É permitido apenas um registro deste tipo.");
                return erros;
            }

            //Adicionando validações de E12 e E13
            if (linha.substring(0,3).equals("E12")) {
                Integer tamanhoLinha = linha.length();
                if (!tamanhoLinha.equals( new Integer(101))){
                    erros.add("E12 deveria conter 101 caracteres, mas contém " + tamanhoLinha.toString());
                    return erros;
                }
            }

            if (linha.substring(0,3).equals("E13")) {
                Integer tamanhoLinha = linha.length();
                if (!tamanhoLinha.equals( new Integer(72))){
                    erros.add("E13 deveria conter 72 caracteres, mas contém " + tamanhoLinha.toString());
                    return erros;
                }
            }

            //Verificando se existem E14 e E15
            if (linha.substring(0,3).equals("E14")) {
                existeE14 = true;
                Integer tamanhoLinha = linha.length();
                if (!tamanhoLinha.equals( new Integer(191))){
                    erros.add("E14 deveria conter 191 caracteres, mas contém " + tamanhoLinha.toString());
                    return erros;
                }

                //Não pode ter mais E14 depois que já teve E15
                if (existeE15){
                    erros.add("Registros fora de ordem. Não é permitido registros E14 após registros E15.");
                    return erros;
                }
            }
            if (linha.substring(0,3).equals("E15")) {
                existeE15 = true;
                Integer tamanhoLinha = linha.length();
                if (!tamanhoLinha.equals( new Integer(267))){
                    erros.add("E15 deveria conter 267 caracteres, mas contém " + tamanhoLinha.toString());
                    return erros;
                }

                //Não pode ter E15 se não teve E14
                if (!existeE14){
                    erros.add("Registros fora de ordem. Não é permitido registros E15 antes de registros E14.");
                    return erros;
                }

            }
        }

        if (!existeE14 && !existeE15) this.bTemCupom = false;
            else this.bTemCupom = true;

        return erros;
    }


    /*
    Valida o arquivo apenas para alertas, sem rejeitar.
     */
    public ArrayList<String> validaEstruturaAlertaECF(ArrayList<String> linhasArquivo) {

        String linha;
        ArrayList<String> retorno = new ArrayList<String>();
        int numLinha = 1;

        //Preciso garantir que o tipo de cada campo seja respeitado
        for (Iterator iterator = linhasArquivo.iterator(); iterator.hasNext(); ) {
            linha = (String) iterator.next();

            if (linha.trim().equals("")) continue;

            String sID = linha.substring(0, 3);

            //{**** E01 - Identificação do ECF ****}
            if (sID.equals("E01")) {
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "96 a 98", "10", linha.substring(95, 98), "N", "Null", "E01 - Número de ordem sequencial do ECF no estabelecimento usuário possui caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "96 a 98", "10", linha.substring(95, 98), "N", "N<>0", "E01 - Número de ordem sequencial do ECF no estabelecimento usuário composto apenas por zeros."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "96 a 98", "10", linha.substring(95, 98), "N", "N<>B", "E01 - Número de ordem sequencial do ECF no estabelecimento usuário composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "113 a 115", "14", linha.substring(112, 115), "X", "X=TDM", "E01 - Código do comando utilizado para gerar o arquivo difere de TDM."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "152 a 166", "20", linha.substring(151, 166), "X", "X<>B", "E01 - Versão do Ato COTEPE informado em branco."));
                //Valida o período
                retorno.addAll(this.validaPeriodoAlertas(linha.substring(127, 135), linha.substring(135, 143)));

            }
            //{**** E02 - Identificação do Atual contribuinte usuário do ECF ****}
            else if (sID.equals("E02")) {
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "73 a 112", "7", linha.substring(72, 112), "X", "X<>B", "E02 - Nome comercial (razão social/denominação) do contribuinte informado apenas com espaços."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "113 a 232", "8", linha.substring(112, 120), "X", "X<>B", "E02 - Endereço do estabelecimento usuário do ECF informado apenas com espaços."));
            }
            //{**** E14 - Cupom Fiscal, Nota Fiscal de Venda a Consumidor e Bilhete de Passagem ****}
            else if (sID.equals("E14") && !linha.substring(122,123).equalsIgnoreCase("S") ) {
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "137 a 137", "17", linha.substring(136, 137), "X", "X=[D,A, ]", "E14 - Indicador de ordem de aplicação de desconto/acréscimo em Subtotal diferente de \"D\", \"A\" ou branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "178 a 191", "19", linha.substring(177, 191), "N", "CPFCNPJ", "E14 - CPF/CNPJ informado com dígito verificador inválido."));
            }
            //{**** E15 - Cupom Fiscal, Nota Fiscal de Venda a Consumidor e Bilhete de Passagem ****}
            else if (sID.equals("E15") && !linha.substring(230,231).equalsIgnoreCase("S") ) {
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 61", "8", linha.substring(58, 61), "N", "N<>CONS[" + linhasArquivo.get(numLinha-2).substring(58,61)+"]" , "E15 - Número do item não consecutivo ao item anterior do mesmo documento."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "210 a 223", "16", linha.substring(209, 223), "N", "NVAL<>["
                        + this.calculaValor(linha.substring(185, 193), linha.substring(193, 201), linha.substring(201, 209), linha.substring(175, 182), linha.substring(265, 266), linha.substring(266, 267), 16) + "]"
                        , "E15 - Valor total líquido difere do resultado do (Valor unitário * Quantidade) - Descontos + Acréscimos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "232 a 238", "19", linha.substring(231, 238), "N", "NVAL>[" + linha.substring(175, 182)+"]" , "E15 - Valor da Quantidade cancelada superior ao informado no campo quantidade."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "239 a 251", "20", linha.substring(238, 251), "N", "NVAL>["
                        + this.calculaValor(linha.substring(185, 193), "0", "0", linha.substring(231, 238), linha.substring(265, 266), linha.substring(266, 267), 20) + "]"
                        , "E15 - Valor cancelado superior ao resultado da (Quantidade cancelada * Valor unitário"));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "265 a 265", "22", linha.substring(264, 265), "X", "X=[T,A]", "E15 - Indicador de arredondamento ou truncamento diferente de \"T\" e \"A\" "));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "266 a 266", "23", linha.substring(265, 266), "N", "Nin[0..3]", "E15 - Casas decimais da quantidade deve estar entre 0 e 3."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "266 a 266", "23", linha.substring(265, 266), "N", "N<>B", "E15 - Casas decimais da quantidade composto apenas por espaços em branco."));
            }
            else if (sID.equals("E19") && !linha.substring(122,123).equalsIgnoreCase("S") ) {
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "178 a 191", "19", linha.substring(177, 191), "N", "CPFCNPJ", "E14 - CPF/CNPJ informado com dígito verificador inválido."));
            }

            numLinha++;
        }
        return retorno;
    }

    /*
        Método para calcular valor e verificar se está correto, avaliando junto com descontos, acréscimos e quantidade.
        Usado na validaEstruturaAlertaECF

     */

    public String calculaValor(String valUnit, String valDesc, String valAcresc, String qtde, String cDecQ, String cDecVU, Integer iCType){
        String resultado = "";
        Float fQtde,cValUnit, cResult, cValDesc, cValAcresc;

        cValUnit = new Float(0);
        fQtde = new Float(0);



        try {
            Integer iCDecQ = new Integer(cDecQ);
            Integer iCDecVU = new Integer(cDecVU);

            switch (iCDecQ){
                case 0:
                    fQtde = new Float(qtde);
                    break;
                case 1:
                    fQtde = new Float(qtde.substring(0,6) + "." + qtde.substring(6,7));
                    break;
                case 2:
                    fQtde = new Float(qtde.substring(0,5) + "." + qtde.substring(5,7));
                    break;
                case 3:
                    fQtde = new Float(qtde.substring(0,4) + "." + qtde.substring(4,7));
                    break;
            }

            switch (iCDecVU){
                case 2:
                    cValUnit = new Float(valUnit.substring(0,6) + "." + valUnit.substring(6,8));
                    break;
                case 3:
                    cValUnit = new Float(valUnit.substring(0,5) + "." + valUnit.substring(5,8));
            }

            switch (iCType){
                case 16:
                    cValDesc = new Float(valDesc.substring(0,6) + "." + valDesc.substring(6,8));
                    cValAcresc = new Float(valAcresc.substring(0,6) + "." + valAcresc.substring(6,8));
                    BigDecimal valorExato = new BigDecimal(( cValUnit*fQtde) - cValDesc + cValAcresc).setScale(2, RoundingMode.HALF_DOWN);
                    resultado  = valorExato.toString();
                    break;
                case 20:
                    valorExato = new BigDecimal(fQtde * cValUnit);
                    resultado  = valorExato.toString();
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    /*
        Valida estrutura ECF. Caso caia em uma das situações validadas, REJEITA o arquivo
     */
    public ArrayList<String> validaEstruturaRejeitaECF(ArrayList<String> linhasArquivo){

        String linha;
        ArrayList<String> retorno = new ArrayList<String>();
        int numLinha = 1;

        //Preciso garantir que o tipo de cada campo seja respeitado
        for (Iterator iterator = linhasArquivo.iterator(); iterator.hasNext();) {
            linha = (String) iterator.next();

            if (linha.trim().equals("")) continue;

            String sID = linha.substring(0, 3) ;

            if (sID.equals("E01")){
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"4 a 23","2",linha.substring(3, 23),"X","X<>B","E01 - Número de fabricação do ECF composto apenas por espaços em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"4 a 23","2",linha.substring(3, 23),"X","X<>0","E01 - Número de fabricação do ECF composto apenas por zeros." )  );
                this.setNumFabricECF(linha.substring(3, 23));
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"99 a 112","11",linha.substring(98, 112),"N","N<>B","E01 - CNPJ do estabelecimento usuário do ECF composto apenas por espaços em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"99 a 112","11",linha.substring(98, 112),"N","SOCNPJ","E01 - CNPJ do estabelecimento usuário do ECF inválido." )  );
                this.setCnpjContribuinte(linha.substring(98, 112));
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"128 a 135","17",linha.substring(127, 135),"D","Null","E01 - Data inicial do período a ser capturado inválida." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"128 a 135","17",linha.substring(127, 135),"D","D<>B","E01 - Data inicial do período a ser capturado informada em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"128 a 135","17",linha.substring(127, 135),"D","D<2014","E01 - Data inicial do período superior a 01/12/2014." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"136 a 143","18",linha.substring(135, 143),"D","Null","E01 - Data final do período a ser capturado inválida." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"136 a 143","18",linha.substring(135, 143),"D","D<>B","E01 -Data final do período a ser capturado informada em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"128 a 135","17",linha.substring(127, 135),"D","D>MESPOS","E01 - Data inicial do período a ser capturado com mês superior ao permitido." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"136 a 143","18",linha.substring(135, 143),"D","D>MESPOS","E01 - Data final do período a ser capturado com mês superior ao permitido." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"136 a 143","18",linha.substring(135, 143),"D","DTM=" + linha.substring(127,135),"E01 - Data final do período com mês informado diferente daquele informado para a Data inicial do período." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"136 a 143","18",linha.substring(135, 143),"D","DTP=" + linha.substring(127,135),"E01 - Data inicial do período capturado deve ser anterior a Data final do período." )  );
                retorno.addAll(this.validaObrigadoEFD(linha.substring(127, 135), linha.substring(135, 143)));
            }
            else if (sID.equals("E02")){
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"4 a 23","2",linha.substring(3, 23),"X","X<>B","E02 - Número de fabricação do ECF composto apenas por espaços em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"4 a 23","2",linha.substring(3, 23),"X","X<>0","E02 - Número de fabricação do ECF composto apenas por zeros." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"45 a 58","5",linha.substring(44, 58),"N","N<>B","E02 - CNPJ do estabelecimento usuário do ECF composto apenas por espaços em branco." )  );
                retorno.addAll(this.avaliaString( String.valueOf(numLinha),"45 a 58","5",linha.substring(44, 58),"N","SOCNPJ","E02 - CNPJ do estabelecimento usuário do ECF inválido." )  );
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 72", "6", linha.substring(58, 72), "X", "X<>B", "E02 - Inscrição Estadual do estabelecimento usuário composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 72", "6", linha.substring(58, 72), "X", "X=N", "E02 - Inscrição Estadual do estabelecimento usuário possui caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 72", "6", linha.substring(58, 72), "X", "IE", "E02 - Inscrição Estadual do estabelecimento usuário é inválida."));
            }
            else if (sID.equals("E13")){
                //Validar totalizador parcial
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 59", "7", linha.substring(52, 59), "X", "TOT_PARCIAL_COTEPE", "E13 - Totalizador parcial não correspondente aos da tabela do ATO COTEPE/ICMS N° 17, DE 29 DE MARÇO DE 2004"));
            }
            //{**** E14 - Cupom Fiscal, Nota Fiscal de Venda a Consumidor e Bilhete de Passagem ****}
            //{ Não valido linhas com S no indicador de cancelamento }
            else if (sID.equals("E14")){
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "4 a 23", "2", linha.substring(3, 23), "X", "X<>E01["+linhasArquivo.get(0).substring(3, 23)+"]", "E14 - Número de fabricação do ECF diverge daquele informado no registro E01. Arquivo possui cupons fiscais emitidos por mais de um equipamento Emissor de Cupons Fiscais."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "4 a 23", "2", linha.substring(3, 23), "X", "X<>B", "E14 - Número de fabricação do ECF composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "4 a 23", "2", linha.substring(3, 23), "X", "X<>0", "E14 - Número de fabricação do ECF composto apenas por zeros."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "Null", "E14 - Número do Contador (CCF, CVC ou CBP) composto de caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "N<>0", "E14 - Número do Contador (CCF, CVC ou CBP) informado com conteúdo zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "N<>B", "E14 - Número do Contador (CCF, CVC ou CBP) composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "Null", "E14 - Número do COO composto de caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "N<>0", "E14 - Número do COO informado com valor zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "N<>B", "E14 - Número do COO composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 66", "7", linha.substring(58, 66), "D", "Null", "E14 - Data de início da emissão do documento inválida."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 66", "7", linha.substring(58, 66), "D", "D<>B", "E14 - Data de início da emissão do documento informada em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 66", "7", linha.substring(58, 66), "D", "Din[" + linhasArquivo.get(0).substring(127,135)+"]", "E14 - Data de início da emissão do documento está fora do período informado nos campos 15 e 16 do registro E01."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "67 a 80", "9", linha.substring(66, 80), "N", "Null", "E14 - Subtotal do documento composto de caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "67 a 80", "9", linha.substring(66, 80), "N", "N<>B", "E14 - Subtotal do documento composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "81 a 93", "10", linha.substring(80, 93), "N", "Null", "E14 - Valor do desconto ou percentual de desconto sobre valor do subtotal do documento não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "81 a 93", "10", linha.substring(80, 93), "N", "N<>B", "E14 - Valor do desconto ou percentual de desconto sobre valor do subtotal do documento composto apeas por espaços em branco."));
                if (linha.substring(93,94).equals("V")){
                    retorno.addAll(this.avaliaString(String.valueOf(numLinha), "81 a 93", "10", linha.substring(80, 93), "N", "NVAL>[" + linha.substring(66,80)+"]", "E14 - Valor do desconto sobre valor do subtotal do documento superior ao valor informado no campo Subtotal do Documento."));
                }
                else if (linha.substring(93,94).equals("P")){
                    retorno.addAll(this.avaliaString(String.valueOf(numLinha), "81 a 93", "10", linha.substring(80, 93), "N", "NVAL>[10000]", "E14 - Percentual de desconto sobre valor do subtotal do documento superior a 100%."));
                }
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "94 a 94", "11", linha.substring(93, 94), "X", "X=[V,P, ]", "E14 - Indicador do tipo de desconto sobre subtotal difrente de \"V\", \"P\" ou branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "95 a 107", "12", linha.substring(94, 107), "N", "Null", "E14 - Valor do acréscimo sobre o subtotal não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "95 a 107", "12", linha.substring(94, 107), "N", "N<>B", "E14 - Valor do acréscimo sobre o subtotal composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "108 a 108", "13", linha.substring(107, 108), "X", "X=[V,P, ]", "E14 - Indicador do tipo de acréscimo sobre subtotal diferente de \"V\", \"P\" ou branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "109 a 122", "14", linha.substring(108, 122), "N", "Null", "E14 - Valor total líquido não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "109 a 122", "14", linha.substring(108, 122), "N", "N<>B", "E14 - Valor total líquido composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "109 a 122", "14", linha.substring(108, 122), "N", "CURR<>0", "E14 - Valor total líquido zerado."));
                if (!linha.substring(122,123).equals("S")){
                    retorno.addAll(this.avaliaString(String.valueOf(numLinha), "109 a 122", "14", linha.substring(108, 122), "N", "CURR<=MAX", "E14 - Valor total líquido superior a R$ 3.600.000,00."));
                }
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "123 a 123", "15", linha.substring(122, 123), "X", "X=[S,N]", "E14 - Indicador de cancelamento diferente de \"S\" ou \"N\"."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "124 a 136", "16", linha.substring(123, 136), "N", "Null", "E14 - Valor do cancelamento de acréscimo sobre o subtotal não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "124 a 136", "16", linha.substring(123, 136), "N", "N<>B", "E14 - Valor do cancelamento de acréscimo sobre o subtotal composto apenas por espaços em branco."));

                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "178 a 191", "19", linha.substring(177, 191), "N", "Null", "E14 - CPF/CNPJ informado não é numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "178 a 191", "19", linha.substring(177, 191), "N", "N<>B", "E14 - CPF/CNPJ composto apenas por espaços em branco."));
            }
            // {**** E15 - Detalhe do Cupom Fiscal, da Nota Fiscal de Venda a Consumidor ou do Bilhete de Passagem ****}
            // { Não valido linhas com S no indicador de cancelamento }
            else if (sID.equals("E15")){

                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "4 a 23", "2", linha.substring(3, 24), "X", "X<>B", "E15 - Número de fabricação do ECF composto apenas por espaços."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "4 a 23", "2", linha.substring(3, 24), "X", "X<>0", "E15 - Número de fabricação do ECF composto apenas por espaços."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "Null", "E15 - Número do COO composto de caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "N<>0", "E15 - Número do COO informado com valor zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "47 a 52", "6", linha.substring(46, 52), "N", "N<>B", "E15 - Número do COO composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "Null", "E15 - Número do contador (CCF, CVC ou CBP) composto de caracteres não numéricos."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "N<>0", "E15 - Número do contador (CCF, CVC ou CBP) informado com conteúdo zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "53 a 58", "7", linha.substring(52, 58), "N", "N<>B", "E15 - Número do contador (CCF, CVC ou CBP) composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 61", "8", linha.substring(58, 61), "N", "Null", "E15 - Número do item não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 61", "8", linha.substring(58, 61), "N", "N<>0", "E15 - Número do item igual a zero."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "59 a 61", "8", linha.substring(58, 61), "N", "N<>B", "E15 - Número do item composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "62 a 75", "9", linha.substring(61, 75), "X", "X<>B", "E15 - Código do produto ou serviço composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "76 a 175", "10", linha.substring(75, 175), "X", "X<>B", "E15 - Descrição do produto ou serviço composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "176 a 182", "11", linha.substring(175, 182), "N", "Null", "E15 - Quantidade do item não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "176 a 182", "11", linha.substring(175, 182), "N", "N<>0", "E15 - Quantidade do item zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "176 a 182", "11", linha.substring(175, 182), "N", "N<>B", "E15 - Quantidade do item composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "186 a 193", "13", linha.substring(185, 193), "N", "Null", "E15 - Valor unitário do produto não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "186 a 193", "13", linha.substring(185, 193), "N", "N<>B", "E15 - Valor unitário do produto composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "186 a 193", "13", linha.substring(185, 193), "N", "CURR<>0", "E15 - Valor unitário do produto zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "186 a 193", "13", linha.substring(185, 193), "N", "CURR<=MAX", "E15 - Valor unitário do produto superior a R$ 3.600.000,00."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "194 a 201", "14", linha.substring(193, 201), "N", "Null", "E15 - Valor do desconto sobre o item não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "194 a 201", "14", linha.substring(193, 201), "N", "N<>B", "E15 - Valor do desconto sobre o item composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "194 a 201", "14", linha.substring(193, 201), "N", "CURR<=MAX", "E15 - Valor do desconto sobre o item superior a R$ 3.600.000,00."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "202 a 209", "15", linha.substring(201, 209), "N", "Null", "E15 - Valor do acréscimo sobre o item não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "202 a 209", "15", linha.substring(201, 209), "N", "N<>B", "E15 - Valor do acréscimo sobre o item composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "202 a 209", "15", linha.substring(201, 209), "N", "CURR<=MAX", "E15 - Valor do acréscimo sobre o item superior a R$ 3.600.000,00."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "210 a 223", "16", linha.substring(209, 223), "N", "Null", "E15 - Valor total líquido não númerico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "210 a 223", "16", linha.substring(209, 223), "N", "N<>B", "E15 - Valor total líquido composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "210 a 223", "16", linha.substring(209, 223), "N", "CURR<>0", "E15 - Valor total líquido zerado."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "210 a 223", "16", linha.substring(209, 223), "N", "CURR<=MAX", "E15 - Valor total líquido superior a R$ 3.600.000,00."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "224 a 230", "17", linha.substring(223, 230), "X", "TOT_PARCIAL_COTEPE", "E15 - Totalizador parcial não correspondente aos da tabela do ATO COTEPE/ICMS N° 17, DE 29 DE MARÇO DE 2004"));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "231 a 231", "18", linha.substring(230, 231), "X", "X=[S,N,P]", "E15 - Indicador de cancelamento diferente de \"S\", \"N\" ou \"P\"."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "232 a 238", "19", linha.substring(231, 238), "N", "Null", "E15 - Valor da quantidade cancelada não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "232 a 238", "19", linha.substring(231, 238), "N", "N<>B", "E15 - Valor da quantidade cancelada composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "239 a 251", "20", linha.substring(238, 251), "N", "Null", "E15 - Valor cancelado não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "239 a 251", "20", linha.substring(238, 251), "N", "N<>B", "E15 - Valor cancelado composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "252 a 264", "21", linha.substring(251, 264), "N", "Null", "E15 - Valor do cancelamento de acréscimo do item não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "252 a 264", "21", linha.substring(251, 264), "N", "N<>B", "E15 - Valor do cancelamento de acréscimo do item composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "266 a 266", "23", linha.substring(265, 266), "N", "Null", "E15 - Casas decimais da quantidade não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "266 a 266", "23", linha.substring(265, 266), "N", "N<>B", "E15 - Casas decimais da quantidade composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "267 a 267", "24", linha.substring(266, 267), "N", "Null", "E15 - Casas decimais de valor unitário não numérico."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "267 a 267", "24", linha.substring(266, 267), "N", "N<>B", "E15 - Casas decimais de valor unitário composto apenas por espaços em branco."));
                retorno.addAll(this.avaliaString(String.valueOf(numLinha), "267 a 267", "24", linha.substring(266, 267), "N", "Nin[2..3]", "E15 - Casas decimais de valor unitário deve ser igual a 2 ou 3."));
            }

            numLinha++;
        }

        return retorno;
    }

    /*
    Método principal de validação dos campos.
    Na hora de chamar o método, é passado qual o tipo de validação a ser feita.
     */
    private ArrayList<String> avaliaString(String sLinha, String sColuna, String sCampo, String sStr, String sTipo, String sConteudo, String sMsg){
        ArrayList<String> erros =  new ArrayList<String>();
        boolean bTodosSemCPFCNPJ = true;

        Float cLimiteVal = new Float(3600000);

        //Validações para campos numéricos
        if (sTipo.equals("N")){

            if (!sStr.trim().equals("") && !sConteudo.contains("N<>B")){


                if (sConteudo.contains("Null")){
                    try{
                        Float a = Float.valueOf(sStr);
                    }
                    catch(NumberFormatException  e){
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com dados alfa-numéricos - '" +sStr  + "'. Registro/Erro: '" + sMsg);
                    }

                    return erros;
                }


                //Verifica se o valor é negativo
                if (sStr.contains("-")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com valor negativo -'" +sStr  + "'. Registro/Erro: '" + sMsg);
                }


                //Verifica se o valor é maior do que zero para valor monetario
                if (sConteudo.contains("CURR<>0")){
                    try{
                        Float a = Float.valueOf(sStr);
                        if (a.compareTo(new Float(0)) <0){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo monetário com valor inválido - '" +sStr  + "'. Registro/Erro: '" + sMsg);
                        }
                    }
                    catch (NumberFormatException e){
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" +sStr  + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Verifica se o valor é maior do que zero para Inteiros
                if (sConteudo.contains("N<>0")){
                    try {
                        Integer a = Integer.valueOf(sStr);
                        if (a.compareTo(new Integer(0)) < 0) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo númerico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }

                //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM **************
                //Verifica se o valor é igual a zero para Inteiros
                if (sConteudo.contains("N=0")){
                    try {
                        Integer a = Integer.valueOf(sStr);
                        if (!(a.compareTo(new Integer(0)) == 0)) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo númerico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Verifica se o valor é superior ao informado no campo
                if (sConteudo.contains("NVAL>[")){
                    String sAux = sConteudo.substring(sConteudo.indexOf("[")+1, sConteudo.indexOf("]") );
                    try {
                        Float a = Float.valueOf(sAux);
                        Float s = Float.valueOf(sStr);
                        if (s.compareTo(a) > 0) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com valor superior ao esperado - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Verifica se o valor é diferente ao informado no campo
                if (sConteudo.contains("NVAL<>[")){
                    String sAux = sConteudo.substring(sConteudo.indexOf("[")+1, sConteudo.indexOf("]") );
                    String sAuxStr = sStr.substring(0,12) + "." + sStr.substring(12,14);

                    try {
                        Float a = Float.valueOf(sAux);
                        Float s = Float.valueOf(sAuxStr);

                        if (!(a.compareTo(s) == 0)){
                               if ((s.compareTo(new Float(a - 0.01)) < 0) || (s.compareTo(new Float(a + 0.01)) > 0)){
                                   erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com valor diferente do esperado - '" + sStr + "'. Registro/Erro: '" + sMsg);
                               }
                        }

                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Verifica se o valor é consecutivo
                if (sConteudo.contains("N<>CONS[")){
                    String sAux = sConteudo.substring(sConteudo.indexOf("[")+1, sConteudo.indexOf("]") );

                    try {
                        Integer a = Integer.valueOf(sStr);
                        Integer s = Integer.valueOf(sAux);

                        if ((!(a.compareTo(new Integer(1)) == 0)) && (!(a.compareTo(new Integer(s+1)) == 0))) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com valor diferente do esperado - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Verifica se o valor é maior do que o valor máximo permitido
                if (sConteudo.contains("CURR<=MAX")){

                    try {
                        Float a = Float.valueOf(sStr);
                        a = a/100;
                        if (a.compareTo(cLimiteVal) > 0){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo monetário com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }

                //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM **************
                //Se for CPF
                if (sConteudo.contains("SOCPF")){
                    ValidacaoDeCpf validacaoDeCpf = new ValidacaoDeCpf(sStr);
                    if (!validacaoDeCpf.isCpfValido()){
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }


                //Se for CNPJ
                if (sConteudo.contains("SOCNPJ")){
                    ValidacaoDeCnpj validacaoDeCnpj = new ValidacaoDeCnpj(sStr);
                    if (!validacaoDeCnpj.isValidCNPJ()){
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }

                //Se for CPF ou CNPJ
                if (sConteudo.contains("CPFCNPJ")){
                    if (!sStr.equals("00000000000000"))
                    {
                        bTodosSemCPFCNPJ = false;
                        ValidacaoDeCpf validacaoDeCpf = new ValidacaoDeCpf(sStr.substring(sStr.length()-11,sStr.length()));
                        if (!validacaoDeCpf.isCpfValido()){
                            ValidacaoDeCnpj validacaoDeCnpj = new ValidacaoDeCnpj(sStr);
                            if (!validacaoDeCnpj.isValidCNPJ()){
                                erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                            }
                        }
                    }
                    return erros;
                }


                //Se estiver em um intervalo de numeração
                if (sConteudo.contains("Nin")){
                    String sAux = sConteudo.substring(sConteudo.indexOf("Nin[")+4, sConteudo.indexOf("Nin[")+4 +1 );
                    String sAux1 = sConteudo.substring(sConteudo.indexOf("Nin[")+7, sConteudo.indexOf("Nin[")+7 +1 );

                    try {
                        Integer iAux = Integer.valueOf(sAux);
                        Integer iAux1 = Integer.valueOf(sAux1);
                        Integer iStr = Integer.valueOf(sStr);

                        if (!  ((iStr.compareTo(iAux) > 0) || (iStr.compareTo(iAux) == 0) && (iStr.compareTo(iAux1) < 0) || (iStr.compareTo(iAux1) ==0))  ) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }

                //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM **************
                //Se for S ou N
                if (sConteudo.contains("C=")){
                    String sAux = sConteudo.substring(sConteudo.indexOf("C=")+2, sConteudo.indexOf("C=")+2  +1 );

                    try {
                        Float a = Float.valueOf(sStr);
                        if ((sAux.equals("S")) && (!a.equals(new Float(0)))){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                        else if ((sAux.equals("N")) && (a.equals(new Float(0)))){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo numérico com dados alfa-numéricos - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }
            }

            //Verifica se o valor é diferente de Brancos
            else if (sConteudo.contains("N<>B")){
                if (sStr.trim().equals("")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }
        }


        //Validações para campos alfa-numérico
        else if (sTipo.equals("X")){

            //Verifica se o conteudo é diferente de espaços em branco
            if (sConteudo.contains("X<>B")){
                if (sStr.trim().equals("")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo alfa-numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM **************
            //Verifica se o conteudo é igual a espaços em branco
            if (sConteudo.contains("X=B")){
                if (sStr.trim().equals("")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo alfa-numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //Verifica se o conteudo é diferente zeros
            if (sConteudo.contains("X<>0")){
                if (!sStr.trim().equals("")){

                    TextUtils tu = new TextUtils();
                    if (tu.isNumeric(sStr)){
                        Integer a = null;
                        try {
                            a = new Integer(sStr);
                        } catch (NumberFormatException e) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo alfa-numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                        if (a.equals(new Integer(0))){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo alfa-numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    }
                }
                return erros;
            }


            //Verifica se o conteudo é numérico
            if (sConteudo.contains("X=N")){
                try {
                    Integer a = new Integer(sStr.trim());
                } catch (NumberFormatException e) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo não possui valor numérico -  '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


            //Validar inscricao estadual
            if (sConteudo.contains("IE")){
                if (!sStr.trim().equals(this.inscricao)){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Inscrição do arquivo não corresponde à inscrição em uso no sistema  '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


            //Valida TDM
            if (sConteudo.contains("X=TDM")){
                if (!sStr.trim().equals("TDM")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo alfa-numérico com valor inválido - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //Verifica se o conteudo é diferente do especificado para X. Ex. S ou N
            if (sConteudo.contains("X=[")){
                String sAux = sConteudo.substring(sConteudo.indexOf("[")+1, sConteudo.indexOf("]") );
                if (!sAux.contains(sStr)){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo com conteúdo inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //Verifica se o número de fabricação diverge do registro E01
            if (sConteudo.contains("X<>E01[")){
                String sAux = sConteudo.substring(sConteudo.indexOf("[")+1, sConteudo.indexOf("]") );
                if (!sStr.trim().equals(sAux.trim())){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo com conteúdo inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //Verifica se o totalizador parcial do cotepe está de acordo com a tabela
            if (sConteudo.contains("TOT_PARCIAL_COTEPE")){
                if (!this.validaTotalizadorParcial(sStr)){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ".  Situação: Campo totalizador parcial inválido -  '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


        }

        //Validações para campo de Data
        else if (sTipo.equals("D")) {
            if (!sStr.trim().equals("")) {

                if (sConteudo.contains("Null")) {
                    try {
                        Float a = Float.valueOf(sStr);
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico de data com dados alfa-numéricos  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }

                    String dataFormatada = sStr.substring(6, 8) + "/" + sStr.substring(4, 6) + "/" + sStr.substring(0, 4);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);
                    try {
                        Date data = sdf.parse(dataFormatada);
                    } catch (ParseException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                    return erros;
                }
            }

            //Verifica se o conteudo é diferente de espaços em branco
            if (sConteudo.contains("D<>B")) {
                if (sStr.trim().equals("")) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            // Data de emissao deve estar dentro do período definido nos campo 17 e 18 do E01
            //  Pode conter ate o dia seguinte da data final - 03-04-2014
            if (sConteudo.contains("Din[")) {
                String sAux01 = sConteudo.substring(sConteudo.indexOf("Din[") + 4, sConteudo.indexOf("Din[") + 4 + 6);
                try {
                    Integer iAux01 = Integer.valueOf(sAux01);
                    String sAux = sConteudo.substring(sConteudo.indexOf("Din[") + 4, sConteudo.indexOf("Din[") + 4 + 8);

                    Integer iStr01 = Integer.valueOf(sStr.substring(0,6));
                    if (!(iStr01.compareTo(iAux01) == 0)){
                        Integer iAux02 = new Integer(sAux.substring(4,6)) + 1;
                        Integer iAux03 = new Integer(sStr.substring(4,6));
                        if (! (iAux02.equals(iAux03) || sStr.substring(6,8).equals("01")) ){
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    }

                } catch (NumberFormatException e) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data com valor alfanumérico  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


            //Busca Mes da Data informada, deve ser igual
            if (sConteudo.contains("DTM=")) {
                String sAux01 = sConteudo.substring(sConteudo.indexOf("DTM=") + 4, sConteudo.length());
                if (!sAux01.substring(4, 6).equals(sStr.substring(4, 6))) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


            //Data inicial deve ser Anterior ao final do periodo
            if (sConteudo.contains("DTP=")) {
                String sAux01 = sConteudo.substring(sConteudo.indexOf("DTP=") + 4, sConteudo.length());

                try {
                    Integer iAux01 = Integer.valueOf(sAux01);
                    Integer iStr = Integer.valueOf(sStr);
                    if (sStr.substring(0, 8).compareTo(sAux01.substring(0, 8)) < 0) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                } catch (NumberFormatException e) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data com valor alfanumérico  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }


            //Valida se a data é inferior ao período mínimo
            if (sConteudo.contains("D<2014")) {
                if (!sStr.trim().equals("")) {
                    try {
                        Integer iAux = Integer.valueOf(sStr);
                        if (iAux.compareTo(new Integer(20141201)) < 0) {
                            erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                        }
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data com valor alfanumérico  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                }
                return erros;
            }


            //Valida data para ano inferior ao estipulado
            if (sConteudo.contains("D>MESPOS")) {
                //adiciona um dia ao mes atual
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 1);
                Date dt = cal.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
                String sAuxStr = sdf.format(dt);

                try {
                    Integer iAuxStr = Integer.valueOf(sAuxStr);
                    Integer iStr = Integer.valueOf(sStr);

                    if (iStr.compareTo(iAuxStr) > 0) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }

                } catch (NumberFormatException e) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de data com valor alfanumérico  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

        }
        //Validações para campo de Hora
        else if (sTipo.equals("H")) {

            if (!sStr.trim().equals("")){
                //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM **************
                if (sConteudo.contains("Null")){

                    try {
                        Float a = new Float(sStr);
                    } catch (NumberFormatException e) {
                        erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo numérico de hora com valor alfanumérico  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                    }
                }

                //********** NÃO FOI TESTADO - NÃO É USADO EM LUGAR NENHUM  **************
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String sHora = sStr.substring(0,2) + ":" + sStr.substring(2,4) +  ":" + sStr.substring(4,6);

                try {
                    Date dt = format.parse(sHora);
                } catch (ParseException e) {
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de hora inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }

            //********** NÃO FOI TESTADO  - NÃO É USADO EM LUGAR NENHUM **************
            //Verifica se o conteudo é diferente de espaços em branco
            if (sConteudo.contains("H<>B")) {
                if (sStr.trim().equals("")){
                    erros.add("Linha " + sLinha + ", Coluna " + sColuna + ". Campo Nro: " + sCampo + ". Situação: Campo de hora inválido  - '" + sStr + "'. Registro/Erro: '" + sMsg);
                }
                return erros;
            }
        }

        return erros;
    }

    /*
    Valida o campo totalizador parcial, conforme  Ato Cotepe.
     */
    private boolean validaTotalizadorParcial(String sStr){
        boolean bConformeCotepe = false;

        if (sStr.trim().length() == 2 ){

            if (sStr.substring(0,1).equals("F")) bConformeCotepe = true; //Enquadrado como Fn
            if (sStr.substring(0,1).equals("I")) bConformeCotepe = true; //Enquadrado como In
            if (sStr.substring(0,1).equals("N")) bConformeCotepe = true; //Enquadrado como Nn
            if (sStr.substring(0,2).equals("DO")) bConformeCotepe = true; //Enquadrado como DO
            if (sStr.substring(0,2).equals("AO")) bConformeCotepe = true; //Enquadrado como AO
            if (sStr.substring(0,2).equals("DT")) bConformeCotepe = true; //Enquadrado como DT
            if (sStr.substring(0,2).equals("CT")) bConformeCotepe = true; //?? Enquadrado como CT mas não tem na tabela
            if (sStr.substring(0,2).equals("DS")) bConformeCotepe = true; //Enquadrado como DS
            if (sStr.substring(0,2).equals("AT")) bConformeCotepe = true; //Enquadrado como AT
            if (sStr.substring(0, 2).equals("AS")) bConformeCotepe = true; //Enquadrado como AS

        }
        else if (sStr.trim().length() == 3 ){
            if (sStr.substring(0,2).equals("FS")) bConformeCotepe = true; //Enquadrado como FSn
            if (sStr.substring(0,2).equals("IOF")) bConformeCotepe = true; //Enquadrado como IOF
            if (sStr.substring(0,2).equals("IS")) bConformeCotepe = true; //Enquadrado como ISn
            if (sStr.substring(0,2).equals("NS")) bConformeCotepe = true; //Enquadrado como NSn
        }
        else if (sStr.trim().length() == 4 ){
            if (sStr.substring(0,4).equals("OPNF")) bConformeCotepe = true; //Enquadrado como OPNF
        }
        else if (sStr.trim().length() == 5 ){
            if (sStr.substring(0,5).equals("Can-T")) bConformeCotepe = true; //Enquadrado como Can-T
            if (sStr.substring(0,5).equals("Can-O")) bConformeCotepe = true; //Enquadrado como Can-O
            if (sStr.substring(0,5).equals("Can-S")) bConformeCotepe = true; //Enquadrado como Can-S
            if (sStr.substring(0,1).equals("T")) bConformeCotepe = true; //Enquadrado como Tnnnn
            if (sStr.substring(0,1).equals("S")) bConformeCotepe = true; //Enquadrado como Snnnn
        }
        else if (sStr.trim().length() == 7 ){
            if (sStr.substring(2,3).equals("S")) bConformeCotepe = true; //Enquadrado como xxSnnnn
            if (sStr.substring(2,3).equals("T")) bConformeCotepe = true; //Enquadrado como xxTnnnn
        }

        return bConformeCotepe;
    }


    public void finalizar(String arqCaminhoCompleto){

        File file = new File(arqCaminhoCompleto);

        try {
            FileManagement fMng = new FileManagement();
            String hashMD5Arquivo  = fMng.getHashMD5(file);

            FileWriter fw = new FileWriter(file,true);
            fw.write("EAD" + hashMD5Arquivo);

            fw.close();

            this.gravarFinalizacao();





        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> validaObrigadoEFD(String dataInicial, String dataFinal ){
        ArrayList<String> erros =  new ArrayList<String>();
        Calendar cInicial = Calendar.getInstance();
        cInicial.set(Integer.parseInt(dataInicial.substring(0, 4)),Integer.parseInt(dataInicial.substring(4,6))  -1 , Integer.parseInt(dataInicial.substring(6, 8)));

        int mes = cInicial.get(Calendar.MONTH) +1;
        DecimalFormat df = new DecimalFormat("00");
        String refeArquivo =  String.valueOf(cInicial.get(Calendar.YEAR)) + df.format(mes);
        this.setRefeArquivo(refeArquivo);

        if (this.obrigadoEFD(this.getRefeArquivo(),this.getInscricao().toString())){
            erros.add("Este contribuinte é obrigado a enviar EFD nesta referência. O arquivo não pode ser enviado.");
        }
        return erros;
    }


    public boolean obrigadoEFD(String refeArquivo, String inscricao){
        String sql = " SELECT indi_obrigat FROM EFD_CONTRIBUINTE_OBRIGADO  " +
                " where numr_inscricao =  ? " +
                " and ano_mes_referencia = ? " ;
        try{
            String obrigadoEFD  = jdbcTemplate.queryForObject(sql, String.class, inscricao, refeArquivo);

            if (obrigadoEFD.equals("S")) return true;
            else return false;

        }catch (EmptyResultDataAccessException e){
            //Se não achou nada, retorna falso e permite enviar o arquivo
            return false;
        }
    }

    private ArrayList<String> validaPeriodoAlertas(String dataInicial, String dataFinal ){

        ArrayList<String> erros =  new ArrayList<String>();

        Calendar cInicial = Calendar.getInstance();
        cInicial.set(Integer.parseInt(dataInicial.substring(0, 4)),Integer.parseInt(dataInicial.substring(4,6)) -1 , Integer.parseInt(dataInicial.substring(6, 8)));


        Calendar cFinal = Calendar.getInstance();
        cFinal.set(Integer.parseInt(dataFinal.substring(0, 4)),Integer.parseInt(dataFinal.substring(4,6))  -1 , Integer.parseInt(dataFinal.substring(6, 8)));

        //Validar se começa no primeiro dia e termina no último
        Integer diaInicial = cInicial.get(Calendar.DATE);
        Integer diaFinal = cFinal.get(Calendar.DATE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if ((diaInicial.compareTo(cInicial.getActualMinimum(Calendar.DAY_OF_MONTH) ) !=0) || (diaFinal.compareTo(cFinal.getActualMaximum(Calendar.DAY_OF_MONTH) ) !=0)) {
            erros.add("E01 - O arquivo não contempla o mês inteiro. Data inicial: " + dateFormat.format(cInicial.getTime())  + " - Data final: " + dateFormat.format(cFinal.getTime()));
        }

        //Validar se já foi recebido outro nesse mesmo mês - se foi, vai ser substituido
        int mes = cInicial.get(Calendar.MONTH) +1;
        DecimalFormat df = new DecimalFormat("00");
        String refeArquivo =  String.valueOf(cInicial.get(Calendar.YEAR)) + df.format(mes);

        if (this.existeArquivoReferencia(refeArquivo, this.getInscricao().toString())){
            erros.add("Já existe um arquivo nessa referência para essa inscrição. Caso o envio seja confirmado, o arquivo anterior será desconsiderado. Este arquivo será considerado uma Retificadora. ");
            this.setIsRetificadora(true);
        }
        else this.setIsRetificadora(false);

        return erros;
    }

    public boolean existeArquivoReferencia(String refeArquivo, String inscricao){
        String sql = " SELECT id_arquivo_memoria_fiscal_ecf FROM MFD_ARQUIVO_MEMORIA_FISCAL_ECF  " +
                " where numr_inscricao =  ? " +
                " and refe_arquivo = ? " +
                " and tipo_finalidade_arquivo = 0 " +
                " and  ROWNUM = 1 " ;

        try{
                String idArquivo  = jdbcTemplateMFD.queryForObject(sql, String.class, inscricao, refeArquivo);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public boolean gravarFinalizacao(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nomeTemporario =this.getInscricao().toString() + "_" +  sdf.format(Calendar.getInstance().getTime());
        Integer codgVersaoLeiaute = 001;  //Versao do Leiaute do Ato Cotepe (2004)
        int qtdLinhaArquivo ;
        int tipofinalidadeArquivo;
        String refeArquivo = this.getRefeArquivo();
        Integer inscricao = this.getInscricao();
        String cnpjContribuinte = this.getCnpjContribuinte();
        String tipoModeloArqEcf;
        String statusProcessamento;
        String indiExportNFG;


        if (this.isRetificadora()) tipofinalidadeArquivo= 1;
        else tipofinalidadeArquivo = 0;
        qtdLinhaArquivo = 0; //sempre é zero, significa que ainda não foi processada nenhuma linha
        tipoModeloArqEcf ="M";
        statusProcessamento = "R";
        indiExportNFG = "N";

        int idInserido= this.inserirEntradaDoArquivo(nomeTemporario,codgVersaoLeiaute,qtdLinhaArquivo, tipofinalidadeArquivo, refeArquivo, inscricao,cnpjContribuinte , tipoModeloArqEcf, statusProcessamento, indiExportNFG );

        Date dataArquivo = this.buscaDataEntrega(idInserido);
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String data =  sdf.format(dataArquivo);

        //Construir nome do arquivo para renomear.
        // 1. Transmissor:  "E_" + id do registro gerado + "_001"
        // 2. Separador (-) e tipo do arquivo : "-MFD"
        // 3. Separador (-) e inscricao estadual
        // 4. Separador (-) e cnpj
        // 5. Separador (-) e numero de fabricacao do ECF
        // 6. Separador (-) e finalidade
        // 7. Separador (-) e referencia
        // 8. Separador (-) e versao do leiaute --No momento só tem a 001 (Ato cotepe março 2004)
        // 8. Separador (-) e data hora entrega


        String nomeFinal = "E_" + String.valueOf(idInserido) + "001" + "-MFD-" + this.getInscricao().toString() + "-" + this.getCnpjContribuinte() + "-" + this.getNumFabricECF() + "-"
                           + String.valueOf(tipofinalidadeArquivo) + "-" + this.getRefeArquivo() + "-001-" + data;


        //Renomear o arquivo original
        FileManagement fmng = new FileManagement();
        fmng.salvarArquivoValidado(this.getCaminhoCompleto(), nomeFinal);

        this.atualizarNomeArquivo(idInserido, nomeFinal);

         return true;
    }


    private void atualizarNomeArquivo(Integer idArquivo, String nomeArquivo) {
        String sqlUpdate= " UPDATE MFD_ARQUIVO_MEMORIA_FISCAL_ECF set NOME_ARQUIVO= ? "+
                " WHERE ID_ARQUIVO_MEMORIA_FISCAL_ECF= ?";
        try {
            int retorno = jdbcTemplate.update(sqlUpdate, idArquivo,nomeArquivo);
            if (retorno > 0) {
                logger.info("Nome do arquivo atualizado");
            } else {
                logger.error("Erro nao esperado na atualização do nome do arquivo. ");
            }
        }   catch (Exception e){
            logger.error("Erro nao esperado na atualização do nome do arquivo. ");
        }
    }


    public Date buscaDataEntrega(int idArquivo){
        String sql = " SELECT DATA_ENTREGA_ARQUIVO FROM MFD_ARQUIVO_MEMORIA_FISCAL_ECF  " +
                " where ID_ARQUIVO_MEMORIA_FISCAL_ECF =  ? ";

        try{
            Date dataArquivo = jdbcTemplateMFD.queryForObject(sql, Date.class, idArquivo);
            return dataArquivo;
        }catch (EmptyResultDataAccessException e){
            return null;
        }catch (Exception e){
            return null;
        }
    }



    public int inserirEntradaDoArquivo(final String nomeTemporario,  final Integer codgVersaoLeiaute, final int qtdLinhaArquivo, final int tipofinalidadeArquivo,  final String refeArquivo,
                                       final Integer inscricao,final String cnpjContribuinte , final String tipoModeloArqEcf, final String statusProcessamento, final String indiExportNFG ) {
        final String sqlInsert = "insert into MFD_ARQUIVO_MEMORIA_FISCAL_ECF( " +
                " NOME_ARQUIVO, " +
                " CODG_VERSAO_LEIAUTE, " +
                " QTDE_LINHA_ARQUIVO, " +
                " TIPO_FINALIDADE_ARQUIVO, " +
                " REFE_ARQUIVO, " +
                " NUMR_INSCRICAO, " +
                " TIPO_MODELO_ARQ_ECF, " +
                " STAT_PROCESM_ARQUIVO_ECF, " +
                " INDI_EXPORTACAO_NFG, " +
                " NUMR_CNPJ_ESTAB_USUARIO, " +
                " DATA_ENTREGA_ARQUIVO ) " +
                " values(?,?,?,?,?,?,?,?,?,?,sysdate) ";

        Integer idInserido = null;

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int result = jdbcTemplateMFD.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection connection)
                        throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[]{"ID_ARQUIVO_MEMORIA_FISCAL_ECF"});
                    ps.setString(1, nomeTemporario);
                    ps.setInt(2, codgVersaoLeiaute);
                    ps.setInt(3, qtdLinhaArquivo);
                    ps.setInt(4, tipofinalidadeArquivo);
                    ps.setString(5, refeArquivo);
                    ps.setInt(6, inscricao);
                    ps.setString(7, tipoModeloArqEcf);
                    ps.setString(8, statusProcessamento);
                    ps.setString(9, indiExportNFG);
                    ps.setString(10, cnpjContribuinte);

                    try {
                        return ps;
                    }
                    catch(Exception e){
                        throw  e;
                    }

                }
            }, holder);
            idInserido = holder.getKey().intValue();

        }catch (Exception e){
            throw new NFGException("Erro ao tentar incluir registro do arquivo na tabela do MFD: "+e.getMessage());
        }

        return idInserido;
    }


    public boolean isbTodosSemCPFCNPJ() {
        return bTodosSemCPFCNPJ;
    }

    public void setbTodosSemCPFCNPJ(boolean bTodosSemCPFCNPJ) {
        this.bTodosSemCPFCNPJ = bTodosSemCPFCNPJ;
    }

    public Integer getInscricao() {
        return inscricao;
    }

    public void setInscricao(Integer inscricao) {
        this.inscricao = inscricao;
    }

    public boolean isbTemCupom() {
        return bTemCupom;
    }

    public void setbTemCupom(boolean bTemCupom) {
        this.bTemCupom = bTemCupom;
    }

    public String getRefeArquivo() {
        return refeArquivo;
    }

    public void setRefeArquivo(String refeArquivo) {
        this.refeArquivo = refeArquivo;
    }

    public String getCnpjContribuinte() {
        return cnpjContribuinte;
    }

    public void setCnpjContribuinte(String cnpjContribuinte) {
        this.cnpjContribuinte = cnpjContribuinte;
    }

    public boolean isRetificadora() {
        return retificadora;
    }

    public void setIsRetificadora(boolean isRetificadora) {
        this.retificadora = isRetificadora;
    }

    public String getNumFabricECF() {
        return numFabricECF;
    }

    public void setNumFabricECF(String numFabricECF) {
        this.numFabricECF = numFabricECF;
    }

    public String getCaminhoCompleto() {
        return caminhoCompleto;
    }

    public void setCaminhoCompleto(String caminhoCompleto) {
        this.caminhoCompleto = caminhoCompleto;
    }


}
