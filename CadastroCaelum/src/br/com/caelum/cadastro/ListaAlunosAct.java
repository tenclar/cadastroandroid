package br.com.caelum.cadastro;

import java.util.List;

import br.com.caelum.cadastro.adapter.ListaAlunosAdapter;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.modelo.Aluno;
import br.com.caelum.support.EnviaContatosTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaAlunosAct extends Activity {
	/** Called when the activity is first created. */
	private Aluno alunoSelecionado;
	private List<Aluno> alunos;
	private ListView listaAlunos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		listaAlunos = (ListView) findViewById(R.id.listaAlunos);
		carregaLista();

		listaAlunos.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				alunoSelecionado = alunos.get(position);
				Intent edicao = new Intent(ListaAlunosAct.this, FormularioAct.class);
				edicao.putExtra("alunoSelecionado",
						(Aluno) listaAlunos.getItemAtPosition(position));
				startActivity(edicao);

			}
		});
		listaAlunos.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				alunoSelecionado = alunos.get(position);
				return false;
			}

		});
		registerForContextMenu(listaAlunos);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_novo:
			// Toast.makeText(ListaAlunos.this, "Você Clicou no novo Aluno",
			// Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ListaAlunosAct.this, FormularioAct.class);
			startActivity(intent);
			return false;
			
		case R.id.menu_mapa:			
			Intent mapa = new Intent(ListaAlunosAct.this, MapaAct.class);
			startActivity(mapa);	
		return false;
			case R.id.menu_sync:			
				new EnviaContatosTask(this).execute();				
			return false;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(alunoSelecionado.getNome());
		MenuItem ligar = menu.add(0, 0, 0, "Ligar");
		Intent intentLigar = new Intent(Intent.ACTION_DIAL);
		intentLigar.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
		ligar.setIntent(intentLigar);

		MenuItem sms = menu.add(0, 1, 0, "Enviar SMS");
		sms.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				SmsManager smsManager = SmsManager.getDefault();
				PendingIntent sentIntent = PendingIntent.getActivity(ListaAlunosAct.this, 0,
						null, 0);
				if (PhoneNumberUtils.isWellFormedSmsAddress(alunoSelecionado
						.getTelefone())) {
					smsManager.sendTextMessage(alunoSelecionado.getTelefone(),
							null, "sua nota é: " + alunoSelecionado.getNota(),
							sentIntent, null);
					Toast.makeText(ListaAlunosAct.this, "SMS Envidado com sucesso",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ListaAlunosAct.this, "Falha no SMS - tente novamente",
							Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		// Intent intentSms = new Intent(Intent.ACTION_VIEW);
		// intentSms.setData(Uri.parse("sms:"+alunoSelecionado.getTelefone()));
		// intentSms.putExtra("sms_body",
		// "Teste de envio de mensagem do aplicativo");
		// sms.setIntent(intentSms);

		menu.add(0, 2, 0, "Achar no Mapa");
		
		MenuItem site = menu.add(0, 3, 0, "Navegar no Site");
		Intent intentSite = new Intent(Intent.ACTION_VIEW);
		intentSite.setData(Uri.parse(alunoSelecionado.getSite()));
		site.setIntent(intentSite); 

		MenuItem deletar = menu.add(0, 4, 0, "Deletar");
		deletar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				new AlertDialog.Builder(ListaAlunosAct.this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("Deletar")
						.setMessage("Deseja deletar?")
						.setPositiveButton("Sim",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										AlunoDAO dao = new AlunoDAO(
												ListaAlunosAct.this);
										dao.deletar(alunoSelecionado);
										dao.close();
										carregaLista();
									}
								}).setNegativeButton("Não", null).show();

				return false;
			}
		});
		menu.add(0, 5, 0, "Enviar email");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		carregaLista();
	}

	private void carregaLista() {

		AlunoDAO alunoDAO = new AlunoDAO(ListaAlunosAct.this);
		alunos = alunoDAO.getLista();
		alunoDAO.close();
		// ArrayAdapter<Aluno> adapter = new
		// ArrayAdapter<Aluno>(this,android.R.layout.simple_list_item_1,
		// alunos);
		ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);
		listaAlunos = (ListView) findViewById(R.id.listaAlunos);
		listaAlunos.setAdapter(adapter);

	}

}