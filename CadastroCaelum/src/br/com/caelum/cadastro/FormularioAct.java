package br.com.caelum.cadastro;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.modelo.Aluno;

public class FormularioAct extends Activity {

	protected static final int TIRA_FOTO = 101;
	private Aluno aluno;
	private EditText nome;
	private EditText telefone;
	private EditText site;
	private RatingBar nota;
	private EditText endereco;
	private Button botao;
	private ImageButton imageButton; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulario);
		this.buscaComponentesDaTela();		
		
		botao = (Button) findViewById(R.id.botao);
		this.aluno = (Aluno) getIntent().getSerializableExtra("alunoSelecionado");
		
		if(aluno == null){
			aluno = new Aluno();
			
		}else{
			botao.setText("Alterar");
			populaTelaComDadosAluno();
			
			
		}	
		
		botao.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {	
				
				AlunoDAO dao = new AlunoDAO(FormularioAct.this);
				if (aluno.getId() == null) {
					populaAlunoComDadosDaTela();
					dao.insere(aluno);					
					Toast.makeText(FormularioAct.this, "Salvo",
							 Toast.LENGTH_SHORT).show();
				} else {
					populaAlunoComDadosDaTela();				
					dao.alterar(aluno);					
					Toast.makeText(FormularioAct.this, "Alterado",
							 Toast.LENGTH_SHORT).show();
				}
				dao.close();
				finish();
			}
		});
		
		imageButton = (ImageButton) findViewById(R.id.imagem);
		imageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String arquivo = Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis()+".jpg";
				File file = new File(arquivo);
				Uri outputFileUri = Uri.fromFile(file);
				aluno.setFoto(arquivo);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent,TIRA_FOTO);
				
				
			}
		});
		this.carregaImagem();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == TIRA_FOTO){
			if(resultCode!=RESULT_OK){
				aluno.setFoto(null);
				
			}
			carregaImagem();
//			Bitmap myPicture = (Bitmap) data.getExtras().get("data");
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			myPicture.compress(CompressFormat.PNG,0,bos);
//			byte[] bitmapdata = bos.toByteArray();
//			
//			imageButton.setImageBitmap(myPicture);
//			
		}
		
	}
	private void carregaImagem() {
		Bitmap bm;
		if(aluno.getFoto()!= null){
			bm = BitmapFactory.decodeFile(aluno.getFoto());
			bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
			
		} else {
			bm = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
		}
		imageButton.setImageBitmap(bm);
		
	}
	private void populaTelaComDadosAluno() {
		nome.setText(aluno.getNome());
		telefone.setText(aluno.getTelefone());
		site.setText(aluno.getSite());
		nota.setRating(new Float(aluno.getNota()));
		endereco.setText(aluno.getEndereco());
		
	}

	protected void populaAlunoComDadosDaTela() {
		this.aluno.setNome(this.nome.getEditableText().toString());
		this.aluno.setEndereco(this.endereco.getEditableText().toString());
		this.aluno.setTelefone(this.telefone.getEditableText().toString());
		this.aluno.setSite(this.site.getEditableText().toString());
		this.aluno.setNota(this.nota.getRating());

	}

	protected void buscaComponentesDaTela() {
		this.nome = (EditText) findViewById(R.id.nome);
		this.telefone = (EditText) findViewById(R.id.telefone);
		this.site = (EditText) findViewById(R.id.site);
		this.nota = (RatingBar) findViewById(R.id.nota);
		this.endereco = (EditText) findViewById(R.id.endereco);

	}

}
