package br.com.caelum.cadastro;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.modelo.Aluno;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapaAct extends MapActivity  {
	private MapController mapController;
	private LocationListener locationListener = new LocationListener() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		public void onProviderDisabled(String provider) {
			atualizarMeuLocal(null);
			
		}
		
		public void onLocationChanged(Location location) {
			atualizarMeuLocal(location);
			
		}
	}; 
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.map_layout);
		MapView mapView = (MapView) findViewById(R.id.maps_view);
		 mapController = mapView.getController();
		mapView.setSatellite(true);
		mapView.setStreetView(true);		
		mapView.displayZoomControls(true);
		mapController.setZoom(17);
		
		List<Overlay> overlays = mapView.getOverlays();		
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		overlays.add(myLocationOverlay);
		Log.i("Log-------------------------------------", "passa");
		
//		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//		criteria.setAltitudeRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_HIGH);
//		String provider = locationManager.getBestProvider(criteria, true);		
//		Location location = locationManager.getLastKnownLocation(provider);
//		atualizarMeuLocal(location);				
//		locationManager.requestLocationUpdates(provider, 2000, 20, locationListener);
		
//		AlunoDAO adao = new AlunoDAO(this);
//		List<Aluno> listAlunos = adao.getLista();
//		adao.close();
//		Geocoder geo = new Geocoder(MapaAct.this,Locale.getDefault());
//		
//		for (Aluno aluno : listAlunos){
//			Address address = null;
//			try {
//				List<Address> enderecos = geo.getFromLocationName(aluno.getEndereco(), 1);
//				if(enderecos.size() > 0) address = enderecos.get(0);
//			} catch (IOException e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//			if(address !=null){
//				Bitmap bitmap = BitmapFactory.decodeResource(MapaAct.this.getResources(), R.drawable.noimage);
//				if(aluno.getFoto() != null){
//					try {
//						FileInputStream fis = new FileInputStream(aluno.getFoto());
//						bitmap = BitmapFactory.decodeStream(fis);
//						fis.close();
//						bitmap= Bitmap.createScaledBitmap(bitmap, 30, 30, true);
//						
//					} catch (IOException e) {
//						// TODO: handle exception
//						throw new RuntimeException(e);
//					}
//				}
//				
//				AlunoOverlay alunoOverlay = new AlunoOverlay(address,bitmap);
//				mapView.getOverlays().add(alunoOverlay);
//			}
//			
//		}
//		
//		mapView.invalidate();
	}
	protected void atualizarMeuLocal(Location location) {
		// TODO Auto-generated method stub
		if ( location != null){
			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
			mapController.animateTo(point);
		}
	}
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;		
		
	}
	
	

}
