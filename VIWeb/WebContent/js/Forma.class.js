/*
 * VIWeb - Visualização de Informações na Web
 * Autores: 
 *		- Leandro Hernandez Almeida 
 *		- Leonardo Hernandez Almeida
 * Orientador: Prof. Dr. Bianchi Serique Meiguins
*/

var Forma = Base.extend({
	QUADRADO: 0,
	TRIANGULO: 1,
	CIRCULO: 2,
	
	desenhar: function(ctx, forma, x, y, tam){
		switch(forma){
			case this.QUADRADO:
				ctx.fillRect(x-(tam/2), y-(tam/2), tam, tam);
				ctx.strokeRect(x-(tam/2), y-(tam/2), tam, tam);
				break;
			
			case this.TRIANGULO:
				ctx.beginPath();
				ctx.moveTo(x-(tam/2), y+(tam/2));
				ctx.lineTo(x, y-tam+(tam/2)); // apice do triangulo
				ctx.lineTo(x+(tam/2), y+(tam/2));
				ctx.closePath();
				ctx.fill();
				ctx.stroke();
				break;
			
			case this.CIRCULO:
				ctx.beginPath();
				ctx.arc(x, y, Math.round(tam/2), 0, Math.PI*2, true);
				ctx.fill();
				ctx.stroke();
				break;
		}
	}
});