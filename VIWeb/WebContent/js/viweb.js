/*
 * VIWeb - Visualização de Informações na Web
 * Autores: 
 *		- Leandro Hernandez Almeida 
 *		- Leonardo Hernandez Almeida
 * Orientador: Prof. Dr. Bianchi Serique Meiguins
*/
incluir = function(file){
	document.write('<script type="text/javascript" src="'+ file +'"></script>');
};

incluir("js/lib/prototype.js");
incluir("js/lib/tabs.js");
incluir("js/lib/slider_new.js");
incluir("js/lib/canvastext.js");
incluir("js/Base.js");
incluir("js/FiltroDiscreto.class.js");
incluir("js/FiltroContinuo.class.js");
incluir("js/Forma.class.js");
incluir("js/Dispersao.js");