/*
 * VIWeb - Visualização de Informações na Web
 * Autores: 
 *		- Leandro Hernandez Almeida 
 *		- Leonardo Hernandez Almeida
 * Orientador: Prof. Dr. Bianchi Serique Meiguins
*/

// constantes
var CONTINUO = 1;
var DISCRETO = 0;
var STRING = 1;
var INTEGER = 2;
var DOUBLE = 3;
var DATA = 4;

// objetos que serao retornados (dados)
var itens;
var cores;
var colunas;
var config;
var rsliders = [];

// desenho
var tamObj = 15; // tamanho do item
var _forma;
var canvas;
var canvasForma;
var canvasCor;
var g;

// deslocamentos e tamanho do eixo
var tamanhoEixo;
var bordaX;
var bordaY;

// ponto inicial (0,0 do sistema)
var ptInicialX;
var ptInicialY;

// formulario de filtros
var formFiltros;
var formConfig;
var checksFiltro;
var selectsConfig;


inicializar = function(){
	_forma = new Forma();
	formConfig = $("formConfig");
	formFiltros = $("formFiltros");
};

configurarDisplay = function(){
	canvas = $("canvas");
	canvasForma = $("canvasForma");
	canvasCor = $("canvasCor");
	tamanhoEixo = 600;
	bordaX = (canvas.width - tamanhoEixo)/2;
	bordaY = 30;
	ptInicialX = bordaX;
	ptInicialY = bordaY + tamanhoEixo;
	
	Event.observe(canvas, 'click', mouseClicked);
};

var startTime,endTime,totalTime;
desenhar = function(){
	try{
		g = canvas.getContext("2d");
	}catch(err){
		alert("Seu navegador não suporta Canvas.\nNecessário Firefox 2+, Opera 9+ ou Safari.");
		return;
	}
	
	startTime = new Date();
	
	g.clearRect(0, 0, canvas.width, canvas.height);
	CanvasTextFunctions.enable(g);
	desenharEixos();
	desenharItens();
	desenharRotulos();
	
	endTime = new Date();
	totalTime = (endTime-startTime);
	$("resposta").innerHTML += "<p><b>Tempo Renderização:</b> "+totalTime+" ms.</p>";
};

desenharEixos = function(){
	// eixos principais
	g.fillStyle = "rgb(0,0,0)";
	g.fillRect(ptInicialX, (ptInicialY-tamanhoEixo), 2, tamanhoEixo); // eixo x
	g.fillRect(ptInicialX, ptInicialY, tamanhoEixo, 2); // eixo y
	
	// eixos complementares (cinza claro)
	g.fillStyle = "rgb(200,200,200)";
	g.fillRect(ptInicialX, (ptInicialY-tamanhoEixo), tamanhoEixo, 1);
	g.fillRect(ptInicialX+tamanhoEixo, (ptInicialY-tamanhoEixo), 1, tamanhoEixo);
	
	// grade
	var variacaoEixo = tamanhoEixo / (config.rotulosX.length - 1);
	for(var i=1; i<config.rotulosX.length-1; i++){
		g.fillRect(ptInicialX, ptInicialY-(variacaoEixo*i), tamanhoEixo, 1);
		g.fillRect(ptInicialX+(variacaoEixo*i), (ptInicialY-tamanhoEixo), 1, tamanhoEixo);
	}
};

desenharRotulos = function(){
	// nomes dos eixos
	g.strokeStyle = "rgb(0,0,0)";
	g.save();
  		g.translate(10, canvas.height/2);
  		g.rotate(-3.1416/2);
  		
  		g.drawText("", 10, 0, 0, config.eixoY);
  	g.restore();
  	g.drawText("", 10, canvas.width/2, canvas.height-5, config.eixoX);
  	
  	
  	// valores dos rotulos
	var variacaoEixo = tamanhoEixo / (config.rotulosX.length - 1);
	var x, y;
	g.strokeStyle = "rgb(150,150,150)";
	
		// eixo X
	x = ptInicialX;
	y = ptInicialY + 20;
	for(var i=0; i<config.rotulosX.length; i++){
		g.drawText("", 8, x, y, config.rotulosX[i].toString());
		x += variacaoEixo;
	}
	
		// eixo Y
	x = ptInicialX - bordaX + 20;
	y = ptInicialY;
	for(var i=0; i<config.rotulosY.length; i++){
		g.drawText("", 8, x, y, config.rotulosY[i].toString());
		y -= variacaoEixo;
	}
};

desenharItens = function(){
	var n = itens.length - 1;
	g.strokeStyle = "gray";
	for(var i=n; i>=0; i--){
		g.fillStyle = cores[ itens[i].c ];
		_forma.desenhar(g, itens[i].f, ptInicialX + itens[i].x, ptInicialY - itens[i].y, tamObj);
	}
};

desenharLegendaForma = function(){
	var gF;
	try{
		gF = canvasForma.getContext("2d");
	}catch(err){
		alert("Seu navegador não suporta Canvas.\nNecessário Firefox 2+, Opera 9+ ou Safari.");
		return;
	}
	gF.clearRect(0, 0, canvasForma.width, canvasForma.height);
	gF.fillStyle = "rgb(0,0,0)";
	CanvasTextFunctions.enable(gF);
	
	// pega coluna de Forma
	var col = null;
	for(var i=0; i<colunas.length; i++){
		if(colunas[i].nome == config.forma){
			col = colunas[i];
			break;
		}
	}
	if(col != null){ // poe legenda de Formas
		var x=20;
		var y=10;
		
		for(var j=0; j<col.valores.length; j++){
			_forma.desenhar(gF, j, x, y, 15);
			gF.drawText("", 10, x+20, y+5, col.valores[j].toString());
			y += 25;
		}
	}
};
desenharLegendaCor = function(){
	var gC;
	try{
		gC = canvasCor.getContext("2d");
	}catch(err){
		alert("Seu navegador não suporta Canvas.\nNecessário Firefox 2+, Opera 9+ ou Safari.");
		return;
	}
	
	gC.clearRect(0, 0, canvasCor.width, canvasCor.height);
	gC.fillStyle = "rgb(0,0,0)";
	CanvasTextFunctions.enable(gC);
	
	// pega coluna de Cor
	var col = null;
	for(var i=0; i<colunas.length; i++){
		if(colunas[i].nome == config.cor){
			col = colunas[i];
			break;
		}
	}
	if(col != null){ // poe legenda de Formas
		var x=10;
		var y=10;
		var w=30;
		var h=15;
		
		if(col.valores.length > 10){// desenha primeiro e ultimo
			canvasCor.height = 75;
			// primeira cor
			gC.fillStyle = cores[0];
			gC.fillRect(x, y, w, h);
			gC.drawText("", 10, x+w+10, y+10, col.valores[0].toString());

			// cor do meio			
			y += 20;
			var ind = Math.floor(cores.length/2);
			
			gC.fillStyle = cores[ind];
			gC.fillRect(x, y, w, h);
			gC.drawText("", 10, x+w+10, y+10, col.valores[ind].toString());

			// ultima cor
			y += 20;
			
			gC.fillStyle = cores[cores.length-1];
			gC.fillRect(x, y, w, h);
			gC.drawText("", 10, x+w+10, y+10, col.valores[cores.length-1].toString());
		}else{ // desenha todos as cores
			canvasCor.height = cores.length * 22;
			for(var j=0; j<col.valores.length; j++){
				gC.fillStyle = cores[j];
				gC.fillRect(x, y, w, h);
				gC.drawText("", 10, x+w+10, y+10, col.valores[j].toString());
				y += 20;
			}
		}
	}
};

/*
* Envia uma requisição de filtro para o servidor via AJAX. 
*/
filtrar = function(){
	showLoading(true);
	// construir JSON de filtro
	var obj; // objeto do tipo: {"valor": "", "marcado": true/false} 
	var objFiltro; // sera objeto filtro
	var arrayFiltros = []; // array com todos os filtros
	
	var checks;
	for(var i=0; i<colunas.length; i++){
		
		if(colunas[i].desc == DISCRETO){
			objFiltro = new FiltroDiscreto(colunas[i].nome);
			checks = formFiltros.getInputs('checkbox', colunas[i].nome);
			for(var j=0; j<checks.length; j++){
				obj = {"valor": checks[j].value, "marcado": checks[j].checked};
				objFiltro.add(obj);
			}
		}else{
			for(var j=0; j<rsliders.length; j++){
				var trackID = rsliders[j].track.id;
				var colunaComp = colunas[i].nome + "_track"; // para comparar
				if(colunaComp == trackID){
					objFiltro = new FiltroContinuo(colunas[i].nome);
					var valores = rsliders[j].values;
					
					if(colunas[i].tipo == INTEGER){
						objFiltro.min = parseInt(valores[0]);
						objFiltro.max = parseInt(valores[1]);
					}else{
						objFiltro.min = parseFloat(formatDouble(valores[0], 2));
						objFiltro.max = parseFloat(formatDouble(parseFloat(valores[1]), 2));
					}
					
					break;
				}
			}
		}
		arrayFiltros.push(objFiltro);
	}
	
	var url = "/VIWeb/Consulta";
	var opcoes = {
		method: 'post',
		postBody: 'filtro='+arrayFiltros.toJSON(),
		onSuccess: sucessoFiltro,
		onFailure: falhaRequisicao
	}
	new Ajax.Request(url, opcoes);
};

sucessoFiltro = function(t){
	itens = t.responseText.evalJSON();
	desenhar();
	$("resposta").innerHTML = "<p><b>Total de Itens:</b> "+ itens.length + "</p>";
	showLoading(false);
};
/*
* Envia uma requisição de filtro para o servidor via AJAX. 
*/
configurar = function(){
	var selecionado;
	var configuracao = {
		eixoX: "",
		eixoY: "",
		forma: "",
		cor: ""
	};
	
	// na pratica soh envia uma config de cada vez.
	// eixo X
	selecionado = formConfig.eixoX.options[formConfig.eixoX.selectedIndex];
	if(selecionado.value != config.eixoX)
		configuracao.eixoX = selecionado.value;
	// eixo Y
	selecionado = formConfig.eixoY.options[formConfig.eixoY.selectedIndex];
	if(selecionado.value != config.eixoY)
		configuracao.eixoY = selecionado.value;
	// forma
	selecionado = formConfig.forma.options[formConfig.forma.selectedIndex];
	if(selecionado.value != config.forma)
		configuracao.forma = selecionado.value;
	// cor
	selecionado = formConfig.cor.options[formConfig.cor.selectedIndex];
	if(selecionado.value != config.cor)
		configuracao.cor = selecionado.value;
	
	var url = "/VIWeb/Consulta";
	var opcoes = {
		method: 'post',
		postBody: 'config='+Object.toJSON(configuracao),
		onSuccess: sucessoConfig,
		onFailure: falhaRequisicao
	}
	showLoading(true);
	new Ajax.Request(url, opcoes);
};

sucessoConfig = function(t){
	json = t.responseText.evalJSON();
	itens = json.itens;
	cores = json.cores;
	config = json.config;
	desenhar();
	desenharLegendaForma();
	desenharLegendaCor();
	$("resposta").innerHTML = "<p><b>Total de Itens:</b> "+ itens.length + "</p>";
	showLoading(false);
};


/*
* Primeira execucao do sistema.
* - Pega informacoes da base (colunas e seus valores, itens (x, y, f, c)).
* - Configura visualizacao (atributos selecionados automaticamente para forma e cor).
* - Monta interface.
* - Desenha os itens.
*/
carregarDados = function(){
	var url = "/VIWeb/Consulta";
	var opcoes = {
		method: 'get',
		parameters: {"base":0, "eixo": tamanhoEixo, "pixelMinX": 0, "pixelMinY": 0 },
		onSuccess: sucessoLoad,
		onFailure: falhaRequisicao
	}
	showLoading(true);
	new Ajax.Request(url, opcoes);
};
sucessoLoad = function(t){
	json = t.responseText.evalJSON();
	itens = json.itens;
	cores = json.cores;
	colunas = json.colunas;
	config = json.config;
	
	montarInterface();
	addChangeListeners();
	desenhar();
	desenharLegendaForma();
	desenharLegendaCor();
	$("resposta").innerHTML = "<p><b>Total de Itens:</b> "+ itens.length + "</p>";
	$("resposta").innerHTML += "<p><b>Total de Colunas:</b> "+ colunas.length + "</p>";
	showLoading(false);
};
falhaRequisicao = function(t){
	$("resposta").innerHTML = "Erro: <br/>"+ t.responseText;
	showLoading(false);
};

/*
* Cria interface de filtros e configuracao da visualizacao.
*/
montarInterface = function(){
	
	// filtros discretos
	for(var i=0; i<colunas.length; i++){
		if(colunas[i].desc == DISCRETO)
			addFiltroDiscreto(colunas[i], formFiltros);
	}
	
	// filtros continuos
	// preencher SELECT de configuracao de Eixos (X e Y) (Apenas colunas tipo CONTINUO)
	for(var i=0; i<colunas.length; i++){
		if(colunas[i].desc == CONTINUO){
			addFiltroContinuo(colunas[i], formFiltros);
			addOptionEixos(colunas[i], formConfig);
		}
	}
	
	// preencher SELECT de configuracao de Cor (Todos os atributos)
	
	for(var i=0; i<colunas.length; i++)
		addOptionCor(colunas[i], formConfig);
	
	// preencher SELECT de configuracao de Forma (atributos com valores < 4)
	for(var i=0; i<colunas.length; i++)
		if(colunas[i].valores.length < 4)
			addOptionForma(colunas[i], formConfig);
	
};

/*
* Adiciona ouvintes (listeners) aos checkboxes/selects
*/
addChangeListeners = function(){
	checksFiltro = formFiltros.getInputs("checkbox");
	for(var i=0; i<checksFiltro.length; i++)
		Event.observe(checksFiltro[i], 'click', filtrar);

	selectsConfig = formConfig.getElements();
	for(var i=0; i<selectsConfig.length; i++)
		Event.observe(selectsConfig[i], 'change', configurar);
};

addOptionForma = function(coluna, form){
	var option = document.createElement('option');
	option.appendChild(document.createTextNode(coluna.nome));
	option.setAttribute('value', coluna.nome);
	if(coluna.nome == config.forma)
		option.setAttribute('selected', 'selected');
	
	form.forma.appendChild(option);
};
addOptionCor = function(coluna, form){
	var option = document.createElement('option');
	option.appendChild(document.createTextNode(coluna.nome));
	option.setAttribute('value', coluna.nome);
	if(coluna.nome == config.cor)
		option.setAttribute('selected', 'selected');
	
	form.cor.appendChild(option);
};
addOptionEixos = function(coluna, form){
	var optionX = document.createElement('option');
	var optionY = document.createElement('option');
	optionX.setAttribute('value', coluna.nome);
	optionY.setAttribute('value', coluna.nome);
	optionX.appendChild(document.createTextNode(coluna.nome));
	optionY.appendChild(document.createTextNode(coluna.nome));
	
	if(coluna.nome == config.eixoX)
		optionX.setAttribute('selected', 'selected');
	if(coluna.nome == config.eixoY)
		optionY.setAttribute('selected', 'selected');
	
	form.eixoX.appendChild(optionX);
	form.eixoY.appendChild(optionY);
};

/*
* Cria filtro discreto (checkbox)
*/
addFiltroDiscreto = function(coluna, form){
	var field = document.createElement('fieldset');
	field.setAttribute('style', 'display:block; border:1px solid navy; padding:5px; font-size:12px; font-family:verdana, sans-serif;');
	
	var legend = document.createElement('legend');
	legend.setAttribute('style', 'color: navy; font-weight: bold;');
	legend.appendChild(document.createTextNode(coluna.nome));
	field.appendChild(legend);
	
	var ck, lb;
	var valor;
	var div = document.createElement('div');
	
	if(coluna.valores.length > 4)
		div.setAttribute('style', 'height: 100px; overflow: auto;');
	else
		div.setAttribute('style', 'overflow: auto;');
		
	for(var i=0; i<coluna.valores.length; i++){
		valor = coluna.valores[i];
		
		ck = document.createElement('input');
		ck.setAttribute('id', coluna.nome+""+i);
		ck.setAttribute('name', coluna.nome);
		ck.setAttribute('type', 'checkbox');
		ck.setAttribute('checked', 'checked');
		ck.setAttribute('value', valor);
		
		lb = document.createElement('label');
		lb.setAttribute('for', coluna.nome+""+i);
		lb.appendChild(document.createTextNode(valor));
		
		div.appendChild(ck);
		div.appendChild(lb);
		div.innerHTML += "<br/>";
	}
	
	field.appendChild(div);
	form.appendChild(field);
};

/*
* Cria filtro continuo (rangeslider)
*/
addFiltroContinuo = function(coluna, form){
	var field = document.createElement('fieldset');
	field.setAttribute('style', 'display:block; border:1px solid navy; padding:5px; font-size:12px; font-family:verdana, sans-serif;');
	
	var legend = document.createElement('legend');
	legend.setAttribute('style', 'color: navy; font-weight: bold;');
	legend.appendChild(document.createTextNode(coluna.nome));
	field.appendChild(legend);
	
	// cria divs
	var divTrack = document.createElement("div");
	divTrack.setAttribute("id", coluna.nome+"_track");
	divTrack.setAttribute("class", "track");
	
	var divHandle1 = document.createElement("div");
	var divHandle2 = document.createElement("div");
	divHandle1.innerHTML = "&lt;";
	divHandle1.setAttribute("id", coluna.nome+"_handle1");
	divHandle1.setAttribute("class", "handle");
	divHandle2.innerHTML = "&gt;";
	divHandle2.setAttribute("id", coluna.nome+"_handle2");
	divHandle2.setAttribute("class", "handle");
	
	divTrack.appendChild(divHandle1);
	divTrack.appendChild(divHandle2);
	
	var min = coluna.valores[0];
	var max = coluna.valores[coluna.valores.length-1];
	var divValue = document.createElement("div");
	divValue.innerHTML = "["+ formatDouble(min, 2) +" - "+ formatDouble(max, 2) +"]";
	divValue.setAttribute("id", coluna.nome + "_value");
	divValue.setAttribute("style", "text-align:center;");
	
	// adiciona ao field
	field.appendChild(divTrack);
	field.appendChild(divValue);
	form.appendChild(field);
	
	// cria controle
	rsliders.push( criarRangeSlider(coluna, [divHandle1, divHandle2], divTrack, divValue) );
};

mouseClicked = function(e){
	// retira tb as bordas do navegador ate o elemento canvas (offsets)
	var x = (Event.pointerX(e) - canvas.offsetLeft) - ptInicialX;
	var y = ptInicialY - (Event.pointerY(e) - canvas.offsetTop);
	
	var objClick = {
		"x":x,
		"y":y
	};
	
	// envia requisicao das informacoes do item na posicao clicada, se houver.
	var url = "/VIWeb/Consulta";
	var opcoes = {
		method: 'post',
		postBody: 'click='+Object.toJSON(objClick),
		onSuccess: sucessoInfo,
		onFailure: falhaRequisicao
	}
	showLoading(true);
	new Ajax.Request(url, opcoes);
};
sucessoInfo = function(t){
	$("detalhes-item").innerHTML = t.responseText;
	showLoading(false);
};

function showLoading(bool){
	if(bool)
		$("loading").className = "show";
	else
		$("loading").className = "hide";
}

function criarRangeSlider(coluna, handles, track, trackValue){
	var min = coluna.valores[0];
	var max = coluna.valores[coluna.valores.length-1];
	var slider;
	if(coluna.tipo == INTEGER){
		slider = new Control.Slider(handles, track, {
			range: $R(min, max),
			values: $R(min, max),
			step:1,
			restricted:true,
			sliderValue: [min, max],
			onSlide: function(v){
				trackValue.innerHTML = "["+ v[0] + " - " + v[1] + "]";
			},
			onChange: filtrar
		});
	}else{
		slider = new Control.Slider(handles, track, {
			range: $R(min, max),
			step:1,
			restricted:true,
			sliderValue: [min, max],
			onSlide: function(v){
				trackValue.innerHTML = "["+ formatDouble(v[0], 2) + " - " + formatDouble(v[1], 2) + "]";
			},
			onChange: filtrar
		});
	}
	return slider;
}
function formatDouble(num, f) {
    if(num.toString().indexOf(".") == -1)
    	return num;
    
    f = parseInt(f / 1 || 0);
    if (f < 0 || f > 20) {
        //alert("The number of fractional digits is out of range");
    }
    if (isNaN(num)) {
        return "NaN";
    }
    var s = num < 0 ? "-" : "", x = Math.abs(num);
    if (x > Math.pow(10, 21)) {
        return s + x.toString();
    }
    var m = Math.round(x * Math.pow(10, f)).toString();
    if (!f) {
        return s + m;
    }
    while (m.length <= f) {
        m = "0" + m;
    }
    return s + m.substring(0, m.length - f) + "." + m.substring(m.length - f);
}

function gerarRelatorio(){
	// nova janela chamando o GeradorRelatorio
	window.open("/VIWeb/GeradorRelatorio");
	return false;
}

Event.observe(window, 'load', inicializar);
Event.observe(window, 'load', configurarDisplay);
Event.observe(window, 'load', carregarDados);