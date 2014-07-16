package br.com.caelum.support;

import java.util.List;

import br.com.caelum.converter.AlunoConverter;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.modelo.Aluno;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class EnviaContatosTask extends AsyncTask<Object, Object, String> {
	private final Context context;
	private final String endereco = "http://www.caelum.com.br/mobile";
	private ProgressDialog progress;
	

	public EnviaContatosTask(Context context) {
		this.context = context;
	}
	
	@Override
	public void onPreExecute() {		
		super.onPreExecute();
		this.progress = ProgressDialog.show(context,"aguarde ...", "Enviado dados para a web", true,true);
	}
	
	@Override
	public String doInBackground(Object... params) {
		AlunoDAO dao = new AlunoDAO(this.context);
		List<Aluno> alunos = dao.getLista();
		dao.close();
		String listaJson = new AlunoConverter().toJSON(alunos);		
		String jsonResposta = new WebClient(endereco).post(listaJson);
		return jsonResposta;

	}
	@Override
	public void onPostExecute(String result) {		
		super.onPostExecute(result);
		Toast.makeText(context,result, Toast.LENGTH_LONG).show();
		progress.dismiss();
	}

}
