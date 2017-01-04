function trocaTab(num, maintab, area){
	// muda estilo do LI (primeiro coloca todos como normal, sem estilo)
	var lis = $(maintab).getElementsByTagName("li");
	for(var i=0; i<lis.length; i++){
		lis[i].className = "";
		if(i == num-1)
			lis[i].className = "selected";
	}
	
	// ativa tab (primeiro esconde todas)
	var divs = $(area).getElementsByTagName("div");
	var len = divs.length;
	var c=0;
	for(var i=0; i<len; i++){
		c = divs[i].id.indexOf("tab"); 
		if(c == -1) // uma div do usuario, nao representa o conteudo de uma aba!
			continue;
		else{
			divs[i].className = "hide"; //esconde todas as abas sempre
			
			if(divs[i].id.substring(3) == num)
				divs[i].className = "show";
		}
	}
}