package br.com.caelum.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.jdbc.modelo.Categoria;
import br.com.caelum.jdbc.modelo.Produto;

public class ProdutosDao {

	private Connection con;

	public ProdutosDao(Connection con) {
		this.con = con;
	}

	public void salva(Produto produto) throws SQLException {
		String sql = "insert into Produto (nome, descricao) values (?,?)";
		try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, produto.getNome());
			stmt.setString(2, produto.getDescricao());
			stmt.execute();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				int id = rs.getInt("id");
				produto.setId(id);
			}
		}
	}

	public List<Produto> lista() throws SQLException {
		List<Produto> produtos = new ArrayList<>();

		String sql = "select * from produto";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.execute();

			transformaResultadoEmProdutos(produtos, stmt);
		}

		return produtos;
	}

	public List<Produto> busca(Categoria categoria) throws SQLException {
		System.out.println("executando uma query");
		List<Produto> produtos = new ArrayList<>();

		String sql = "select * from produto where categoria_id = ?";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, categoria.getId());
			stmt.execute();

			transformaResultadoEmProdutos(produtos, stmt);
		}

		return produtos;

	}

	public void transformaResultadoEmProdutos(List<Produto> produtos, PreparedStatement stmt) throws SQLException {
		try (ResultSet rs = stmt.getResultSet()) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				String descricao = rs.getString("descricao");
				Produto produto = new Produto(nome, descricao);
				produto.setId(id);

				produtos.add(produto);
			}
		}
	}

}
