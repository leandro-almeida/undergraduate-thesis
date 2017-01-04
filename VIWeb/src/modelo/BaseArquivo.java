package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import util.FormatadorValores;
import filtro.FiltroContinuo;
import filtro.FiltroDiscreto;
import filtro.ModeloFiltro;

public class BaseArquivo extends BaseDados {

	private File arquivoAtual;
	private BufferedReader bf;
	private String linha;
	private String[] nome_coluna;

	private ArrayList<Item> base = new ArrayList<Item>();

	private static BaseArquivo baseArquivo;

	public static BaseArquivo getInstance() {
		if (baseArquivo == null) {
			baseArquivo = new BaseArquivo();
		}
		return baseArquivo;
	}

	public File getCaminho() {
		return arquivoAtual;
	}

	public void setCaminho(File caminho) {
		arquivoAtual = caminho;
	}

	private void carregaColunas() {

		try {
			linha = bf.readLine();
			nome_coluna = linha.split("\t");
			for (int i = 0; i < nome_coluna.length; i++) {
				nome_coluna[i] = nome_coluna[i].trim();
			}
			linha = bf.readLine();
			String[] tipo_coluna = linha.split("\t");

			/*
			 * Trecho de criacao das colunas, especificando o nome e o tipo de
			 * cada uma
			 */
			for (int i = 0; i < nome_coluna.length; i++) {
				Coluna coluna = new Coluna();
				coluna.setNome(nome_coluna[i]);
				coluna.setTipo(tipo_coluna[i].trim());
				colunas.put(coluna.getNome(), coluna);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void carregaCoovisItem() {
		/*
		 * Agora vamos criar os itens e armazenar no arrayList Tambem
		 * precisamos preencher os valores de cada coluna no treeSet valores
		 */
		Object valorItem = null;
		base.clear();
		itens.clear();
		int contLinha = 0;
		Coluna coluna = null;
		try {
			while ((linha = bf.readLine()) != null) {
				try {
					contLinha++;
					Item item = new Item();
					String[] valores = linha.split("\t");
					for (int i = 0; i < valores.length; i++) {
						coluna = colunas.get(nome_coluna[i]);

						if (coluna.getTipo() == Coluna.INTEGER) {
							valorItem = Integer.valueOf(valores[i].trim());
						} else if (coluna.getTipo() == Coluna.DOUBLE) {
							valorItem = Double.valueOf(valores[i].trim());
						} else if (coluna.getTipo() == Coluna.DATA) {
							valorItem = FormatadorValores.dateFormatter
									.parse(valores[i].trim());
						} else if (coluna.getTipo() == Coluna.STRING) {
							valorItem = valores[i].trim();
						}

						item.setValorColuna(coluna.getNome(), valorItem);
						coluna.addValor(valorItem);
					}
					base.add(item);
					itens.add(item);
				} catch (NumberFormatException e) {
					System.out.println("Erro na linha = " + contLinha
							+ " e coluna = " + coluna.getNome());
					e.printStackTrace();
					continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Erro na linha = " + contLinha
							+ " e coluna = " + coluna.getNome());
					e.printStackTrace();
					continue;
				} catch (ParseException e) {
					System.out.println("Erro ao tratar data na linha = "
							+ contLinha + " e coluna = " + coluna.getNome());
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			System.out.println("Erro na linha = " + contLinha);
			e.printStackTrace();
		}
	}

	public void setDescricaoColuna() {
		/*
		 * Aqui vamos definir se a coluna eh DISCRETO OU CONTINUO
		 */
		for (int i = 0; i < colunas.size(); i++) {
			Coluna c = colunas.get(nome_coluna[i]);
			c.setDescricao();
		}
	}

	public boolean carregaBaseDados() {
		this.setCaminho(new File(ConfBaseDados.getInstance().getCaminho()));
		inicializaEstruturas();
		base.clear();
		try {
			FileReader ler = new FileReader(arquivoAtual);
			bf = new BufferedReader(ler);

			carregaColunas();
			carregaCoovisItem();

			/*
			 * Especificar se a coluna eh DISCRETO Ou CONTINUO somente apos
			 * preencher os valores da coluna Isto porque se uma coluna INTEGER
			 * tiver poucos valores, ela sera DISCRETO
			 */
			setDescricaoColuna();
			return true;

		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public ArrayList<Item> getBaseTotal() {
		return base;
	}

	public void filtrar(Collection<ModeloFiltro> paramFiltros) {
		itens.clear();
		boolean inserir = true;
		Object[] filtros = paramFiltros.toArray();
		
		for (int i = 0; i < base.size(); i++) {
			inserir = true;
			for (int j = 0; j < filtros.length; j++) {
				inserir = isInserir(base.get(i), (ModeloFiltro) filtros[j]);
				if (!inserir) {
					break;
				}
			}
			if (inserir) {
				itens.add(base.get(i));
			}
		}
	}

	private boolean isInserir(Item Item, ModeloFiltro filtro) {
		if (filtro instanceof FiltroDiscreto) {
			String valorItem = FormatadorValores.formataValorColuna(filtro.getColuna().getTipo(), Item.getValorColuna(filtro.getColuna().getNome()));
			return (Boolean) ((FiltroDiscreto) filtro).getValoresFiltro().get(valorItem);
		}
		if (filtro instanceof FiltroContinuo) {
			if (filtro.getColuna().getTipo() == Coluna.INTEGER) {
				return compareInteger(Item.getValorColuna(filtro.getColuna().getNome()), (FiltroContinuo)filtro);
			}
			if (filtro.getColuna().getTipo() == Coluna.DOUBLE) {
				return compareDouble(Item.getValorColuna(filtro.getColuna().getNome()), (FiltroContinuo)filtro);
			} else {
				return compareData(Item.getValorColuna(filtro.getColuna().getNome()), (FiltroContinuo)filtro);
			}
		}
		return false;
	}


	private boolean compareInteger(Object valorItem, FiltroContinuo filtro) {
		int valor = ((Integer) valorItem).intValue();
		int valorMax = (Integer) filtro.getMaxAtual();
		int valorMin = (Integer) filtro.getMinAtual();
		if (valor >= valorMin && valor <= valorMax) {
			return true;
		}
		return false;
	}

	private boolean compareDouble(Object valorItem, FiltroContinuo filtro) {
		double valor = ((Double) valorItem).doubleValue();
		double valorMax = (Double) filtro.getMaxAtual();
		double valorMin = (Double) filtro.getMinAtual();
		if (valor >= valorMin && valor <= valorMax) {
			return true;
		}
		return false;
	}

	private boolean compareData(Object valorItem, FiltroContinuo filtro) {
//		long valor = ((Date) valorItem).getTime();
//		long valorMax = ((Date) filtro.getMaxValorAtual()).getTime();
//		long valorMin = ((Date) filtro.getMinValorAtual()).getTime();
//		if (valor >= valorMin && valor <= valorMax) {
//			return true;
//		}
		return false;
	}
}
