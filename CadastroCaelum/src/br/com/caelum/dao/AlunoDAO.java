package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.modelo.Aluno;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlunoDAO extends SQLiteOpenHelper {

	private static final int VERSAO = 1;
	private static final String TABELA = "CadastroCaelum";
	private static final String DATABASE = "FJ57";
	private static final String[] COLS = { "id", "nome", "telefone",
			"endereco", "site", "nota", "foto" };

	public AlunoDAO(Context context) {
		super(context, DATABASE, null, VERSAO);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABELA + "(id INTEGER PRIMARY KEY, "
				+ "nome TEXT UNIQUE NOT NULL, "
				+ "telefone TEXT, endereco TEXT, "
				+ "site TEXT, nota REAL, foto TEXT);";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABELA;
		db.execSQL(sql);
		onCreate(db);

	}

	public void insere(Aluno aluno) {
		ContentValues values = new ContentValues();
		values.put("nome", aluno.getNome());
		values.put("telefone", aluno.getTelefone());
		values.put("endereco", aluno.getEndereco());
		values.put("site", aluno.getSite());
		values.put("nota", aluno.getNota());
		values.put("foto", aluno.getFoto());
		getWritableDatabase().insert(TABELA, null, values);
	}

	public List<Aluno> getLista() {
		List<Aluno> alunos = new ArrayList<Aluno>();
		Cursor c = getWritableDatabase().query(TABELA, COLS, null, null, null,
				null, null);
		while (c.moveToNext()) {
			Aluno aluno = new Aluno();
			aluno.setId(c.getLong(0));
			aluno.setNome(c.getString(1));
			aluno.setTelefone(c.getString(2));
			aluno.setEndereco(c.getString(3));
			aluno.setSite(c.getString(4));
			aluno.setNota(c.getDouble(5));
			aluno.setFoto(c.getString(6));
			alunos.add(aluno);
		}
		c.close();
		return alunos;
	}

	public void deletar(Aluno aluno) {
		getWritableDatabase().delete(TABELA, "id=?", new String[]{aluno.getId().toString()});
		
	}

	public void alterar(Aluno aluno) {
		ContentValues values = new ContentValues();
		values.put("nome", aluno.getNome());
		values.put("telefone", aluno.getTelefone());
		values.put("endereco", aluno.getEndereco());
		values.put("site", aluno.getSite());
		values.put("nota", aluno.getNota());
		values.put("foto", aluno.getFoto());
		getWritableDatabase().update(TABELA, values, "id=?", new String[]{aluno.getId().toString()});
	}

	public boolean isAluno(String telefone) {
		
			Cursor rawQuery = getReadableDatabase().rawQuery("Select telefone from "+ TABELA +" where telefone = ?", new String[]{telefone});
			int total = rawQuery.getCount();			
		return total > 0;
	}

}
