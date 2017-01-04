/*
 * VIWeb - Visualização de Informações na Web
 * Autores: 
 *		- Leandro Hernandez Almeida 
 *		- Leonardo Hernandez Almeida
 * Orientador: Prof. Dr. Bianchi Serique Meiguins
*/

var FiltroDiscreto = Base.extend({
	constructor: function(atr) {
		this.atributo = atr;
		this.valores = new Array();
    },
    
	atributo: null,
	valores: null,
    
	setAtributo: function(atr){
		this.atributo = atr;
	},
	add: function(valor){
		this.valores.push(valor);
	}
});