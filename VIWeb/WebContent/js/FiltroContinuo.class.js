/*
 * VIWeb - Visualização de Informações na Web
 * Autores: 
 *		- Leandro Hernandez Almeida 
 *		- Leonardo Hernandez Almeida
 * Orientador: Prof. Dr. Bianchi Serique Meiguins
*/

var FiltroContinuo = Base.extend({
	constructor: function(atr) {
		this.atributo = atr;
    },
    
	atributo: null,
	min: null,
	max: null,
    
	setAtributo: function(atr){
		this.atributo = atr;
	},
});