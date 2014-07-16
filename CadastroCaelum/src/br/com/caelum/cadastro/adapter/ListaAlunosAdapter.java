package br.com.caelum.cadastro.adapter;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.modelo.Aluno;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ListaAlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos;
	private final Activity activity;
	
	
	public ListaAlunosAdapter(Activity activity,List<Aluno> alunos) {
		this.alunos = alunos;
		this.activity = activity;
		
	}
	
	public int getCount() {
		
		return alunos.size();
	}

	public Object getItem(int position) {
		return alunos.get(position);
	}

	public long getItemId(int position) {
		
		return alunos.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Aluno aluno = alunos.get(position);
		View view = activity.getLayoutInflater().inflate(R.layout.item, null);
		
		LinearLayout fundo = (LinearLayout) view.findViewById(R.id.fundo);
		if ( position % 2 ==0 ){
			fundo.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
			
		}else {
			fundo.setBackgroundColor(activity.getResources().getColor(R.color.linha_impar));
		}
		TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(aluno.toString());
		
		Bitmap bm;
		if(aluno.getFoto()!=null){
			bm=BitmapFactory.decodeFile(aluno.getFoto());			
		}else {
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.noimage);
			
		}
		bm= Bitmap.createScaledBitmap(bm, 64,64, true);
		ImageView foto = (ImageView) view.findViewById(R.id.foto);
		foto.setImageBitmap(bm);
		
		return view;
	}

}
