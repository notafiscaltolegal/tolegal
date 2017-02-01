/**
 * Created by lucas-mp on 10/10/14.
 */

function Cidadao(options) {
	var me = this;

	var mensagemErroAtivacaoCadastro = $("#contaInativada").val();
	var mensagemSucessoAtivacaoCadastro = $("#contaAtivada").val();
	var mensagemSucessoCadastro = $("#cadastroSucesso").val();
	var mensagemErroCadastro = $("#cadastroErro").val();

	if (mensagemErroAtivacaoCadastro != ""
			&& mensagemErroAtivacaoCadastro != null) {

		nfgMensagens.show(ALERT_TYPES.ERROR, mensagemErroAtivacaoCadastro);
	}

	if (mensagemSucessoAtivacaoCadastro != ""
			&& mensagemSucessoAtivacaoCadastro != null) {

		nfgMensagens.show(ALERT_TYPES.SUCCESS, mensagemSucessoAtivacaoCadastro);
	}

	if (mensagemSucessoCadastro != "" && mensagemSucessoCadastro != null) {

		nfgMensagens.show(ALERT_TYPES.SUCCESS, mensagemSucessoCadastro);
	}

	if (mensagemErroCadastro != "" && mensagemErroCadastro != null) {

		nfgMensagens.show(ALERT_TYPES.ERROR, mensagemErroCadastro);
	}

	$(".inputCpf").mask("999.999.999-99");
	$(".inputCep").mask("99999-999");
	$(".inputIE").mask("99.999.999-9");
	$("#loginIE").mask("99.999.999-9");

	$(".inputTelefone").mask("(99) 9999-9999?9");
	$(".mascaraValor").maskMoney();
	$(".mascaraValor").maskMoney('mask');
	$(".textCpf").html(toCpfFormat($(".textCpf").html()));
	$("#loginIE").html(toIEFormat($("#loginIE").html()));
	$(".textCnpj").html(toCnpjFormat($(".textCnpj").html()));
	$(".textMoney").html(toMoneyFormat($(".textMoney").html()));
	$(".textTelefone").html(toTelFormat($(".textTelefone").html()));
	$(".textCep").html(toCepFormat($(".textCep").html()));

	$(".datepicker").each(function() {
		me.initDatePicker($(this));
	});

	$(".showDadosDaBase").tooltip({
		position : {
			my : "left top",
			at : "right+5 top-5"
		}
	}).off("focusin focusout");

	$("#btnsTelaLogin").tooltip({
		position : {
			my : "left top",
			at : "right+5 top-5"
		}
	}).off("focusin focusout");

	$(".dadosMinhasNotas").tooltip({
		track : true
	}).off("focusin focusout");

	$(".dadosPontosDasNotas").tooltip({
		track : true
	}).off("focusin focusout");

	$("#inputValidaCpf").focus();

	if ($("#numeroDeTentativas").val() > 2) {
		me.ativarCaptcha("#captchaDiv", "containerCaptcha");
		if ($("#inputValidaCpf").val() == "") {
			setTimeout(function() {
				$("#inputValidaCpf").focus();
			}, 600);
		}
	}

	if ($("#numeroDeTentativasLogin").val() > 2) {
		me.ativarCaptcha("#captchaLogin", "containerCaptchaLogin");

		if ($("#loginCpf").val() == "") {
			setTimeout(function() {
				$("#loginCpf").focus();
			}, 600);
		}

		if ($("#loginIE").val() == "") {
			setTimeout(function() {
				$("#loginIE").focus();
			}, 600);
		}
	}

	// Quando o campo for de confirmacao, nao e permitido dar crtl+v.
	$(".campoConfirmacao").bind('paste', function(e) {
		e.preventDefault();
	});

	me.preValidacaoDoCidadao();
	me.preValidacaoDoCidadaoCertificado();
	me.preValidacaoDoCidadaoPreCadastrado();
	me.cadastraCidadao();
	me.loginCidadao();
	me.salvaNovaSenha();
	me.loginSubmit();
	me.loginSubmitEmpresa();
	me.emailRecuperarSenha();
	me.gravaPerfil();
	me.concluirPerfil();
	me.gravaNovaSenhaPerfil();
	me.verificaCep();
	me.carregaMunicipios();
	me.alteraSorteio();
	me.selecionaUf();
	me.limparFiltros();
	me.apliqueMaskFilter();

	if (options.nomeTela == 'telaInicial') {
		new ModaisCidadao({
			cidadao : me
		});
		me.listarNotasCidadao();
		me.listarReclamacoes();
		// me.dadosMeuPlacar();
		me.carregaDadosPremiacao();

		me.salvaNovaReclamacao();
		me.fluxoDeTelaNovaReclamacao();
		me.mudancaAcaoReclamacao();
		me.alterarReclamacao();
		me.fechaModalConfirmacao();
		me.ConfirmaAlteracaoReclamacao();
		me.resetaElementosAcaoReclamacao();

		me.voltarTelaDeReclamacao();

		me.verificaInconsistenciaCidadao();
		if (!me.carregaDadosSorteio($("#selectSorteios").val())) {
			$(".panelSorteios").html("Não houveram sorteios até o momento.")
		}
	} else if (options.nomeTela == 'pontos') {
		new ModaisCidadao({
			cidadao : me
		});
	} else if (options.nomeTela == 'minhasNotasDetalhe') {
		new ModaisCidadao({
			cidadao : me
		});
	} else if (options.nomeTela == 'conclusaoDadosPerfil') {
		new ModaisCidadao({
			cidadao : me,
			nomeTela : 'conclusaoDadosPerfil'
		});
	} else if (options.nomeTela == 'premiacao') {
		new ModaisCidadao({
			cidadao : me
		});
	} else if (options.nomeTela == 'reclamacaoDetalhe') {
		new ModaisCidadao({
			cidadao : me
		});
	}

	document.onkeydown = function(e) {
		var evt = e || window.event;
		if (evt.keyCode == '13') {
			$(".btn-submitform").click();
		}
	}

}

Cidadao.prototype.carregaUFMunicipios = function(uf, municipio) {
	var me = this;
	if (uf == null)
		return;

	$("#selectUf option").filter(function() {
		return $(this).text() == uf;
	}).prop('selected', true);

	$.when(me.carregaMunicipios(uf)).done(function() {
		if (municipio != null) {
			$("#selectMunicipio option").filter(function() {

				return $(this).val() == municipio;

			}).prop('selected', true);
		}
	});
}

Cidadao.prototype.preValidacaoDoCidadaoPreCadastrado = function() {
	var me = this;
	$("#btnValidaDadosCidadaoPreCadastrado").click(function(e) {
		// dataGenPessoaFisica deve estar de acordo com os parametros de
		// preValidaDadosCidadao do CidadaoController
		var dataGenPessoaFisica = me.trataDadosPreCadastro();
		me.chamaPreValidadorDeCadastro(dataGenPessoaFisica);
	});
}

Cidadao.prototype.preValidacaoDoCidadaoCertificado = function() {
	var me = this;
	$("#btnValidaDadosCidadaoCertificado").click(function(e) {
		// dataGenPessoaFisica deve estar de acordo com os parametros de
		// preValidaDadosCidadao do CidadaoController
		var dataGenPessoaFisica = me.trataDadosDaPreValidacaoCertificado();
		me.chamaPreValidadorDeCadastro(dataGenPessoaFisica);
	});
}

Cidadao.prototype.preValidacaoDoCidadao = function() {
	var me = this;
	$("#btnValidaDadosCidadao").click(function(e) {
		// dataGenPessoaFisica deve estar de acordo com os parametros de
		// preValidaDadosCidadao do CidadaoController
		var dataGenPessoaFisica = me.trataDadosDaPreValidacao();
		me.chamaPreValidadorDeCadastro(dataGenPessoaFisica);
	});
};

Cidadao.prototype.trataDadosPreCadastro = function() {
	var cpf = null, dataDeNascimento = null, nomeDaMae = null, challenge = null, captchaResponse = null;
	var emailPreCadastro = null, nomePreCadastro = null, telefonePreCadastro = null;
	var cpfVal = $("#cpfPreCadastro").val();
	var dataDeNascimentoVal = $("#dtNascPreCadastro").val();

	nomeDaMae = $("#nomeMaePreCadastro").val().toUpperCase();
	emailPreCadastro = $("#emailPreCadastro").val();
	nomePreCadastro = $("#nomePreCadastro").val();
	telefonePreCadastro = $("#telefonePreCadastro").val();

	try {
		cpf = onlyNumbers(cpfVal);
	} catch (e) {
	}

	if (dataDeNascimentoVal != "__/__/____") {
		dataDeNascimento = dataDeNascimentoVal;
	}

	return {
		cpf : cpf,
		nomeDaMae : nomeDaMae,
		dataDeNascimento : dataDeNascimento,
		challenge : challenge,
		captchaResponse : captchaResponse,
		ehCertificado : false,
		emailPreCadastro : emailPreCadastro,
		nomePreCadastro : nomePreCadastro,
		telefonePreCadastro : telefonePreCadastro
	}

}

Cidadao.prototype.trataDadosDaPreValidacaoCertificado = function() {
	var cpf = null, dataDeNascimento = null, nomeDaMae = null, challenge = null, captchaResponse = null;
	var emailPreCadastro = null, nomePreCadastro = null, telefonePreCadastro = null;
	var cpfVal = $("#cpfPessoaCertificado").val();
	var dataDeNascimentoVal = $("#dataNascimentoCertificado").val();

	try {
		cpf = onlyNumbers(cpfVal);
	} catch (e) {
	}

	if (dataDeNascimentoVal != "__/__/____") {
		dataDeNascimento = dataDeNascimentoVal;
	}

	return {
		cpf : cpf,
		nomeDaMae : nomeDaMae,
		dataDeNascimento : dataDeNascimento,
		challenge : challenge,
		captchaResponse : captchaResponse,
		ehCertificado : true,
		emailPreCadastro : emailPreCadastro,
		nomePreCadastro : nomePreCadastro,
		telefonePreCadastro : telefonePreCadastro
	}
}

Cidadao.prototype.trataDadosDaPreValidacao = function() {
	var formParams = serializeFormToObject($("#formValidaDadosCidadao"));
	var cpf = null, nomeDaMae = null, dataDeNascimento = null, challenge = null, captchaResponse = null;
	var emailPreCadastro = null, nomePreCadastro = null, telefonePreCadastro = null;

	try {
		cpf = onlyNumbers(formParams.cpfPessoa);
	} catch (e) {
	}

	if (formParams.nomeMaePessoa != "") {
		nomeDaMae = formParams.nomeMaePessoa.toUpperCase();
	}

	if (formParams.dNascimentoPessoa != "__/__/____") {
		dataDeNascimento = formParams.dNascimentoPessoa;
	}

	if ($("#recaptcha_challenge_field").val() != undefined) {
		challenge = $("#recaptcha_challenge_field").val();
	}

	if ($("#recaptcha_response_field").val() != undefined) {
		captchaResponse = $("#recaptcha_response_field").val();
	}

	return {
		cpf : cpf,
		nomeDaMae : nomeDaMae,
		dataDeNascimento : dataDeNascimento,
		challenge : challenge,
		captchaResponse : captchaResponse,
		ehCertificado : false,
		emailPreCadastro : emailPreCadastro,
		nomePreCadastro : nomePreCadastro,
		telefonePreCadastro : telefonePreCadastro
	}
}

Cidadao.prototype.chamaPreValidadorDeCadastro = function(dataGenPessoaFisica) {
	var me = this;

	if (dataGenPessoaFisica.cpf == null || dataGenPessoaFisica.cpf == '') {
		nfgMensagens.show(ALERT_TYPES.ERROR, "O cpf é obrigatório.");
		return false;
	}

	if (dataGenPessoaFisica.nomeDaMae == null
			|| dataGenPessoaFisica.nomeDaMae == '') {
		nfgMensagens.show(ALERT_TYPES.ERROR, "O nome da mãe é obrigatório.");
		return false;
	}

	if (dataGenPessoaFisica.nomeDaMae.split(" ").length < 2) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"O nome da mãe deve ter no mínimo dois nomes!");
		return false;
	}

	if (dataGenPessoaFisica.dataDeNascimento == null) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"A data de nascimento é obrigatório.");
		return false;
	}

	dataGenPessoaFisica.telefone = $("#telCidadao").val();

	$
			.ajax({
				url : enderecoSite + "/cidadao/validardadoscidadao",
				data : dataGenPessoaFisica,
				type : 'POST',
				success : function(response) {
					nfgMensagens.show(ALERT_TYPES.WARN, "Conclua seu cadastro");

					// prepara formulario de efetivacao de cadastro
					$("#nomeCadastroCidadao").val(response.nome);
					$("#emailCidadao").val(response.email);
					$("#cpfCidadao").val(dataGenPessoaFisica.cpf);
					$("#nomeMaeCidadao").val(dataGenPessoaFisica.nomeDaMae);
					$("#dataNascCidadao").val(
							dataGenPessoaFisica.dataDeNascimento);
					$("#telCidadao").val(response.telefone).mask(
							"(99) 9999-9999?9");
					$("#cpfCadastroParticipante").val(response.cpf);
					$("#showDadosDaBase").css("display", "block");
					$("#captchaDiv").css("display", "none");

					// cadastro convencional
					$("#inputValidaCpf").prop('disabled', true);
					$("#inputValidaNomeDaMae").prop('disabled', true);
					$("#inputValidaDataDeNascimento").prop('disabled', true);
					$("#spanValidaDataDeNascimento").hide();
					$("#btnValidaDadosCidadao").hide();
					$("#btnValidaDadosCidadao").removeClass("btn-submitform");

					// coclusao de pre-cadastro
					$("#cpfPreCadastro").prop('disabled', true);
					$("#nomeMaePreCadastro").prop('disabled', true);
					$("#dtNascPreCadastro").prop('disabled', true);
					$("#spanNascPreCadastro").hide();
					$("#btnValidaDadosCidadaoPreCadastrado").hide();
					$("#btnValidaDadosCidadaoPreCadastrado").removeClass(
							"btn-submitform");

					// adicionando acao de key 13 para o botao de concluir
					// cadastro
					$("#cadastrarCidadao").addClass("btn-submitform");

					// limpa senhas
					$("#passCidadao").val("");
					$("#passCidadaoConfirm").val("");
					$("#passCidadaoAtual").val("");

					// preenchendo do pre- cadastro
					if (response.emailPreCadastro != null) {
						$("#emailCidadao").val(response.emailPreCadastro);
					}
					if (response.telefonePreCadastro != null) {
						$("#telCidadao").val(response.telefonePreCadastro);
					}
					if (response.nomePreCadastro != null) {
						$("#nomeCadastroCidadao").val(response.nomePreCadastro);
					}

					// verifica se tem ou nao senha homologada e se a credencia
					// esta Ativa.
					// sempre quando for inativa, o user pode entrar com uma
					// senha nova
					// se for ativo, quer dizer que ele tem uma senha atual
					// (caso contrario o server lanca uma excecao).
					if (response.senhaCadastrada == null
							|| response.senhaAtiva == 'I') {
						$("#divNovaSenha").css('display', 'block');
						$("#divSenhaAtual").css('display', 'none');
					} else {
						$("#divNovaSenha").css('display', 'none');
						$("#divSenhaAtual").css('display', 'block');
					}

					// logica de carregar endereco
					me.carregaFormularioEndereco(response.enderecoCadastrado);

					$("#emailCidadao").focus();

					try {
						Recaptcha.destroy();
					} catch (e) {
					}
				},
				error : function(response) {
					if (response.responseJSON.semCredencial) {
						window.location.replace(enderecoSite
								+ response.responseJSON.urlRedirect);
						return;
					}

					trataErroDoServidor(response)

					if (response.responseJSON.ativarCaptcha) {
						me.ativarCaptcha("#captchaDiv", "containerCaptcha");
					}

					$("#btnValidaDadosCidadaoPreCadastrado").css("display",
							"block");

					try {
						Recaptcha.reset();
					} catch (e) {
					}
				}
			});
}

Cidadao.prototype.cadastraCidadao = function() {
	var me = this;

	$("#cadastrarCidadao").click(
			function(e) {
				if (!me.validaParametrosCadastrarCidadao()) {
					return;
				}
				var form = $("#formCadastroCidadao");
				var formParams = serializeFormToObject(form);

				var data = {
					nome : $("#nomeCadastroCidadao").val(),
					email : formParams.emailCidadao,
					telefone : me.removeMascaraTelefone(formParams.telCidadao),
					senha : formParams.passCidadao,
					senhaAtual : formParams.passCidadaoAtual,
					nomeDaMae : formParams.nomeMaeCidadao,
					cpf : formParams.cpfCidadao,
					dataDeNascimento : formParams.dataNascCidadao,
					cep : onlyNumbers(formParams.cepCidadao),
					tipoLogradouro : formParams.selectLogradouro,
					nomeLogradouro : formParams.logradouroCidadao,
					nomeBairro : formParams.bairroCidadao,
					numero : formParams.numeroCidadao,
					complemento : formParams.complementoCidadao,
					municipio : formParams.selectMunicipio,
					endHomologado : formParams.enderecoHomologado,
					participaSortreio : $("#checkParticipaSorteio").is(
							':checked'),
					recebeEmail : $("#checkRecebeEmail").is(':checked')
				}

				ajaxBloqueioDeTela("#cadastrarCidadao", enderecoSite
						+ "/cidadao/efetuarcadastro", data, 'POST', function(
						response) {

					if (response.urlRedirect != null) {
						nfgMensagens.show(ALERT_TYPES.SUCCESS,
								"Ative seu cadastro no e-mail.", true);
						window.location.replace(enderecoSite
								+ response.urlRedirect);
						nfgMensagens.show(ALERT_TYPES.SUCCESS,
								"Ative seu cadastro no e-mail.", true);
					}

				});

			});
};

Cidadao.prototype.validaParametrosCadastrarCidadao = function() {
	var me = this;

	// CAMPOS
	var email = $("#emailCidadao").val().trim();
	var emailConfirma = $("#emailCidadaoConfirma").val();
	var telefone = $("#telCidadao").val();
	var senha = $("#passCidadao").val();
	var senhaConfirma = $("#passCidadaoConfirm").val();
	var senhaAtual = $("#passCidadaoAtual").val();

	var cep = $("#cepCidadao").val();

	var tipoLogradouro = $("#selectLogradouro").val();
	var nomeLogradouro = $("#logradouroCidadao").val();

	var numero = $("#numeroCidadao").val();
	var bairro = $("#bairroCidadao").val();
	var complemento = $("#complementoCidadao").val();

	var uf = $("#selectUf").val();
	var cidade = $("#selectMunicipio").val();

	// VALIDACOES EMAIL E TEL:
	if (email == null || telefone == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "E-mail Obrigatório!");
		$("#emailCidadao").focus();
		return false;
	} else if (email.length > 0 && !ehEmail(email)) {
		nfgMensagens.show(ALERT_TYPES.ERROR, "E-mail Inválido!");
		$("#emailCidadao").focus();
		return false;
	} else if (email.length > 0 && email != emailConfirma) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"O E-mail deve coincidir com a sua confirmação!");
		$("#emailCidadaoConfirma").focus();
		return false;
	} else if (telefone == null || telefone == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo Telefone!");
		$("#telCidadao").focus();
		return false;
	} else // VALIDACOES DE ENDERECO:
	if (me.enderecoInvalido(cep, tipoLogradouro, nomeLogradouro, numero,
			bairro, complemento, uf, cidade, false)) {
		return false;
	} else if (me.senhaValida(senha, senhaConfirma, senhaAtual, false)) { // VALIDACOES
		// DE
		// SENHA
		return true;
	}
}

Cidadao.prototype.carregaFormularioEndereco = function(enderecoCadastrado) {
	var me = this;
	if (enderecoCadastrado) {
		$("#cepCidadao").val(enderecoCadastrado.cep);
		$("#logradouroCidadao").val(enderecoCadastrado.nomeLogradouro);
		$("#bairroCidadao").val(enderecoCadastrado.nomeBairro);
		$("#numeroCidadao").val(enderecoCadastrado.numero);
		$("#complementoCidadao").val(enderecoCadastrado.complemento);
		$("#selectMunicipio").val(enderecoCadastrado.municipio);
		$("#selectLogradouro").val(enderecoCadastrado.tipoLogradouro);
		var enderecoHomolog = enderecoCadastrado.indiHomologCadastro;

		if (enderecoCadastrado.cep != null) {
			me.carregaEnderecoPorCep();
		} else {
			me.carregaUFMunicipios('TO');
		}

		if (enderecoHomolog != null && enderecoHomolog == 'S') {
			$('#inputsEndereco :input').attr('readonly', true);
			$('#inputsEndereco :input').attr('tabindex', -1);
			$('#inputsEndereco :input').attr('title', "");

			$('#inputsEndereco :input').keydown(function(evt) {
				return false;
			});

			$(".combo-endereco").attr('disabled', true);
			$("#enderecoHomologado").val('S');
			$("#labelEndHomolog").css("display", "block");
		} else {
			$('#inputsEndereco :input').attr('readonly', false);
			$("#enderecoHomologado").val('N');
			$("#labelEndHomolog").css("display", "none");
			$(".combo-endereco").attr('disabled', false);
		}

	} else {
		me.carregaUFMunicipios('TO');
	}
}

Cidadao.prototype.verificaCep = function() {
	var me = this;

	$("body").on("blur", "#cepCidadao", function(e) {
		me.carregaEnderecoPorCep();
	});
}

Cidadao.prototype.carregaEnderecoPorCep = function() {
	var me = this;

	if ($("#enderecoHomologado").val() == null
			|| $("#enderecoHomologado").val() != 'S') {

		ajaxBloqueioDeTela("#enderecoPanel", enderecoSite
				+ "/cidadao/verificaEndereco", {
			cep : onlyNumbers($("#cepCidadao").val(), [ '-' ])
		}, 'POST',
				function(response) {
					if (!response.cepInvalido) {

						$("#logradouroCidadao").val(response.nomeLogradouro);
						$("#bairroCidadao").val(response.nomeBairro);
						$("#numeroCidadao").val(response.numero);
						$("#complementoCidadao").val(response.complemento);

						me.carregaUFMunicipios(response.nomeUf,
								response.ibgeMunicipio);
					} else {

						$("#logradouroCidadao").val("");
						$("#bairroCidadao").val("");
						$("#numeroCidadao").val("");
						$("#complementoCidadao").val("");
					}
				});
	}
}

Cidadao.prototype.enderecoInvalido = function(cep, tipoLogradouro,
		nomeLogradouro, numero, bairro, complemento, uf, cidade, modal) {
	if ($("#enderecoHomologado").val() == 'S') {
		return false; // nao valida endereco com cadastro homologado.
	}

	if (cep == null || cep == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo CEP!", modal);
		$("#cepCidadao").focus();
	} else if (nomeLogradouro == null || nomeLogradouro == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o nome do Logradouro!",
				modal);
		$("#logradouroCidadao").focus();
	}

	else if (bairro == null || bairro == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo Bairro!", modal);
		$("#bairroCidadao").focus();
	} else if (uf == null || uf == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo UF!", modal);
		$("#selectUf").focus();
	} else if (cidade == null || cidade == "" || cidade == "Selecione") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo Cidade!", modal);
		$("#selectMunicipio").focus();
	} else {
		return false;
	}
	return true;
}

Cidadao.prototype.senhaValida = function(senha, senhaConfirma, senhaAtual,
		modal) {

	if ($("#divSenhaAtual").css('display') == 'block') {
		if (senhaAtual == null || senhaAtual == "") {
			nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo Senha!",
					modal);
			$("#passCidadaoAtual").focus();
			return false;
		}
		return true;
	}

	if (senha == null || senha == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR, "Preencha o campo Senha !", modal);
		$("#passCidadao").focus();
	} else if (senhaConfirma == null || senhaConfirma == "") {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"Preencha o campo de confirmaç&atilde;o de Senha !", modal);
		$("#passCidadaoConfirm").focus();
	} else if (senha.length != 10) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"A senha deve conter 10 caracteres!", modal);
		$("#passCidadao").focus();
	} else if (senha.match(/\D+/) == null) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"A senha deve conter uma ou mais letras!", modal);
		$("#passCidadao").focus();
	} else if (senha.match(/\d+/) == null) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"A senha deve conter um ou mais n&#250;meros!", modal);
		$("#passCidadao").focus();
	} else if (senhaConfirma != senha) {
		nfgMensagens.show(ALERT_TYPES.ERROR,
				"A senha deve coincidir com a sua confirmaç&atilde;o!", modal);
		$("#passCidadaoConfirm").focus();
	} else {
		return true;
	}
	return false;
}

Cidadao.prototype.loginCidadao = function() {
	var me = this;

	$("#loginCpf").val("");
	$("#loginSenha").val("");
	$("#loginCpf").focus();

	$("#linkRecuperarSenha").click(function(e) {
		$(".formEmailRecuperacaoSenha").css("display", "block");
	});

}

Cidadao.prototype.loginSubmit = function() {
	var me = this;
	$("#loginSubmit").click(
			function(e) {
				var challenge = null;
				var captchaResponse = null;

				if ($("#recaptcha_challenge_field").val() != undefined) {
					challenge = $("#recaptcha_challenge_field").val();
				}

				if ($("#recaptcha_response_field").val() != undefined) {
					captchaResponse = $("#recaptcha_response_field").val();
				}

				$.ajax({
					url : enderecoSite + "/cidadao/efetuarlogin",
					data : {
						cpf : me.removeMascaraCpf($("#loginCpf").val()),
						senha : $("#loginSenha").val(),
						challenge : challenge,
						captchaResponse : captchaResponse
					},
					type : 'POST',
					success : function(response) {
						if (response.loginInvalido != null) {
							try {
								Recaptcha.reset();
							} catch (e) {
							}

							setTimeout(function() {
								$("#loginCpf").focus();
							}, 500);

							nfgMensagens.show(ALERT_TYPES.ERROR,
									response.loginInvalido)
							if (response.ativarCaptchaLogin) {
								me.ativarCaptcha("#captchaLogin",
										"containerCaptchaLogin");
							}
						} else {
							try {
								Recaptcha.destroy();
							} catch (e) {
							}
							window.location.replace(enderecoSite
									+ response.urlRedirect);
						}
					}
				});
			});
}

Cidadao.prototype.loginSubmitEmpresa = function() {
	var me = this;
	$("#loginSubmitEmpresa").click(
			function(e) {
				var challenge = null;
				var captchaResponse = null;

				if ($("#recaptcha_challenge_field").val() != undefined) {
					challenge = $("#recaptcha_challenge_field").val();
				}

				if ($("#recaptcha_response_field").val() != undefined) {
					captchaResponse = $("#recaptcha_response_field").val();
				}

				if ($("#loginIE").val() == null || $("#loginIE").val() == '') {
					nfgMensagens.show(ALERT_TYPES.ERROR,
							"Inscrição Estadual é obrigatória.");
					return false;
				}

				if ($("#loginSenha").val() == null
						|| $("#loginSenha").val() == '') {
					nfgMensagens
							.show(ALERT_TYPES.ERROR, "Senha é obrigatória.");
					return false;
				}

				$.ajax({
					url : enderecoSite + "/contribuinte/efetuarlogin",
					data : {
						ie : me.removeMascaraIE($("#loginIE").val()),
						senha : $("#loginSenha").val(),
						challenge : challenge,
						captchaResponse : captchaResponse
					},
					type : 'POST',
					success : function(response) {
						if (response.loginInvalido != null) {
							try {
								Recaptcha.reset();
							} catch (e) {
							}

							setTimeout(function() {
								$("#loginIE").focus();
							}, 500);

							nfgMensagens.show(ALERT_TYPES.ERROR,
									response.loginInvalido)
							if (response.ativarCaptchaLogin) {
								me.ativarCaptcha("#captchaLogin",
										"containerCaptchaLogin");
							}
						} else {
							try {
								Recaptcha.destroy();
							} catch (e) {
							}
							window.location.replace(enderecoSite
									+ response.urlRedirect);
						}
					}
				});
			});
}

Cidadao.prototype.apliqueMaskFilter = function() {
	$(".inputInscricao").mask("99.999.999-9");
};

Cidadao.prototype.apliqueMask = function() {
	// mask cnpj
	$(".maskCnpj").each(
			function() {
				var $campo = $(this);
				var texto = $campo.text();
				if (texto.length == 14) {
					var cnpj_original = texto;
					var cnpj = cnpj_original.substr(0, 2) + "."
							+ cnpj_original.substr(2, 3) + "."
							+ cnpj_original.substr(5, 3) + "/"
							+ cnpj_original.substr(8, 4) + "-"
							+ cnpj_original.substr(12, 2);
					texto = cnpj;
				} else if (texto.length == 15) {
					texto += "-";
				}
				$campo.text(texto);
			});
	// mask inscricao
	$(".maskInscricao").each(
			function() {
				var $campo = $(this);
				var texto = $campo.text();
				if (texto.length == 9) {
					var inscricao_original = texto;
					var inscricao = inscricao_original.substr(0, 2) + "."
							+ inscricao_original.substr(2, 3) + "."
							+ inscricao_original.substr(5, 3) + "-"
							+ inscricao_original.substr(8, 2);
					texto = inscricao;
				}
				$campo.text(texto);
			});
};

Cidadao.prototype.emailRecuperarSenha = function() {
	var me = this;
	$("#submitEmailRecuperarSenha")
			.click(
					function(e) {
						$
								.ajax({
									url : enderecoSite
											+ "/cidadao/recuperarsenha",
									data : {
										cpf : me.removeMascaraCpf($(
												"#cpfRecuperacao").val())
									},
									type : 'POST',
									success : function(response) {
										nfgMensagens.show(ALERT_TYPES.SUCCESS,
												response.status);
									},
									error : function(response) {
										if (response.responseJSON.semCredencial) {
											window.location
													.replace(enderecoSite
															+ response.responseJSON.urlRedirect);
											return;
										}
									}
								});
						// $(".formEmailRecuperacaoSenha").css("display","none");
						$("#cpfRecuperacao").val("");
					});
}

Cidadao.prototype.salvaNovaSenha = function() {
	var me = this;

	$("#cadastrarNovaSenha").click(function(e) {
		var senha = $("#novaSenha").val();
		var senhaConfirma = $("#novaSenhaConfirm").val();

		if (!me.senhaValida(senha, senhaConfirma, null, false)) {
			return;
		}

		$.ajax({
			url : enderecoSite + "/cidadao/gravaNovaSenha",
			data : {
				cpf : $("#cpfCidadao").val(),
				tokenSenha : $("#token").val(),
				novaSenha : senha,
				novaSenhaConfirm : senhaConfirma
			},
			type : 'POST',
			success : function(response) {
				window.location.replace(enderecoSite + response.urlRedirect);
			}
		});
	});
}

Cidadao.prototype.listarReclamacoes = function() {
	var me = this;
	me.pagination = new NFGPagination({
		url : enderecoSite + "/cidadao/listarReclamacoes",
		containerSelector : "#containerReclamacoes",
		templateSelector : "#tabelaReclamacoes",
		formFilterSelector : "#formReclamacoes",
		btnFilterSelector : "#btnReclamacoes",
		beforeLoad : function(data) {
			bloqueioDeTela("#dadosReclamacoes");
		},
		afterLoad : function(data) {
			$("#dadosReclamacoes").unblock();
		}
	});
	me.pagination.init(0);
}

Cidadao.prototype.listarNotasCidadao = function() {
	var me = this;
	me.pagination = new NFGPagination({
		url : enderecoSite + "/cidadao/listarNotasCidadao",
		containerSelector : "#containerMinhasNotas",
		templateSelector : "#tabelaMinhasNotas",
		formFilterSelector : "#formMinhasNotas",
		btnFilterSelector : "#filtrarMinhasNotas",
		beforeLoad : function(data) {
			bloqueioDeTela("#dadosMinhasNotas");
			if (data) {
				data.cpfFiltro = $("#cpfFiltro").val();
			}
		},
		afterLoad : function(data) {
			$("#dadosMinhasNotas").unblock();
			$("#contentMinhasNotas").css("display", "inline");

		}
	});
	me.pagination.init(0);
};

Cidadao.prototype.listarPremiacao = function(sorteioId) {
	var me = this;
	me.pagination = new NFGPagination({
		url : enderecoSite + "/cidadao/listarPremiacao",
		containerSelector : "#containerPremiacao",
		templateSelector : "#tabelaPremiacao",
		formFilterSelector : "#formPremiacao",
		beforeLoad : function(data) {
			data.idSorteio = sorteioId;
			bloqueioDeTela("#dadosPremiacao");
		},
		afterLoad : function(data) {
			$("#dadosPremiacao").unblock();
		}
	});
	me.pagination.init(0);
};

Cidadao.prototype.gravaNovaSenhaPerfil = function() {
	var me = this;
	$("#atualizarSenhaPerfil").click(function(e) {
		var senhaAtual = $("#senhaPerfilAntiga").val();
		var senha = $("#senhaPerfilNova").val();
		var senhaConfirma = $("#senhaPerfilConfirm").val();

		if (!me.senhaValida(senha, senhaConfirma, null, true)) {
			return;
		}

		$.ajax({
			url : enderecoSite + "/cidadao/gravaNovaSenhaPerfil",
			data : {
				cpf : $("#cpfPerfil").val(),
				senhaPerfilAntiga : senhaAtual,
				senhaPerfilNova : senha,
				senhaPerfilConfirm : senhaConfirma
			},
			type : 'POST',
			success : function(response) {
				window.location.replace(enderecoSite + response.urlRedirect);
			}
		});
	});
}

Cidadao.prototype.verificaInconsistenciaCidadao = function() {
	var me = this;
	var nomePainel = $("#nome").text();
	var nomeTopo = $("#lblNomeUsuario").text();
	if (nomePainel != nomeTopo) {
		$.ajax({
			url : enderecoSite + "/cidadao/verificaInconsistencia",
			data : {
				nomePainel : nomePainel,
				nomeTopo : nomeTopo
			},
			type : "POST",
			success : function(resposta) {

			}
		});
	}
}

Cidadao.prototype.carregaDadosPremiacao = function(sorteioId) {
	var me = this;
	ajaxBloqueioDeTela("#dadosPremiacao", enderecoSite+ "/cidadao/carregaDadosDaPremiacao",
			{idSorteio : sorteioId}, 
			'POST',
			function(response) {
				if (response.isUsuarioPremiado) {
					
					$("#textUsuarioPremiado").html("Você foi sorteado!");
					
					me.listarPremiacao(sorteioId);
					
					$("#containerPremiacao").css('display', 'inline');
					
				} else {
					$("#textUsuarioPremiado").html("Você n&atilde;o foi sorteado.");
					$("#containerPremiacao").css('display', 'none');
				}
			});
}

Cidadao.prototype.dadosMeuPlacar = function() {
	ajaxBloqueioDeTela("#dadosMeuPlacar", enderecoSite + "/cidadao/meuPlacar",
			{}, 'POST', function(response) {
				if (response.numeroDeNotas != null) {
					$("#numeroDeNotasMeuPlacar").html(response.numeroDeNotas);
					$("#pontosProximoSorteio").html(
							response.pontosProximoSorteio);
					$("#totalPremiacaoMeuPlacar").html(
							response.totalPremiacaoMeuPlacar);
				}
				$("#contentMeuPlacar").css("display", "inline");
			});
}

Cidadao.prototype.concluirPerfil = function() {
	var me = this;
	$("#btnConclusaoPerfil").click(
			function(e) {
				var email = $("#emailCidadaoPerfil").val();
				if (!ehEmail(email)) {
					nfgMensagens.show(ALERT_TYPES.ERROR, "E-mail inválido!",
							true);
					return;
				}

				var senha = $("#passCidadao").val();
				var senhaConfirma = $("#passCidadaoConfirm").val();
				var cep = $("#cepCidadao").val();
				var tipoLogradouro = $("#selectLogradouro").val();
				var nomeLogradouro = $("#logradouroCidadao").val();
				var numero = $("#numeroCidadao").val();
				var bairro = $("#bairroCidadao").val();
				var complemento = $("#complementoCidadao").val();
				var uf = $("#selectUf").val();
				var cidade = $("#selectMunicipio").val();
				var enderecoHomologado = $("#enderecoHomolog").val();

				if (me.enderecoInvalido(cep, tipoLogradouro, nomeLogradouro,
						numero, bairro, complemento, uf, cidade, true)) {
					return;
				}

				if (!me.senhaValida(senha, senhaConfirma, null, true)) {
					return;
				}

				var data = {
					cpf : $("#cpfPerfil").val(),
					email : email,
					telefone : me.removeMascaraTelefone($("#telCidadaoPerfil")
							.val()),
					participaSortreio : $("#checkParticipaSorteio").is(
							':checked'),
					recebeEmail : $("#checkRecebeEmail").is(':checked'),

					cep : onlyNumbers(cep),
					tipoLogradouro : tipoLogradouro,
					nomeLogradouro : nomeLogradouro,
					nomeBairro : bairro,
					numero : numero,
					complemento : complemento,
					municipio : cidade,
					endHomologado : enderecoHomologado,
					senha : senha
				};

				ajaxBloqueioDeTela("#btnConclusaoPerfil", enderecoSite
						+ "/cidadao/gravaConclusaoPerfil", data, 'POST',
						function(response) {
							window.location.replace(enderecoSite
									+ response.urlRedirect);
						});

			});
}

Cidadao.prototype.gravaPerfil = function() {
	var me = this;
	$("#atualizarPerfil").click(
			function(e) {
				var email = $("#emailCidadaoPerfil").val().trim();
				if (email.length > 0 && !ehEmail(email)) {
					nfgMensagens.show(ALERT_TYPES.ERROR, "E-mail inválido!",
							true);
					return;
				}

				var cep = $("#cepCidadao").val();
				var tipoLogradouro = $("#selectLogradouro").val();
				var nomeLogradouro = $("#logradouroCidadao").val();
				var numero = $("#numeroCidadao").val();
				var bairro = $("#bairroCidadao").val();
				var complemento = $("#complementoCidadao").val();
				var uf = $("#selectUf").val();
				var cidade = $("#selectMunicipio").val();
				var enderecoHomologado = $("#enderecoHomolog").val();

				if (me.enderecoInvalido(cep, tipoLogradouro, nomeLogradouro,
						numero, bairro, complemento, uf, cidade, true)) {
					return;
				}

				var data = {
					cpf : $("#cpfPerfil").val(),
					email : email,
					telefone : me.removeMascaraTelefone($("#telCidadaoPerfil")
							.val()),
					participaSortreio : $("#checkParticipaSorteio").is(
							':checked'),
					recebeEmail : $("#checkRecebeEmail").is(':checked'),

					cep : onlyNumbers(cep),
					tipoLogradouro : tipoLogradouro,
					nomeLogradouro : nomeLogradouro,
					nomeBairro : bairro,
					numero : numero,
					complemento : complemento,
					municipio : cidade,
					endHomologado : enderecoHomologado
				};

				ajaxBloqueioDeTela("#atualizarPerfil", enderecoSite
						+ "/cidadao/gravaPerfil", data, 'POST', function(
						response) {
					window.location
							.replace(enderecoSite + response.urlRedirect);
				});

			});
}

Cidadao.prototype.ativarCaptcha = function(div, container) {
	$(div).css("display", "inline-block");

	var RecaptchaOptions = {
		theme : "clean",
		callback : Recaptcha.focus_response_field,
		lang : 'pt'
	};

	// PROD
	Recaptcha.create("6LdalwwUAAAAAGv62TpI91iKF-oOWij_7m54SlSQ", container,
			RecaptchaOptions);

}

Cidadao.prototype.initDatePicker = function($campo) {
	if (isIE()) {
		$campo.on('keydown', function() {
			return false;
		});
	}
	$campo.datetimepicker({
		formatDate : 'd-m-Y',
		format : 'd/m/Y',
		lang : 'pt',
		timepicker : false,
		allowBlank : true,
		maxDate : 0,
		mask : true,
		closeOnDateSelect : true,
		yearStart : 1900,
		validateOnBlur : false
	});

	$(".datepickerIcon").click(function() {
		$campo.datetimepicker('show');
	}).css("top", "0").css("cursor", "pointer");
};

Cidadao.prototype.removeMascaraTelefone = function(valor) {
	return removeCharacteres(valor, [ '-', '(', ')' ]);
};

Cidadao.prototype.removeMascaraCpf = function(cpf) {
	return removeCharacteres(cpf, [ '_', '.', '-', '.' ]);
};

Cidadao.prototype.removeMascaraIE = function(ie) {
	return removeCharacteres(ie, [ '.', '.', '-' ]);
};

Cidadao.prototype.removeMascaraValorTotal = function(valor) {
	var novoValor = removeCharacteres(valor, [ '_', '.', '.', 'R', '$' ]);
	return novoValor.replace(",", ".").trim();
};

Cidadao.prototype.alteraSorteio = function() {
	var me = this;
	$("#selectSorteios").on("change", function(e) {
		me.carregaDadosSorteio($("#selectSorteios").val());
	});
}

Cidadao.prototype.carregaDadosSorteio = function(sorteioId) {
	
	var me = this;
	
	if (sorteioId != null) {
		ajaxBloqueioDeTela(
				"#dadosSorteioSelecionado",
				enderecoSite + "/cidadao/carregaDadosDoSorteioParaCidadao",
				{
					idSorteio : sorteioId
				},
				'POST',
				function(response) {
					$("#dtSorteio").html(response.sorteio.dataRealizacaoStr);
					$("#textDtLoteria").html(
							response.sorteio.dataExtracaoLoteriaStr);
					$("#textSorteioRealizado").html(
							response.sorteio.realizadoBoolean ? "Sim"
									: "N&atilde;o");
					$("#textTotalDocsPeriodo").html(response.totalDocs);
					$("#textTotalPontos")
							.html(
									response.sorteio.status == "1" ? ("Aguardando geraç&atilde;o de bilhetes.")
											.fontcolor("goldenrod")
											: response.sorteio.status == "2" ? ("Gerando bilhetes.")
													.fontcolor("goldenrod")
													: response.totalPontos);
					$("#textTotalBilhetes")
							.html(
									response.sorteio.status == "1" ? ("Aguardando geraç&atilde;o de Bilhetes.")
											.fontcolor("goldenrod")
											: response.sorteio.status == "2" ? ("Gerando bilhetes.")
													.fontcolor("goldenrod")
													: response.totalBilhetes);
					$("#textNumeroLoteria")
							.html(response.sorteio.numeroLoteria);

					if (response.sorteio.status == "1"
							|| response.sorteio.status == "2") {
						$("#linkDetalhamentoBilhetes").prop("disabled", true);
						$("#linkDetalhamentoPontos").prop("disabled", true);
					} else {
						$("#linkDetalhamentoBilhetes").prop("disabled", false);
						$("#linkDetalhamentoPontos").prop("disabled", false);
					}
					
					me.carregaDadosPremiacao(sorteioId);

				});
		return true;
	} else {
		return false;
	}
}

Cidadao.prototype.selecionaUf = function() {
	var me = this;
	$("#selectUf").on("change", function(e) {
		me.carregaMunicipios($("#selectUf").val());
	});
}

Cidadao.prototype.limparFiltros = function() {
	var me = this;
	$(".limparFiltro").click(function() {
		$(this).closest('form').find("input[type=text], textarea").val("");
		me.eventoFiltrar();
	});
}

Cidadao.prototype.eventoFiltrar = function() {
	$("#filtrarMinhasNotasEmDetalhe").click();
}

Cidadao.prototype.carregaBancos = function() {

	return ajaxBloqueioDeTela("#divBanco", enderecoSite
			+ "/cidadao/carregaBancosPorCodigo", {}, 'POST',
			function(response) {
				var select = $("#selectBanco");
				select.find('option').remove().end().append(
						'<option >Selecione</option>').val(null);
				$.each(response.bancos, function() {
					select.append($("<option />").val(this.codigo).text(
							this.nome));
				});

			})
};

Cidadao.prototype.carregaMunicipios = function(uf) {
	if (uf != null) {

		return ajaxBloqueioDeTela("#divUfMunicipio", enderecoSite
				+ "/cidadao/carregaCidadesPorUf", {
			uf : uf
		}, 'POST', function(response) {
			var select = $("#selectMunicipio");
			select.find('option').remove().end().append(
					'<option >Selecione</option>').val(null);
			$.each(response.municipios, function() {
				select.append($("<option />").val(this.codigoMunicipio).text(
						this.nome));
			});
		})
	}
};

Cidadao.prototype.salvaNovaReclamacao = function() {
	var me = this;
	$("body")
			.on(
					"click",
					"#btnCriaNovaReclamacao",
					function() {
						var motivoReclamacao = $(
								"input[type='radio'][name='radioMotivoReclamacao']:checked")
								.val();
						var problemaEmpresaReclamacao = $(
								"input[type='radio'][name='radioProblemaEmpresa']:checked")
								.val();

						var tipoDocFiscalReclamacao = $(
								"input[type='radio'][name='radioTipoDocReclamacao']:checked")
								.val();

						var dataEmissaoDocFiscal = $(
								"#dataEmissaoDocFiscalReclamacao").val();
						var numeroReclamacao = $("#numeroDocFiscalReclamacao")
								.val();

						// tratamento para os fins devidos, devido tipo input
						// number do html5
						// numeroReclamacao =
						// Math.abs(parseInt(numeroReclamacao));

						var iEReclamacao = me.removeMascaraIE($(
								"#ieDocFiscalReclamacao").val());
						var valorReclamacao = me.removeMascaraValorTotal($(
								"#valorDocFiscalReclamacao").val());

						document.getElementById("txtDataEmissao1").innerHTML = dataEmissaoDocFiscal;
						document.getElementById("txtValorCompra1").innerHTML = toMoneyFormat(valorReclamacao);
						document.getElementById("txtInscricaoEstadual1").innerHTML = iEReclamacao;
						document
								.getElementById("txtNumeroDocFiscalReclamacao1").innerHTML = numeroReclamacao;

						document.getElementById("txtDataEmissao2").innerHTML = dataEmissaoDocFiscal;
						document.getElementById("txtValorCompra2").innerHTML = toMoneyFormat(valorReclamacao);
						document.getElementById("txtInscricaoEstadual2").innerHTML = iEReclamacao;
						document
								.getElementById("txtNumeroDocFiscalReclamacao2").innerHTML = numeroReclamacao;

						switch (motivoReclamacao) {
						case '1':
							document.getElementById("txtMotivoReclamacao").innerHTML = "Cupom Fiscal/Nota Fiscal/Nota Fiscal de Consumidor Eletrônica com meu CPF n&atilde;o consta no painel Minhas Notas do Portal do Cidad&atilde;o";
							break;
						case '2':
							document.getElementById("txtMotivoReclamacao").innerHTML = "N&atilde;o perguntou se eu desejava incluir o CPF na nota/cupom fiscal";
							break;
						case '3':
							document.getElementById("txtMotivoReclamacao").innerHTML = "Exigiu a elaboraç&atilde;o de um cadastro para a colocaç&atilde;o do CPF";
							break;
						case '4':
							document.getElementById("txtMotivoReclamacao").innerHTML = "Alegou problema com equipamento ou sistema";
							break;
						case '5':
							document.getElementById("txtMotivoReclamacao").innerHTML = "Dificultou o fornecimento do documento fiscal com CPF";
							break;
						case '6':
							document.getElementById("txtMotivoReclamacao").innerHTML = "Alegou n&atilde;o participar do programa.";
							break;
						}

						if (iEReclamacao == null || iEReclamacao.length == 0) {
							nfgMensagens.show(ALERT_TYPES.ERROR,
									"Preencha o campo I.E.", true);
							$("#ieDocFiscalReclamacao").focus();
							return;
						}
						if (dataEmissaoDocFiscal == null
								|| dataEmissaoDocFiscal == "__/__/____") {
							nfgMensagens.show(ALERT_TYPES.ERROR,
									"Preencha o campo Data de Emissão.", true);
							$("#dataEmissaoDocFiscalReclamacao").focus();
							return;
						} else {
							var dataMinima = retornaData35DiasAtras();
							var dataReclamacaoDate = formatDateToSave(dataEmissaoDocFiscal);
							var newDataMinima = retornaMesAno(dataMinima);
							var newDataReclamacao = retornaMesAno(dataReclamacaoDate);

							dataEmissaoDocFiscal = formatDateToSave(dataEmissaoDocFiscal);
							var dataDentroDoPrazo = !(newDataReclamacao < newDataMinima);

						}

						if (numeroReclamacao == null
								|| numeroReclamacao.length == 0) {
							nfgMensagens.show(ALERT_TYPES.ERROR,
									"Preencha o campo "
											+ $("#lblNumCooDoc").html()
													.replace(':', '') + ".",
									true);
							$("#numeroDocFiscalReclamacao").focus();
							return;
						}
						if (valorReclamacao == null
								|| valorReclamacao.length == 0
								|| valorReclamacao <= 0) {
							nfgMensagens.show(ALERT_TYPES.ERROR,
									"Preencha o campo Valor.", true);
							$("#valorDocFiscalReclamacao").focus();
							return;
						}

						if ($('#fileReclamacao').val() != null
								&& $('#fileReclamacao').val().length > 0) {
							var ext = $('#fileReclamacao').val().split('.')
									.pop().toLowerCase();
							if ($.inArray(ext, [ 'pdf', 'png', 'jpg', 'jpeg' ]) == -1) {
								nfgMensagens
										.show(
												ALERT_TYPES.ERROR,
												"Extens&atilde;o de imagem inválida! O arquivo deve estar em uma das seguinte extensões: jpg, pdf ou png.",
												true);
								return;
							}
						}

						if ($('#fileReclamacao').val() == '') {

							nfgMensagens.show(ALERT_TYPES.ERROR,
									"Insira um anexo de documento.", true);
							return;

						}

						var urlBusca = enderecoSite
								+ "/cidadao/buscarEmpresaPorIe";
						me.buscarEmpresaPorIe(urlBusca, iEReclamacao);
						me.confirmarReclamacaoBtnSalvar(motivoReclamacao,
								dataDentroDoPrazo);

						var formData = new FormData(
								$("#formNovaReclamacaoCidadao")[0]);

						formData.append("numeroReclamacao", numeroReclamacao);
						formData.append("tipoDocFiscalReclamacao",
								tipoDocFiscalReclamacao);
						formData.append("codgMotivo", motivoReclamacao);
						formData.append("dataEmissaoDocFiscal",
								dataEmissaoDocFiscal);
						formData.append("iEReclamacao", iEReclamacao);
						formData.append("valorReclamacao", valorReclamacao);
						formData.append("dataDentroDoPrazo", dataDentroDoPrazo);
						formData.append("problemaEmpresaReclamacao",
								problemaEmpresaReclamacao);

						me.concluirInclusaoReclamacao(formData);
					});
}

Cidadao.prototype.concluirInclusaoReclamacao = function(formData) {
	$("body").off("click", "#btnSalvarNovaReclamacao");
	$("body")
			.on(
					"click",
					"#btnSalvarNovaReclamacao",
					function() {
						bloqueioDeTela("#btnSalvarNovaReclamacao");

						$
								.ajax({
									url : enderecoSite
											+ "/cidadao/salvarNovaReclamacao",
									data : formData,
									type : 'POST',
									cache : false,
									contentType : false,
									processData : false,
									success : function(response) {
										$("#btnSalvarNovaReclamacao").unblock();

										if (response.sucesso) {
											$("#modalHome").modal('hide');
											// $("#formReclamacoes").submit();
											$("#btnReclamacoes").click();

										} else {
											if (response.msg_erro == null
													|| response.msg_erro == undefined) {
												nfgMensagens
														.show(
																ALERT_TYPES.ERROR,
																'O sistema se comportou de forma inesperada - '
																		+ moment()
																				.format(
																						'DD/MM/YYYY HH:mm:ss')
																		+ '. Tente novamente após 5 minutos.',
																true);
											} else {
												nfgMensagens
														.show(
																ALERT_TYPES.ERROR,
																response.msg_erro,
																true);
											}
											//
											// O sistema se comportou de forma
											// inesperada - ' +
											// moment().format('DD/MM/YYYY
											// HH:mm:ss') + '. Tente novamente
											// após 5 minutos.
										}
									}
								});
					});
}

Cidadao.prototype.mudancaAcaoReclamacao = function() {
	var me = this;
	$("body").on("change", "#selectNovoStatusReclamacao", function() {
		if ($(this).val() == "-1") {
			$("#btnAlterarReclamacao").css('display', 'none');
		} else {
			$("#btnAlterarReclamacao").css('display', 'inline');
		}

		switch ($(this).val()) {
		case "-1":/** Aguardar */
			$("#divInfoReclamacao").css('display', 'none');
			return;
		case "11":/** Cancelar */
			$("#divInfoReclamacao").css('display', 'none');
			return;
		case "9":/** Concordar resposta empresa */
			$("#divInfoReclamacao").css('display', 'block');
			return;
		case "10":/** Recusar resposta empresa */
			$("#divInfoReclamacao").css('display', 'block');
			return;
		}
	});
}

Cidadao.prototype.alterarReclamacao = function() {

	var me = this;

	$("body")
			.on(
					"click",
					"#btnAlterarReclamacao",
					function() {
						var tipoCompl = $(
								"#selectNovoStatusReclamacao option:selected")
								.text();

						var title = "<strong>Confirmaç&atilde;o</strong>";
						var botaoConfirmar = "<div class='col-sm-1'></div><a class='btn btn-default col-sm-5 fechaModalConfirmacao' >N&atilde;o</a>";
						var botaoCancelar = "<a class='btn btn-primary col-sm-5 fechaModalConfirmacao' id='btnConfirmaAlteracaoReclamacao'>Sim</a><div class='col-sm-1'></div>";

						me.confirmModal = new ConfirmModal(title,
								"Tem certeza que deseja "
										+ tipoCompl.toLowerCase() + " ?",
								botaoConfirmar, botaoCancelar);
						me.confirmModal.open();
					});
}

Cidadao.prototype.ConfirmaAlteracaoReclamacao = function() {
	var me = this;
	$("body")
			.on(
					"click",
					"#btnConfirmaAlteracaoReclamacao",
					function() {
						var codgTipoCompl = $(
								"#selectNovoStatusReclamacao option:selected")
								.val();
						var comentario = $("#infoReclamacao").val();
						var idReclamacao = $("#idReclamacao").val();

						ajaxBloqueioDeTela(
								this,
								enderecoSite
										+ '/cidadao/alterarSituacaoReclamacao',
								{
									idReclamacao : idReclamacao,
									novoCodgTipoCompl : codgTipoCompl,
									infoReclamacao : comentario
								},
								'POST',
								function(response) {

									if (response.sucesso) {
										nfgMensagens
												.show(
														ALERT_TYPES.SUCCESS,
														"Reclamaç&atilde;o atualizada com sucesso. Continue acompanhando-a na listagem de andamento da reclamaç&atilde;o.",
														true);
									} else {
										nfgMensagens
												.show(
														ALERT_TYPES.ERROR,
														"Erro ao tentar atualizar a situaç&atilde;o da reclamaç&atilde;o. Tente de novo posteriormente.",
														true);
									}

									$("#btnAndamentoReclamacao").click();
									$("#btnReclamacoes").click();
									me.carregaComboAcoesDisponiveisReclamacao();
									me.resetaElementosAcaoReclamacao();

								})

					});
}

Cidadao.prototype.fechaModalConfirmacao = function() {
	var me = this;
	$("body").on("click", ".fechaModalConfirmacao", function() {
		me.confirmModal.close();
	});
}

Cidadao.prototype.resetaElementosAcaoReclamacao = function() {
	var me = this;

	$("#divInfoReclamacao").css('display', 'none');
	$("#btnAlterarReclamacao").css('display', 'none');
	$("#selectNovoStatusReclamacao").val("-1");

	var codgSituacao = $("#codgSituacaoReclamacao").val();
	if (codgSituacao == 2 || codgSituacao == 3 || codgSituacao == 5) {
		$("#painelAcoesCidadaoReclamacao").html("");
	} else {
		$("#painelAcoesCidadaoReclamacao").css('display', 'block');
	}

}

Cidadao.prototype.carregaComboAcoesDisponiveisReclamacao = function() {
	var me = this;
	var idReclamacao = $("#idReclamacao").val();
	$.ajax({
		url : enderecoSite + "/cidadao/selectAcoesDisponiveis",
		data : {
			idReclamacao : idReclamacao
		},
		type : 'POST',
		success : function(response) {
			var select = $("#selectNovoStatusReclamacao");
			select.find('option').remove().end().append(
					'<option value="-1" >Aguardar</option>');
			$.each(response.acoesDisponiveis, function() {
				select.append($("<option />").val(this.codigo).text(
						this.tipoSituacaoAcao));
			});
		}
	});
}

Cidadao.prototype.confirmarReclamacaoBtnSalvar = function(motivoReclamacao,
		dataDentroDoPrazo) {
	$("#messagesContainerModal").html("");
	$("#motivoReclamacaoDiv").css("display", "none");
	$("#problemaEmpresaDiv").css("display", "none");
	$("#tipoDocReclamacaoDiv").css("display", "none");
	$("#dadosDocReclamacaoDiv").css("display", "none");
	$("#divBtnCriaNovaReclamacao").css("display", "none");

	if (motivoReclamacao == "1") {
		$("#divConfirmacaoReclamacaoNotasNoPainel").css("display", "block");
	} else {
		$("#divConfirmacaoReclamacaoProblemasComEmpresa").css("display",
				"block");
	}

	if (dataDentroDoPrazo) {
		$("#textoDataDentroDoPrazo").css("display", "block");
	} else {
		$("#textoDataDentroDoPrazo").css("display", "none");
	}

	$("#divBtnSalvarNovaReclamacao").css("display", "block");
}

Cidadao.prototype.voltarTelaDeReclamacao = function() {
	$("body")
			.on(
					"click",
					"#btnVoltarReclamacao",
					function() {
						var motivoReclamacao = $(
								"input[type='radio'][name='radioMotivoReclamacao']:checked")
								.val();

						if (motivoReclamacao == "1") {
							$("#problemaEmpresaDiv").css("display", "none");
						} else {
							$("#problemaEmpresaDiv").css("display", "block");
						}

						$("#motivoReclamacaoDiv").css("display", "block");
						$("#tipoDocReclamacaoDiv").css("display", "block");
						$("#dadosDocReclamacaoDiv").css("display", "block");
						$("#divBtnCriaNovaReclamacao").css("display", "block");
						$("#divConfirmacaoReclamacaoNotasNoPainel").css(
								"display", "none");
						$("#divConfirmacaoReclamacaoProblemasComEmpresa").css(
								"display", "none");
						$("#divBtnSalvarNovaReclamacao").css("display", "none");
					});
}

Cidadao.prototype.buscarEmpresaPorIe = function(url, inscricao) {
	$.get(url, {
		inscricao : inscricao
	}, function(data) {
		$('#nomeFantasia1').html(data.nomeFantasia);
		$('#nomeFantasia2').html(data.nomeFantasia);
	});
}

Cidadao.prototype.fluxoDeTelaNovaReclamacao = function() {
	var me = this;

	$("body").on("click", ".radioMotivoReclamacao", function() {

		var naoEmitiuDocumento = "0";
		var naoConstaEmMinhasNotas = "1";
		var problemaEmpresa = "2";

		$("#tooltipIEReclamacao").tooltip({
			content : '<img src="/to-legal/images/nf_ie_b.png" />'
		});
		$("#tooltipDataReclamacao").tooltip({
			content : '<img src="/to-legal/images/nf_data_b.png" />'
		});
		$("#tooltipNumeroReclamacao").tooltip({
			content : '<img src="/to-legal/images/nf_nro_b.png" />'
		});
		$("#tooltipValorReclamacao").tooltip({
			content : '<img src="/to-legal/images/nf_valor_b.png" />'
		});

		switch ($(this).val()) {
		case problemaEmpresa:
			$("#problemaEmpresaDiv").css("display", "block");
			$("#tipoDocReclamacaoDiv").css("display", "block");
			$("#dadosDocReclamacaoDiv").css("display", "block");
			$("#linkDenunciaDiv").css("display", "none");
			$("#divBtnCriaNovaReclamacao").css("display", "block");
			$("#divConfirmacaoReclamacao").css("display", "none");
			$("#divBtnSalvarNovaReclamacao").css("display", "none");
			break;
		case naoConstaEmMinhasNotas:
			$("#problemaEmpresaDiv").css("display", "none");
			$("#tipoDocReclamacaoDiv").css("display", "block");
			$("#dadosDocReclamacaoDiv").css("display", "block");
			$("#linkDenunciaDiv").css("display", "none");
			$("#divBtnCriaNovaReclamacao").css("display", "block");
			$("#divConfirmacaoReclamacao").css("display", "none");
			$("#divBtnSalvarNovaReclamacao").css("display", "none");
			break;
		case naoEmitiuDocumento:
			$("#problemaEmpresaDiv").css("display", "none");
			$("#tipoDocReclamacaoDiv").css("display", "none");
			$("#dadosDocReclamacaoDiv").css("display", "none");
			$("#linkDenunciaDiv").css("display", "block");
			$("#divBtnCriaNovaReclamacao").css("display", "none");
			$("#divConfirmacaoReclamacao").css("display", "none");
			$("#divBtnSalvarNovaReclamacao").css("display", "none");
			break;
		}
	});

	$("body").on("click", ".radioTipoDocReclamacao", function() {
		var notaFiscal = "1";
		var notaFiscalEletronica = "2";
		var cupom = "3";

		if ($(this).val() == notaFiscal) {
			$("#lblNumCooDoc").html("*N&uacute;mero:")
			$("#tooltipIEReclamacao").css("display", "block");
			$("#tooltipDataReclamacao").css("display", "block");
			$("#tooltipNumeroReclamacao").css("display", "block");
			$("#tooltipValorReclamacao").css("display", "block");
			$("#tooltipIEReclamacao").tooltip({
				content : '<img src="/to-legal/images/nf_ie_b.png" />'
			});
			$("#tooltipDataReclamacao").tooltip({
				content : '<img src="/to-legal/images/nf_data_b.png" />'
			});
			$("#tooltipNumeroReclamacao").tooltip({
				content : '<img src="/to-legal/images/nf_nro_b.png" />'
			});
			$("#tooltipDataReclamacao").tooltip({
				content : '<img src="/to-legal/images/nf_valor_b.png" />'
			});
		} else if ($(this).val() == notaFiscalEletronica) {
			$("#lblNumCooDoc").html("*N&uacute;mero:")
			$("#tooltipIEReclamacao").css("display", "none");
			$("#tooltipDataReclamacao").css("display", "none");
			$("#tooltipNumeroReclamacao").css("display", "none");
			$("#tooltipValorReclamacao").css("display", "none");
		} else if ($(this).val() == cupom) {
			$("#lblNumCooDoc").html("*COO:");
			$("#tooltipIEReclamacao").css("display", "block");
			$("#tooltipDataReclamacao").css("display", "block");
			$("#tooltipNumeroReclamacao").css("display", "block");
			$("#tooltipValorReclamacao").css("display", "block");
			$("#tooltipIEReclamacao").tooltip({
				content : '<img src="/to-legal/images/cf_ie_b.png" />'
			});
			$("#tooltipDataReclamacao").tooltip({
				content : '<img src="/to-legal/images/cf_data_b.png" />'
			});
			$("#tooltipNumeroReclamacao").tooltip({
				content : '<img src="/to-legal/images/cf_coo_b.png" />'
			});
			$("#tooltipValorReclamacao").tooltip({
				content : '<img src="/to-legal/images/cf_valor_b.png" />'
			});
		}
	});
}
