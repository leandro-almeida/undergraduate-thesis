package filtro;

import modelo.Coluna;
import modelo.ModeloAbstrato;

public class ModeloFiltro extends ModeloAbstrato{
	private Coluna coluna;
	
	public Coluna getColuna() {
		return coluna;
	}

	public void setColuna(Coluna coluna) {
		this.coluna = coluna;
	}
}
